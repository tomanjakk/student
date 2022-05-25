package com.school.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.school.mapper.GradeMapper;
import com.school.school.pojo.Grade;
import com.school.school.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;


@Service
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper,Grade> implements GradeService{
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> gradePage, String gradeName) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper();

        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        queryWrapper.orderByDesc("id");
        IPage<Grade> page = baseMapper.selectPage(gradePage, queryWrapper);
        return page;
    }

    @Override
        public List<Grade> getGrades(){
            List<Grade> grades = baseMapper.selectList(null);
            return grades;
        }

}