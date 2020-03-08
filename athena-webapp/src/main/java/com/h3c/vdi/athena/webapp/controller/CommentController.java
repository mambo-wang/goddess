package com.h3c.vdi.athena.webapp.controller;
import com.h3c.vdi.athena.webapp.dto.CommentDTO;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by z13339 on 2018/3/21.
 */
@Api(value = "评论controller", tags = {"作业评论相关操作"})
@RestController
public class CommentController {

    @Resource
    private HomeworkFeignService homeworkFeignService;

    /**
     * 添加评论
     *
     * @param commentDTO
     */
    @RequestMapping(value = "/homework/comments", method = RequestMethod.POST)
    public void addComment(@RequestBody CommentDTO commentDTO) {
        homeworkFeignService.addComment(commentDTO);
    }

    /**
     * 删除评论
     *
     * @param id
     */
    @RequestMapping(value = "/homework/comments/{id}", method = RequestMethod.DELETE)
    public void deleteComment(@PathVariable Long id) {
        homeworkFeignService.deleteComment(id);
    }

    /***
     * 根据作业信息
     * 获取其评论信息
     * @param relationId
     * @return
     */
    @RequestMapping(value = "/homework/comments/{id}/{type}", method = RequestMethod.GET)
    public List<CommentDTO> getCommentByRelationId(@PathVariable Long relationId, @PathVariable int type) {
        return homeworkFeignService.getCommentByRelationId(relationId, type);
    }

}
