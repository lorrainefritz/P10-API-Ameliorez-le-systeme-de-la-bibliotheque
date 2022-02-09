package com.OC.p7v2api.controllers;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.StockService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
public class BookController {

    private final BookService bookService;

    @GetMapping(value ="/books")
    public List<Book> findAllBooks() {
        log.info("HTTP GET request received at /books with findAllBooks");
        return bookService.findAllBooks();
    }

    @GetMapping(value = "/books/{id}")
    public Book getABook(@PathVariable(value = "id") Integer id) {
        log.info("HTTP GET request received at /books/"+id+" with getABook");
        return bookService.getABookById(id);
    }
}
