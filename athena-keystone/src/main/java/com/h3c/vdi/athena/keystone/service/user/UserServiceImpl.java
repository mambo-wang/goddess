package com.h3c.vdi.athena.keystone.service.user;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.common.utils.MD5Util;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.keystone.constant.CommonConst;
import com.h3c.vdi.athena.keystone.dao.RoleDao;
import com.h3c.vdi.athena.keystone.dao.UserDao;
import com.h3c.vdi.athena.keystone.dto.RoleDTO;
import com.h3c.vdi.athena.keystone.dto.RoleType;
import com.h3c.vdi.athena.keystone.dto.SecureUserDTO;
import com.h3c.vdi.athena.keystone.dto.UserDTO;
import com.h3c.vdi.athena.keystone.entity.Role;
import com.h3c.vdi.athena.keystone.entity.User;
import com.h3c.vdi.athena.keystone.exception.ErrorCodes;
import com.h3c.vdi.athena.keystone.feign.configserver.ConfigServerFeignClient;
import com.h3c.vdi.athena.keystone.feign.homework.HomeworkFeignService;
import com.h3c.vdi.athena.keystone.feign.netdisk.NetDiskFeignService;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    UserDao userDao;

    @Resource
    RoleDao roleDao;

    @Resource
    private NetDiskFeignService netDiskFeignService;

    private StringManager sm = StringManager.getManager("User");

    @Resource
    private HomeworkFeignService homeworkFeignService;

    @Resource
    private ConfigServerFeignClient configServerFeignClient;

    private final String ADMIN = "admin";

    @Override
    public List<User> find(String loginName, String userName, String email) {
        return userDao.findAll(getUserSpecification(loginName, userName, email, CommonConst.IS_DELETED_N));
    }

    @Override
    public List<UserDTO> findDTOs(String loginName, String userName, String email) {
        List<User> all = find(loginName, userName, email);
        return convertToDTOs(all);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<UserDTO> findAllDTOs(Integer roleType) {

        String name;
        if(RoleType.TEACHER.getValue() == roleType){
            name = RoleType.TEACHER.getName();
        }else if(RoleType.ADMIN.getValue() == roleType){
            name = RoleType.ADMIN.getName();
        } else {
            name = RoleType.STUDENT.getName();
        }
        List<User> findByRole = userDao.findByRoleAndDeleted(name,BasicConstant.IS_DELETED_N);
        return this.convertToDTOs(findByRole);
    }

    @Override
    public List<UserDTO> convertToDTOs(List<User> all) {
        return all.stream().map(this::convertUserToUserDTO).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public User findUserByLoginName(String loginName) {
        return userDao.findByUsernameAndDeleted(loginName, BasicConstant.IS_DELETED_N);
    }

    @Override
    public UserDTO findUserDTOByName(String loginName) {
        User user = findUserByLoginName(loginName);
        return convertUserToUserDTO(user);
    }

    @Override
    public UserDTO convertUserToUserDTO(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setPassword(null);
        if(Objects.nonNull(user.getRoles())){
            userDTO.setRoleList(user.getRoles().stream().map(this::convertRoleToRoleDTO).collect(Collectors.toList()));
        }
        return userDTO;
    }

    @Override
    public SecureUserDTO findUserDetailsByLoginName(String loginName) {
        User user = userDao.findByUsernameAndDeleted(loginName, BasicConstant.IS_DELETED_N);
        SecureUserDTO secureUserDTO = new SecureUserDTO();
        if (Objects.isNull(user) || Objects.isNull(user.getRoles())) {
            return secureUserDTO;
        }
        secureUserDTO.setUsername(user.getUsername());
        secureUserDTO.setPassword(user.getPassword());
        List<String> roles = new ArrayList<>();
        user.getRoles().stream().forEach((var) -> roles.add(var.getName()));
        secureUserDTO.setRoles(roles);
        return secureUserDTO;
    }

    private Specification<User> getUserSpecification(String loginName, String userName, String email, String deleted) {
        return (root, criteriaQuery, cb) -> {
            Predicate predicate = null;
            predicate = andEquals(predicate, root, User.FIELD_USERNAME, loginName, cb);
            predicate = andEquals(predicate, root, User.FIELD_NAME, userName, cb);
            predicate = andEquals(predicate, root, User.FIELD_EMAIL_ADDRESS, email, cb);
            predicate = andEquals(predicate, root, User.FIELD_IS_DELETED, deleted, cb);
            return predicate;
        };
    }

    private <T, V> Predicate andEquals(Predicate predicate, Root<T> root, String column, V value, CriteriaBuilder cb) {
        if (value instanceof CharSequence && StringUtils.isEmpty((CharSequence) value)) {
            return predicate;
        }
        if (Objects.nonNull(value)) {
            Predicate p2 = cb.equal(root.get(column), value);
            if (Objects.nonNull(predicate)) {
                predicate = cb.and(predicate, p2);
            } else {
                predicate = p2;
            }
        }
        return predicate;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long addUser(UserDTO userDTO) {
        judgeUser(userDTO);

        User user = convertUserDTOToUser(userDTO);
        user.setPassword(MD5Util.md5(userDTO.getPassword()));
        user.setDeleted(BasicConstant.IS_DELETED_N);
        userDao.save(user);
        //TODO 待测
        //this.netDiskFeignService.createUser(userDTO);
        User userResult = userDao.findByUsernameAndDeleted(userDTO.getUsername(), CommonConst.IS_DELETED_N);
        if (!Objects.isNull(userResult)) {
            return userResult.getId();
        } else {
            return null;
        }
    }

    /**
     * 验证用户
     */
    private void judgeUser(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            log.warn("add user failed ,userDTO is null");
            throw new AppException("账号不存在");
        }
        //根据教师名判断本地教师是否重复
        if (!Objects.isNull(findUserByLoginName(userDTO.getUsername()))) {
            log.warn(" loginName repeat, add fail");
            throw new AppException("同名账号已存在");
        }
        if (Collections.isEmpty(userDTO.getRoleList())) {
            log.warn("add user failed ,roleList is null ");
            throw new AppException("用户角色不存在");
        }
    }

    /**
     * UserDTO实体转换为User实体
     *
     * @param userDTO
     * @return
     */
    private User convertUserDTOToUser(UserDTO userDTO) {
        User userTemp = new User();
        userTemp.setUsername(userDTO.getUsername().trim());
        userTemp.setName(userDTO.getName());
        userTemp.setMobileNo(userDTO.getMobileNo());
        userTemp.setEmailAddress(userDTO.getEmailAddress());
        userTemp.setRoles(convertRoleDTOsToRoles(userDTO.getRoleList()));
        //userTemp.setPhoto(userDTO.getPhoto());
        return userTemp;
    }

    /**
     * 将RoleDTO list转化为Role List
     *
     * @param roleDTOList
     * @return
     */
    private List<Role> convertRoleDTOsToRoles(List<RoleDTO> roleDTOList) {
        List<Role> roleList = roleDTOList.stream().map(this::convertRoleDTOToRole).collect(Collectors.toList());
        return roleList;
    }

    private Role convertRoleDTOToRole(RoleDTO roleDTO) {
        Role role = roleDao.findOne(roleDTO.getId());
        return role;
    }

    private RoleDTO convertRoleToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setSysName(role.getSysName());
        roleDTO.setDescription(this.getDescriptionByRoleName(role.getName()));
        return roleDTO;
    }

    private String getDescriptionByRoleName(String name){
        if(StringUtils.equals(name, RoleType.ADMIN.getName())){
            return RoleType.ADMIN.getDescription();
        }else if(StringUtils.equals(name, RoleType.TEACHER.getName())){
            return RoleType.TEACHER.getDescription();
        } else {
            return RoleType.STUDENT.getDescription();
        }
    }

    @Override
    public UserDTO queryUserById(Long id) {
        User user = userDao.findOne(id);
        return convertUserToUserDTO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(UserDTO userDTO) {
        if (userDTO == null) {
            log.warn("update user failed,userDTO is null");
            //  throw new AppException(ErrorCodes.USER_NOT_EXIST);
        }
        User userSource = userDao.findOne(userDTO.getId());
        if (userSource == null) {
            log.warn("update user failed,find userSource by id is null");
            //throw new AppException(ErrorCodes.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(userDTO, userSource, "id", "password");
        userDao.save(userSource);
        netDiskFeignService.modifyUser(userDTO);
//        //更新Session缓存中的教师信息
//        User user = new User();
//        BeanUtils.copyProperties(userSource, user);
//        sessionMgr.onModifyUser(user);
    }

    @Override
    public void updateUserInfo(UserDTO userDTO){
        User userCurrent = this.queryCurrentLoginUser();
        userCurrent.setMobileNo(userDTO.getMobileNo());
        userCurrent.setEmailAddress(userDTO.getEmailAddress());
        userDao.save(userCurrent);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUsers(List<Long> idList) {
        User userCurrent = queryCurrentLoginUser();
        if(idList.contains(userCurrent.getId())){
            log.warn("can't delete userself");
            throw new AppException(sm.getString("delete.failed.user.self"));
        }
        idList.forEach(id->this.deleteUser(id));
    }

    private void deleteUser(Long userId){
        User user = userDao.findOne(userId);
        if (null == user) {
            log.warn("delete user failed because user of id: "+userId+" not exists");
            throw new AppException(sm.getString("delete.failed.no.user"));
        }
        checkNotAdmin(user);
        this.netDiskFeignService.removeUser(user.getUsername());
//        todo:删除用户皮肤信息，视前天进度判断是否加入
//        configServerFeignClient.deleteSkinConfig(user.getUsername());
        Long roleId = user.getRoles().get(0).getId();
        homeworkFeignService.deleteUser(userId,roleId);
        user.setDeleted(BasicConstant.IS_DELETED_Y);
        user.setRoles(null);
        userDao.save(user);
    }

    @Override
    public List<UserDTO> findUsersNotInGroups(Long[] userIds){
        List<User> userList = userDao.findStudentsNotInIds(Arrays.asList(userIds));
        return this.convertToDTOs(userList);
    }

    @Override
    public void archiveUsers(List<Long> userIds){
        List<User> users = userDao.findByIdIn(userIds);
        users.forEach(user -> user.setDeleted(CommonConst.ARCHIVED));
        userDao.save(users);
        List<String> userNames = users.stream().map(user -> user.getUsername()).collect(Collectors.toList());
        netDiskFeignService.removeUser(userNames);
    }

    @Override
    public User currentLoginUser() {
        DefaultUserDetails defaultUserDetails = Utils.getLoginUser();
        return this.findUserByLoginName(defaultUserDetails.getUsername());
    }

    /**
     * 判断是否为当前登录用户
     *
     * @return
     */
    private User queryCurrentLoginUser() {
        User user = this.currentLoginUser();
        return user;
    }


    /**
     * 登录名为admin的用户不能删除
     *
     * @param user
     */
    private void checkNotAdmin(User user) {
        if (ADMIN.equals(user.getUsername())) {
            log.debug("delete user failed ,admin not can delete");
            throw new AppException(sm.getString("delete.failed.admin"));
        }
    }

    @Override
    public UserDTO findLocalUserById(Long id) {
        User user = userDao.findOne(id);
        if (user == null) {
            //  throw new AppException(ErrorCodes.USER_NOT_EXIST);
        }
        UserDTO userDTO = convertUserToUserDTO(user);
        return userDTO;
    }

    @Override
    public void resetPassword(Long id) {
        User user = userDao.findOne(id);
        if (Objects.isNull(user) || Objects.equals(user.getDeleted(),BasicConstant.IS_DELETED_Y)) {
            throw new AppException("用户已删除");
        }
        user.setPassword(MD5Util.md5("123456"));
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword("123456");
        netDiskFeignService.modifyUser(userDTO);
        userDao.save(user);
    }

    @Override
    public void updatePassword(UserDTO userDTO) {
        User user = userDao.findOne(userDTO.getId());
        if (StringUtils.isBlank(userDTO.getOldPassword())) {
            throw new AppException("原密码不能为空");
        }
        if (!MD5Util.authenticatePassword(user.getPassword(), userDTO.getOldPassword())) {
            throw new AppException("原密码输入错误");
        }
        if (StringUtils.isBlank(userDTO.getPassword())) {
            throw new AppException("新密码不能为空");
        }
        if (Objects.equals(userDTO.getPassword(), userDTO.getOldPassword())) {
            throw new AppException("新密码不能与原密码一样");
        }
        user.setPassword(MD5Util.md5(userDTO.getPassword()));
        netDiskFeignService.modifyUser(userDTO);
        userDao.save(user);
    }

    @Override
    public List<UserDTO> queryUserByIds(List<Long> ids) {

        List<User> users = userDao.findByIdIn(ids);
        return this.convertToDTOs(users);

    }
}
