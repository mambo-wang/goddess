package com.h3c.vdi.athena.configserver.service.config;

import com.h3c.vdi.athena.common.concurrent.VDIExecutorServices;
import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.JsonUtils;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.configserver.config.nextcloud.NextCloudConfig;
import com.h3c.vdi.athena.configserver.exception.ErrorCodes;
import com.h3c.vdi.athena.configserver.feign.NetDiskFeignClient;
import com.h3c.vdi.athena.configserver.mapper.MountInfoMapper;
import com.h3c.vdi.athena.configserver.model.dto.ConfigDTO;
import com.h3c.vdi.athena.configserver.model.dto.DiskDTO;
import com.h3c.vdi.athena.configserver.model.dto.PreDataDTO;
import com.h3c.vdi.athena.configserver.model.entity.MountInfo;
import com.h3c.vdi.athena.common.utils.SSHExecutor;
import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.configserver.model.entity.Parameter;
import com.h3c.vdi.athena.configserver.service.parameter.ParameterMgr;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Service("configMgr")
@Slf4j
public class ConfigMgrImpl implements ConfigMgr {

    @Resource
    private ParameterMgr parameterMgr;

    @Resource
    private MountInfoMapper mountInfoMapper;

    @Resource
    private RollBackChain rollBackChain;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private NextCloudConfig nextCloudConfig;

    @Resource
    private NetDiskFeignClient netDiskFeignClient;

    private StringManager sm = StringManager.getManager("Config");

    public static final String NEXTCLOUD_INIT_FILE = BasicConstant.MOUNT_POINT_NEXTCLOUD + "/core/skeleton";

    private ExecutorService service = VDIExecutorServices.get().getIoBusyService();

    @Override
    public PreDataDTO queryDisks() {
        SSHConfig sshConfig = getSSHConfigFromRedis();
        try (SSHExecutor ssh = SSHExecutor.newInstance(sshConfig)){
            //查询全部分区
            List<DiskDTO> diskDTOS = this.queryDisks(ssh);

            List<DiskDTO> disks = diskDTOS.stream()
                    .filter(diskDTO -> !diskDTO.getPath().contains("/dev/mapper") && !diskDTO.getPath().contains("/dev/sda"))
                    .map(diskDTO -> {
                        try {
                            diskDTO.setSubDisks(queryMoreByDisk(ssh, diskDTO.getPath()));
                        } catch (Exception e) {
                            log.warn("query sub disks command exec fail {}", e.getMessage());
                        }
                        return diskDTO;
                    })
                    .collect(Collectors.toList());
            List<String> types = queryFileSystemTypes(ssh);
            PreDataDTO preDataDTO = new PreDataDTO();
            preDataDTO.setDisks(disks);
            preDataDTO.setFileSystemTypes(types);
            return preDataDTO;

        } catch (Exception e) {
            log.warn("query disks command exec fail {}", e.getMessage());
            throw new AppException(ErrorCodes.CMD_EXEC_FAIL);
        }
    }

    private SSHConfig getSSHConfigFromRedis(){
        SSHConfig sshConfig = JsonUtils.parseSSHConfig(redisUtil.get(RedisUtil.REDIS_KEY_SSHCONFIG));
        log.info("get ssh config from redis success", sshConfig.toString());
        return sshConfig;
    }

    public List<DiskDTO> queryMoreByDisk(SSHExecutor ssh, String path) throws InterruptedException, JSchException, IOException {

        Set<String> results = ssh.exec("fdisk -l " + path + " | grep -A 15 'Start'");

        return results.stream()
                .filter(s -> !s.contains("Start"))
                .map(s -> {
                    String[] strings = s.split("\\s+");
                    DiskDTO diskDTO = new DiskDTO();
                    long size = Long.valueOf(strings[3]);
                    String sizeGb = String.format("%.1f",size /(float)(1024*1024)) + "GB";
                    diskDTO.setSize(sizeGb);
                    diskDTO.setPath(strings[0]);
                    return diskDTO;
                })
                .collect(Collectors.toList());
    }

