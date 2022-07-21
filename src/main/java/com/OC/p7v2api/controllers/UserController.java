package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
    private final BorrowDtoMapper borrowDtoMapper;
    private final ReservationDtoMapper reservationDtoMapper;
    private final ReservationService reservationService;
    private final BookService bookService;
   /* private final BorrowService borrowService;*/


    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> findAllUsers(){
        log.info("HTTP GET request received at /users with findAllUsers");
        return new ResponseEntity<>(userDtoMapper.userToAllUserDto(userService.findAllUsers()), HttpStatus.OK);
    }

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
    public ResponseEntity<List<BorrowDto>> findBorrowsByUserId(@RequestParam Integer userId) throws Exception {
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



    @Transactional
    @GetMapping("/users/allReservationsToRetrieveFromLibrary")
    public ResponseEntity<List<ReservationDto>> generateAreservationListToRetrieveFromLibrary() throws Exception {
        log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary");
        List<Book> books = bookService.getAllBooksSortedAscendingById();
        List<Reservation> reservationsToRetrieveFromLibrary = new ArrayList<>();
        for (Book currentBook : books) {
            log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary in for each book where currentBook title is {} ",currentBook.getTitle());
            if (currentBook.getStock().getNumberOfCopiesAvailable() > 0 && currentBook.getNumberOfReservation() > 0) {
                log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary where the currentBook has > 0 numberOfCopiesAvailable AND > 0 for booktitle is {}", currentBook.getTitle());
                List<Reservation> reservationsForTheCurrentBook = currentBook.getReservations();
                if (checkIfReservationHasExpired(reservationsForTheCurrentBook.get(0)) == true) {
                    log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary where the first reservation of the list has expired and will be delete. reservation id is {}",reservationsForTheCurrentBook.get(0).getId());
                    reservationService.deleteAReservationById(reservationsForTheCurrentBook.get(0).getId());
                    reservationsForTheCurrentBook = bookService.getAscendingSortedReservations(currentBook);
                    log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary where the first reservation of the list was expired the new first reservation id is {}",reservationsForTheCurrentBook.get(0).getId());
                }
                //setting the Endate for the first reservation for this book to 48h after today
                log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary before setting the endDate of firstReservation");
                Reservation firstReservation = reservationsForTheCurrentBook.get(0);
                firstReservation.setEndDate(Date.from(Instant.now().plusSeconds(172800)));
                log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary after setting the endDate of firstReservation the endDate is {}",firstReservation.getEndDate());
                // add to the reservationList to send an email to
                reservationsToRetrieveFromLibrary.add(firstReservation);
                log.info("HTTP POST request received at /users/account/allReservationsToRetrieveFromLibrary reservationToRetrieveFromLibrary size being ", reservationsToRetrieveFromLibrary.size());
            }
        }
        return new ResponseEntity<>(reservationDtoMapper.reservationsToAllReservationDto(reservationsToRetrieveFromLibrary), HttpStatus.OK);
    }

    private boolean checkIfReservationHasExpired(Reservation reservation) {
        Date today = Date.from(Instant.now());
        log.info("in reservationBatchProcessing in checkIfReservationHasExpired");
        Date endDate = reservation.getEndDate();
        // SI les dates sont == retourne 0 si today>returnDate =-1
        // today<returnDate = 1
        if (today.compareTo(endDate) > 0) {
            log.info("in reservationBatchProcessing in checkIfReservationHasExpired where the date isOutDated");
            return true;
        }
        return false;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDto> saveAUser(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws Exception {
        log.info("HTTP POST request received at /users with saveAUser");
        if (userDto == null) {
            log.info("HTTP POST request received at /users with saveAUser where userDto is null");
            return new ResponseEntity<>(userDto, HttpStatus.NO_CONTENT);
        }
        else if (bindingResult.hasErrors()){
            log.info("HTTP POST request received at /users with saveAUser where userDto is not valid");
            return new ResponseEntity<>(userDto, HttpStatus.FORBIDDEN);
        }
        else {
            User  user = userService.saveAUser(userDtoMapper.userDtoToUser(userDto));
            userService.saveAUser(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

}



