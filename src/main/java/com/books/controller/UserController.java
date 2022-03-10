package com.books.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.books.common.dto.LoginDto;
import com.books.common.lang.Result;
import com.books.entity.User;
import com.books.service.UserService;
import com.books.shiro.AccountProfile;
import com.books.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Dur02
 * @since 2022-03-01
 */
@RestController
public class UserController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @GetMapping("/index")
    public Result index() {
        User user = userService.getById(1);
        return Result.succ(user);
    }

    @PostMapping("/test")
    public Result test(@Validated @RequestBody User user) {
        return Result.succ(user);
    }

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto,HttpServletResponse response) {
        SecurityUtils.getSubject().logout();
        //每次登录先注销之前登陆账号的信息，否则登录成功后账户信息还是之前的
        System.out.println(loginDto);
        User user = userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));
        Assert.notNull(user,"用户不存在");
//        !user.getPassword().equals(SecureUtil.md5(loginDto.getPassword())) 可数据库进行md5或其他加密，此处先不做
        if (!user.getPassword().equals(loginDto.getPassword())){
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtil.generateToken(user.getId());
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-header","Authorization");
        return Result.succ(MapUtil.builder()
                .put("id",user.getId())
                .put("username",user.getUsername())
                .put("role",user.getRole())
                .map()

        );
    }

    @RequiresAuthentication
    @PostMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

}
