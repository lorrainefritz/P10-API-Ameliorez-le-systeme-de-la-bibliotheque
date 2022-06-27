package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
public class BookController {

    private final BookSlimWithLibraryAndStockDtoMapper bookDtoMapper;
    private final StockService stockService;
    private final BookService bookService;
    private final LibraryService libraryService;


    @GetMapping(value = "/books")
    public ResponseEntity<List<BookSlimWithLibraryAndStockDto>> bookList() {
        log.info("HTTP GET request received at /books with bookList");
        return new ResponseEntity<>(bookDtoMapper.booksToAllBooksDto(bookService.getAscendingSortedBooksById()), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/books/search")
    public ResponseEntity booksListWithAKeyword(@RequestParam(value = "keyword") String keyword) {
        log.info("HTTP GET request received at /books with keyword : " + keyword + " with booksListWithAKeyword");
        if (keyword == null) {
            return new ResponseEntity<>(bookDtoMapper.booksToAllBooksDto(bookService.findAllBooks()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookDtoMapper.booksToAllBooksDto(bookService.findBooksWithKeyword(keyword)), HttpStatus.OK);
        }
    }


    @PostMapping(value = "/books")
    public ResponseEntity<BookSlimWithLibraryAndStockDto> saveABook(@RequestBody BookSlimWithLibraryAndStockDto bookDto) {
        log.info("HTTP POST request received at /books with saveABook");
        if (bookDto == null) {
            log.info("HTTP POST request received at /books with saveABook where bookDto is null");
            return new ResponseEntity<>(bookDto, HttpStatus.NO_CONTENT);
        } else {
            Stock stock = stockService.saveAStock(bookDtoMapper.bookDtoToStock(bookDto));
            Book book = bookService.saveABook(bookDtoMapper.bookDtoToBook(bookDto));
            Library library = libraryService.saveALibrary(bookDtoMapper.bookDtoToLibrary(bookDto));
            book.setLibrary(library);
            book.setStock(stock);
            bookService.saveABook(book);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
    }

    @DeleteMapping(value = "/books/delete/{id}")
    public ResponseEntity deleteABook(@PathVariable Integer id) {
        log.info("HTTP DELETE request received at /books/delete/" + id + " with deleteABook");
        if (id == null) {
            log.info("HTTP DELETE request received at /books/delete/id where id is null");
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        }
        Book book = bookService.getABookById(id);
        Stock stock = book.getStock();
        stockService.deleteAStock(stock);
        book.setLibrary(null);
        bookService.deleteABook(book);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }



}
