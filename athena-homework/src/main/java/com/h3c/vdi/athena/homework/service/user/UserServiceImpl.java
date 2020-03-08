package com.h3c.vdi.athena.homework.service.user;


import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.common.utils.MD5Util;
import com.h3c.vdi.athena.common.utils.PoiUtils;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.controller.UserController;
import com.h3c.vdi.athena.homework.dao.ClassEntityDao;
import com.h3c.vdi.athena.homework.dao.RegistrarDao;
import com.h3c.vdi.athena.homework.dao.UserClassRelationDao;
import com.h3c.vdi.athena.homework.dto.*;
import com.h3c.vdi.athena.homework.entity.ClassEntity;
import com.h3c.vdi.athena.homework.entity.Registrar;
import com.h3c.vdi.athena.homework.entity.UserClassRelation;
import com.h3c.vdi.athena.homework.feign.keystone.UserFeignService;
import com.h3c.vdi.athena.homework.service.classEntity.ClassEntityService;
import com.h3c.vdi.athena.homework.service.homework.HomeworkService;
import com.h3c.vdi.athena.homework.service.homeworkSubmission.HomeworkSubmissionService;
import com.h3c.vdi.athena.homework.service.homeworktemplate.HomeworkTemplateService;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.registrar.RegistrarService;
import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author w16051
 * @date 2018/3/2
 */
@Service
public class UserServiceImpl implements UserService {

    private final String REGISTER_SUCCESS="success";
    private final String REGISTER_WAIT="wait";
    private final String REGISTER_FAILED="fail";

    private static StringManager sm = StringManager.getManager("User");

    @Resource
    private UserFeignService userFeignService;

    @Resource
    private ClassEntityDao classEntityDao;

    @Resource
    private ClassEntityService classEntityService;

    @Resource
    private UserClassRelationDao userClassRelationDao;

    @Resource
    private RegistrarService registrarService;

    @Resource
    private LessonGroupService lessonGroupService;

    @Resource
    private HomeworkSubmissionService homeworkSubmissionService;

    @Resource
    private HomeworkService homeworkService;

    @Resource
    private HomeworkTemplateService homeworkTemplateService;

    @Resource
    RegistrarDao registrarDao;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public RegisterResultDTO submitRegisterRequest(RegistrarDTO registrarDTO)
    {
        RegisterResultDTO registerResultDTO=new RegisterResultDTO();
        ClassEntity classEntity = classEntityDao.findByIdAndDeleted(registrarDTO.getClassId(), BasicConstant.IS_DELETED_N);
        //用户名（学号不能重复）
        List<String> userNames = this.findAllDTOs(3).stream().map(x->x.getUsername()).collect(Collectors.toList());
        if(userNames.contains(registrarDTO.getUsername())){
            registerResultDTO.setResult(REGISTER_FAILED);
            registerResultDTO.setError(sm.getString("duplicate.username"));
            return registerResultDTO;
        }
        //如果所选班级存在并且该班级没有班长，则验证邀请码
        if(!Objects.isNull(classEntity)&&Objects.isNull(classEntity.getMonitorId())) {
            //检查注册信息中是否有邀请码，若没有则创建普通用户
            if(Objects.isNull(registrarDTO.getInviteCode())){
                addStudent(registrarDTO,false,null);
                registerResultDTO.setResult(REGISTER_WAIT);
                registerResultDTO.setMonitorId(null);
                registerResultDTO.setMonitorName(null);
            }
            //若选中班级在数据库中没有邀请码，返回失败信息，失败原因：邀请码失效
            else if (Objects.isNull(classEntity.getInviteCode())){
                registerResultDTO.setResult(REGISTER_FAILED);
                registerResultDTO.setError(sm.getString("no.monitor.entity.no.code"));
                return registerResultDTO;
            }
            //比较输入的邀请码和数据库中的邀请码，若相同则创建班长并销毁邀请码，否则失败
            else if (!Objects.equals(registrarDTO.getInviteCode(),classEntity.getInviteCode())){
                registerResultDTO.setResult(REGISTER_FAILED);
                registerResultDTO.setError(sm.getString("code.is.wrong"));
                return registerResultDTO;
            }else{
                //添加学生并设置为班长
                addStudent(registrarDTO,true,classEntity);
                //设置注册结果DTO并返回
                registerResultDTO.setResult(REGISTER_SUCCESS);
            }
        }
        //如果所选班级已经有班长，需要班长进行认证
        else if(!Objects.isNull(classEntity)){
            if(!Objects.isNull(registrarDTO.getInviteCode())){
                registerResultDTO.setResult(REGISTER_FAILED);
                registerResultDTO.setError(sm.getString("monitor.already.exist"));
                return registerResultDTO;
            }
            //添加事件
            addStudent(registrarDTO,false,null);
            registerResultDTO.setResult(REGISTER_WAIT);
            UserDTO monitor=userFeignService.queryUserById(classEntity.getMonitorId());
            registerResultDTO.setMonitorId(monitor.getId());
            registerResultDTO.setMonitorName(monitor.getName());
        }
        //如果所选班级不存在，返回失败
        else{
            registerResultDTO.setResult(REGISTER_FAILED);
            registerResultDTO.setError(sm.getString("class.not.exist"));
        }
        return registerResultDTO;
    }

