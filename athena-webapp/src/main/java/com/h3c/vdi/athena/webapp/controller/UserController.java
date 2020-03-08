package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.utils.PoiUtils;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.webapp.dto.HandleUserDTO;
import com.h3c.vdi.athena.webapp.dto.RegisterResultDTO;
import com.h3c.vdi.athena.webapp.dto.RegistrarDTO;
import com.h3c.vdi.athena.webapp.dto.UserDTO;
import com.h3c.vdi.athena.webapp.enums.CheckEvent;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import com.h3c.vdi.athena.webapp.service.KeystoneFeignService;
import com.h3c.vdi.athena.webapp.service.MultipartFileFeignService;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by w16051 on 2018/3/5.
 */
@Api(value = "用户管理Controller", tags = {"用户管理相关操作"})
@RestController
public class UserController {

    private final String REGISTER_SUCCESS="success";
    private final String REGISTER_WAIT="wait";
    private final String REGISTER_FAILED="fail";

    @Resource
    HomeworkFeignService homeworkFeignService;

    @Resource
    KeystoneFeignService keystoneFeignService;

    @Resource
    MultipartFileFeignService multipartFileFeignService;

    private static StringManager sm = StringManager.getManager("User");


    @ApiOperation(value = "学生注册",notes = "返回注册结果，如果失败，结果包含失败原因，如果待班长批准，结果包含班长名字")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/users/register",method = RequestMethod.POST)
    public RegisterResultDTO studentRegister(@RequestBody RegistrarDTO registrarDTO)
    {
        RegisterResultDTO result= homeworkFeignService.studentRegister(registrarDTO);
        return result;
    }


    @ApiOperation(value = "学生申请加入班级",notes = "学生申请加入班级，此时学生已经登录，但不属于任何班级")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/users/applyForClass", method = RequestMethod.POST)
    public void applyForClass(@RequestParam(value = "userId")Long userId,@RequestParam(value = "classId")Long classId){
        homeworkFeignService.applyForClass(userId,classId);
    }


    @ApiOperation(value = "查询用户",notes = "根据用户角色查询用户")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/homework/users")
    public List<UserDTO> queryUsers(@RequestParam(value = "role_type") Integer role_type) {
        return homeworkFeignService.queryUsers(role_type);
    }

    @ApiOperation(value = "查询学生",notes = "查出来已加入班级的学生")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header",
            defaultValue = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwiYWRtaW5cIixcInBhc3N3b3JkXCI6XCJhZG1pblwiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.CaT8umLPTPrYvui7p1gteLFDt-Vvr4SZtvYKUN0lyv2EGIrjqlHXmh8Bu62jMTyxKIqYm8nE2gaxJpbDLDqooQ")
    @GetMapping("/homework/users/students")
    public List<UserDTO> queryStudents() {
        return homeworkFeignService.queryStudents();
    }

    @ApiOperation(value = "重置用户密码")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping("/keystone/users/reset_password")
    public void resetPassword(@ApiParam(value = "操作用户的id", required = true)@RequestParam(value = "id") Long id){
        keystoneFeignService.resetPassword(id);
    }

    @ApiOperation(value = "修改密码")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping("/keystone/users/update_password")
    public void updatePassword(@RequestBody UserDTO userDTO){
        keystoneFeignService.updatePassword(userDTO);
    }

    @ApiOperation(value = "管理员添加用户")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PostMapping("/keystone/users")
    public Long addUser(@RequestBody UserDTO userDTO){
        return keystoneFeignService.addUser(userDTO);
    }


    @ApiOperation(value = "根据id查询用户",notes = "根据id查询用户")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/users/{id}", method = {RequestMethod.GET})
    public UserDTO getUserById(@PathVariable Long id) {
        return homeworkFeignService.getUserById(id);
    }


