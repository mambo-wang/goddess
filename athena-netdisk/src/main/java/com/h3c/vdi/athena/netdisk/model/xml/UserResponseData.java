package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author z15722
 * @date 2018/9/13 15:53
 */
@XmlRootElement(name = "ocs")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UserResponseData extends OcsBase {
    @XmlElement(name="data")
    private UserInfo data;
}
