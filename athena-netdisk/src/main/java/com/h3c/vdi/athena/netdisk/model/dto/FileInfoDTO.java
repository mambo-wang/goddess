package com.h3c.vdi.athena.netdisk.model.dto;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/19
 */
@Data
public class FileInfoDTO {
    private String name;
    private String path;
    private Integer type;
    private String contentLength;
    private String modified;
    private String absolutePath;

    /**0:未分享   1：分享给别人   2：别人分享给我*/
    private Integer shared;
    private List<ShareDTO> shares;

    /**查询目录树的时候使用*/
    private List<FileInfoDTO> subFiles;
}
