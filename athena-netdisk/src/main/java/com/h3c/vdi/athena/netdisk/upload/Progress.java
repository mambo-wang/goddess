package com.h3c.vdi.athena.netdisk.upload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by w16051 on 2018/4/25.
 */
@Getter
@Setter
@NoArgsConstructor
public class Progress {

    /**到目前为止读取的比特数*/
    private long pBytesRead;
    /**文件总大小*/
    private long pContentLength;
    /**正在读取第几个文件*/
    private long pItems;

    @Override
    public String toString() {
        float tmp = (float)pBytesRead;
        float result = tmp/pContentLength*100;
        return "Progress [pBytesRead=" + pBytesRead + ", pContentLength="
                + pContentLength + ", pItems=" + pItems + ", percentage=" + result + "% ]";
    }
}
