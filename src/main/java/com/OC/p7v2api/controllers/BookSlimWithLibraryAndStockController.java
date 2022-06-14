package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookSlimWithLibraryAndStockController {

    private final BookSlimWithLibraryAndStockDtoMapper bookSlimWithLibraryAndStockDtoMapper;
    private final StockService stockService;
    private final BookService bookService;
    private final LibraryService libraryService;
    /*private final BorrowService borrowService;*/
    private final ReservationService reservationService;
    private final UserService userService;

    @GetMapping(value = "/books")
    public ResponseEntity<List<BookSlimWithLibraryAndStockDto>> bookSlimsList() {
        log.info("HTTP GET request received at /bookSlims with bookSlimsList");
        return new ResponseEntity<>(bookSlimWithLibraryAndStockDtoMapper.booksStocksLibrariesToAllBookSlimWithLibraryAndStockDto(bookService.findAllBooks()), HttpStatus.OK);
    }
@Transactional
    @GetMapping(value = "/books/search")
    public ResponseEntity booksSlimsListWithAKeyword(@RequestParam(value="keyword") String keyword) {
        log.info("HTTP GET request received at /bookSlims with keyword : " + keyword + " with searchForABooksSlimsListWithAKeyword");
        if (keyword == null) {
            return new ResponseEntity<>(bookSlimWithLibraryAndStockDtoMapper.booksStocksLibrariesToAllBookSlimWithLibraryAndStockDto(bookService.findAllBooks()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookSlimWithLibraryAndStockDtoMapper.booksStocksLibrariesToAllBookSlimWithLibraryAndStockDto(bookService.findBooksWithKeyword(keyword)), HttpStatus.OK);
        }
    }


    @PostMapping(value = "/books")
    public ResponseEntity<BookSlimWithLibraryAndStockDto> saveABookSlim(@RequestBody BookSlimWithLibraryAndStockDto bookSlimDto) {
        log.info("HTTP POST request received at /bookSlims with saveABookSlim");
        Stock stock = stockService.saveAStock(bookSlimWithLibraryAndStockDtoMapper.bookSlimWithLibraryAndStockDtoToStock(bookSlimDto));
        Book book = bookService.saveABook(bookSlimWithLibraryAndStockDtoMapper.bookSlimWithLibraryAndStockDtoToBook(bookSlimDto));
        Library library = libraryService.saveALibrary(bookSlimWithLibraryAndStockDtoMapper.bookSlimWithLibraryAndStockDtoToLibrary(bookSlimDto));
        book.setLibrary(library);
        book.setStock(stock);
        bookService.saveABook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookSlimDto);
    }

    @DeleteMapping(value = "/books/delete/{id}")
    public ResponseEntity deleteABookSlim(@PathVariable Integer id) {
        log.info("HTTP DELETE request received at /bookSlims/" + id + " with deleteABookSlim");
        Book book = bookService.getABookById(id);
        Stock stock = book.getStock();
        stockService.deleteAStock(stock);
        book.setLibrary(null);
        bookService.deleteABook(book);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @GetMapping(value="/books/findById")
    public ResponseEntity getABookFromId(@RequestParam Integer bookId){
        log.info("HTTP GET request received at /bookSlims/" + bookId+ " with getABookFromId");
        return new ResponseEntity<>(bookService.getABookById(bookId),HttpStatus.OK);

    }

    @PostMapping(value = "books/reservation")
    public void makeAReservation(@RequestParam Integer bookId,@RequestParam Integer userId){
        log.info("HTTP POST request received at /books/reservation, where bookId is {} and userId is {}", bookId,userId);

        Book book = bookService.getABookById(bookId);
        User user = userService.getAUserById(userId);
        Date startDate = Date.from(Instant.now());
        Date endDate = Date.from(Instant.now().plusSeconds(172800));//48h
        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setReservationPosition(book.getNumberOfReservation()+1);
        reservationService.saveAReservation(reservation);
        book.setNumberOfReservation(book.getNumberOfReservation()+1);
        book.setNearestReturnDate(endDate);
        bookService.saveABook(book);


    }

    /*private List<Book>  methode (List<BookSlimWithLibraryAndStockDto> booksDto){
        List<Book> books = bookSlimWithLibraryAndStockDtoMapper.booksStocksLibrariesDtoToAllBookSlimWithLibraryAndStock(booksDto);
        for (Book book : books) {
            log.info("HTTP GET request received at /books with listOfBooksSlims where cookie isNotNull in for each");
            log.info("in BookSlimController in setNumberOfBorrowsAndNearestDateOfReturn method with bookSlimBean {}", book.getTitle());
            List<Borrow> listOfBorrowsForTheBook = borrowService.findBorrowsByBookId(book.getId());
            List<Reservation> listOfReservationsForTheBook = reservationService.findReservationsByBookId(book.getId());
            List<Date> returnDates = new ArrayList<>();
            for (Borrow borrow : listOfBorrowsForTheBook) {
                returnDates.add(borrow.getReturnDate());
            }
            Date latestDate = Collections.max(returnDates);
            log.info("in BookService in setNumberOfBorrowsAndNearestDateOfReturn method with latestDate {}", latestDate);
            book.setNearestDateOfReturn(latestDate);
            log.info("in BookService in setNumberOfBorrowsAndNearestDateOfReturn method with listOfReservationsForTheBook {}", listOfBorrowsForTheBook.size());
            book.setNumberOfBorrows(listOfReservationsForTheBook.size());
    }*/


}
