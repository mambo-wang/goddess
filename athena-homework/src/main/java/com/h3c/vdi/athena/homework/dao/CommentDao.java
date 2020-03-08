package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.College;
import com.h3c.vdi.athena.homework.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by z13339 on 2018/3/20.
 */
public interface CommentDao extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<College> {

    /**
     * 根据作业Id查询作业的总数
     *
     * @param relationId
     * @return
     */
    @Query(value = "select count(1) from tbl_comment where IS_DELETED='n'", nativeQuery = true)
    int countCommentsByRelationId(Long relationId);

    /**
     * 根据作业Id查询作业
     *
     * @param relationId
     * @param deleted
     * @return
     */
    List<Comment> findByRelationIdAndTypeAndDeleted(Long relationId, int type, String deleted);

    Comment findByIdAndDeleted(Long id, String deleted);
}
