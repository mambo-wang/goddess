package com.h3c.vdi.athena.netdisk.model.xml;

import lombok.Getter;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
public class ShareElement extends OcsBase{

    @Getter
    @XmlElement(name = "element")
    private List<ShareInfo> element;
}
