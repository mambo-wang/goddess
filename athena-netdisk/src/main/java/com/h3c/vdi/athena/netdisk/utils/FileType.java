package com.h3c.vdi.athena.netdisk.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/8
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum  FileType {


    /**文件夹*/
    folder("folder", "", 0, Collections.emptyList()),
    others("others", "", 6, Collections.emptyList()),

    /** 文件 */
    document("document", Constant.FileType.DOCUMENT, 1, Arrays.asList(Constant.FileType.DOCUMENT.split("\\|"))),
    music("music", Constant.FileType.MUSIC, 2, Arrays.asList(Constant.FileType.MUSIC.split("\\|"))),
    video("video",Constant.FileType.VIDEO, 3, Arrays.asList(Constant.FileType.VIDEO.split("\\|"))),
    picture("picture",Constant.FileType.PICTURE, 4, Arrays.asList(Constant.FileType.PICTURE.split("\\|"))),
    compress("compress", Constant.FileType.COMPRESS, 5, Arrays.asList(Constant.FileType.COMPRESS.split("\\|")));

    private String desc;
    private String value;
    private Integer type;
    private List<String> values;

    public static String findValueByDesc(String desc){
        return valueOf(desc.toLowerCase()).getValue();
    }

    public static Integer findType(String fileName){
        String typeStr = StringUtils.substringAfterLast(fileName, ".");
        if(document.getValues().contains(typeStr)){
            return document.getType();
        } else if(music.getValues().contains(typeStr)){
            return music.getType();
        } else if(video.getValues().contains(typeStr)){
            return video.getType();
        } else if(picture.getValues().contains(typeStr)){
            return picture.getType();
        } else if(compress.getValues().contains(typeStr)){
            return compress.getType();
        } else {
            return others.getType();
        }
    }

}