    @ApiOperation(value = "修改用户",notes = "修改用户")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/users",method = {RequestMethod.PUT},consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@RequestBody UserDTO userDTO) {
        homeworkFeignService.updateUser(userDTO);
    }

    @ApiOperation(value = "用户修改自己的信息",notes = "只能修改手机号和邮箱地址")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/keystone/users/update",method = {RequestMethod.PUT},consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserInfo(@RequestBody UserDTO userDTO){
        keystoneFeignService.updateUserInfo(userDTO);
    }

    @ApiOperation(value = "删除用户",notes = "可批量删除")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping( value = {"/homework/users/{idList}"}, method = {RequestMethod.DELETE})
    public void deleteUsers(@ApiParam(value = "被删除用户的id list", required = true)@PathVariable(name = "idList") ArrayList<Long> idList) {
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setData(idList);
        keystoneFeignService.deleteUsers(commonDTO);
    }

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    private void sendToUser(Long monitorId){
        simpMessagingTemplate.convertAndSend("/topic/getResponse/"+monitorId,1);
    }

    @ApiOperation(value = "查找当前用户待处理的注册申请",notes = "查找当前用户待处理的注册申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/homework/users/unhandledRegistrars")
    public List<RegistrarDTO> queryAllUnhandledRegistrars(){
        return homeworkFeignService.queryAllUnhandledRegistrars();
    }


    @ApiOperation(value = "查找所有当前登录用户为处理人的注册申请",notes = "查找所有当前登录用户为处理人的注册申请，包括待处理的和已经处理的")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/homework/users/registrars")
    public List<RegistrarDTO> queryAllRegistrars(){
        return homeworkFeignService.queryAllRegistrars();
    }


    @ApiOperation(value = "处理注册申请",notes = "处理注册申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/homework/users/registrars/check")
    public boolean check(@RequestBody HandleUserDTO handleUserDTO) {
        return homeworkFeignService.check(handleUserDTO);
    }

    @ApiOperation(value = "查找当前登录用户所在班级的所有学生",notes = "如果当前登录用户不是班长则报错")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/homework/users/classmates")
    public List<UserDTO> queryClassmates(){
        return homeworkFeignService.queryClassmates();
    }


    @ApiOperation(value = "班长将某个学生从班级中踢出来",notes = "班长将某个学生从班级中踢出来")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/homework/users/kickOut")
    public void kickOut(@RequestBody HandleUserDTO handleUserDTO){
        homeworkFeignService.kickOut(handleUserDTO);
    }

    @ApiOperation(value = "转让班长",notes = "如果当前登录用户不是班长则报错")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/homework/users/changeMonitor/{userId}")
    public void changeMonitor(@ApiParam(value = "转让为班长的用户的ID", required = true) @PathVariable Long userId){
        homeworkFeignService.changeMonitor(userId);
    }


    @ApiOperation(value = "变更申请",notes = "变更申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/homework/users/changeClass/{classId}")
    public void changeClass(@PathVariable(value = "classId") Long classId){
        homeworkFeignService.changeClass(classId);
    }


    @ApiOperation(value = "在过渡界面点击我是班长，然后输入邀请码，提交申请",notes = "在过渡界面点击我是班长，然后输入邀请码，提交申请")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping(value = "/homework/users/isMonitor/{inviteCode}")
    public void isMonitor(@PathVariable(value = "inviteCode") Integer inviteCode){
        homeworkFeignService.isMonitor(inviteCode);
    }

    @ApiOperation(value = "上传学生导入文件")
    @PostMapping(value = "/homework/users/uploadTemplate",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UserDTO> importStu(@RequestPart(name = "file") MultipartFile file) throws IOException{
        return multipartFileFeignService.importStuFile(file);
    }

    @ApiOperation(value = "导入学生")
    @PostMapping(value = "/homework/users/importStudents")
    public List<UserDTO> importStudents(@RequestBody List<UserDTO> userDTOS){
        return homeworkFeignService.importStudents(userDTOS);
    }

    @ApiOperation(value = "下载学生导入模板")
    @GetMapping(value = "/homework/users/downloadStuTemplate")
    public void downloadStuTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得文件名称，包括后缀名
        String fileName = sm.getString("user.download.tip.template") + "." + "xls";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        //创建表格
        Workbook importTemplate = this.createWorkbookMould();
        //提示浏览器以下载的形式打开窗口
        PoiUtils.downloadBrowser(importTemplate, response, fileName, request);
    }

    public Workbook createWorkbookMould(){
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<String> headersOfMould = createHeadersOfMould();
        Sheet sheet = PoiUtils
                .createSheetWithRichTitle(workbook, sm.getString("user.download.tip.template")
                        , this.getMouldTitleRowData(workbook,headersOfMould), 0);

        //this.createContentOfMould(sheet, workbook, headersOfMould);

        return workbook;
    }

    private List<RichTextString> getMouldTitleRowData(Workbook workbook, List<String> headersOfMould){
        return headersOfMould.stream()
                .map(header -> header.substring(0, header.indexOf(sm.getString("user.list.brackets.left")))
                        + "\n" + header.substring(header.indexOf(sm.getString("user.list.brackets.left"))) )
                .map(header -> this.getRichText(workbook, header))
                .collect(Collectors.toList());
    }

    private List<String> createHeadersOfMould() {
        List<String> headersOfMould = new ArrayList<>();
        headersOfMould.add(sm.getString("user.list.index.tip"));
        headersOfMould.add(sm.getString("user.list.userName.tip"));
        headersOfMould.add(sm.getString("user.list.name.tip"));
        headersOfMould.add(sm.getString("user.list.classCode.tip"));
        headersOfMould.add(sm.getString("user.list.mobileNo.tip"));
        headersOfMould.add(sm.getString("user.list.emailAddress.tip"));
        return headersOfMould;
    }

    private RichTextString getRichText(Workbook workbook, String header){
        HSSFRichTextString richTextString = new HSSFRichTextString(header);
        if(header.contains(sm.getString("user.list.brackets.left"))) {
            richTextString.applyFont(0, header.indexOf(sm.getString("user.list.brackets.left")), PoiUtils.getFirstCellFont(workbook));
            richTextString.applyFont(header.indexOf(sm.getString("user.list.brackets.left")), header.length(), PoiUtils.getMouldCellFont(workbook));
        }
        return richTextString;
    }
}
