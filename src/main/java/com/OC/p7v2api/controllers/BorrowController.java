package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.Reservation;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.mappers.BorrowDtoMapper;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.UserService;
import com.OC.p7v2api.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowController {
    private final BorrowDtoMapper borrowDtoMapper;
    private final BorrowService borrowService;
    private final UserService userService;
    private final TokenUtil tokenUtil;
    private final BookService bookService;
    private final BookSlimWithLibraryAndStockDtoMapper bookSlimWithLibraryAndStockDtoMapper;

    @Transactional
    @GetMapping(value = "users/account/borrows")
    public ResponseEntity<List<BorrowDto>> borrowList(@CookieValue(value ="jwtToken")String token) {
        log.info("HTTP GET request received at users/account/borrows with borrowList");
        String username = tokenUtil.checkTokenAndRetrieveUsernameFromIt(token);
         log.info("HTTP GET request received at users/account/borrows with borrowList where username is {}" ,username);
        User user = userService.findAUserByUsername(username);
        log.info("HTTP GET request received at users/account/borrows with borrowList where {} is the user", user.getLastName());
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(user.getBorrows()), HttpStatus.OK);
    }

    @PostMapping(value = "/users/account/borrows/extend")
    public void extendABorrow(@RequestParam Integer borrowId) {
        log.info("HTTP POST request received at users/account/borrows with borrowList where id is ", borrowId);
        Borrow borrow = borrowService.extendABorrow(borrowId);
    }

    @GetMapping(value = "/allBorrows")
    public ResponseEntity<List<BorrowDto>> checkIfBorrowsAreExpired() {
        log.info("HTTP GET request received at /allBorrows");
        List<Borrow> borrows = borrowService.findAllBorrows();
        Date today = Date.from(Instant.now());
        for (Borrow borrow : borrows) {
            log.info("HTTP GET request received at /allBorrows in borrows list");
            Date returnDate = borrow.getReturnDate();
            // SI les dates sont == retourne 0 si today>returnDate =-1
            // today<returnDate = 1
            if (today.compareTo(returnDate) > 0) {
                log.info("HTTP GET request received at /allBorrows in borrows list when borrow isOutdated");
                borrow.setOutdated(true);
            }
        }
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(borrows), HttpStatus.OK);
    }
    @Transactional
    @GetMapping("books/borrows")
    public ResponseEntity<List<BorrowDto>>findBorrowsByBookId(@RequestParam Integer bookId){
        log.info("HTTP GET request received at books/borrows with borrowList where bookId is {}",bookId);
        Book book = bookService.getABookById(bookId);
        List<Borrow> borrowsByBook =  borrowService.findBorrowsByBook(book);
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(borrowsByBook), HttpStatus.OK);
    }


}