package com.school.school.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project: ssm_sms
 * @description: 用户登录表单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;

}
