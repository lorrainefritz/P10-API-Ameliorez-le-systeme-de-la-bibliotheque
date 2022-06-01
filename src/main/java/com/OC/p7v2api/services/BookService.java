package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAllBooks(){
        log.info("in BookService in findAllBooks method");
        return bookRepository.findAll();
    }

    public Book getABookById(Integer id) {
        log.info("in BookService in getOneBookById method");
        return bookRepository.getById(id);
    }


    public Book saveABook(Book book) {
        log.info("in BookService in addBook method");
        return bookRepository.save(book);
    }



    public void deleteABook(Book book) {
        log.info("in BookService in deleteABook method");
        bookRepository.delete(book);
    }

    public List<Book> findBooksWithKeyword(String keyword) {
        log.info("in BookService in findBooksWithKeyword method  with keyword : " + keyword);
        return bookRepository.findBooksByKeyword(keyword);
    }




}
