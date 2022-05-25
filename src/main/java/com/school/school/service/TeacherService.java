package com.school.school.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.school.pojo.LoginForm;
import com.school.school.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getteacherByid(Long userId);

    IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher);
}
