package com.h3c.vdi.athena.netdisk.service.disk;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.h3c.vdi.athena.common.concurrent.VDIExecutorServices;
import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.*;
import com.h3c.vdi.athena.netdisk.config.rest.RestConnection;
import com.h3c.vdi.athena.netdisk.exception.ErrorCodes;
import com.h3c.vdi.athena.netdisk.feign.homework.HomeworkFeignClient;
import com.h3c.vdi.athena.netdisk.mappers.ShareMapper;
import com.h3c.vdi.athena.netdisk.model.dto.*;
import com.h3c.vdi.athena.netdisk.config.rest.NextCloudConfig;
import com.h3c.vdi.athena.netdisk.model.rest.ShareFileReq;
import com.h3c.vdi.athena.netdisk.model.entity.Share;
import com.h3c.vdi.athena.netdisk.model.xml.*;
import com.h3c.vdi.athena.netdisk.service.user.NCUserMgr;
import com.h3c.vdi.athena.netdisk.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Slf4j
@Service("netDiskMgr")
public class NetDiskMgrImpl implements NetDiskMgr {

    @Resource
    private NextCloudConfig nextCloudConfig;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RestConnection restConnection;

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private NCUserMgr ncUserMgr;

    @Resource
    private HomeworkFeignClient homeworkFeignClient;

    //文件操作使用io密集线程池
    private ExecutorService service = VDIExecutorServices.get().getIoBusyService();

    /**
     * 列出指定路径下的文件以及文件夹
     * @param path 路径
     * @return
     * @throws IOException
     */
    @Override
    public List<FileInfoDTO> listFiles(String path) throws IOException {

        String username = Utils.getLoginUsername();
        Sardine sardine = connectWithNextcloud(username);

        List<FileInfoDTO> files = listFilesWithoutShareInfo(sardine, path, username);

        //我分享给被人的文件
        List<ShareDTO> shareOwner = queryShareInfo("false");
        //别人分享给我的文件
        List<ShareDTO> shareWith = queryShareInfo("true");

        Map<String, List<ShareDTO>> groupByPath = shareOwner.stream()
                .collect(Collectors.groupingBy(ShareDTO::getPath));

        return files.stream()
                .map(e -> addShareInfo(e, groupByPath, shareWith))
                .collect(Collectors.toList());
    }

