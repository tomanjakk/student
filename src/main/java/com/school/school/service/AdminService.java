package com.school.school.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.school.pojo.Admin;
import com.school.school.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminByid(Long userId);

    IPage<Admin> getAdmins(Page<Admin> pageParam, String adminName);
}
