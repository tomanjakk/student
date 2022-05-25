package com.school.school.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.school.pojo.Admin;
import com.school.school.pojo.Student;
import com.school.school.pojo.Teacher;
import com.school.school.service.AdminService;
import com.school.school.service.StudentService;
import com.school.school.service.TeacherService;
import com.school.school.util.JwtHelper;
import com.school.school.util.MD5;
import com.school.school.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "系统管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {


    @Autowired
    private AdminService adService;

    @ApiOperation("分页获取所有Admin信息【带条件】")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(@PathVariable Integer pageNo,
                              @PathVariable Integer pageSize,
                              String adminName){
        Page<Admin> pageParam = new Page<>(pageNo,pageSize);
        IPage<Admin> page = adService.getAdmins(pageParam, adminName);
        return Result.ok(page);
    }

//    @ApiOperation("添加或修改Admin信息")
//    @PostMapping("/saveOrUpdateAdmin")
//    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
//        if (!Strings.isEmpty(admin.getPassword())) {
//            admin.setPassword(MD5.encrypt(admin.getPassword()));
//        }
//        adService.saveOrUpdate(admin);
//        return Result.ok();
//    }

    //POST
    //	http://localhost:9002/sms/adminController/saveOrUpdateAdmin  admin
    @ApiOperation("增加或修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result  saveOrUpdateAdmin(
            @ApiParam("JSON格式的Admin对象") @RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if (id==null || 0 ==id) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adService.saveOrUpdate(admin);
        return Result.ok();

    }
    @ApiOperation("删除Admin信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        adService.removeByIds(ids);
        return Result.ok();
    }



}
