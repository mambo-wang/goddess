package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by w14014 on 2018/9/25.
 */
@XmlRootElement(name = "ocs")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Groups extends OcsBase {

    @XmlElement(name = "data")
    private GroupWrap data;
}
