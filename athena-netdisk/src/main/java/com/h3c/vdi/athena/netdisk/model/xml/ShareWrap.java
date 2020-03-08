package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@XmlRootElement(name = "ocs")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ShareWrap extends OcsBase{

    @XmlElement(name = "data")
    private ShareInfo data;
}
