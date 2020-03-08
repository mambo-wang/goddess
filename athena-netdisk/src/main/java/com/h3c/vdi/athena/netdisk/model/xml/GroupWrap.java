package com.h3c.vdi.athena.netdisk.model.xml;

import com.h3c.vdi.athena.netdisk.model.xml.Element;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by w14014 on 2018/9/25.
 */
public class GroupWrap {

    @Getter
    @XmlElement(name = "groups")
    private Element groups;
}
