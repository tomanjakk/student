package com.school.school.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.school.pojo.Grade;
import com.school.school.service.GradeService;
import com.school.school.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年纪控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    @ApiOperation("获取所有Grade信息")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGradeById(
            @ApiParam("JSON的年级id集合,映射为后台List<Integer>")@RequestBody List<Integer> ids
    )
    {
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("添加或者修改年级信息")
    @PostMapping("/saveOrUpdateGrade")
    public  Result saveOrUpdateGrade(
            @ApiParam("JSON的grade对象转换后台数据模型")@RequestBody Grade grade
    ){
        // 调用服务层方法,实现添加或者修改年级信息
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("根据年级名称模糊查询,带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数量") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页面大小")@PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配名称")String gradeName){

        Page<Grade> GradePage = new Page<>(pageNo, pageSize);
        IPage<Grade> pageRs=gradeService.getGradeByOpr(GradePage,gradeName);
       return Result.ok(pageRs);
    }
}
