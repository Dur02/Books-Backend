package com.books.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.books.common.lang.Result;
import com.books.entity.Book;
import com.books.entity.History;
import com.books.service.BookService;
import com.books.service.HistoryService;
import com.books.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.books.controller.BookController;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Dur02
 * @since 2022-03-01
 */
@RestController
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @Autowired
    BookService bookService;

    @RequiresAuthentication
    @PostMapping("/history")
    public Result history(@RequestBody Map<String,Object> param) {
        Integer id = (Integer) param.get("id");
//        System.out.println(id);
        History history = new History();
        history.setUserid(id);
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",history.getUserid());
        List<History> selectList = historyService.list(queryWrapper);
        if (selectList.size() == 0) {
            return Result.fail("无此用户借书记录");
        }
        return Result.succ(selectList);
    }


}
