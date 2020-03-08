package com.h3c.vdi.athena.netdisk.model.xml;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.netdisk.utils.Constant;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author z15722
 * @date 2018/8/28 20:23
 */
@Data
@XmlRootElement(name = "ocs")
@XmlAccessorType(XmlAccessType.FIELD)
public class OcsBaseData extends OcsBase {

    @XmlElement(name="data")
    private String data;

    public void checkResult() {
        if (!Constant.STATUS_CODE_OK_100.equals(super.getMeta().getStatuscode()) &&
                !Constant.STATUS_CODE_OK_200.equals(super.getMeta().getStatuscode())) {
            throw new AppException(super.getMeta().getMessage());
        }
    }
}
