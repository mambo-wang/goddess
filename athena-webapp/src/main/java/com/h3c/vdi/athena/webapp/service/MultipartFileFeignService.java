package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.AttachmentDTO;
import com.h3c.vdi.athena.webapp.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author w16051
 * @date 2018/4/26
 */
@FeignClient(value = "athena-gateway", configuration =FeignMultipartSupportConfig.class)
public interface MultipartFileFeignService {
    /********************************** 附件**************************************/
    /**
     * 下载文件
     * @param attachmentDTO
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/homework/attachments/attachment_download", method = RequestMethod.POST
            ,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void downloadAttachment(@RequestParam(value = "attachmentDTO") AttachmentDTO attachmentDTO, HttpServletResponse response) throws IOException;

    @RequestMapping(value = "/zuul/homework/users/uploadTemplate", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<UserDTO> importStuFile(@RequestPart(value = "file") MultipartFile file) throws IOException;
}
