package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author z15722
 * @date 2018/8/28 14:35
 */
public class Element {
    @Getter
    @XmlElement(name = "element")
    private List<String> element;
}
