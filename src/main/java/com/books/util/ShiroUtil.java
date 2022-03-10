package com.books.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.books.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {
    public static AccountProfile getProfile() {
        JSONObject jsonObject = JSONUtil.parseObj(SecurityUtils.getSubject().getPrincipal());
        return JSONUtil.toBean(jsonObject,AccountProfile.class);
    }
}
