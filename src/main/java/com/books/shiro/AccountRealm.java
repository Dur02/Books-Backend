package com.books.shiro;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.books.controller.UserController;
import com.books.entity.User;
import com.books.service.UserService;
import com.books.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        JSONObject jsonObject = JSONUtil.parseObj(principals.getPrimaryPrincipal());
        System.out.println(jsonObject);
        String rrr = jsonObject.get("role").toString();
        System.out.println(jsonObject.get("role"));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        roles.add(rrr);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        System.out.println("JWT-------------------------"+token);
        String userId = jwtUtil.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        System.out.println(userId);
        User user = userService.getById(Integer.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }
//        if (user.getRole() == 0){
//            throw new
//        }
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user,profile);
        System.out.println("--------------");
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }

    public Set<String> getRole(String userid) {
        User user = userService.getById(userid);
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole().toString());
        return roles;
    }
}
