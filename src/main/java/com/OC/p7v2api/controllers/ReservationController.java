package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Reservation;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.mappers.ReservationDtoMapper;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationDtoMapper reservationDtoMapper;
    private final BookSlimWithLibraryAndStockDtoMapper bookSlimWithLibraryAndStockDtoMapper;
    private final BookService bookService;

    @Transactional
    @GetMapping("books/reservations")
    public ResponseEntity<List<ReservationDto>>findReservationsByBookId(@RequestParam Integer bookId){
        log.info("HTTP GET request received at books/reservations with reservationList where bookId is {}",bookId);
        Book book = bookService.getABookById(bookId);
         List<Reservation> reservationListForABook =  reservationService.findReservationsByBook(book);
        return new ResponseEntity<>(reservationDtoMapper.reservationsToAllReservationDto(reservationListForABook), HttpStatus.OK);
    }




}
