package com.h3c.vdi.athena.netdisk.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.netdisk.model.dto.BatchShareFileDTO;
import com.h3c.vdi.athena.netdisk.model.dto.FileInfoDTO;
import com.h3c.vdi.athena.netdisk.model.dto.MoveCopyDTO;
import com.h3c.vdi.athena.netdisk.service.appconfig.AppConfigMgr;
import com.h3c.vdi.athena.netdisk.service.disk.NetDiskMgr;
import feign.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/17
 */
@Slf4j
@RestController
@RequestMapping(value = "/netdisk")
public class NetDiskController {

    @Resource
    private NetDiskMgr netDiskMgr;

    @Resource
    private AppConfigMgr appConfigMgr;

    @GetMapping(value = "/listFiles")
    public List<FileInfoDTO> listFiles(@RequestParam(name = "path") String path) throws IOException {
        log.info("list files --> path is {}", path);
        return netDiskMgr.listFiles(path);
    }

    @GetMapping(value = "/listFiles/tree")
    public List<FileInfoDTO> listTreeFiles() throws IOException {
        return netDiskMgr.listFiles();
    }

    @GetMapping(value = "/singleFile")
    public FileInfoDTO getSingleInfo(@RequestParam(name = "path") String path) throws IOException {
        String username = Utils.getLoginUsername();
        return netDiskMgr.getSingleFileInfo(path, username);
    }

    @PostMapping(value = "/uploadFile",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonDTO<String> uploadFile(@RequestPart(name = "file")MultipartFile file,
                                        @RequestParam(name = "path", defaultValue = "/", required = false) String path,
                                        @RequestParam(name = "username")String username,
                                        @RequestParam(name = "fileName")String fileName) throws IOException {
        log.info("------------upload in netdisk controller:{}", fileName);
        this.netDiskMgr.uploadFiles(file, path, username, fileName);
        return new CommonDTO<>("success");

    }

    @PostMapping(value = "/uploadFile/batch",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonDTO<String> uploadBatchFile(@RequestPart(name = "multipartFiles")MultipartFile[] files,
                                             @RequestParam(name = "path", defaultValue = "/", required = false) String path,
                                             @RequestParam(name = "username") String username) throws IOException {
        this.netDiskMgr.uploadFiles(files,path, username);
        return new CommonDTO<>("success");

    }

    @DeleteMapping(value = "/removeFile")
    public void removeFile(@RequestParam(name = "path")String[] paths) throws IOException {
        netDiskMgr.removeFile(paths);
    }

    @GetMapping(value = "/downloadFile")
    public void downloadFile(@RequestParam(name = "path")String path, @RequestParam(name = "username")String username, HttpServletResponse response) throws IOException {
        this.netDiskMgr.downloadFiles(path, username, response);
    }

    @PostMapping(value = "/shareFile")
    public void shareFile(@RequestBody BatchShareFileDTO shareFileDTO){
        this.netDiskMgr.shareFile(shareFileDTO);
    }

    @DeleteMapping(value = "/shareFile/{shareIds}")
    public void cancelShare(@PathVariable(value = "shareIds")Long[] shareIds){
        this.netDiskMgr.cancelShareFile(shareIds);
    }

    @GetMapping(value = "/sharedFile")
    public List<FileInfoDTO> sharedWithMe(@RequestParam(value = "shared_with_me")String sharedWithMe){

        return netDiskMgr.querySharedWith(sharedWithMe);
    }

    @PostMapping(value = "/createFolder")
    public void createFolder(@RequestParam(name = "path")String path) throws IOException {
        this.netDiskMgr.createFolder(path);
    }

    @GetMapping(value = "/listFiles/{type}")
    public List<FileInfoDTO> queryByType(@PathVariable(value = "type") String type){

        return netDiskMgr.queryByType(type);
    }

    @PostMapping(value = "/moveFile")
    public void moveFile(@RequestBody MoveCopyDTO moveCopyDTO){
        this.netDiskMgr.moveFile(moveCopyDTO);
    }

    @PostMapping(value = "/copyFile")
    public void copyFile(@RequestBody MoveCopyDTO moveCopyDTO){
        this.netDiskMgr.copyFile(moveCopyDTO);
    }

    @PutMapping(value = "/init")
    public void init(){
        this.appConfigMgr.initAppConfigIfNotInit();
    }
}
