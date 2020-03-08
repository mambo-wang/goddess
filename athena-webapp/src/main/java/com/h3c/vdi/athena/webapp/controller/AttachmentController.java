package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.webapp.config.encoder.FeignSpringFormEncoder;
import com.h3c.vdi.athena.webapp.dto.AttachmentDTO;
import com.h3c.vdi.athena.webapp.service.MultipartFileFeignService;
import com.h3c.vdi.athena.webapp.service.UploadFeignService;
import feign.Feign;
import feign.Param;
import feign.jackson.JacksonDecoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by w16051 on 2018/4/17.
 */
@Api(value = "作业附件controller", tags = {"附件上传下载操作"})
@RestController
@RequestMapping(value = "/homework/attachments")
public class AttachmentController {

    @Resource
    MultipartFileFeignService multipartFileFeignService;


    @ApiOperation(value = "下载文件",notes = "")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PostMapping(value = "/attachment_download")
    public void downloadAttachment(@RequestBody AttachmentDTO attachmentDTO, HttpServletResponse response) throws IOException{
        multipartFileFeignService.downloadAttachment(attachmentDTO,response);
    }


    @Value("http://athena-gateway:58765")
    private String HTTP_FILE_UPLOAD_URL;

    /**
     * 上传文件，一次上传一个
     * @param
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传附件",notes = "每调用一次接口上传一个文件，最大不超过200MB")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/attachment_upload", method = RequestMethod.POST)
    public Long uploadAttachment(@Param("file") MultipartFile file){
        String userName = Utils.getLoginUsername();
        UploadFeignService uploadFeignService = Feign.builder()
                .encoder(new FeignSpringFormEncoder())
                .decoder(new JacksonDecoder())
                .target(UploadFeignService.class,HTTP_FILE_UPLOAD_URL);
        return uploadFeignService.uploadAttachment(file,userName);
    }
}
