package com.h3c.vdi.athena.homework.exception;

import com.h3c.vdi.athena.common.exception.BasicErrorCode;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public interface ErrorCodes extends BasicErrorCode {

    int HTTP_INTERNAL_SERVER_ERROR = 100_500_001;

    /****************** homework服务2开头: 2**_***_*** *******************/

    /**===========班级======*/
    /**
     * 班级信息不完整
     */
    int CLASS_ENTITY_NOT_COMPLETE = 200_026_006;
    /**
     * 入学年份不能为null
     */
    int CLASS_TIME_NOT_EMPTY = 200_026_001;

    /**所属学院不能为null
     */
    int CLASS_COLLEGEID_NOT_EMPTY = 200_026_002;

    /**
     * 班级未找到
     */
    int CLASS_ENTITY_NOT_FOUND = 200_026_004;

    /**
     * 班级名称不能为空
     *
     * @param errorCode
     * @return
     */
    int CLASS_NAME_NOT_EMPTY = 200_026_008;

    /**
     * 班级名称的长度不能超过64位
     */
    int ClASS_NAME_LENGTH_MAX = 200_026_020;


    /**
     * 相同班级编号已经存在
     */
    int CLASS_ENTITY_CODE_EXISTED = 200_026_007;



    /**===========学院======*/

    /**
     * 学院被引用，不能被删除
     */
    int COLLEGE_USED = 200_025_001;
    /**
     * 学院未找到
     */
    int COLLEGE_NOT_FOUND = 200_025_004;
    /**
     * 学院信息不完整
     */
    int COLLEGE_NOT_COMPLETE = 200_025_005;

    /**
     * 学院名称不合法
     */
    int COLLEGE_NAME_ILLEGLE = 200_025_006;

    /**
     * 学院已经存在
     */
    int COLLEGE_FOUND = 200_025_007;

    /**
     * 学院名称不能超过64个字符
     */
    int COLLEGE_NAME_LENGTH_MAX=200_025_008;

    /**===========评论======*/
    int COMMENT_NOT_FIND = 200_027_001;


    /**************************************** 课程组 200_010_xxx *********************************************************/
    /** 课程组不存在或已被删除 */
    int GROUP_NOT_FOUND = 200_010_001;
    /** 相同名称课程组已存在 */
    int GROUP_EXISTS = 200_010_002;


    /**************************************** 学生注册 200_011_xxx *********************************************************/
    /** 该注册已被处理或注册信息错误 */
    int REGISTRAR_NOT_FOUND = 200_011_001;
    /** 当前登录者没有处理权限 */
    int REGISTRAR_NOT_PERMITTED = 200_011_002;


    /****************************************  作业模块  200_012_xxx *********************************************************/
    /** 找不到对应的提交作业 **/
    int FIND_NO_HOMEWORK_SUBMISSION=200_012_001;
    /** 前台传过来的作业信息不合法，无法下发作业 **/
    int HOMEWORK_ILLEGAL = 200_012_002;
    /** 作业名重复 */
    int HOMEWORK_NAME_DUPLICATED = 200_012_003;
    /** 作业不存在 */
    int HOMEWORK_NOT_FOUND = 200_012_004;
    /** 没有分数，请查看分数后尝试修改 */
    int HOMEWORK_NO_SCORE = 200_012_005;
    /** 作业答案和附件均为空或所属作业为空 **/
    int HOMEWORK_SUBMISSION_NOT_LEGAL = 200_012_006;
    /** 已过截止日期，不能修改作业 **/
    int TIME_OVER_DEADLINE = 200_012_007;

    /**************************************** 作业模板 200_013_xxx *********************************************************/
    /**
     * 作业模板名称重复
     */
    int HOMEWORK_TEMPLATE_NAME_REPEAT = 200_013_001;

    /**
     * 作业模板不存在
     */
    int HOMEWORK_TEMPLATE_NOT_FOUND = 200_013_002;

    /***************************************** 附件 200_014_xxx  *************************************************************/
    /**
     * 附件上传失败
     */
    int ATTACHMENT_UPLOAD_FAILED = 200_014_001;

    static String getErrorMessage(final int errorCode, Object... args) {
        return stringManager.getString(PREFIX + errorCode, args);
    }
}
