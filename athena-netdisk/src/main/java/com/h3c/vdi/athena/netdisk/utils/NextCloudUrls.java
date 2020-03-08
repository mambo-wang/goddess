package com.h3c.vdi.athena.netdisk.utils;

/**
 * @author z15722
 * @date 2018/8/28 14:11
 */
public interface NextCloudUrls {

    interface User {

        String QUERY_USER_BY_ID = "ocs/v1.php/cloud/users/%s";
        String USER = "ocs/v1.php/cloud/users";
        String USER_SEARCH = "ocs/v1.php/cloud/users?search=%s";
        String USER_ENABLE = "ocs/v1.php/cloud/users/%s/enable";
        String USER_DISABLE = "ocs/v1.php/cloud/users/%s/disable";
        String USER_DELETE = "ocs/v1.php/cloud/users/%s";
        String USER_MODIFY = "ocs/v1.php/cloud/users/%s?key=%s&value=%s";
        String USER_GROUP = USER + "/%s/groups";

    }

    interface Group{
        String GROUP = "ocs/v1.php/cloud/groups";
        String GROUP_SEARCH = GROUP + "?search=%s";
        String GROUP_DELETE = GROUP + "/%s";
    }

    interface File{
        String BASE_WEBDAV = "/remote.php/webdav";
        String SHARE = "ocs/v2.php/apps/files_sharing/api/v1/shares";
        String SHARED_WITH = "ocs/v2.php/apps/files_sharing/api/v1/shares?shared_with_me=%s";
        String SHARE_DELETE = SHARE + "/%s";
        String ABSOLUTE_PATH = "/var/file/nextcloud/data/%s/files";
        String BASE_DAV = "/remote.php/dav";
        String CREATE_FOLDER = BASE_DAV + "/files/%s%s";
    }


}
