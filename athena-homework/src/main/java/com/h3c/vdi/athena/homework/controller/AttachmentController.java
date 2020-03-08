package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.dto.AttachmentDTO;
import com.h3c.vdi.athena.homework.service.attachment.AttachmentService;
import feign.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by w16051 on 2018/3/28.
 */
@RestController
@RequestMapping(value = "/attachments")
public class AttachmentController {

    @Resource
    AttachmentService attachmentService;

    /**
     * 下载文件
     * @param attachmentDTO
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/attachment_download")
    public void downloadAttachment(@RequestBody AttachmentDTO attachmentDTO, HttpServletResponse response) throws IOException{
        attachmentService.downloadAttachment(attachmentDTO,response);
    }



    /**
     * 上传文件，一次上传一个
     * @param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/attachment_upload", method = RequestMethod.POST)
    public Long uploadAttachment(@Param("file") MultipartFile file,@Param("username") String username) throws IOException{
        return attachmentService.uploadAttachment(file,username);
    }
}
