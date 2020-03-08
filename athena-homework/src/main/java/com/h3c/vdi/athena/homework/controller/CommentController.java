package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.dto.CommentDTO;
import com.h3c.vdi.athena.homework.service.comment.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by z13339 on 2018/3/20.
 */
@RestController
@RequestMapping("/comments")
public class CommentController {
    private Logger logger = LoggerFactory.getLogger(CollegeController.class);

    @Resource
    private CommentService commentService;

    /**
     * 添加评论
     *
     * @param commentDTO
     */
    @RequestMapping(method = RequestMethod.POST)
    public void addComment(@RequestBody CommentDTO commentDTO) {
            commentService.addComment(commentDTO);
            logger.info("add comment success",commentDTO);


    }

    /**
     * 删除评论
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteComment(@PathVariable Long id) {

            commentService.deleteComment(id);
            logger.info("delete comment success");



    }

    /***
     * 根据作业信息
     * 获取其评论信息
     * @param relationId
     * @return
     */
    @RequestMapping(value = "/{id}/{type}", method = RequestMethod.GET)
    public List<CommentDTO> getCommentByRelationId(@PathVariable Long relationId, @PathVariable int type) {

            List<CommentDTO> commentDTOS = commentService.getCommentByRelationId(relationId,type);
            logger.info("query comments success.comments size is {}", commentDTOS.size());
            return commentDTOS;

    }

}
