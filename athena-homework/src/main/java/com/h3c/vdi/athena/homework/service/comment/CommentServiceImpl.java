package com.h3c.vdi.athena.homework.service.comment;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.dao.CommentDao;
import com.h3c.vdi.athena.homework.dto.CommentDTO;
import com.h3c.vdi.athena.homework.entity.Comment;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by z13339 on 2018/3/20.
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Override
    public void addComment(CommentDTO commentDTO) {
        if (Objects.isNull(commentDTO)) {
            throw new AppException(ErrorCodes.COMMENT_NOT_FIND);
        }
        Comment comment = this.coverCommentDtoToComment(commentDTO);
        comment.setDeleted(BasicConstant.IS_DELETED_N);
        comment.setCreateTime(System.currentTimeMillis());
        int count = commentDao.countCommentsByRelationId(commentDTO.getRelationId());
        comment.setFloor(count + 1);
        commentDao.save(comment);

    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentDao.findByIdAndDeleted(id, BasicConstant.IS_DELETED_N);
        if (Objects.isNull(comment)) {
            throw new AppException(ErrorCodes.COMMENT_NOT_FIND);
        }
        comment.setDeleted(BasicConstant.IS_DELETED_Y);
        commentDao.save(comment);
    }

    @Override
    public List<CommentDTO> getCommentByRelationId(Long relationId, int type) {
        List<Comment> lists = commentDao.findByRelationIdAndTypeAndDeleted(relationId, type, BasicConstant.IS_DELETED_N);
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        List<CommentDTO> commonDTOS = lists.stream().map(x -> this.coverCommentToCommentDto(x)).collect(Collectors.toList());
        return commonDTOS;
    }

    private Comment coverCommentDtoToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        return comment;
    }

    private CommentDTO coverCommentToCommentDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        return commentDTO;
    }
}
