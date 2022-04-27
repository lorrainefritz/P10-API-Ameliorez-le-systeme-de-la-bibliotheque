package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.mappers.UserDtoMapper;
import com.OC.p7v2api.security.CustomAuthenticationFilter;
import com.OC.p7v2api.security.CustomCheckToken;
import com.OC.p7v2api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
@Transactional
public class UserController {

    private final CustomCheckToken customCheckToken;
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

   /* @PostMapping(value = "/login")
    public ResponseEntity authenticate(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        log.info("in UserController in AUTHENTICATE");
        return new ResponseEntity(username,HttpStatus.OK);

    }*/

    @GetMapping(value="/users/account")
    public ResponseEntity<UserDto> getUserFromToken(@CookieValue(value ="jwtToken")String token){
        log.info("in UserController in getUserFromToken method where token is {}" ,token);
        String username = customCheckToken.checkTokenAndRetrieveUsernameFromIt(token);
        log.info("in UserController in getUserFromToken method where username is {}" ,username);
         User user = userService.findAUserByUsername(username);
        log.info("in UserController in getUserFromToken after retrieve user {} with role {}" ,user.getUsername(),user.getRole());
        return new ResponseEntity<>(userDtoMapper.userToUserDtoMapper(user), HttpStatus.OK);
    }







}
