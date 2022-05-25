package com.school.school;

import com.school.school.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test1 {
    @Autowired
    private AdminService adminService;

    @Test
    public void ss(){
        System.out.println(adminService.count());
    }
}
