package com.h3c.vdi.athena.homework.service.homeworkPraise;

import com.h3c.vdi.athena.homework.dto.HomeworkPraiseDTO;

import java.util.List;

/**
 * Created by z13339 on 2018/4/24.
 */
public interface HomeworkPraiseService {

   void addPraise(Long homeworkSubmitId,Long userId);


   void deletePraise(Long homeworkPraiseId, Long userId);

   List<HomeworkPraiseDTO> queryPraisesByHomeworkId(Long homeworkSubmitId);
}
