package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class bookSlimWithLibraryAndStockController {

    private final BookSlimWithLibraryAndStockDtoMapper bookSlimWithLibraryAndStockDtoMapper;
    private final StockService stockService;
    private final BookService bookService;
    private final LibraryService libraryService;

    @GetMapping(value="bookSlims")
    public ResponseEntity <List<BookSlimWithLibraryAndStockDto>> bookSlimsList(){
        log.info("HTTP GET request received at /bookSlims with bookSlimsList");
        return new ResponseEntity<>(bookSlimWithLibraryAndStockDtoMapper.booksStocksLibrariesToAllBookSlimWithLibraryAndStockDto(bookService.findAllBooks()), HttpStatus.OK);
    }
    @PostMapping(value="bookSlims")
    public ResponseEntity<BookSlimWithLibraryAndStockDto>saveABookSlim(@RequestBody BookSlimWithLibraryAndStockDto bookSlimDto){
        log.info("HTTP POST request received at /bookSlims with saveABookSlim");
        Stock stock = stockService.saveAStock(bookSlimWithLibraryAndStockDtoMapper.bookSlimWithLibraryAndStockDtoToStock(bookSlimDto));
        Book book = bookService.saveABook(bookSlimWithLibraryAndStockDtoMapper.bookSlimWithLibraryAndStockDtoToBook(bookSlimDto));
        Library library = libraryService.saveALibrary(bookSlimWithLibraryAndStockDtoMapper.bookSlimWithLibraryAndStockDtoToLibrary(bookSlimDto));
        book.setLibrary(library);
        book.setStock(stock);
        bookService.saveABook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookSlimDto);
    }
    @DeleteMapping(value="bookSlims/delete/{id}")
    public ResponseEntity deleteABookSlim(@PathVariable Integer id){
        log.info("HTTP DELETE request received at /bookSlims/"+ id +" with deleteABookSlim");
        Book book = bookService.getABookById(id);
        Stock stock = book.getStock();
        stockService.deleteAStock(stock);
        book.setLibrary(null);
        bookService.deleteABook(book);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
