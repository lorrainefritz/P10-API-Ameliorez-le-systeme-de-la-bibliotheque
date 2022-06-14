package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.Reservation;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.mappers.BorrowDtoMapper;
import com.OC.p7v2api.mappers.ReservationDtoMapper;
import com.OC.p7v2api.mappers.UserDtoMapper;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.ReservationService;
import com.OC.p7v2api.token.TokenUtil;
import com.OC.p7v2api.security.UserAuthentication;
import com.OC.p7v2api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
@Transactional
public class UserController {

    private final TokenUtil tokenUtil;
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;
    private final UserAuthentication userAuthentication;
    AuthenticationManager authenticationManager;
    private final BorrowDtoMapper borrowDtoMapper;
    private final BorrowService borrowService;
    private final ReservationDtoMapper reservationDtoMapper;
    private final ReservationService reservationService;
    private final BookService bookService;


    @PostMapping(value = "/login")
    public ResponseEntity authenticate(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) throws ServletException, IOException {
        log.info("in UserController in AUTHENTICATE with username {}", username);
        String token = userAuthentication.successfulAuthentication(username, password);
        return new ResponseEntity(token, HttpStatus.OK);

    }


    @GetMapping(value = "/users/account")
    public ResponseEntity<UserDto> getUserFromToken(@CookieValue(value = "jwtToken") String token) {
        log.info("in UserController in getUserFromToken method where token is {}", token);
        String username = tokenUtil.checkTokenAndRetrieveUsernameFromIt(token);
        log.info("in UserController in getUserFromToken method where username is {}", username);
        User user = userService.findAUserByUsername(username);
        log.info("in UserController in getUserFromToken after retrieve user {} with role {}", user.getUsername(), user.getRole());
        return new ResponseEntity<>(userDtoMapper.userToUserDtoMapper(user), HttpStatus.OK);
    }


    @Transactional
    @GetMapping("users/borrowsFromUserId")
    public ResponseEntity<List<BorrowDto>> findBorrowsByUserId(@RequestParam Integer userId) {
        log.info("HTTP GET request received users/borrowsFromUserId with borrowList where userId is {}", userId);
        User user = userService.getAUserById(userId);
        log.info("HTTP GET request received users/borrowsFromUserId with borrowList where username is {}", user.getUsername());
        List<Borrow> borrows = user.getBorrows();
        log.info("HTTP GET request received users/borrowsFromUserId with borrowList size is {}", borrows.size());
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(borrows), HttpStatus.OK);

    }

    @Transactional
    @GetMapping("/users/account/reservations")
    public ResponseEntity<List<ReservationDto>> reservationList(@CookieValue(value = "jwtToken") String token) {
        log.info("HTTP GET request received at users/account/resevations with reservationList");
        String username = tokenUtil.checkTokenAndRetrieveUsernameFromIt(token);
        log.info("HTTP GET request received at users/account/resevations with reservationList where username is {}", username);
        User user = userService.findAUserByUsername(username);
        log.info("HTTP GET request received at users/account/resevations with reservationList where {} is the user", user.getLastName());
        List<Reservation> reservations = user.getReservations();
        log.info("HTTP GET request received at users/account/resevations with reservationList where reservationList size is {} ", reservations.size());
        return new ResponseEntity<>(reservationDtoMapper.reservationsToAllReservationDto(reservations), HttpStatus.OK);
    }

    @PostMapping("/users/account/reservations/delete")
    public void deleteAReservation(@RequestParam Integer reservationId) {
        log.info("HTTP POST request received at /users/account/reservations/delete with borrowList where id is {} ", reservationId);
        Reservation reservation = reservationService.getAReservationById(reservationId);
        Reservation nextReservation = null;
        Reservation firstReservation = null;
        Book book = reservation.getBook();
        List<Reservation> reservationListForTheCurrentBook = book.getReservations();
        int positionOfTheCurrentReservationInListForTheCurrentBook = reservationListForTheCurrentBook.indexOf(reservation);
        log.info("HTTP POST request received at /users/account/reservations/delete where reservationListForTheCurrentBook size is {} and positionOfTheCurrentReservationInListForTheCurrentBook is {}", reservationListForTheCurrentBook.size(), positionOfTheCurrentReservationInListForTheCurrentBook );
        try {
            log.info("HTTP POST request received at /users/account/reservations/delete in try catch");

            firstReservation = reservationListForTheCurrentBook.get(0);
            log.info("HTTP POST request received at /users/account/reservations/delete in try catch in  firstReservation initialize  where id is {} and startDate is {}", firstReservation.getId(), firstReservation.getStartDate());
            nextReservation = reservationListForTheCurrentBook.get(positionOfTheCurrentReservationInListForTheCurrentBook+1);
            log.info("HTTP POST request received at /users/account/reservations/delete in try catch in nextReservation initialize where id is{} and startDate {}", nextReservation.getId(), nextReservation.getStartDate());
        } catch (Exception e) {
            log.info("HTTP POST request received at /users/account/reservations/delete in previous and Or next reservations not found");
        }
        reservationListForTheCurrentBook.remove(positionOfTheCurrentReservationInListForTheCurrentBook);
       reservationService.deleteAReservation(reservation);
        book.setNumberOfReservation(book.getNumberOfReservation() - 1);
        bookService.saveABook(book);

        if (firstReservation != null ) {
            log.info("HTTP POST request received at /users/account/reservations/delete in previous reservation is not null ");
            book.setNearestReturnDate(firstReservation.getEndDate());
            /*firstReservation.setReservationPosition(1);*/
            reservationService.saveAReservation(firstReservation);
            bookService.saveABook(book);
        }
        if (nextReservation != null && reservationListForTheCurrentBook.size()>1) {
            log.info("HTTP POST request received at /users/account/reservations/delete in next reservation is not null");
            for (Reservation currentReservation : reservationListForTheCurrentBook) {
                log.info("HTTP POST request received at /users/account/reservations/delete in next reservation is not null for each reservation");
             if(currentReservation.getReservationPosition()> positionOfTheCurrentReservationInListForTheCurrentBook){
               currentReservation.setReservationPosition(currentReservation.getReservationPosition()-1);
               reservationService.saveAReservation(currentReservation);
             }
            }
        }
    }


}
