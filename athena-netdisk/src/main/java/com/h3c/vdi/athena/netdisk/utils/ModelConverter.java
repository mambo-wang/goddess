package com.h3c.vdi.athena.netdisk.utils;
import com.github.sardine.DavResource;
import com.h3c.vdi.athena.common.utils.PoiUtils;
import com.h3c.vdi.athena.netdisk.model.dto.FileInfoDTO;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @author w14014
 * @date 2018/9/19
 */
public class ModelConverter {

    public static FileInfoDTO fileConvert(DavResource davResource, String username){
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        fileInfoDTO.setName(davResource.getName());
        fileInfoDTO.setPath(StringUtils.substringAfterLast(davResource.getPath(), NextCloudUrls.File.BASE_WEBDAV));
        if(davResource.isDirectory()){
            fileInfoDTO.setType(FileType.folder.getType());
        } else {
            fileInfoDTO.setType(FileType.findType(davResource.getName()));
        }
        fileInfoDTO.setContentLength(formatContentLength(davResource.getContentLength()));
        fileInfoDTO.setModified(PoiUtils.formatFullDateTime(davResource.getModified().getTime()));
        fileInfoDTO.setAbsolutePath(String.format(NextCloudUrls.File.ABSOLUTE_PATH, username) + fileInfoDTO.getPath());
        return fileInfoDTO;
    }

    public static String formatContentLength(long contentLength){
        String fileSize;
        float size = Float.valueOf(String.format("%.2f",contentLength/(float)Constant.KB));
        if(size > Constant.KB){
            if(size > Constant.MB){
                fileSize = String.format("%.2f",contentLength/(float)Constant.GB) + " GB";
            } else {
                fileSize = String.format("%.2f",contentLength/(float)Constant.MB) + " MB";
            }
        }else {
            fileSize = Math.abs(size) + " KB";
        }
        return fileSize;
    }
}