    /**
     * 添加学生账户
     * @param registrarDTO
     * @param isMonitor
     * @param classEntity
     */
    @Transactional(rollbackFor = Exception.class)
    private void addStudent(RegistrarDTO registrarDTO,boolean isMonitor,ClassEntity classEntity){
        UserDTO userDTO=convertFromRegistrarDTOToUserDTO(registrarDTO);
        Long userId=userFeignService.addUser(userDTO);
        //判断该用户是否添加成功
        if(!Objects.isNull(userId)) {
            if(isMonitor){
                //设置班级的班长为该用户,且删除邀请码
                classEntity.setMonitorId(userId);
                classEntity.setInviteCode(null);
                classEntityDao.save(classEntity);
                //添加该班级和该用户的联系
                this.addUserClassRelation(userId, classEntity.getId());
            } else {
                //如果不是班长，将创建好的userID加到注册用户表中，并保存
                registrarDTO.setUserId(userId);
                registrarService.addRegistrar(registrarDTO);
            }
        }else
            throw new AppException("用户创建失败，请刷新后重试");
    }

    @Override
    public void addStudentToClass(Registrar registrar){
        UserClassRelation userClassRelation = new UserClassRelation();
        userClassRelation.setUserId(registrar.getUserId());
        userClassRelation.setClassId(registrar.getClassId());
        userClassRelation.setDeleted(CommonConst.NOT_DELETED);
        userClassRelationDao.save(userClassRelation);
    }


    private UserDTO convertFromRegistrarDTOToUserDTO(RegistrarDTO registrarDTO)
    {
        UserDTO userDTO=new UserDTO();
        userDTO.setName(registrarDTO.getName());
        userDTO.setUsername(registrarDTO.getUsername());
        userDTO.setPassword(registrarDTO.getPassword());
        userDTO.setMobileNo(registrarDTO.getMobileNo());
        userDTO.setEmailAddress(registrarDTO.getEmailAddress());
        userDTO.setRoleList(buildStudentRoleList());
        return userDTO;
    }

