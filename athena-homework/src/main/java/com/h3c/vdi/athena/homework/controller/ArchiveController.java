package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.service.archive.ArchiveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by w16051 on 2018/10/13.
 */
@RestController
@RequestMapping(value = "/archive")
public class ArchiveController {

    @Resource
    ArchiveService archiveService;

    @PutMapping(value = "/{year}")
    public void archive(@PathVariable String year){
        archiveService.archiveDataByYear(year);
    }

    @GetMapping(value = "/years")
    public List<String> queryYears(){
        return archiveService.queryYears();
    }
}
