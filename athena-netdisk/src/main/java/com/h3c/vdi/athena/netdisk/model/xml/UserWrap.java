package com.h3c.vdi.athena.netdisk.model.xml;

import com.h3c.vdi.athena.netdisk.model.xml.Element;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author z15722
 * @date 2018/8/28 19:25
 */
public class UserWrap {

    @Getter
    @XmlElement(name="users")
    private Element users;
}
