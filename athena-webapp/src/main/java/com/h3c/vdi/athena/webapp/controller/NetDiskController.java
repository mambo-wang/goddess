package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.config.encoder.FeignSpringFormEncoder;
import com.h3c.vdi.athena.webapp.dto.*;
import com.h3c.vdi.athena.webapp.service.NetDiskFeignService;
import com.h3c.vdi.athena.webapp.service.UploadFeignService;
import feign.Feign;
import feign.Response;
import feign.jackson.JacksonDecoder;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author w14014
 * @date 2018/9/20
 */
@Api(value = "个人网盘Controller", tags = {"个人网盘操作接口"})
@Slf4j
@RestController
@RequestMapping(value = "/netdisk")
public class NetDiskController {

    @Resource
    private NetDiskFeignService netDiskFeignService;

    @Value("http://athena-gateway:58765")
    private String HTTP_FILE_UPLOAD_URL;

    @ApiOperation(value = "查询文件列表",notes = "查询某个目录下的文件列表，path默认值为“/”根路径（相对路径）")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/listFiles")
    public List<FileInfoDTO> listFiles(@ApiParam(value = "查询哪个路径（相对路径）下的文件")@RequestParam(name = "path", defaultValue = "/", required = false) String path) {
        return netDiskFeignService.listFiles(path);
    }

    @ApiOperation(value = "查询文件目录树",notes = "查询所有文件")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/listFiles/tree")
    public List<FileInfoDTO> listTreeFiles() {
        return netDiskFeignService.listFiles();
    }

    @ApiOperation(value = "删除文件", notes = "path为相对路径")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @DeleteMapping(value = "/removeFile")
    public void removeFile(@ApiParam(value = "文件的相对路径", required = true)@RequestParam(name = "path")String[] path) throws IOException {
        this.netDiskFeignService.removeFile(path);
    }

    @ApiOperation(value = "批量上传文件", notes = "把给定的文件上传到给定的路径（相对路径）当中")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PostMapping(value = "/uploadFile/batch",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonDTO<String> uploadBatchFile(@RequestPart(name = "multipartFiles")MultipartFile[] files,
                                             @ApiParam(value = "上传到哪个路径")@RequestParam(name = "path", defaultValue = "/", required = false) String path,
                                             @ApiParam(value = "用户名")@RequestParam(name = "username") String username) throws IOException {

        UploadFeignService uploadFeignService = Feign.builder()
                .encoder(new FeignSpringFormEncoder())
                .decoder(new JacksonDecoder())
                .target(UploadFeignService.class, HTTP_FILE_UPLOAD_URL);
        return uploadFeignService.uploadBatchFiles(files, path, username);

    }

    @ApiOperation(value = "上传文件", notes = "把给定的文件上传到给定的路径（相对路径）当中")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/uploadFile",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonDTO<String> uploadFile(@RequestPart("file") MultipartFile file,
                                        @ApiParam(value = "用户名")@RequestParam("username") String username,
                                        @ApiParam(value = "上传到哪个路径")@RequestParam(name = "path", defaultValue = "/", required = false) String path
    ) throws IOException {
        UploadFeignService uploadFeignService = Feign.builder()
                .encoder(new FeignSpringFormEncoder())
                .decoder(new JacksonDecoder())
                .target(UploadFeignService.class, HTTP_FILE_UPLOAD_URL);
        return uploadFeignService.uploadFiles(file, path, username, file.getOriginalFilename());

    }

