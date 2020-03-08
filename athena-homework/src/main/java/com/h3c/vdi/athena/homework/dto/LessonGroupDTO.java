package com.h3c.vdi.athena.homework.dto;


import java.io.Serializable;
import java.util.List;

/**
 * Created by w14014 on 2018/3/8.
 */
public class LessonGroupDTO implements Serializable{
    private static final long serialVersionUID = 8032832110117464680L;

    private Long id;

    private UserDTO user;

    private String name;

    private Integer memberLimit;

    private List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(Integer memberLimit) {
        this.memberLimit = memberLimit;
    }
}
