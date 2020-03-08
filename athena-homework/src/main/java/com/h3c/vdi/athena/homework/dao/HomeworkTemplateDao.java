package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.HomeworkTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HomeworkTemplateDao extends JpaRepository<HomeworkTemplate, Long> {

    /**
     * 查询所有状态为未删除的作业模板
     *
     * @return 状态为未删除的作业模板列表
     */
    @Query(value = "select * from tbl_homework_template where IS_DELETED = 'n'", nativeQuery = true)
    List<HomeworkTemplate> findAll();

    /**
     * 根据用户id查询状态为未删除的作业模板
     *
     * @param userId 用户id
     * @return 作业模板集合
     */
    @Query(value = "select * from tbl_homework_template where USER_ID=(?1) and IS_DELETED = 'n'", nativeQuery = true)
    List<HomeworkTemplate> findByUserId(Long userId);

    HomeworkTemplate findByNameAndIsDeleted(String name, String delete);

    @Query(value = "select Name from tbl_homework_template where Name like '?1%' and IS_DELETED = 'n'", nativeQuery = true)
    List<String> findLikeName(String name);

    HomeworkTemplate findByIdAndIsDeleted(Long id, String delete);
}