    /**
     * 为注册者创建角色列表，因为只有学生注册，所以直接将学生角色赋给注册者
     * @return
     */
    private List<RoleDTO> buildStudentRoleList() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(CommonConst.STUDENT_ID);
        roleDTO.setName(RoleType.STUDENT.getName());
        roleDTO.setDescription(RoleType.STUDENT.getDescription());
        roleDTO.setSysName(CommonConst.STUDENT_SYS);
        List<RoleDTO> roleDTOS = new ArrayList<>();
        roleDTOS.add(roleDTO);
        return roleDTOS;
    }

    /**
     * 添加班级和用户的联系记录到数据库
     * @param userId
     * @param classId
     */
    private void addUserClassRelation(Long userId,Long classId)
    {
        UserClassRelation userClassRelation = new UserClassRelation();
        userClassRelation.setClassId(classId);
        userClassRelation.setUserId(userId);
        userClassRelationDao.save(userClassRelation);
    }



    @Override
    public List<UserDTO> findAllDTOs(Integer role_type) {
        List<UserDTO> list = userFeignService.findAllDTOs(role_type);
        return list;
    }

    @Override
    public List<UserDTO> findAllStudents() {
        List<UserDTO> list = userFeignService.findAllDTOs(RoleType.STUDENT.getValue());

        return list.parallelStream()
                .map(this::addClassInfo)
                .filter(userDTO -> Objects.nonNull(userDTO.getClassNo()))
                .collect(Collectors.toList());
    }

    private UserDTO addClassInfo(UserDTO userDTO){

        Long classId = userClassRelationDao.findClassIdByUserId(userDTO.getId());
        if(Objects.isNull(classId)){
            return userDTO;
        }
        ClassEntity classEntity = classEntityService.queryClassEntityById(classId);
        if(Objects.isNull(classEntity)){
            return userDTO;
        }
        userDTO.setClassNo(classEntity.getCode());
        return userDTO;
    }

    @Override
    public UserDTO queryUserById(Long id) {
        UserDTO userDTO = userFeignService.queryUserById(id);
        return this.modifyUserDTO(userDTO);
    }

    @Override
    public UserDTO queryUserByUserName(String username){
        UserDTO userDTO = userFeignService.findByUsername(username);
        return this.modifyUserDTO(userDTO);
    }

    private UserDTO modifyUserDTO(UserDTO userDTO){
        Long classId=classEntityDao.queryIdByMonitorId(userDTO.getId());
        if(Objects.isNull(classId))
            userDTO.setMonitor(false);
        else
            userDTO.setMonitor(true);
        Long classIdByUserId = userClassRelationDao.findClassIdByUserId(userDTO.getId());
        userDTO.setClassId(classIdByUserId);
        Registrar registrar = registrarDao.queryByUserId(userDTO.getId());
        if (Objects.nonNull(registrar)){
            userDTO.setCheckStatus(registrar.getCheckStatus().toString());
            userDTO.setSubmitTime(registrar.getSubmitTime());
            userDTO.setHandleTime(registrar.getHandleTime());
            userDTO.setComments(registrar.getComments());
            ClassEntityDTO classEntityDTO = classEntityService.queryClassEntityDTOById(registrar.getClassId());
            userDTO.setRegisterClass(classEntityDTO);
        }
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        userFeignService.updateUser(userDTO);
    }

    //todo 点赞相关的删除
    @Override
    public void deleteUser(Long userId, Long roleId) {
        //如果用户是学生将其从课程组和班级中移出，删除下发作业
        if(roleId.equals(CommonConst.STUDENT_ID)){
            lessonGroupService.removeUserOutGroup(userId);
            UserClassRelation userClassRelation = userClassRelationDao.findByUserId(userId);
            userClassRelation.setDeleted(CommonConst.DELETED);
            homeworkSubmissionService.deleteHomeworkSubmissionsByUserId(userId);
        }
        //如果是普通老师，删除课程组(会级联删除作业)，删除作业模板
        else if (roleId.equals(CommonConst.TEACHER_ID)){
            lessonGroupService.removeGroupByUserId(userId);
            homeworkTemplateService.deleteHomeworkTemplateByUserId(userId);
        }
    }

    @Override
    public UserDTO currentLoginUser() {
        DefaultUserDetails defaultUserDetails = Utils.getLoginUser();
        return userFeignService.findByUsername(defaultUserDetails.getUsername());
    }


    @Override
    public void applyForClass(Long userId,Long classId){
        UserClassRelation userClassRelation = userClassRelationDao.findByUserId(userId);
        //检测申请的用户是否已经加入某班级，如果已经加入，直接跳出提示
        if(Objects.nonNull(userClassRelation)){
            throw new AppException("您已成功加入某班级，请刷新后重试");
        }else{
            Registrar registrar = registrarDao.queryByUserId(userId);
            if(Objects.nonNull(registrar)){
                if(Objects.equals(registrar.getCheckStatus(),CheckStatus.UNCHECKED)){
                    throw new AppException("您已经有加入班级申请正在审批中，请勿重复申请");
                }else{
                    registrar.setCheckStatus(CheckStatus.UNCHECKED);
                    registrar.setClassId(classId);
                    registrar.setSubmitTime(System.currentTimeMillis());
                    registrarDao.save(registrar);
                }
            }else{
                Registrar registrarNew = new Registrar();
                UserDTO userDTO = this.queryUserById(userId);
                registrarNew.setUserId(userId);
                registrarNew.setUsername(userDTO.getUsername());
                registrarNew.setPassword(userDTO.getPassword());
                registrarNew.setName(userDTO.getName());
                registrarNew.setMobileNo(userDTO.getMobileNo());
                registrarNew.setEmailAddress(userDTO.getEmailAddress());
                registrarNew.setClassId(classId);
                registrarNew.setSubmitTime(System.currentTimeMillis());
                registrarNew.setCheckStatus(CheckStatus.UNCHECKED);
                registrarDao.save(registrarNew);
            }
        }
    }

    @Override
    public String getRoleName(UserDTO userDTO) {
        if(Objects.isNull(userDTO) || CollectionUtils.isEmpty(userDTO.getRoleList())){
            return RoleType.STUDENT.getName();
        }
        List<String> roles = userDTO.getRoleList().stream().map(RoleDTO::getName).collect(Collectors.toList());
        if(roles.contains(RoleType.ADMIN.getName())){
            return RoleType.ADMIN.getName();
        }
        if(roles.contains(RoleType.TEACHER.getName())){
            return RoleType.TEACHER.getName();
        }
        if(roles.contains(RoleType.STUDENT.getName())){
            return RoleType.STUDENT.getName();
        }
        return RoleType.STUDENT.getName();
    }

    @Override
    public List<UserDTO> queryClassmates(){
        Long currentUserId = this.currentLoginUser().getId();
        Long classId=classEntityDao.queryIdByMonitorId(currentUserId);
        if(Objects.isNull(classId))
            throw new AppException(sm.getString("is.not.monitor"));
        List<Long> userIds = userClassRelationDao.findUserIdsByClassId(classId).stream().map(BigInteger::longValue).collect(Collectors.toList());
        List<UserDTO> userDTOS = userIds.stream().map(id->this.queryUserById(id)).collect(Collectors.toList());
        return userDTOS;
    }

    @Override
    public void kickOut(HandleUserDTO handleUserDTO){
        UserClassRelation userClassRelation = userClassRelationDao.findByUserId(handleUserDTO.getId());
        if(Objects.isNull(userClassRelation)){
            throw new AppException(sm.getString("user.already.kicked"));
        }
        userClassRelation.setDeleted(CommonConst.DELETED);
        userClassRelationDao.save(userClassRelation);
        Registrar registrar = registrarDao.queryByUserId(handleUserDTO.getId());
        if(Objects.nonNull(registrar)){
            registrar.setHandleTime(System.currentTimeMillis());
            registrar.setComments(handleUserDTO.getComments());
            registrar.setCheckStatus(CheckStatus.KICKED);
            registrarDao.save(registrar);
        }
        else {     //可能是班长转让后被踢，这时候用户是没有registrar的
            Registrar registrarNew = new Registrar();
            UserDTO userDTO = this.queryUserById(handleUserDTO.getId());
            registrarNew.setUserId(handleUserDTO.getId());
            registrarNew.setUsername(userDTO.getUsername());
            registrarNew.setPassword(userDTO.getPassword());
            registrarNew.setName(userDTO.getName());
            registrarNew.setMobileNo(userDTO.getMobileNo());
            registrarNew.setEmailAddress(userDTO.getEmailAddress());
            registrarNew.setClassId(userClassRelation.getClassId());
            registrarNew.setSubmitTime(System.currentTimeMillis());
            registrarNew.setHandleTime(System.currentTimeMillis());
            registrarNew.setComments(handleUserDTO.getComments());
            registrarNew.setCheckStatus(CheckStatus.KICKED);
            registrarDao.save(registrarNew);
        }
    }

    @Override
    public void changeMonitor(Long userId){
        UserDTO currentUser = this.currentLoginUser();
        UserClassRelation userClassRelation = userClassRelationDao.findByUserId(userId);
        ClassEntity classEntity = classEntityDao.findByIdAndDeleted(userClassRelation.getClassId(),CommonConst.NOT_DELETED);
        if(!Objects.equals(currentUser.getId(),classEntity.getMonitorId())){
            logger.warn("change monitor failed because current user is not the monitor of selected user");
            throw new AppException("当前用户不是所选用户所在班级的班级管理员，没有转让权限");
        }
        classEntity.setMonitorId(userId);
        classEntityDao.save(classEntity);
    }

    @Override
    public void changeClass(Long classId){
        UserDTO currentUser = this.currentLoginUser();
        Registrar registrar = registrarDao.queryByUserId(currentUser.getId());
        if(registrar.getCheckStatus().equals(CheckStatus.PASSED))
            throw new AppException("您已成功加入某班级，请刷新后重新登录");
        registrar.setClassId(classId);
        registrar.setCheckStatus(CheckStatus.UNCHECKED);
        registrarDao.save(registrar);
    }

    @Override
    public void isMonitor(Integer inviteCode){
        UserDTO currentUser = this.currentLoginUser();
        Registrar registrar = registrarDao.queryByUserId(currentUser.getId());
        ClassEntity classEntity = classEntityDao.findByIdAndDeleted(registrar.getClassId(),CommonConst.NOT_DELETED);
        if(Objects.isNull(classEntity))
            throw new AppException("所申请班级已被删除，请确认后再进行操作");
        if(Objects.nonNull(classEntity.getMonitorId()))
            throw new AppException("您所申请的班级已经有班级管理员，每个班只有班级管理员，请确认后重试");
        if (!Objects.equals(inviteCode,classEntity.getInviteCode()))
            throw new AppException("您所输入的邀请码错误，请确认后重试");
        classEntity.setMonitorId(currentUser.getId());
        classEntityDao.save(classEntity);
        UserClassRelation userClassRelation = new UserClassRelation();
        userClassRelation.setUserId(currentUser.getId());
        userClassRelation.setClassId(classEntity.getId());
        userClassRelation.setDeleted(CommonConst.NOT_DELETED);
        userClassRelationDao.save(userClassRelation);
        registrar.setCheckStatus(CheckStatus.PASSED);
        registrarDao.save(registrar);
    }

    /**
     * 导出excel模板
     * @return 表格
     */
