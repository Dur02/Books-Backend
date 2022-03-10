package com.books.service.impl;

import com.books.entity.Book;
import com.books.mapper.BookMapper;
import com.books.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Dur02
 * @since 2022-03-01
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

}
