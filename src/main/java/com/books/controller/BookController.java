package com.books.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.books.common.lang.Result;
import com.books.entity.Book;
import com.books.entity.History;
import com.books.entity.User;
import com.books.service.BookService;
import com.books.service.HistoryService;
import com.books.service.UserService;
import com.books.shiro.AccountProfile;
import com.books.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Dur02
 * @since 2022-03-01
 */
@RestController
public class BookController {
    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    HistoryService historyService;

    @RequiresAuthentication
    @GetMapping("/books")
    public  Result list(){
        List<Book> selectList = bookService.list();
        return Result.succ(selectList);
    }

    @RequiresRoles("1")
    @RequiresAuthentication
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Book book) {
//        AccountProfile accountProfile = ShiroUtil.getProfile();
//        System.out.println(accountProfile);
        System.out.println("在这"+userService.getById(ShiroUtil.getProfile().getId()).getRole());
        Assert.isTrue(userService.getById(ShiroUtil.getProfile().getId()).getRole() == 1,"无权限进行操作");
        Book book2 = new Book();
        book2.setBookname(book.getBookname()).setAuthor(book.getAuthor()).setNumber(book.getNumber());
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bookname",book2.getBookname()).eq("author",book2.getAuthor());
        List<Book> selectList = bookService.list(queryWrapper);

        if (selectList.size() == 0) {
            bookService.saveOrUpdate(book);
        }else {
            book2 = selectList.get(0);
            book2.setNumber(book.getNumber()+book2.getNumber());
            bookService.saveOrUpdate(book2);
        }
        ;
        return Result.succ(book2);
    }

    @RequiresAuthentication
    @PostMapping("/search")
    public Result search(@Validated @RequestBody Map<String,Object> param) {
//        Book book = bookService.getById(1);
//        return Result.succ(book);
        System.out.println("在这2---------------"+userService.getById(ShiroUtil.getProfile().getId()).getUsername());
        String key  = (String) param.get("key");
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("bookname",key).or().like("author",key);
        List<Book> selectList = bookService.list(queryWrapper);
        return Result.succ(selectList);
    }

    @RequiresRoles("admin")
    @RequiresAuthentication
    @PostMapping("/borrow")
    public Result borrow(@Validated @RequestBody Book oldBook) {
        //oldBook中存储的为借取的数量
        Book newBook = bookService.getById(oldBook.getId());
        Assert.isTrue(newBook.getNumber()-oldBook.getNumber() >= 0,"库存不足");
        newBook.setNumber(newBook.getNumber()-oldBook.getNumber());

        History history = new History();
        history.setBookid(oldBook.getId()).setUserid(ShiroUtil.getProfile().getId()).setNumber(oldBook.getNumber());
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bookid",history.getBookid()).eq("userid",history.getUserid());
        List<History> selectList = historyService.list(queryWrapper);

        if (selectList.size() == 0) {
            historyService.saveOrUpdate(history);
        }else {
            History history1 = selectList.get(0);
            history1.setNumber(history1.getNumber()+history.getNumber());
            historyService.saveOrUpdate(history1);
        }
        bookService.saveOrUpdate(newBook);
        return Result.succ(newBook);
    }


    @RequiresAuthentication
    @PostMapping("/return")
    public Result Return(@RequestBody Map<String,Object> param) {
        //oldBook中存储的为归还的数量
        Integer historyId = (Integer) param.get("id");
        Integer number = (Integer) param.get("number");
        Integer bookid = historyService.getById(historyId).getBookid();
        Book oldBook = bookService.getById(bookid);
        oldBook.setNumber(number);

        Book newBook = bookService.getById(oldBook.getId());
        //还要防止用户还书的数量超过所借的数量
        Assert.isTrue(newBook.getNumber()-oldBook.getNumber() >= 0,"超过借取数量");
        newBook.setNumber(newBook.getNumber()+oldBook.getNumber());


        History oldHistory = new History();
        oldHistory.setBookid(oldBook.getId()).setUserid(ShiroUtil.getProfile().getId()).setNumber(oldBook.getNumber());
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bookid",oldHistory.getBookid()).eq("userid",oldHistory.getUserid());
        List<History> selectList = historyService.list(queryWrapper);
        Assert.isTrue(ShiroUtil.getProfile().getId().equals(oldHistory.getUserid()),"无该用户借取记录");

        History newHistory = selectList.get(0);
        newHistory.setNumber(newHistory.getNumber()-oldHistory.getNumber());
        if (newHistory.getNumber() == 0){
            historyService.removeById(newHistory.getId());
            bookService.saveOrUpdate(newBook);
            return Result.succ(newHistory);
        }
        historyService.saveOrUpdate(newHistory);
        bookService.saveOrUpdate(newBook);
        return Result.succ(newHistory);
    }


    @RequiresAuthentication
    @PostMapping("/getBookByID")
    public String getBookByID(@RequestBody Map<String,Object> param) {
        Integer bookid = (Integer) param.get("bookid");
        String bookName = bookService.getById(bookid).getBookname();
        return bookName;
    }
}
