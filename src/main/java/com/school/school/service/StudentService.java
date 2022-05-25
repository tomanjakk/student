package com.school.school.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.school.pojo.LoginForm;
import com.school.school.pojo.Student;


public interface StudentService extends IService<Student> {
//    static Student login(LoginForm loginForm) {
//        return null;
//    }
    Student login(LoginForm loginForm);

    Student getstudentByid(Long userId);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
