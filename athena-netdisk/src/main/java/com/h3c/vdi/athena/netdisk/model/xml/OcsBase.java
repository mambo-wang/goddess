package com.h3c.vdi.athena.netdisk.model.xml;


import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author z15722
 * @date 2018/8/28 14:31
 */
public class OcsBase implements Serializable{

    @XmlElement(name="meta")
    private MetaBase meta;

    public MetaBase getMeta() {
        return meta;
    }
}
