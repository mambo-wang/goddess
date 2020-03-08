package com.h3c.vdi.athena.homework.service.attachment;

import com.h3c.vdi.athena.common.concurrent.VDIExecutorServices;
import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.JsonUtils;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.common.utils.SSHExecutor;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.AttachmentDao;
import com.h3c.vdi.athena.homework.dto.AttachmentDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.AttachmentEntity;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.service.user.UserService;
import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by w16051 on 2018/3/27.
 */
@Service
public class AttachmentServiceImpl implements AttachmentService{

    @Resource
    private AttachmentDao attachmentDao;

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    //单个文件上传最大限制，目前是200MB
    private final Long MAX_ATTACHMENT_SIZE=20971520000L;
    /**
     * 线程池
     */
    private ExecutorService ioService = VDIExecutorServices.get().getIoBusyService();

    private static StringManager sm = StringManager.getManager("Attachment");
    private final String ATTACHMENT_PATH="E:\\upload";
    private final Logger logger= LoggerFactory.getLogger(AttachmentServiceImpl.class);
    @Override
    public List<AttachmentDTO> getAttachmentDTOsByTypeAndRelatedId(Integer type, Long relatedId)
    {
        List<AttachmentEntity> attachments = attachmentDao.queryAttachmentsByTypeAndRelatedIdAndDeleted(type,relatedId, CommonConst.NOT_DELETED);
        List<AttachmentDTO> attachmentDTOS = attachments.stream().map(this::convertFromAttachmentToAttachmentDTO).collect(Collectors.toList());
        return attachmentDTOS;
    }

    private AttachmentDTO convertFromAttachmentToAttachmentDTO(AttachmentEntity attachmentEntity)
    {
        AttachmentDTO attachmentDTO=new AttachmentDTO();
        BeanUtils.copyProperties(attachmentEntity,attachmentDTO);
        return attachmentDTO;
    }

    @Override
    public Long addAttachment(String name,String url)
    {
        AttachmentEntity attachmentEntity=new AttachmentEntity();
        attachmentEntity.setName(name);
        attachmentEntity.setUrl(url);
        attachmentEntity.setDeleted(CommonConst.NOT_DELETED);
        AttachmentEntity attachment=attachmentDao.save(attachmentEntity);
        return attachment.getId();
    }

    @Override
    public void setTypeAndRelatedId(Long id,Integer type,Long relatedId)
    {
        AttachmentEntity attachmentEntity=attachmentDao.getOne(id);
        attachmentEntity.setType(type);
        attachmentEntity.setRelatedId(relatedId);
        attachmentDao.save(attachmentEntity);
    }

    @Override
    public void downloadAttachment(AttachmentDTO attachmentDTO, HttpServletResponse response) throws IOException{
        String fileName=attachmentDTO.getName();
        //第一步：设置响应类型：应用程序强制下载。将ContentType设置成"application/force-download"就不需要设置具体的附件类型了
        response.setContentType("application/force-download");
        //第二步：读取文件
        String path=attachmentDTO.getUrl();
        InputStream in = new FileInputStream(path);
        //设置响应头，对文件进行url编码
        fileName = URLEncoder.encode(fileName,"UTF-8");
        //Content-Disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        //格式说明：content-disposition = "Content-Disposition" ":" disposition-type *( ";" disposition-parm ) 　
        //字段说明：
        //Content-Disposition为属性名
        //disposition-type是以什么方式下载，如attachment为以附件方式下载
        //disposition-parm为默认保存时的文件名
        //服务端向客户端游览器发送文件时，如果是浏览器支持的文件类型，一般会默认使用浏览器打开，比如txt、jpg等，会直接在浏览器中显示，如果需要提示用户保存，就要利用Content-Disposition进行一下处理，关键在于一定要加上attachment：
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        response.setContentLength(in.available());
        //第三步：开始copy
        OutputStream out = response.getOutputStream();
        byte[] b=new byte[1024];
        int len=0;
        while ((len=in.read(b))!=-1){
            out.write(b,0,len);
        }
        out.flush();
        out.close();
        in.close();
    }