    public List<FileInfoDTO> listFilesWithoutShareInfo(Sardine sardine, String path, String username) {

        try {
            String uri = NextCloudUrls.File.BASE_WEBDAV + path;
            String url = nextCloudConfig.accessUrl(uri);
            List<FileInfoDTO> files = listFiles(url, sardine, path, username);
            return files;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 列出目录树
     * @return
     * @throws IOException
     */
    @Override
    public List<FileInfoDTO> listFiles() throws IOException {

        String username = Utils.getLoginUsername();
        Sardine sardine = connectWithNextcloud(username);
        List<FileInfoDTO> files = listFilesWithoutShareInfo(sardine, "/", username);
        for (FileInfoDTO fileInfoDTO:files){
            recursion(fileInfoDTO, sardine, username);
        }
        return files;
    }

    //递归文件夹
    private void recursion(FileInfoDTO fileInfoDTO, Sardine sardine, String username) {
        if(Objects.equals(fileInfoDTO.getType(), FileType.folder.getType())){
            List<FileInfoDTO> fileInfoDTOS = this.listFilesWithoutShareInfo(sardine, fileInfoDTO.getPath(), username);
            if(CollectionUtils.isEmpty(fileInfoDTOS)){
                fileInfoDTO.setSubFiles(Collections.emptyList());
            }else {
                fileInfoDTO.setSubFiles(fileInfoDTOS);
                for (FileInfoDTO fileInfo: fileInfoDTOS){
                    recursion(fileInfo, sardine, username);
                }
            }
        }
    }

    private FileInfoDTO addShareInfo(FileInfoDTO fileInfoDTO, Map<String, List<ShareDTO>> groupByPath, List<ShareDTO> shareWith){
        String path = fileInfoDTO.getPath();

        fileInfoDTO.setShared(Constant.Share.SHARE_NO);

        //我分享给别人
        if(!CollectionUtils.isEmpty(groupByPath.get(path))){
            fileInfoDTO.setShared(Constant.Share.SHARE_OWNER);
            List<ShareDTO> shareDTOS = groupByPath.get(path).stream().map(shareDTO -> {
                shareDTO.setShared(Constant.Share.SHARE_OWNER);
                return shareDTO;
            }).collect(Collectors.toList());
            fileInfoDTO.setShares(shareDTOS);
        }

        //别人分享给我
        Optional<ShareDTO> shareDTOOptional = shareWith.stream().filter(e -> StringUtils.equals(e.getPath(), fileInfoDTO.getPath())).findFirst();
        if(shareDTOOptional.isPresent()){
            ShareDTO shareDTO1 = shareDTOOptional.get();
            fileInfoDTO.setShared(Constant.Share.SHARE_WITH);
            shareDTO1.setShared(Constant.Share.SHARE_WITH);
            List<ShareDTO> shareDTOS = fileInfoDTO.getShares();
            if(CollectionUtils.isEmpty(shareDTOS)){
                fileInfoDTO.setShares(Collections.singletonList(shareDTO1));
            }else {
                shareDTOS.add(shareDTO1);
                fileInfoDTO.setShares(shareDTOS);
            }
        }
        return fileInfoDTO;
    }

    @Deprecated
    @Transactional(readOnly = true)
    private List<ShareDTO> queryShareByShareWith(String shareWithId){

        List<Share> shares = shareMapper.getShareListBySharedUserId(shareWithId, Constant.SEARCH_TYPE_USER);

        return shares.stream()
                .map(this::shareWithConvert)
                .collect(Collectors.toList());
    }

    @Deprecated
    private ShareDTO shareWithConvert(Share share){

        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        UserResponseData data = this.ncUserMgr.queryUserByUserId(shareDTO.getUIdOwner());
        String displayNameOwner = data.getData().getDisplayName();
        shareDTO.setDisplayNameOwner(displayNameOwner);
        if(Objects.equals(shareDTO.getItemType(), Constant.Share.ITEM_TYPE_FOLDER)){
            shareDTO.setFileTarget(shareDTO.getFileTarget() + "/");
        }
        return shareDTO;
    }

    @Deprecated
    @Transactional(readOnly = true)
    private List<ShareDTO> queryShareByOwnerId(String ownerId){
        List<Share> shares = shareMapper.getShareListByOwnerId(ownerId);

        return shares.stream()
                .map(this::shareOwnerConvert)
                .collect(Collectors.toList());
    }

    @Deprecated
    private ShareDTO shareOwnerConvert(Share share){
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);

        UserResponseData data = this.ncUserMgr.queryUserByUserId(shareDTO.getShareWith());
        String displayNameShareWith = data.getData().getDisplayName();

        shareDTO.setShareWithDisplayName(displayNameShareWith);
        if(Objects.equals(shareDTO.getItemType(), Constant.Share.ITEM_TYPE_FOLDER)){
            shareDTO.setFileTarget(shareDTO.getFileTarget() + "/");
        }
        return shareDTO;
    }

    @Override
    public void uploadFiles(MultipartFile file, String path, String username, String fileName) throws IOException {
        String uri = NextCloudUrls.File.BASE_WEBDAV + path + fileName;
        String url = nextCloudConfig.accessUrl(uri);
        Sardine sardine = connectWithNextcloud(username);
        uploadFiles(url, sardine, file);
    }

    @Override
    public void uploadFiles(MultipartFile[] files, String path, String username) throws IOException {

        Sardine sardine = connectWithNextcloud(username);
        for(MultipartFile file: files){
            String uri = NextCloudUrls.File.BASE_WEBDAV + path + file.getOriginalFilename();
            String url = nextCloudConfig.accessUrl(uri);
            uploadFiles(url, sardine, file);
        }
    }

    @Override
    public void downloadFiles(String path, String username, HttpServletResponse response) throws IOException {

        String uri = NextCloudUrls.File.BASE_WEBDAV + path;
        String url = nextCloudConfig.accessUrl(uri);
        Sardine sardine = connectWithNextcloud(username);

        FileInfoDTO fileInfoDTO = this.getSingleFileInfo(path, username);

        if(Objects.equals(fileInfoDTO.getType(), FileType.folder.getType())){
            downloadZipFile(response, fileInfoDTO, fileInfoDTO.getName(), sardine, username);
        }else {
            downloadSingleFile(response, sardine, url, fileInfoDTO.getName());
        }
    }

    /*
    下载压缩包
     */
    private void downloadZipFile(HttpServletResponse response, FileInfoDTO fileInfoDTO, String name, Sardine sardine, String username) throws UnsupportedEncodingException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode( name + ".zip", "UTF-8"));
        try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream(), BasicConstant.CHARSET_GBK)){

            compress(fileInfoDTO, zos, name, sardine, username);

        } catch (IOException e) {
            e.printStackTrace();
            log.warn("download compress file fail, {}", e.getMessage());
            throw new AppException(ErrorCodes.NETDISK_DOWNLOAD_MULTI_FAIL);
        }
    }

    /**
     * 压缩文件夹
     */
    private void compress(FileInfoDTO fileInfoDTO, ZipOutputStream zos, String name, Sardine sardine, String username) throws IOException {
        byte[] buf = new byte[2048];

        //文件夹
        if(Objects.equals(fileInfoDTO.getType(), FileType.folder.getType())){
            List<FileInfoDTO> fileInfoDTOS = this.listFilesWithoutShareInfo(sardine, fileInfoDTO.getPath(), username);
            if(CollectionUtils.isEmpty(fileInfoDTOS)){
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            } else {
                for (FileInfoDTO file : fileInfoDTOS){
                    compress(file, zos, name + "/" + file.getName(), sardine, username);
                }
            }
        } else {
            String uri = NextCloudUrls.File.BASE_WEBDAV + fileInfoDTO.getPath();
            String url = nextCloudConfig.accessUrl(uri);

            zos.putNextEntry(new ZipEntry(name));
            int len;
            log.info("------------get input stream in compress method : {}", uri);
            InputStream in = sardine.get(url);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            zos.flush();
            zos.closeEntry();

            in.close();
        }
    }

    private void downloadSingleFile(HttpServletResponse response, Sardine sardine, String url, String name) {

        BufferedOutputStream out = null;
        InputStream in = null;
        try{
            response.reset();
            out = new BufferedOutputStream(response.getOutputStream());
            //第一步：设置响应类型：应用程序强制下载。将ContentType设置成"application/force-download"就不需要设置具体的附件类型了
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            //设置响应头，对文件进行url编码
            String fileName = URLEncoder.encode(name,"UTF-8");
            //服务端向客户端游览器发送文件时，如果是浏览器支持的文件类型，一般会默认使用浏览器打开，比如txt、jpg等，会直接在浏览器中显示，如果需要提示用户保存，就要利用Content-Disposition进行一下处理，关键在于一定要加上attachment：
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            log.info("------------------get input stream :{}", url);
            in = sardine.get(url);
            byte[] b=new byte[2048];
            int len=0;
            while ((len=in.read(b))!=-1){
                out.write(b,0,len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            log.warn("download single file fail, {}", e.getMessage());
            throw new AppException(ErrorCodes.NETDISK_DOWNLOAD_SINGLE_FAIL);
        }  finally {
            try{
                if(Objects.nonNull(in)){
                    in.close();
                }
                if(Objects.nonNull(out)){
                    out.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public FileInfoDTO getSingleFileInfo(String path, String username) throws IOException {
        String uri = NextCloudUrls.File.BASE_WEBDAV + path;
        String url = nextCloudConfig.accessUrl(uri);

        Sardine sardine = connectWithNextcloud(username);
        log.info("--------------list the single file: {}", uri);
        List<DavResource> resources = sardine.list(url);

        return resources.stream()
                .peek(s -> log.info(s.toString()))
                .map(s -> ModelConverter.fileConvert(s, username))
                .filter(s -> Objects.equals(s.getPath(), path))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCodes.NETDISK_FILE_NOT_FOUND));
    }

    private void removeFile(String path, Sardine sardine) throws IOException {
        String uri = NextCloudUrls.File.BASE_WEBDAV + path;
        String url = nextCloudConfig.accessUrl(uri);
        log.info("----------delete a file: {}", url);
        sardine.delete(url);
    }

    @Override
    public void removeFile(String[] paths) {

        Sardine sardine = this.connectWithNextcloud();
        Stream.of(paths).forEach(path -> {
            try {
                removeFile(path, sardine);
            } catch (IOException e) {
                log.error("delete file fail：{}, reason:{}", path, e.getMessage());
                throw new AppException(ErrorCodes.NETDISK_DELETE_FAIL);
            }
        });
    }

    @Override
    public void shareFile(ShareFileDTO shareFileDTO) {
        connectWithCustom();
        shareFile(shareFileDTO.getShareType(), shareFileDTO.getShareWith(), shareFileDTO.getPath());
    }

    private void shareFile(Integer shareType,String shareWith,String path){
        ShareFileReq shareFileReq = new ShareFileReq();
        shareFileReq.setPath(path);
        shareFileReq.setShareType(shareType);
        shareFileReq.setShareWith(shareWith);
        if(Objects.equals(shareType, Constant.SEARCH_TYPE_USER)){
            shareFileReq.setPermissions(Constant.Share.PERMISSION_USER);
        } else {
            shareFileReq.setPermissions(Constant.Share.PERMISSION_GROUP);
        }
        try {
            connectWithCustom();
            HttpEntity<ShareFileReq> shareFileReqHttpEntity = new HttpEntity<>(shareFileReq);
            ShareWrap shareWrap = restConnection.postWithCustom(NextCloudUrls.File.SHARE, shareFileReqHttpEntity, ShareWrap.class).getBody();
            log.info("share response is {}", shareWrap);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("share fail because of {}", e.getMessage());
        }
    }

    @Override
    public void shareFile(BatchShareFileDTO shareFileDTO) {

        shareFileDTO.getShareWith()
                .forEach(shareWith -> shareFileDTO.getPath()
                        .forEach(onePath -> shareFile(Constant.SEARCH_TYPE_USER, shareWith, onePath)));
    }

    private void cancelShareFile(Long shareId) {
        String url = String.format(NextCloudUrls.File.SHARE_DELETE, shareId);
        OcsBaseData ocsBaseData = restConnection.deleteWithCustom(url, new ParameterizedTypeReference<OcsBaseData>() {}).getBody();
        ocsBaseData.checkResult();

    }

    @Override
    public void cancelShareFile(Long[] shareIds) {
        connectWithCustom();
        Stream.of(shareIds).forEach(this::cancelShareFile);
    }

    @Override
    public List<FileInfoDTO> queryByType(String type) {

        SSHConfig sshConfig = JsonUtils.parseSSHConfig(redisUtil.get(RedisUtil.REDIS_KEY_SSHCONFIG));
        try (SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){
            String username = Utils.getLoginUsername();
            String absolutePath = String.format(NextCloudUrls.File.ABSOLUTE_PATH, username);
            Set<String> result = sshExecutor.exec("find "+absolutePath+" -regextype posix-extended -regex \".*\\.(" + FileType.findValueByDesc(type) + ")\"");
            return result.stream()
                    .filter(StringUtils::isNotEmpty)
                    .map(s -> {
                        FileInfoDTO dto = new FileInfoDTO();
                        dto.setName(StringUtils.substringAfterLast(s,"/"));
                        dto.setPath(StringUtils.substringAfter(s, absolutePath));
                        dto.setAbsolutePath(s);
                        return dto;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            log.warn("query by type fail, {}", e.getMessage());
            throw new AppException(ErrorCodes.NETDISK_QUERY_TYPE_FAIL);
        }
    }

    @Override
    public void createFolder(String path) throws IOException {
        String username = Utils.getLoginUsername();
        Sardine sardine = connectWithNextcloud(username);
        String uri = String.format(NextCloudUrls.File.CREATE_FOLDER, username,path);
        String url = nextCloudConfig.accessUrl(uri);
        log.info("------------------create directory: {}", uri);
        sardine.createDirectory(url);
    }

    @Override
    public void moveFile(MoveCopyDTO moveCopyDTO){
        String username = Utils.getLoginUsername();

        Sardine sardine = connectWithNextcloud(username);

        moveCopyDTO.getSrcPath().forEach(sourcePath -> {
            try {
                moveFile(sourcePath, moveCopyDTO.getDestPath(), sardine, username);
                if(StringUtils.endsWith(sourcePath, "/")){
                    removeFile(sourcePath, sardine);
                }
            } catch (IOException e) {
                log.warn("move file fail {}", e.getMessage());
                throw new AppException(ErrorCodes.NETDISK_MOVE_FAIL);
            }
        });
    }

    private void moveFile(String sourcePath, String destinationPath, Sardine sardine, String username) throws IOException {
        if(StringUtils.endsWith(sourcePath, "/")){
            String folder = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(sourcePath, "/"), "/") + "/";
            destinationPath += folder;
            this.createFolder(destinationPath);
            List<FileInfoDTO> files = this.listFilesWithoutShareInfo(sardine, sourcePath, username);
            for (FileInfoDTO file: files) {
                moveFile(file.getPath(), destinationPath, sardine, username);
            }
        } else {
            String sourceUri = NextCloudUrls.File.BASE_WEBDAV + sourcePath;
            String sourceUrl = nextCloudConfig.accessUrl(sourceUri);

            String destinationUri = NextCloudUrls.File.BASE_WEBDAV + destinationPath + StringUtils.substringAfterLast(sourcePath, "/");
            String destinationUrl = nextCloudConfig.accessUrl(destinationUri);
            log.info("---------------------move file: {} to {}", sourceUri, destinationUri);
            //移动，不覆盖
            sardine.move(sourceUrl, destinationUrl, false);
        }
    }

    @Override
    public void copyFile(MoveCopyDTO moveCopyDTO) {
        String username = Utils.getLoginUsername();
        Sardine sardine = connectWithNextcloud(username);

        moveCopyDTO.getSrcPath().forEach(srcPath -> {
            try {
                copyFile(srcPath, moveCopyDTO.getDestPath(), sardine, username);
            } catch (IOException e) {
                log.warn("copy file fail {}", e.getMessage());
                throw new AppException(ErrorCodes.NETDISK_COPY_FAIL);
            }
        });
    }

    private void copyFile(String sourcePath, String destinationPath, Sardine sardine, String username) throws IOException {

        if(StringUtils.endsWith(sourcePath, "/")){
            String folder = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(sourcePath, "/"), "/") + "/";
            destinationPath += folder;
            this.createFolder(destinationPath);
            List<FileInfoDTO> files = this.listFilesWithoutShareInfo(sardine, sourcePath, username);
            for (FileInfoDTO file: files) {
                copyFile(file.getPath(), destinationPath, sardine, username);
            }
        } else {
            String sourceUri = NextCloudUrls.File.BASE_WEBDAV + sourcePath;
            String sourceUrl = nextCloudConfig.accessUrl(sourceUri);

            String destinationUri = NextCloudUrls.File.BASE_WEBDAV + destinationPath + StringUtils.substringAfterLast(sourcePath,"/");
            String destinationUrl = nextCloudConfig.accessUrl(destinationUri);
            log.info("-------------------copy file: {} to {}", sourceUri, destinationUri);
            //复制
            sardine.copy(sourceUrl, destinationUrl, false);
        }

    }

    @Override
    public List<FileInfoDTO> querySharedWith(String sharedWith) {

        Map<String, List<ShareDTO>> sharedWithMe;
        Map<String, List<ShareDTO>> shareWithOthers;
        if(StringUtils.equals(sharedWith, "true")){

            sharedWithMe = queryShareInfo(sharedWith).stream()
                    .map(shareDTO -> {
                        shareDTO.setShared(Constant.Share.SHARE_WITH);
                        return shareDTO;
                    }).collect(Collectors.groupingBy(ShareDTO::getPath));

            shareWithOthers = queryShareInfo("false").stream()
                    .map(shareDTO -> {
                        shareDTO.setShared(Constant.Share.SHARE_OWNER);
                        return shareDTO;
                    }).collect(Collectors.groupingBy(ShareDTO::getPath));

        }else{

            sharedWithMe = queryShareInfo(sharedWith).stream()
                    .map(shareDTO -> {
                        shareDTO.setShared(Constant.Share.SHARE_OWNER);
                        return shareDTO;
                    }).collect(Collectors.groupingBy(ShareDTO::getPath));

            shareWithOthers = queryShareInfo("true").stream()
                    .map(shareDTO -> {
                        shareDTO.setShared(Constant.Share.SHARE_WITH);
                        return shareDTO;
                    }).collect(Collectors.groupingBy(ShareDTO::getPath));

        }
        return sharedWithMe.entrySet().stream()
                .map(entry -> {
                    List<ShareDTO> shareDTOS = shareWithOthers.get(entry.getKey());
                    if (!CollectionUtils.isEmpty(shareDTOS)) {
                        entry.getValue().addAll(shareDTOS);
                    }
                    return entry;
                }).map(this::shareToFileDTO)
                .collect(Collectors.toList());
    }

    private List<ShareDTO> queryShareInfo(String sharedWith){

        connectWithCustom();
        String url = String.format(NextCloudUrls.File.SHARED_WITH, sharedWith);
        ShareGetWrap shareWrap = restConnection.getWithCustom(url, new ParameterizedTypeReference<ShareGetWrap>() {}).getBody();
        List<ShareInfo> shareInfos = shareWrap.getData().getElement();
        if(CollectionUtils.isEmpty(shareInfos)){
            return Collections.emptyList();
        }
        List<ShareDTO> shareDTOS = shareInfos.stream()
                .map(this::shareInfoToDTO)
                .collect(Collectors.toList());
        return shareDTOS;
    }

    private ShareDTO shareInfoToDTO(ShareInfo shareInfo){

        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setId(shareInfo.id);
        shareDTO.setShareType(shareInfo.shareType);
        shareDTO.setUIdOwner(shareInfo.uIdOwner);
        shareDTO.setDisplayNameOwner(shareInfo.displayNameOwner);
        shareDTO.setSTime(shareInfo.sTime);

        String path = shareInfo.path;
        if(StringUtils.equals(shareInfo.itemType, Constant.Share.ITEM_TYPE_FOLDER)){
            path += "/";
        }
        shareDTO.setPath(path);
        shareDTO.setItemType(shareInfo.itemType);
        shareDTO.setFileTarget(shareInfo.fileTarget);
        shareDTO.setShareWith(shareInfo.shareWith);
        shareDTO.setShareWithDisplayName(shareInfo.shareWithDisplayName);
        shareDTO.setUIdFileOwner(shareInfo.uIdFileOwner);
        shareDTO.setDisplayNameFileOwner(shareInfo.displayNameFileOwner);
        try {
            String groups = homeworkFeignClient.queryGroupsByUsername(shareInfo.shareWith);
            shareDTO.setShareWithGroupInfo(groups);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("query user group info fail, {}", e.getMessage());
        }
        return shareDTO;
    }

    private FileInfoDTO shareToFileDTO(Map.Entry<String, List<ShareDTO>> entry){
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        fileInfoDTO.setShares(entry.getValue());
        String itemType = entry.getValue().get(0).getItemType();
        String path = entry.getValue().get(0).getPath();
        Long time = entry.getValue().get(0).getSTime();
        fileInfoDTO.setModified(PoiUtils.formatFullDateTime(time));
        fileInfoDTO.setPath(path);
        if(Objects.equals(FileType.folder.getDesc(), itemType)){
            fileInfoDTO.setType(FileType.folder.getType());
            fileInfoDTO.setName(StringUtils.substringAfterLast(StringUtils.substringBeforeLast(entry.getKey(), "/"),"/"));
        } else {
            fileInfoDTO.setName(StringUtils.substringAfterLast(entry.getKey(), "/"));
            fileInfoDTO.setType(FileType.findType(fileInfoDTO.getName()));
        }
        return fileInfoDTO;
    }

    /**
     * 新建rest连接
     */
    private void connectWithCustom(){
        //获取当前登陆用户
        String username = Utils.getLoginUsername();
        if(!redisUtil.hasKey(username)){
            log.warn("redis get user password of {} fail", username);
            throw new AppException(ErrorCodes.USER_PASSWORD_GET_FAIL);
        }
        String password = (String) redisUtil.get(username);
        restConnection.createRestClient(username, password);
    }

    /**
     * 或者nextcloud sardine连接
     * @return 连接信息
     */
    private Sardine connectWithNextcloud(){
        //获取当前登陆用户
        String username = Utils.getLoginUsername();
        if(!redisUtil.hasKey(username)){
            log.warn("get user password from redis of {} fail", username);
            throw new AppException(ErrorCodes.USER_PASSWORD_GET_FAIL);
        }
        String password = (String) redisUtil.get(username);
        Sardine sardine = SardineFactory.begin(username,password);
        log.info("connected with nextcloud, username is {}, password is {}", username, password);
        return sardine;
    }

    /**
     * 获取nextcloud sardine连接
     * @return 连接信息
     */
    private Sardine connectWithNextcloud(String username){
        if(!redisUtil.hasKey(username)){
            log.warn("redis get password of {} fail", username);
            throw new AppException(ErrorCodes.USER_PASSWORD_GET_FAIL);
        }
        String password = (String) redisUtil.get(username);
        Sardine sardine = SardineFactory.begin(username,password);
        log.info("connected with nextcloud, username is {}, password is {}", username, password);
        return sardine;
    }

    private List<FileInfoDTO> listFiles(String url, Sardine sardine, String path, String username) throws IOException {
        log.info("----------------list files: {}", url);
        List<DavResource> resources = sardine.list(url);
        if(CollectionUtils.isEmpty(resources)){
            return Collections.emptyList();
        }
        return resources.stream()
                .map(s -> ModelConverter.fileConvert(s, username))
                .filter(fileInfoDTO -> !Objects.equals(fileInfoDTO.getPath(), path))
                .collect(Collectors.toList());
    }

    private void uploadFiles(String url, Sardine sardine, MultipartFile file) throws IOException {

        service.submit(()->{
            try {
                log.info("-------------------upload file: {}", url);
                sardine.put(url, file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                log.error("upload file to nextcloud fail",e);
                throw new AppException(ErrorCodes.NETDISK_UPLOAD_FAIL);
            }
        });
    }


}
