package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.*;
import feign.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/20
 */
@FeignClient(value = "athena-gateway", configuration = FeignMultipartSupportConfig.class)
public interface NetDiskFeignService {

    @RequestMapping(value = "/netdisk/netdisk/listFiles", method = RequestMethod.GET)
    List<FileInfoDTO> listFiles(@RequestParam(name = "path") String path);

    @RequestMapping(value = "/netdisk/netdisk/listFiles/tree", method = RequestMethod.GET)
    List<FileInfoDTO> listFiles();

    @RequestMapping(value = "/netdisk/netdisk/singleFile", method = RequestMethod.GET)
    FileInfoDTO getSingleInfo(@RequestParam(name = "path") String path);

    @RequestMapping(value = "/netdisk/netdisk/removeFile", method = RequestMethod.DELETE)
    void removeFile(@RequestParam(name = "path")String[] path);

    @RequestMapping(value = "/netdisk/netdisk/downloadFile", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Response downloadFile(@RequestParam(name = "path") String path, @RequestParam(name = "username")String username);

    @Deprecated
    @RequestMapping(value = "/zuul/netdisk/netdisk/uploadFile", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonDTO<String> uploadFiles(@RequestPart(value = "file") MultipartFile file, @RequestParam(name = "path") String path);

    @RequestMapping(value = "/netdisk/netdisk/createFolder", method = RequestMethod.POST)
    void createFolder(@RequestParam(name = "path")String path);


    @GetMapping(value = "/netdisk/ncuser/quota")
    QuotaDTO queryQuota();

    @GetMapping(value = "/netdisk/ncuser")
    List<SearchResultDTO> queryUsersOrGroups(@RequestParam(name = "search")String search);

    @Deprecated
    @DeleteMapping(value = "/netdisk/netdisk/shareFile/{shareId}")
    void cancelShare(@PathVariable(value = "shareId")Long shareId);

    @DeleteMapping(value = "/netdisk/netdisk/shareFile/{shareIds}")
    void cancelShare(@PathVariable(value = "shareIds")Long[] shareIds);

    @Deprecated
    @PostMapping(value = "/netdisk/netdisk/shareFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    void shareFile(@RequestBody ShareFileDTO shareFileDTO);

    @PostMapping(value = "/netdisk/netdisk/shareFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    void batchShareFile(@RequestBody BatchShareFileDTO shareFileDTO);

    @GetMapping(value = "/netdisk/netdisk/sharedFile")
    List<FileInfoDTO> sharedWithMe(@RequestParam(value = "shared_with_me")String sharedWithMe);

    @GetMapping(value = "/netdisk/netdisk/listFiles/{type}")
    List<FileInfoDTO> queryByType(@PathVariable(value = "type") String type);

    @Deprecated
    @PostMapping(value = "/netdisk/netdisk/moveFile")
    void moveFile(@RequestParam(name = "srcPath")String srcPath, @RequestParam(name = "destPath")String destPath);

    @PostMapping(value = "/netdisk/netdisk/moveFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    void moveFile(@RequestBody MoveCopyDTO moveCopyDTO);

    @Deprecated
    @PostMapping(value = "/netdisk/netdisk/copyFile")
    void copyFile(@RequestParam(name = "srcPath")String srcPath, @RequestParam(name = "destPath")String destPath);

    @PostMapping(value = "/netdisk/netdisk/copyFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    void copyFile(@RequestBody MoveCopyDTO moveCopyDTO);

}
