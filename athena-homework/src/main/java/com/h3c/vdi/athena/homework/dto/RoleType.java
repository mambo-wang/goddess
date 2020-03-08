package com.h3c.vdi.athena.homework.dto;

import com.h3c.vdi.athena.common.utils.StringManager;

/**
 *
 * @author w14014
 * @date 2018/3/7
 */
public enum RoleType {

    /** 管理员 */
    ADMIN(1, "ADMIN", "role.admin"),
    /** 老师 */
    TEACHER(2, "TEACHER", "role.teacher"),
    /** 学生 */
    STUDENT(3, "STUDENT", "role.student");

    private int value;

    private String name;

    private String description;

    private StringManager stringManager = StringManager.getManager("RoleType");

    RoleType(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return stringManager.getString(description);
    }

    public int getValue() {
        return value;
    }

}
