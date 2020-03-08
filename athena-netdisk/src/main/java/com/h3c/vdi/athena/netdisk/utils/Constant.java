package com.h3c.vdi.athena.netdisk.utils;


/**
 *
 * @author w14014
 * @date 2018/9/18
 */
public interface Constant {

    /** 请求nextcloud返回的成功状态码 */
    String STATUS_CODE_OK_100 = "100";
    String STATUS_CODE_OK_200 = "200";

    /** 模糊查询和分享时使用 */
    Integer SEARCH_TYPE_USER = 0;
    Integer SEARCH_TYPE_GROUP = 1;

    int KB = 1024;
    int MB = KB*1024;
    int GB = MB*1024;

    interface  FileType{
        String DOCUMENT = "txt|pdf|docx|doc|hlp|wps|rtf|html|xls|xlsx|ppt|pptx";
        String MUSIC = "mp3|mid|wma|rm|cda|wav|aif|au|ram|mmf|amr|aac|flac";
        String VIDEO = "avi|mp4|rmvb|wmv|mpg|mov|swf";
        String PICTURE = "bmp|jpeg|jpg|gif|png|pic|tif";
        String COMPRESS = "zip|rar|zip|gzip|gz|jar|arj|z";
    }

    interface Share{
        /** 未分享 */
        Integer SHARE_NO = 0;
        /** 分享给别人 */
        Integer SHARE_OWNER = 1;
        /** 别人分享给我 */
        Integer SHARE_WITH = 2;
        /**分享的文件类型，文件夹和文件两种*/
        String ITEM_TYPE_FOLDER = "folder";
        String ITEM_TYPE_FILE = "file";

        Integer PERMISSION_USER = 19;
        Integer PERMISSION_GROUP =31;

    }

}
