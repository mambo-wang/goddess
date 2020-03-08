package com.h3c.vdi.athena.homework.service.archive;

import java.util.List;

/**
 * Created by w16051 on 2018/10/13.
 */
public interface ArchiveService {

    /**
     * 根据入学年份归档学生数据
     * @param year
     */
    void archiveDataByYear(String year);

    /**
     * 查询已有的入学年份
     * @return
     */
    List<String> queryYears();
}
