package com.h3c.vdi.athena.netdisk.model.dto;

import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Data
public class ShareDTO {

    /**1：分享给别人   2：别人分享给我*/
    private Integer shared;

    private Long id;

    private Integer shareType;

    private String uIdOwner;

    private String displayNameOwner;

    private Long sTime;

    public String uIdFileOwner;

    public String displayNameFileOwner;

    private String path;

    private String itemType;

    private String fileTarget;

    private String shareWith;

    private String shareWithDisplayName;

    private String shareWithGroupInfo;

}
