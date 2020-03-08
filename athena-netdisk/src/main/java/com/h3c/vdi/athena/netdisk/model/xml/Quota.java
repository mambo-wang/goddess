package com.h3c.vdi.athena.netdisk.model.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author z15722
 * @date 2018/9/13 16:48
 */
public class Quota {
    @XmlElement(name="free")
    private long free;
    @XmlElement(name="used")
    private long used;
    @XmlElement(name="total")
    private long total;
    @XmlElement(name="relative")
    private String relative;

    public long getFree() {
        return free;
    }

    public long getUsed() {
        return used;
    }

    public long getTotal() {
        return total;
    }

    public String getRelative() {
        return relative;
    }
}
