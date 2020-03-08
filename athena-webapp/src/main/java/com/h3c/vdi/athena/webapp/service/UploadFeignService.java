package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.common.model.CommonDTO;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by w16051 on 2018/11/8.
 * 用来存放服务间调用上传文件的接口
 */
public interface UploadFeignService {
    /**
     * 上传文件为作业附件，一次上传一个
     * @return
     * @throws IOException
     */
    @RequestLine("POST /zuul/homework/attachments/attachment_upload")
    Long uploadAttachment(@Param("file") MultipartFile file,@Param("username") String username);

    @RequestLine("POST /zuul/netdisk/netdisk/uploadFile")
    CommonDTO<String> uploadFiles(@Param( "file") MultipartFile file, @Param("path") String path, @Param("username") String username, @Param("fileName") String fileName);

    @RequestLine("POST /zuul/netdisk/netdisk/uploadFile/batch")
    CommonDTO<String> uploadBatchFiles(@Param("multipartFiles") MultipartFile[] files, @Param("path") String path, @Param("username") String username);
}