    /**
     * 如果不使用Feign的下载会报错：
     Resolved exception caused by Handler execution: feign.codec.EncodeException: Could not write JSON:
     getOutputStream() has already been called for this response;
     nested exception is com.fasterxml.jackson.databind.JsonMappingException:
     getOutputStream() has already been called for this response (through reference chain:
     org.springframework.security.web.context.HttpSessionSecurityContextRepository$SaveToSessionResponseWrapper["response"]->
     org.springframework.security.web.firewall.FirewalledResponse["response"]->
     org.springframework.cloud.sleuth.instrument.web.TraceHttpServletResponse["response"]->
     org.apache.catalina.connector.ResponseFacade["writer"])
     * */
    @ApiOperation(value = "下载文件", notes = "path为相对路径")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/downloadFile")
    public ResponseEntity<byte[]> downloadFile(@ApiParam(value = "文件的相对路径")@RequestParam(name = "path")String path, @ApiParam(value = "用户名")@RequestParam(name = "username") String username) throws IOException {

        ResponseEntity<byte[]> result;
        InputStream inputStream = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            String fileName = "";
            //文件夹，打成压缩包下载
            if(path.endsWith("/")){
                fileName = StringUtils.substringBeforeLast(path, "/");
                fileName = StringUtils.substringAfterLast(fileName, "/");
                fileName = URLEncoder.encode(fileName + ".zip", "UTF-8");
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            }else {
                fileName = StringUtils.substringAfterLast(path, "/");
                fileName = URLEncoder.encode(fileName, "UTF-8");
                headers.add(HttpHeaders.CONTENT_TYPE, "application/force-download");

            }
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+fileName);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.add(HttpHeaders.CONNECTION, "close");
            Response responseFeign = this.netDiskFeignService.downloadFile(path, username);
            Response.Body body = responseFeign.body();
            inputStream = body.asInputStream();
            byte[] b = new byte[inputStream.available()];
            log.info("----------------the file {}'s size is {}", fileName, inputStream.available());
            inputStream.read(b);
            inputStream.close();
            result = new ResponseEntity<>(b, headers, HttpStatus.OK);
        }catch (IOException ioe){
            throw new AppException("下载错误，请稍后重试！");
        } finally {
            if(Objects.nonNull(inputStream)){
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @ApiOperation(value = "文件分类查询", notes = "type文件类型: video/document/music/picture/compress")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/listFiles/{type}")
    public List<FileInfoDTO> queryByType(@ApiParam(value = "type文件类型(视频、文档、音乐、图片、压缩包): video/document/music/picture/compress", required = true)@PathVariable(value = "type") String type){

        return netDiskFeignService.queryByType(type);
    }

    @ApiOperation(value = "分享文件", notes = "批量分享文件给多个用户")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/shareFile")
    public void shareFile(@RequestBody BatchShareFileDTO shareFileDTO){

        this.netDiskFeignService.batchShareFile(shareFileDTO);
    }

    @ApiOperation(value = "取消分享文件", notes = "批量取消分享文件给某个用户或者分组")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @DeleteMapping(value = "/shareFile/{shareIds}")
    public void cancelShare(@ApiParam(value = "某个分享的主键id", required = true)@PathVariable(value = "shareIds")Long[] shareIds){
        this.netDiskFeignService.cancelShare(shareIds);
    }

    @ApiOperation(value = "查询分享的文件", notes = "查询分享给我的文件")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/shareFile")
    public List<FileInfoDTO> sharedWithMe(@ApiParam(value = "true:别人分享给我，false:我分享给别人", required = true)@RequestParam(value = "shared_with_me")String sharedWithMe){
        List<FileInfoDTO> fileInfoDTOS = this.netDiskFeignService.sharedWithMe(sharedWithMe);
        return fileInfoDTOS;
    }

    @Deprecated
    @ApiOperation(value = "模糊搜索用户或者用户分组", notes = "模糊搜索")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/search")
    public List<SearchResultDTO> queryUsersOrGroups(@ApiParam(value = "根据这个值来搜索", required = true)@RequestParam(name = "search")String search){
        return this.netDiskFeignService.queryUsersOrGroups(search);
    }

    @ApiOperation(value = "查询用户个人网盘使用情况", notes = "查询个人网盘的使用情况")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping(value = "/quota")
    public QuotaDTO queryQuota(){
        return this.netDiskFeignService.queryQuota();
    }

    @ApiOperation(value = "创建文件夹", notes = "在某个目录下创建空文件夹")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/createFolder")
    public void createFolder(@ApiParam(value = "path为新建文件夹的path，也就是当前目录path+/新文件夹名称")@RequestParam(name = "path")String path){
        this.netDiskFeignService.createFolder(path);
    }

    @ApiOperation(value = "移动文件", notes = "批量移动文件到某文件夹，不覆盖重名文件")
    @PostMapping(value = "/moveFile")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    public void moveFile(@RequestBody MoveCopyDTO moveCopyDTO){
        this.netDiskFeignService.moveFile(moveCopyDTO);
    }

    @ApiOperation(value = "复制文件", notes = "批量复制文件到某文件夹,不覆盖重名文件")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @PostMapping(value = "/copyFile")
    public void copyFile(@RequestBody MoveCopyDTO moveCopyDTO){
        this.netDiskFeignService.copyFile(moveCopyDTO);
    }
}
