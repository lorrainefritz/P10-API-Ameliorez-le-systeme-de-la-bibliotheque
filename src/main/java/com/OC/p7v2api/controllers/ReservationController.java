package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
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
import java.time.Instant;
import java.util.Date;
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

    @GetMapping(value = "/allReservations")
    public ResponseEntity<List<ReservationDto>> checkIfReservationsAreExpired() {
        log.info("HTTP GET request received at /allReservations");
        List<Reservation> reservations = reservationService.findAllReservations();
        Date today = Date.from(Instant.now());
        for (Reservation reservation : reservations) {
            log.info("HTTP GET request received at /allReservations list");
            Date endDate = reservation.getEndDate();
            // SI les dates sont == retourne 0 si today>returnDate =-1
            // today<returnDate = 1
            if (today.compareTo(endDate) > 0) {
                log.info("HTTP GET request received at /allReservations list when reservation isOutdated");
                reservationService.deleteAReservationById(reservation.getId());
            }
        }
        return new ResponseEntity<>(reservationDtoMapper.reservationsToAllReservationDto(reservations), HttpStatus.OK);
    }


}
