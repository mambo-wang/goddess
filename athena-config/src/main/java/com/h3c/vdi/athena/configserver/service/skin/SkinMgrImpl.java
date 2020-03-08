package com.h3c.vdi.athena.configserver.service.skin;

import com.h3c.vdi.athena.configserver.mapper.SkinMapper;
import com.h3c.vdi.athena.configserver.model.dto.SkinDTO;
import com.h3c.vdi.athena.configserver.model.entity.Skin;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Service("skinMgr")
public class SkinMgrImpl implements SkinMgr {

    @Resource
    private SkinMapper skinMapper;

    @Override
    public SkinDTO querySkinConfig(String username) {
        Skin skin =  skinMapper.getByUsername(username);

        if(Objects.isNull(skin)){
            skinMapper.insert(username, 1);
            skin = skinMapper.getByUsername(username);
        }

        SkinDTO skinDTO = new SkinDTO();
        BeanUtils.copyProperties(skin, skinDTO);
        return skinDTO;
    }

    @Override
    public void updateSkinConfig(SkinDTO skinDTO) {

        this.skinMapper.update(skinDTO.getUsername(), skinDTO.getSkinNumber());
    }

    @Override
    public void deleteSkinConfig(String username) {

        this.skinMapper.delete(username);
    }
}
