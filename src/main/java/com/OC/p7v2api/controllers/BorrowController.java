package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.RoleDto;
import com.OC.p7v2api.entities.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping(value = "/borrows")
    public ResponseEntity<List<BorrowDto>> findAllBorrows(){
        log.info("HTTP GET request received at /borrows with findAllBorrows");
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(borrowService.findAllBorrows()), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "users/account/borrows")
    public ResponseEntity<List<BorrowDto>> borrowList(@CookieValue(value = "jwtToken") String token) {
        log.info("HTTP GET request received at users/account/borrows with borrowList");
        String username = tokenUtil.checkTokenAndRetrieveUsernameFromIt(token);
        log.info("HTTP GET request received at users/account/borrows with borrowList where username is {}", username);
        User user = userService.findAUserByUsername(username);
        log.info("HTTP GET request received at users/account/borrows with borrowList where {} is the user", user.getLastName());
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(user.getBorrows()), HttpStatus.OK);
    }

    @PostMapping(value = "/users/account/borrows/extend")
    public ResponseEntity extendABorrow(@RequestParam Integer borrowId) throws Exception {
        log.info("HTTP POST request received at users/account/borrows with borrowList where id is ", borrowId);
        if (borrowId == null) {
            log.info("HTTP POST request received at users/account/borrows with borrowList where id is null");
            return new ResponseEntity<>(borrowId, HttpStatus.NO_CONTENT);
        }
        else if (borrowService.findABorrowById(borrowId).isOutdated()){
            log.info("HTTP POST request received at users/account/borrows with borrowList where borrowIsOutdated");
            return new ResponseEntity<>(borrowId, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(borrowDtoMapper.borrowToBorrowDto(borrowService.extendABorrow(borrowId)), HttpStatus.ACCEPTED);
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

    @GetMapping(value = "/borrowById")
    public ResponseEntity getABorrowById(@RequestParam Integer borrowId) throws Exception {
        log.info("HTTP GET request received at /borrowById");
        if (borrowId == null) {
            log.info("HTTP DELETE request received at /borrowById where id is null");
            return new ResponseEntity<>(borrowId, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(borrowDtoMapper.borrowToBorrowDto(borrowService.findABorrowById(borrowId)), HttpStatus.OK);
    }

    @PostMapping(value = "/borrows")
    public ResponseEntity<BorrowDto> saveABorrow(@RequestBody @Validated BorrowDto borrowDto, BindingResult bindingResult) throws Exception {
        log.info("HTTP POST request received at / with saveABorrow");
        if (borrowDto == null) {
            log.info("HTTP POST request received at /borrows with saveABorrow where borrowDto is null");
            return new ResponseEntity<>(borrowDto, HttpStatus.NO_CONTENT);
        }
        else if (bindingResult.hasErrors()){
            log.info("HTTP POST request received at /borrows with saveABorrow where borrowDto is not valid");
            return new ResponseEntity<>(borrowDto, HttpStatus.FORBIDDEN);
        }
        else {

            borrowService.saveABorrow(borrowDtoMapper.borrowDtoToBorrow(borrowDto));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowDto);
    }

    @DeleteMapping(value = "/borrows/delete/{id}")
    public ResponseEntity deleteABorrow(@PathVariable Integer id) throws Exception {
        log.info("HTTP DELETE request received at /borrows/delete/" + id + " with deleteABorow");
        if (id == null) {
            log.info("HTTP DELETE request received at /borrows/delete/id where id is null");
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        }
        borrowService.deleteABorrow(borrowService.findABorrowById(id));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}