//    @Override
//    public Workbook createWorkbookMould(){
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        List<String> headersOfMould = createHeadersOfMould();
//        Sheet sheet = PoiUtils
//                .createSheetWithRichTitle(workbook, sm.getString("user.download.tip.template")
//                        , this.getMouldTitleRowData(workbook,headersOfMould), 0);
//
//        //this.createContentOfMould(sheet, workbook, headersOfMould);
//
//        return workbook;
//    }
//
//    private List<RichTextString> getMouldTitleRowData(Workbook workbook,List<String> headersOfMould){
//        return headersOfMould.stream()
//                .map(header -> header.substring(0, header.indexOf(sm.getString("user.list.brackets.left")))
//                        + "\n" + header.substring(header.indexOf(sm.getString("user.list.brackets.left"))) )
//                .map(header -> this.getRichText(workbook, header))
//                .collect(Collectors.toList());
//    }
//
//    private List<String> createHeadersOfMould() {
//        List<String> headersOfMould = new ArrayList<>();
//        headersOfMould.add(sm.getString("user.list.index.tip"));
//        headersOfMould.add(sm.getString("user.list.userName.tip"));
//        headersOfMould.add(sm.getString("user.list.name.tip"));
//        headersOfMould.add(sm.getString("user.list.classCode.tip"));
//        headersOfMould.add(sm.getString("user.list.mobileNo.tip"));
//        headersOfMould.add(sm.getString("user.list.emailAddress.tip"));
//        return headersOfMould;
//    }
//
//    private RichTextString getRichText(Workbook workbook, String header){
//        HSSFRichTextString richTextString = new HSSFRichTextString(header);
//        if(header.contains(sm.getString("user.list.brackets.left"))) {
//            richTextString.applyFont(0, header.indexOf(sm.getString("user.list.brackets.left")), PoiUtils.getFirstCellFont(workbook));
//            richTextString.applyFont(header.indexOf(sm.getString("user.list.brackets.left")), header.length(), PoiUtils.getMouldCellFont(workbook));
//        }
//        return richTextString;
//    }
//
//    private void createContentOfMould(Sheet sheet, Workbook workbook,List<String> headersOfMould) {
//        //插入示例数据
//        for (int i = 0; i < 10; i++) {
//            //创建行
//            Row row = sheet.createRow(i + 2);
//            row.setHeightInPoints(25);
//            //设置列宽
//            sheet.setColumnWidth(i + 2, 25 * 210);
//            for (int j = 0; j < headersOfMould.size(); j++) {
//                //创建单元格
//                Cell cell = row.createCell(j);
//                cell.setCellStyle(PoiUtils.getMouldCellStyle(workbook));
//
//                if (j == 0 && i == 0) {
//                    cell.setCellValue(i + 1);
//                }else if(i == 0){
//                    cell.setCellValue(this.getValueOfMould(j));
//                }else {
//                    cell.setCellValue("");
//                }
//
//            }
//        }
//    }
//
//    /**
//     * 获取模板的第一行示例数据
//     * @param cellIndex 行号
//     * @return 数据
//     */
//    private String getValueOfMould(int cellIndex){
//
//        switch (cellIndex){
//            case 1: return sm.getString("user.upload.username");
//            case 2: return sm.getString("user.upload.name");
//            case 3: return sm.getString("user.upload.classCode");
//            case 4: return sm.getString("user.upload.mobileNo");
//            case 5: return sm.getString("user.upload.emailAddress");
//            default:return "";
//        }
//    }

    @Override
    public List<UserDTO> importStuFile(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(PoiUtils.isExcelFile(fileType)){
            return this.uploadFile(file.getInputStream(),fileType);
        }else {
            throw new AppException(sm.getString("template.file.extension.wrong"));
        }
    }

    private List<UserDTO> uploadFile(InputStream is, String extensionName) throws IOException{
        Workbook workbook = null;
        if (extensionName != null) {
            //根据文件后缀名不用生成不同的表格对象
            if (extensionName.toLowerCase().equals(CommonConst.FILE_EXCEL_XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (extensionName.toLowerCase().equals(CommonConst.FILE_EXCEL_XLSX)) {
                workbook = new XSSFWorkbook(is);
            }
        }
        assert workbook != null;

        int numberOfSheets = workbook.getNumberOfSheets();

        List<UserDTO> userDTOList =  new ArrayList<>();

        for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex ++) {
            //创建一个工作表
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName = sheet.getSheetName();

            //获取表中的数据行
            for (Row row : sheet) {

                List<String> errorMessages = new ArrayList<>();

                //越过表头，rowNum行号为0是第一行，一般是表头
                if (row.getRowNum() < 1) {
                    continue;
                }

                //序号
                Cell rowNum = row.getCell(0);
                rowNum.setCellType(Cell.CELL_TYPE_STRING);
                String index = rowNum.getStringCellValue();

                if(StringUtils.isEmpty(index)){
                    continue;
                }

                this.checkOneRowData(row, userDTOList);
            }
        }

        is.close();
        return userDTOList;
    }

    private void checkOneRowData(Row row, List<UserDTO> userDTOS){
        //学号
        Cell cellStuNo = row.getCell(1);
        cellStuNo.setCellType(Cell.CELL_TYPE_STRING);
        String stuNo = cellStuNo.getStringCellValue();

        //姓名
        Cell cellStuName = row.getCell(2);
        cellStuName.setCellType(Cell.CELL_TYPE_STRING);
        String stuName = cellStuName.getStringCellValue();

        //班级编号
        Cell cellClassNo = row.getCell(3);
        cellClassNo.setCellType(Cell.CELL_TYPE_STRING);
        String classNo = cellClassNo.getStringCellValue();

        //手机号码
        Cell cellPhoneNo = row.getCell(4);
        cellPhoneNo.setCellType(Cell.CELL_TYPE_STRING);
        String phoneNo = cellPhoneNo.getStringCellValue();

        //邮箱地址
        Cell cellEmailAddress = row.getCell(5);
        cellEmailAddress.setCellType(Cell.CELL_TYPE_STRING);
        String emailAddress = cellEmailAddress.getStringCellValue();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(stuNo);
        userDTO.setName(stuName);
        userDTO.setClassNo(classNo);
        userDTO.setMobileNo(phoneNo);
        userDTO.setEmailAddress(emailAddress);
        userDTOS.add(userDTO);
    }

    @Override
    public List<UserDTO> importStudents(List<UserDTO> userDTOS){
        List<UserDTO> userDTOSWithError = new ArrayList<>();
        List<String> userNames = this.findAllDTOs(3).stream().map(x->x.getUsername()).collect(Collectors.toList());
        userDTOS.forEach(userDTO->this.importStudent(userDTO,userDTOSWithError,userNames));
        return userDTOSWithError;
    }

    private void importStudent(UserDTO userDTO,List<UserDTO> userDTOSWithError,List<String> userNames){
        if(!checkImportStudent(userDTO,userDTOSWithError,userNames))
            return;
        //确认班级是否存在
        ClassEntity classEntity = classEntityDao.queryByCode(userDTO.getClassNo());
        if(Objects.isNull(classEntity)){
            userDTO.setImportError(sm.getString("import.class.not.exists"));
            userDTOSWithError.add(userDTO);
            return;
        }
        //设置密码为默认密码，角色为学生
        userDTO.setPassword(MD5Util.md5("123456"));
        userDTO.setRoleList(buildStudentRoleList());
        //添加用户
        Long userId = userFeignService.addUser(userDTO);
        //添加用户班级关系
        this.addUserClassRelation(userId,classEntity.getId());
    }

    //检查一条学生数据是否合法
    private boolean checkImportStudent(UserDTO userDTO,List<UserDTO> userDTOSWithError,List<String> userNames){
        String userName = userDTO.getUsername();
        if(Objects.isNull(userName)){
            userDTO.setImportError(sm.getString("import.username.is.null"));
            userDTOSWithError.add(userDTO);
            return false;
        }
        if(userNames.contains(userName)){
            userDTO.setImportError(sm.getString("import.username.exists"));
            userDTOSWithError.add(userDTO);
            return false;
        }

        if(Objects.isNull(userDTO.getName())){
            userDTO.setImportError(sm.getString("import.name.is.null"));
            userDTOSWithError.add(userDTO);
            return false;
        }

        if(Objects.isNull(userDTO.getClassNo())){
            userDTO.setImportError(sm.getString("import.classCode.is.null"));
            userDTOSWithError.add(userDTO);
            return false;
        }
        return true;
    }
}