    @Override
    public List<Long> getAttachmentIDsByTypeAndRelatedId(Integer type,Long relatedId){
        return attachmentDao.queryAttachmentIdsByTypeAndRelatedIdAndDeleted(type,relatedId,CommonConst.NOT_DELETED);
    }

    @Override
    public void deleteAttachmentById(Long id){
        AttachmentEntity attachmentEntity=attachmentDao.getOne(id);
        deleteAttachment(attachmentEntity);
    }

    @Override
    public void deleteAttachment(AttachmentEntity attachmentEntity){
        if(Objects.nonNull(attachmentEntity)){
            String path=attachmentEntity.getUrl();
            attachmentEntity.setDeleted(CommonConst.DELETED);
            attachmentDao.save(attachmentEntity);
            File file=new File(path);
            if(file.exists()){
                Boolean result=file.delete();
                if(result){
                    logger.info("delete attachment:"+attachmentEntity.getName()+"  successfully");
                }else{
                    logger.info("delete attachment:"+attachmentEntity.getName()+"  failed");
                }
            }
        }
    }

    @Override
    public void deleteAttachmentsByTypeAndRelatedId(Integer type,Long relatedId){
        List<AttachmentEntity> attachmentEntities=attachmentDao
                .queryAttachmentsByTypeAndRelatedIdAndDeleted(type,relatedId,CommonConst.NOT_DELETED);
        attachmentEntities.stream().forEach(this::deleteAttachment);
    }

    @Override
    public Long uploadAttachment(MultipartFile file, String username) throws IOException{
        Future<Long> future = ioService.submit(()->this.uploadFile(file,username));
        Long attachmentId;
        try {
            attachmentId = future.get();
        }catch (InterruptedException ie){
            logger.warn("upload attachment failed because: "+ie.getMessage());
            throw new AppException(ErrorCodes.ATTACHMENT_UPLOAD_FAILED);
        }catch (ExecutionException e){
            logger.warn("upload attachment failed because: "+e.getMessage());
            throw new AppException(ErrorCodes.ATTACHMENT_UPLOAD_FAILED);
        }
        logger.info("upload file"+file.getOriginalFilename()+"successfully");
        return attachmentId;
    }

