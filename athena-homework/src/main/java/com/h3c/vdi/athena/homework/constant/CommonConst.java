package com.h3c.vdi.athena.homework.constant;

/**
 * Created by w16051 on 2018/3/2.
 * homework项目中共有的一些常量
 */
public interface CommonConst {

    /**
     * 学生角色的主键ID
     */
    Long STUDENT_ID=3L;
    Long TEACHER_ID=2L;

    /**
     * 学生角色的系统名称
     */
    String STUDENT_SYS="athena";

    /**
     * 是否删除
     */
    String DELETED="y";
    String NOT_DELETED="n";
    String ARCHIVED="a";

    /**
     * 附件关联表的类型
     */
    Integer ATTACH_TYPE_HOMEWORK_SUBMISSION =0;
    Integer ATTACH_TYPE_HOMEWORK =1;
    Integer ATTACH_TYPE_TEMPLATE =2;

    /**
     * 是否展示作业
     */
    String NOT_SHOW_HOMEWORK = "n";
    String SHOW_HOMEWORK = "y";



    /**
     * xls格式的Excel文件
     */
    String FILE_EXCEL_XLS = "xls";
    String FILE_EXCEL_XLSX= "xlsx";
}