    private List<String> queryFileSystemTypes(SSHExecutor ssh) throws InterruptedException, JSchException, IOException {

        Set<String> types = ssh.exec("cat /proc/filesystems");
        return types.stream()
                .map(s -> {
                    String[] strings = s.split("\\s+");
                    return strings[strings.length - 1];
                })
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());

    }

    private Set<String> queryMountedDisks(SSHExecutor ssh) throws InterruptedException, JSchException, IOException {
        Set<String> cmd1 = ssh.exec("df -h");
        return cmd1.stream().map(s -> {
            String[] strings = s.split("\\s+");
            return strings[0];
        }).collect(Collectors.toSet());
    }

    private Set<String> queryPvs(SSHExecutor ssh) throws InterruptedException, JSchException, IOException {
        Set<String> cmd1 = ssh.exec("pvs");
        return cmd1.stream()
                .filter(s -> !s.contains("PV"))
                .map( s -> {
                    String[] disks = s.split("\\s+");
                    return disks[1];
                })
                .collect(Collectors.toSet());
    }



    private List<DiskDTO> queryDisks(SSHExecutor ssh) throws InterruptedException, JSchException, IOException {
        Set<String> cmd1 = ssh.exec("fdisk -l  | grep Disk");
        return cmd1.stream()
                .filter(s -> !s.contains("docker") && s.contains("/"))
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private DiskDTO convert(String result){
        String[] disks = result.split("\\s+");
        String rowPath = disks[1];
        String path = StringUtils.substringBeforeLast(rowPath, ":");
        String size = disks[2] + StringUtils.substringBeforeLast(disks[3], ",");
        DiskDTO diskDTO = new DiskDTO();
        diskDTO.setPath(path);
        diskDTO.setSize(size);
        return diskDTO;
    }


    @Override
    public List<MountInfo> queryMountInfos() {
        return mountInfoMapper.selectAll();
    }

    @Override
    public void updateConfig(ConfigDTO configDTO) {
        ConfigDTO config = parameterMgr.queryPathsOfParams();

        //初次挂载
        if(CollectionUtils.isEmpty(config.getFileSystemPath())){
            mount(configDTO.getFileSystemPath(), configDTO.getFileSystemType(), configDTO.getCapacity());
            parameterMgr.updateParameters(configDTO);
        }else {
            if(config.getCapacity() > configDTO.getCapacity()){
                throw new AppException("暂不支持缩容");
            }
            //修改挂载信息，文件系统类型不能修改
            modifyMount(config.getFileSystemPath(), configDTO.getFileSystemPath(), config.getCapacity(), configDTO.getCapacity());
        }
    }

    /**
     * 开机挂载
     */
    @Override
    public void mountLv() {
        SSHConfig sshConfig = getSSHConfigFromRedis();
        try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){
            //挂载
            sshExecutor.exec("mount /dev/mapper/vgathena-lvathena " + Parameter.MOUNT_POINT);
            log.info("------------mount /dev/mapper/vgathena-lvathena to " + Parameter.MOUNT_POINT);

        } catch (Exception e) {
            log.error("------------start up mount fail " + e.getMessage());
        }
    }

    @Override
    public void deployNextCloud() {

        String cmd = "docker run -d --name athena-nextcloud -p " + nextCloudConfig.getPort() + ":80 --env MYSQL_HOST=" + nextCloudConfig.getHostIP() +
                " --env MYSQL_DATABASE=athena-nextcloud --env MYSQL_PASSWORD=cloudos --env MYSQL_USER=root --env NEXTCLOUD_ADMIN_USER="
                + nextCloudConfig.getUsername() + " --env NEXTCLOUD_ADMIN_PASSWORD=" + nextCloudConfig.getPassword() + " -v " +
                Parameter.MOUNT_POINT_NEXTCLOUD + ":/var/www/html --env TZ=Asia/Shanghai -v /etc/localtime:/etc/localtime:ro nextcloud";

        try {
            Runtime.getRuntime().exec(cmd);
            log.info("-------------create docker container nextCloud");
            addInitTarget(new ModifyNextCloudConfig());
            addInitTarget(new ModifyNextCloudFile());

        } catch (Exception e) {
            log.error("--------------create docker container nextCloud fail " + e.getMessage());
            try {
                Runtime.getRuntime().exec("docker rm -f athena-nextcloud");
                log.info("-------------remove docker container nextCloud");
                Runtime.getRuntime().exec(cmd);
                log.info("-------------create docker container nextCloud again");
            } catch (IOException e1) {
                e1.printStackTrace();
                throw new AppException(e.getMessage());
            }
        }
    }

    /**
     * 修改挂载信息
     * @param fromDB
     * @param toModify
     * @param capacity
     */
    private void modifyMount(List<String> fromDB, List<String> toModify,Integer capacityDB, Integer capacity) {

        String errorMsg = "";

        List<String> toReduce = fromDB.stream()
                .filter(x -> !toModify.contains(x))
                .collect(Collectors.toList());

        List<String> toExtend = toModify.stream()
                .filter(x -> !fromDB.contains(x))
                .collect(Collectors.toList());

        //增加物理卷
        List<String> failToExtend = toExtend.stream()
                .map(this::extend)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());


        if(!CollectionUtils.isEmpty(failToExtend)){
            errorMsg += sm.getString("pv.create.fail", failToExtend.toString());
        }

        //移除物理卷
        List<String> failToReduce = toReduce.stream()
                .map(this::reduce)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(failToReduce)){
            errorMsg += sm.getString("pv.remove.fail", failToReduce.toString());
        }

        //根据实际执行结果写数据库
        toModify.addAll(failToReduce);
        toModify.removeAll(failToExtend);
        parameterMgr.updateParameters(Parameter.NAME_FILE_SYSTEM_PATH, String.join(",", toModify));

        //逻辑卷扩容
        if(capacityDB < capacity){
            String result = lvextend(capacityDB, capacity);
            if(StringUtils.isEmpty(result)){
                parameterMgr.updateParameters(Parameter.NAME_FILE_SYSTEM_CAPACITY, capacity.toString());
            } else {
                errorMsg += sm.getString("lv.extend.fail", result);
            }
        }

        if(StringUtils.isNotEmpty(errorMsg)){
            throw new AppException(errorMsg);
        }
    }

    /**
     * 逻辑卷扩容
     * @param capacity 容量
     * @return 成功返回true
     */
    private String lvextend(Integer db, Integer capacity){
        SSHConfig sshConfig = getSSHConfigFromRedis();
        try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){
            rollBackChain.clear();
            //扩容
            sshExecutor.exec("lvextend -L " + capacity + "G /dev/vgathena/lvathena");
            rollBackChain.addRollBack("lvextend -L " + db + "G /dev/vgathena/lvathena");
            //重新加载逻辑卷
            sshExecutor.exec("resize2fs /dev/vgathena/lvathena");
            log.info("------------lvextend " + capacity);
            return "";
        } catch (Exception e) {
            rollBackChain.doRollBack();
            log.error("--------------lvextend fail " + e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * 扩展
     * @param disk 分区
     * @return 扩展失败返回disk，成功返回""
     */
    private String extend(String disk) {
        SSHConfig sshConfig = getSSHConfigFromRedis();
        try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){

            rollBackChain.clear();

            //创建物理卷
            sshExecutor.exec("pvcreate " + disk);
            rollBackChain.addRollBack("pvremove " + disk);

            //将物理卷加入卷组
            sshExecutor.exec("vgextend vgathena " + disk);
            rollBackChain.addRollBack("vgreduce vgathena " + disk);
            log.info("------------extend " + disk);
            return "";
        } catch (AppException e) {
            rollBackChain.doRollBack();
            log.error("--------------extend fail " + e.getMessage());
            return disk;
        } catch (Exception e){
            rollBackChain.doRollBack();
            return disk;
        }
    }

    /**
     * 删除物理卷
     * @param disk 分区
     * @return 移除失败返回disk，成功返回""
     */
    private String reduce(String disk){
        SSHConfig sshConfig = getSSHConfigFromRedis();
        try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){
            rollBackChain.clear();

            //从卷组中移除物理卷
            sshExecutor.exec("vgreduce vgathena " + disk);
            rollBackChain.addRollBack("vgextend vgathena " + disk);

            //删除物理卷
            sshExecutor.exec("pvremove " + disk);
            rollBackChain.addRollBack("pvcreate " + disk);
            return "";
        } catch (AppException e) {
            log.error("--------------reduce fail " + e.getMessage());
            rollBackChain.doRollBack();
            return disk;
        } catch (Exception e) {
            rollBackChain.doRollBack();
            return disk;
        }
    }

    /**
     * 根据物理分区挂载
     * @param fileSystems 物理分区
     * @param fileSystemType 文件系统类型
     * @param capacity 逻辑卷容量
     */
    private void mount(List<String> fileSystems, String fileSystemType, Integer capacity){
        SSHConfig sshConfig = getSSHConfigFromRedis();
        try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){

            rollBackChain.clear();

            //创建物理卷
            Iterator<String> iterator = fileSystems.iterator();
            while (iterator.hasNext()){
                String fileSystem = iterator.next();
                try {
                    sshExecutor.exec("pvcreate " + fileSystem);
                    log.info("-------------create physic volume " + fileSystem);
                    rollBackChain.addRollBack("pvremove " + fileSystem);
                } catch (AppException e) {
                    log.warn("--------------create physic volume fail " + fileSystem);
                    iterator.remove();
                }
            }

            if(CollectionUtils.isEmpty(fileSystems)){
                throw new AppException(ErrorCodes.PV_CREATE_ALL_FAIL);
            }
            //创建卷组
            sshExecutor.exec("vgcreate vgathena " + fileSystems.get(0));
            log.info("-----------------------create volume group " + Parameter.VG_ATHENA + " and add " + fileSystems.get(0));
            rollBackChain.addRollBack("vgremove vgathena");
            //将其他物理卷加入卷组
            for (int i = 1;  i < fileSystems.size(); i++) {
                sshExecutor.exec("vgextend vgathena " + fileSystems.get(i));
                log.info("-----------------extend vg add " + fileSystems.get(i));
                rollBackChain.addRollBack("vgreduce vgathena " + fileSystems.get(i));
            }

            //创建逻辑卷
            sshExecutor.exec("lvcreate -L " + capacity + "G -n lvathena vgathena");
            log.info("---------------------create logic volume " + Parameter.LV_ATHENA);
            //逻辑卷删除时有参数y
            rollBackChain.addRollBack("echo -e \"y\\n\" | lvremove /dev/mapper/vgathena-lvathena");

            //创建ext4文件系统
            sshExecutor.exec("mkfs." + fileSystemType + " /dev/vgathena/lvathena");
            log.info("-----------------------create fileSystem " + fileSystemType);

            //创建临时文件夹
//            sshExecutor.exec("mkdir -p " + Parameter.MOUNT_POINT_TMP);
//            log.info("------------create tmp dir:" + Parameter.MOUNT_POINT_TMP);
//            rollBackChain.addRollBack("rm -rf "+ Parameter.MOUNT_POINT_TMP);

            //创建挂载点
            sshExecutor.exec("mkdir -p " + Parameter.MOUNT_POINT);
            log.info("--------------create mount point " + Parameter.MOUNT_POINT);

            //将挂载点的文件移动至临时文件夹
//            sshExecutor.exec("mv " + Parameter.MOUNT_POINT + "/* " + Parameter.MOUNT_POINT_TMP);
//            log.info("------------mv files to tmp dir");
//            rollBackChain.addRollBack("mv " + Parameter.MOUNT_POINT_TMP + "/* " + Parameter.MOUNT_POINT);

            //挂载
            sshExecutor.exec("mount /dev/mapper/vgathena-lvathena " + Parameter.MOUNT_POINT);
            log.info("------------mount /dev/mapper/vgathena-lvathena to " + Parameter.MOUNT_POINT);
            rollBackChain.addRollBack("umount /dev/mapper/vgathena-lvathena");

            //设置开机自动挂载，不行就在每次服务器启动时重新挂载
//            sshExecutor.exec("echo  \"/dev/mapper/vgathena-lvathena " + Parameter.MOUNT_POINT + " " + fileSystemType + " defaults 0 0" + "\"  >> /etc/fstab");
//            log.info("------------write fstab file");
//            rollBackChain.addRollBack("sed -i '$d' " + "/etc/fstab");

            //将文件移动至挂载点中
//            sshExecutor.exec("cp -R " + Parameter.MOUNT_POINT_TMP + "/* " + Parameter.MOUNT_POINT);
//            log.info("------------cp tmp to mount point");

            //创建网盘和作业附件使用的目录
            sshExecutor.exec("mkdir -p " + Parameter.MOUNT_POINT_NEXTCLOUD);
            sshExecutor.exec("mkdir -p " + Parameter.MOUNT_POINT_ATTACHMENTS);

            //部署nextcloud网盘
            deployNextCloud();

        } catch (Exception e) {
            log.error("--------------mount fail " + e.getMessage());
            rollBackChain.doRollBack();
            throw new AppException(e.getMessage());
        }
    }

    private <T extends AbstractInitHandler> void addInitTarget(T handler) {
        service.submit(handler::run);
    }

    abstract class AbstractInitHandler {

        protected abstract String getOperateMessage();

        protected abstract void add() throws InterruptedException, JSchException, IOException;

        public void run() {
            boolean flag = true;
            int count = 0;
            while (flag) {
                try {
                    add();
                    flag = false;
                    log.info("InitHandler::{} success.", getOperateMessage());
                } catch (Exception ex) {
                    if(count >= 10){
                        break;
                    }
                    log.warn("InitHandler::{} failed.", getOperateMessage(), ex.getMessage());
                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        log.warn("InitHandler::Interrupted while sleep.", e);
                    }
                    count ++;
                }
            }
        }
    }
    class ModifyNextCloudConfig extends AbstractInitHandler {

        @Override
        protected String getOperateMessage() {
            return "modify NextCloud Config";
        }

        @Override
        protected void add() {
            netDiskFeignClient.init();
        }
    }

    class ModifyNextCloudFile extends AbstractInitHandler {

        @Override
        protected String getOperateMessage() {
            return "modify NextCloud file";
        }

        @Override
        protected void add() throws InterruptedException, JSchException, IOException {
            SSHConfig sshConfig = JsonUtils.parseSSHConfig(redisUtil.get(RedisUtil.REDIS_KEY_SSHCONFIG));
            try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)) {
                sshExecutor.exec("rm -rf " + NEXTCLOUD_INIT_FILE + "/*");
                sshExecutor.exec("mv /opt/matrix/H3Cloud-component/tars/athena-nextcloud-initfile/* " + NEXTCLOUD_INIT_FILE);
                log.info("-------------------------remove nextcloud init file, and mv own file ");
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
