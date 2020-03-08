package com.h3c.vdi.athena.homework.service.comment;

import com.h3c.vdi.athena.homework.dto.CommentDTO;

import java.util.List;

/**
 * Created by z13339 on 2018/3/20.
 */
public interface CommentService {
    /**
     * 添加评论
     * @param commentDTO
     */
    void addComment(CommentDTO commentDTO);

    /**
     * 删除评论
     * @param id
     */
    void deleteComment(Long id);

    /***
     * 根据作业信息
     * 获取其评论信息
     * @param relationId
     * @return
     */
    List<CommentDTO> getCommentByRelationId(Long relationId,int type);

}
