package com.h3c.vdi.athena.homework.service.homeworkPraise;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.dao.HomeworkPraiseDao;
import com.h3c.vdi.athena.homework.dto.HomeworkPraiseDTO;
import com.h3c.vdi.athena.homework.dto.HomeworkSubDTO;
import com.h3c.vdi.athena.homework.entity.HomeworkPraise;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import com.h3c.vdi.athena.homework.service.homeworkSubmission.HomeworkSubmissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by z13339 on 2018/4/24.
 */
@Service
public class HomeworkPraiseServiceImpl implements HomeworkPraiseService {
    @Resource
    private HomeworkPraiseDao homeworkPraiseDao;

    @Resource
    private HomeworkSubmissionService homeworkSubmissionService;

    @Override
    public void addPraise(Long homeworkSubmitId, Long userId) {
        HomeworkPraise homeworkPraise = new HomeworkPraise();
        homeworkPraise.setDeleted(BasicConstant.IS_DELETED_N);
        HomeworkSubmission homeworkSubmission = homeworkSubmissionService.queryHomeworkSubById(homeworkSubmitId);
        if(Objects.isNull(homeworkSubmission)){
            throw new AppException("该学生提交作业不存在，请刷新后重试");
        }
        homeworkPraise.setHomeworkSubId(homeworkSubmitId);
        homeworkPraise.setUserId(userId);
        homeworkPraise.setCreatTime(System.currentTimeMillis());
        homeworkPraiseDao.save(homeworkPraise);
    }

    @Override
    public void deletePraise(Long homeworkPraiseId, Long userId) {
        HomeworkPraise praise = homeworkPraiseDao.findByHomeworkSubIdAndUserId(homeworkPraiseId, userId,BasicConstant.IS_DELETED_N);
        if(Objects.nonNull(praise)){
            praise.setDeleted(BasicConstant.IS_DELETED_Y);
            homeworkPraiseDao.save(praise);
        }
    }

    @Override
    public List<HomeworkPraiseDTO> queryPraisesByHomeworkId(Long homeworkSubmitId) {
        List<HomeworkPraise> homeworkSubId = homeworkPraiseDao.findByHomeworkSubId(homeworkSubmitId,BasicConstant.IS_DELETED_N);
        if(CollectionUtils.isEmpty(homeworkSubId)){
            return null;
        }
        List<HomeworkPraiseDTO> collect = homeworkSubId.stream()
                .map(a -> convertHomeworkPraiseToDTO(a))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return collect;
    }

    private HomeworkPraiseDTO convertHomeworkPraiseToDTO(HomeworkPraise homeworkPraise){
        HomeworkPraiseDTO homeworkPraiseDTO = new HomeworkPraiseDTO();
        if(Objects.nonNull(homeworkPraise)){
            BeanUtils.copyProperties(homeworkPraise,homeworkPraiseDTO,"homeworkSubmission");
            HomeworkSubDTO homeworkSubDTO = homeworkSubmissionService.queryHomeworkSubDTOById(homeworkPraise.getHomeworkSubId());
            homeworkPraiseDTO.setHomeworkSubDTO(homeworkSubDTO);
        }
        return homeworkPraiseDTO;
    }
}
