package com.h3c.vdi.athena.netdisk.model.xml;

import com.h3c.vdi.athena.netdisk.model.xml.Element;
import com.h3c.vdi.athena.netdisk.model.xml.Quota;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author z15722
 * @date 2018/9/13 16:46
 */
public class UserInfo {

    @XmlElement(name="enabled")
    private boolean enabled;

    /**
     * id 是云盘用户的登录名
     */
    @XmlElement(name="id")
    private String id;

    @XmlElement(name="quota")
    private Quota quota;

    @XmlElement(name="email")
    private String email;

    @XmlElement(name="displayName")
    private String displayName;

    @XmlElement(name="phone")
    private String phone;

    @XmlElement(name="address")
    private String address;

    @XmlElement(name="website")
    private String website;

    @XmlElement(name="twitter")
    private String twitter;

    @XmlElement(name="groups")
    private Element groups;

    @XmlElement(name="language")
    private String language;

    public boolean isEnabled() {
        return enabled;
    }

    public String getId() {
        return id;
    }

    public Quota getQuota() {
        return quota;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getTwitter() {
        return twitter;
    }

    public Element getGroups() {
        return groups;
    }

    public String getLanguage() {
        return language;
    }
}