    /**
     * 上传方法，用来作为Callable方法
     * @param file
     * @return
     * @throws IOException
     */
    private Long uploadFile(MultipartFile file, String userName) throws IOException{
        //获取指定存储路径，如果不存在就新建一个
        Path path = Paths.get(ATTACHMENT_PATH,userName);
        if(!Files.exists(path))
            Files.createDirectory(path);
//        //创造请求解析器
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
//                request.getSession().getServletContext());
//        //判断 request 是否有文件上传，即多部分请求
//        if (!multipartResolver.isMultipart(request)) {
//            System.out.println("没有文件上传！");
//        }
//        //转换成多部分request
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        //创建ID用来返回数据库中附件实体的ID
        Long attachId;
//        //取得request中的所有文件名
//        Iterator<String> iter = multiRequest.getFileNames();
//        while (iter.hasNext()) {
            //取得上传文件
//            MultipartFile multipartFile = multiRequest.getFile(iter.next());
            //检查附件是否超过了附件的最大限制
            if(file.getSize()>MAX_ATTACHMENT_SIZE){
                logger.warn("attachment is larger than max size of attachment");
                throw new AppException(sm.getString("attachment.too.large",MAX_ATTACHMENT_SIZE/1024/1024));
            }
            //获取上传文件的名称
            String fileName=file.getOriginalFilename();
            String url;
            url = this.upload(path.toString(),file);
            attachId = this.addAttachment(fileName,url);
//        }
        return attachId;
    }
    /**
     * 执行上传操作，返回上传后文件的完整路径，文件名被重命名为UUID用来避免重名
     * @param uploadPath
     * @param multipartFile
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    private String upload(String uploadPath,MultipartFile multipartFile) throws IllegalStateException,IOException{
        String fileName=multipartFile.getOriginalFilename();
        //修改文件名称uuid
        String fileUUIDname = UUID.randomUUID().toString();
        //获取后缀
        String postfix=fileName.substring(fileName.lastIndexOf(".")+1);
        //修改后完整的文件名称
        String NewFileName=fileUUIDname+"."+postfix;
        //完整路径
        Path completePath=Paths.get(uploadPath,NewFileName);
        String completePathString = completePath.toString();
        if (!StringUtils.isEmpty(fileName)){
            File targetFile=new File(completePathString);
            //转存文件
            multipartFile.transferTo(targetFile);
            return completePathString;
        }else
            return "null";
    }

    public List<Long> makeAttachmentsByUrls(List<String> urls){
        List<Long> attachmentIds;
        try {
            attachmentIds = this.makeAttachments(urls,null,null);
        }catch (Exception e){
            logger.warn("拷贝文件命令执行失败");
            e.printStackTrace();
            throw new AppException("根据网盘文件添加附件失败，请查看文件是否存在，并刷新后重试");
        }
        return attachmentIds;
    }

    private List<Long> makeAttachments(List<String> urls,Long relatedId,Integer relatedType) throws JSchException {
        List<Long> attachmentIds=new ArrayList<>();
        SSHConfig config = JsonUtils.parseSSHConfig(redisUtil.get(RedisUtil.REDIS_KEY_SSHCONFIG));
        SSHExecutor sshExecutor = SSHExecutor.newInstance(config);
        urls.forEach(url->attachmentIds.add(this.makeAttachment(url,sshExecutor,relatedId,relatedType)));
        sshExecutor.close();
        return attachmentIds;
    }

    private Long makeAttachment(String url,SSHExecutor sshExecutor,Long relatedId,Integer relatedType){
        String path;
        try {
            path = this.copyAttachment(url,sshExecutor);
        }catch (Exception e){
            logger.warn("拷贝文件命令执行失败");
            e.printStackTrace();
            throw new AppException("根据网盘文件添加附件失败，请查看文件是否存在，并刷新后重试");
        }
        String originalName = url.substring(url.lastIndexOf('/')+1);
        AttachmentEntity attachment = new AttachmentEntity();
        attachment.setName(originalName);
        attachment.setUrl(path);
        attachment.setDeleted(CommonConst.NOT_DELETED);
        if(Objects.nonNull(relatedId))
            attachment.setRelatedId(relatedId);
        if(Objects.nonNull(relatedType))
            attachment.setType(relatedType);
        return attachmentDao.save(attachment).getId();
    }

    private String copyAttachment(String url,SSHExecutor sshExecutor) throws InterruptedException, JSchException, IOException{
        String fileUUIDname = UUID.randomUUID().toString();
        String postfix=url.substring(url.lastIndexOf(".")+1);
        String path = ATTACHMENT_PATH + fileUUIDname + postfix;
        String cmd = "cp -rf " + url + " " + path;
        sshExecutor.exec(cmd);
        return path;
    }

    @Override
    public void addAttachmentWithHomeworkTemplate(Long homeworkTemplateId,List<Long> attachmentIds){
        List<String> attachmentUrls = attachmentDao.findAll(attachmentIds)
                .stream().map(attachmentEntity -> attachmentEntity.getUrl()).collect(Collectors.toList());
        try {
            this.makeAttachments(attachmentUrls,homeworkTemplateId,CommonConst.ATTACH_TYPE_TEMPLATE);
        }catch (Exception e){
            logger.warn("failed to copy files when add attachments for homeworkTemplate creating");
            e.printStackTrace();
            throw new AppException("添加作业模板失败，请确认作业附件是否上传成功，并刷新后重试");
        }
    }
}
