package com.h3c.vdi.athena.configserver.service.skin;

import com.h3c.vdi.athena.configserver.model.dto.SkinDTO;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
public interface SkinMgr {

    SkinDTO querySkinConfig(String username);

    void updateSkinConfig(SkinDTO skinDTO);

    void deleteSkinConfig(String username);

}
