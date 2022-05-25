package com.school.school.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.school.pojo.Grade;

import java.util.List;

public interface GradeService extends IService<Grade> {
    IPage<Grade> getGradeByOpr(Page<Grade> gradePage, String gradeName);

    List<Grade> getGrades();


//    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);
//
//    List<Grade> getGrades();
}
