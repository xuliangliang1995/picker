package com.grasswort.picker.elastic.service;

import com.grasswort.picker.elastic.entity.BookBean;
import com.grasswort.picker.elastic.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname BookService
 * @Description TODO
 * @Date 2019/11/30 14:55
 * @blame Java Team
 */
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    //@PostConstruct
    public void addABook() {
        BookBean bookBean = new BookBean("1", "本草纲目", "李时珍", "2018-10-01");
        bookRepository.save(bookBean);
    }

    @PostConstruct
    public void findById() {
        Optional<BookBean> bookBeanOpt = this.findById("1");
        if (bookBeanOpt.isPresent()) {
            System.out.println(bookBeanOpt.get());
        }
    }

    public Optional<BookBean> findById(String id) {
        return bookRepository.findById(id);
    }
}
