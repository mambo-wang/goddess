package com.h3c.vdi.athena.netdisk.model.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
public class ShareInfo {

    @XmlElement(name = "id")
    public Long id;

    @XmlElement(name = "share_type")
    public Integer shareType;

    @XmlElement(name = "uid_owner")
    public String uIdOwner;

    @XmlElement(name = "displayname_owner")
    public String displayNameOwner;

    @XmlElement(name = "permissions")
    public Integer permissions;

    @XmlElement(name = "stime")
    public Long sTime;

    @XmlElement(name = "uid_file_owner")
    public String uIdFileOwner;

    @XmlElement(name = "displayname_file_owner")
    public String displayNameFileOwner;

    @XmlElement(name = "path")
    public String path;

    @XmlElement(name = "item_type")
    public String itemType;

    @XmlElement(name = "file_target")
    public String fileTarget;

    @XmlElement(name = "share_with")
    public String shareWith;

    @XmlElement(name = "share_with_displayname")
    public String shareWithDisplayName;

}
