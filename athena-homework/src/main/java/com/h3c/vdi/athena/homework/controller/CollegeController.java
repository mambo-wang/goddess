package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.homework.dto.CollegeDTO;
import com.h3c.vdi.athena.homework.entity.College;
import com.h3c.vdi.athena.homework.service.college.CollegeService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z13339 on 2018/3/3.
 */
@RestController
@RequestMapping("/colleges")
public class CollegeController {
    private Logger logger = LoggerFactory.getLogger(CollegeController.class);

    @Resource
    private CollegeService collegeMgr;

    private static StringManager sm = StringManager.getManager(College.class);


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CollegeDTO findById(@PathVariable Long id) {

            CollegeDTO list = collegeMgr.queryCollegeById(id);
            return list;

    }

    @GetMapping
    public List<CollegeDTO> queryCollegeDTOs(){
        return collegeMgr.queryAllColleges();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addCollege(@RequestBody CollegeDTO collegeDTO) {

            collegeMgr.addCollege(collegeDTO);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateCollege(@RequestBody CollegeDTO collegeDTO) {

            collegeMgr.updateCollege(collegeDTO);
    }

    @RequestMapping(value = "/addbatch", method = RequestMethod.POST)
    public List<String> addCollegeBatch(@RequestBody List<CollegeDTO> collegeDTOList) {
        List<String> result = new ArrayList<String>();
        if (CollectionUtils.isEmpty(collegeDTOList)) {
            return new ArrayList<String>();
        }
        int size = collegeDTOList.size();
        for (int i = 0; i < size; i++) {
            CollegeDTO collegeDTO = collegeDTOList.get(i);
            try {
                collegeMgr.addCollege(collegeDTO);
            } catch (AppException e) {
                logger.warn("add college failed: ", e);
                result.add(i + ":" + collegeDTO + "," + e.getErrorMessage());
            } catch (Exception ex) {
                logger.warn("add college failed: ", ex);
                result.add(i + ":" + collegeDTO + "," + ex.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public void deleteCollege(@RequestBody CommonDTO<ArrayList<Long>> commonDTO) {
        ArrayList<Long> idList = commonDTO.getData();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }

        Long count = idList.stream()
                .filter(x -> collegeMgr.deleteCollege(x))
                .count();
        if (count == 0) {
            throw new AppException(sm.getString("delete.college.failure"));
        } else if (count > 0 && count < idList.size()) {
            String resultDesc = sm.getString("delete.result", count, (idList.size() - count));
            throw new AppException(resultDesc);
        }
    }


}
