package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@XmlRootElement(name = "ocs")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ShareGetWrap extends OcsBase{

    @XmlElement(name = "data")
    private ShareElement data;
}
