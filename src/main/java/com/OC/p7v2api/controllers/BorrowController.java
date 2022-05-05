package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.mappers.BorrowDtoMapper;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowController {
    private final BorrowDtoMapper borrowDtoMapper;
    private final BorrowService borrowService;
    private final UserService userService;

    @Transactional
    @GetMapping(value = "users/account/borrows")
    public ResponseEntity<List<BorrowDto>> borrowList(@RequestParam Integer userId) {
        User user = userService.getAUserById(userId);
        log.info("HTTP GET request received at users/account/borrows with borrowList where {} is the user", user.getLastName());
        return new ResponseEntity<>(borrowDtoMapper.borrowToAllBorrowDto(user.getBorrows()), HttpStatus.OK);
    }
    @PostMapping(value = "/users/account/borrows/extend")
    public void extendABorrow(@RequestParam Integer borrowId){
        log.info("HTTP POST request received at users/account/borrows with borrowList where id is ",borrowId);
        Borrow borrow = borrowService.extendABorrow(borrowId);
    }
}
