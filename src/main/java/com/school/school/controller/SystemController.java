package com.school.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.school.school.pojo.Admin;
import com.school.school.pojo.LoginForm;
import com.school.school.pojo.Student;
import com.school.school.pojo.Teacher;
import com.school.school.service.AdminService;
import com.school.school.service.StudentService;
import com.school.school.service.TeacherService;
import com.school.school.util.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId=JwtHelper.getUserId(token);
        Integer userType=JwtHelper.getUserType(token);
        Map<String,Object> map=new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin=adminService.getAdminByid(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student=studentService.getstudentByid(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher=teacherService.getteacherByid(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }


    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        System.out.println("类型是+"+loginForm.getUserType());
        HttpSession session = request.getSession();
        String sessionverifiCode= (String) session.getAttribute("verifiCode");
        String loginsession=loginForm.getVerifiCode();

        if ("".equals(sessionverifiCode) || null==sessionverifiCode){
            return Result.fail().message("验证码失效");
        }
        if (!sessionverifiCode.equalsIgnoreCase(loginsession)){
            return Result.fail().message("验证码有误");
        }
//移除seeeion
        session.removeAttribute("verifiCode");

        Map<String,Object> map=new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin=adminService.login(loginForm);
                    if (null !=admin){
                        map.put("token",JwtHelper.createToken(admin.getId().longValue(),1));
                    }else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                 return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student= studentService.login(loginForm);

                    if (null !=student){
                        //                    String token= JwtHelper.createToken(admin.getId().longValue())
                        map.put("token",JwtHelper.createToken(student.getId().longValue(),2));
                    }else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher=teacherService.login(loginForm);
                    if (null !=teacher){
                        map.put("token",JwtHelper.createToken(teacher.getId().longValue(),3));
                    }else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此人");
    }

    @GetMapping("/getVerifiCodeImage")
     public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    获取图片
    BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
//    获取图片验证码
    String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        System.out.println("图片验证码是+"+verifiCode);
//    将文本放入seeeion域
    HttpSession session = request.getSession();
    session.setAttribute("verifiCode", verifiCode);
//    响应到浏览器
ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
    }



    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("文件二进制数据") @RequestPart("multipartFile") MultipartFile multipartFile
    ){

        //使用UUID随机生成文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //生成新的文件名字
        String filename = uuid.concat(multipartFile.getOriginalFilename());
        //生成文件的保存路径(实际生产环境这里会使用真正的文件存储服务器)
        String portraitPath ="D:/桌面/school/xm/target/classes/public/upload/".concat(filename);
        //保存文件
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String headerImg ="upload/"+filename;
        return Result.ok(headerImg);
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token,
                            @PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd){
        boolean yOn = JwtHelper.isExpiration(token);
        if(yOn){
            //token过期
            return Result.fail().message("token失效!");
        }
        //通过token获取当前登录的用户id
        Long userId = JwtHelper.getUserId(token);
        //通过token获取当前登录的用户类型
        Integer userType = JwtHelper.getUserType(token);
        // 将明文密码转换为暗文
        oldPwd= MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);
        if(userType == 1){
            QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
            Admin admin = adminService.getOne(queryWrapper);
            if (null!=admin) {
                admin.setPassword(newPwd);
                adminService.saveOrUpdate(admin);
            }else{
                return Result.fail().message("原密码输入有误！");
            }
        }else if(userType == 2){
            QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
            Student student = studentService.getOne(queryWrapper);
            if (null!=student) {
                student.setPassword(newPwd);
                studentService.saveOrUpdate(student);
            }else{
                return Result.fail().message("原密码输入有误！");
            }
        }
        else if(userType == 3){
            QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
            Teacher teacher = teacherService.getOne(queryWrapper);
            if (null!=teacher) {
                teacher.setPassword(newPwd);
                teacherService.saveOrUpdate(teacher);
            }else{
                return Result.fail().message("原密码输入有误！");
            }
        }
        return Result.ok();
    }

}
