package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.mappers.UserDtoMapper;
import com.OC.p7v2api.token.TokenUtil;
import com.OC.p7v2api.security.UserAuthentication;
import com.OC.p7v2api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

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

/*    @PostMapping(value = "/login")
    public ResponseEntity authenticate(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) throws ServletException, IOException {
        log.info("in UserController in AUTHENTICATE with username {}", username);
        Authentication authentication = userAuthentication.attemptAuthentication(username,password);
        String token = userAuthentication.successfulAuthentication(authentication);

        return new ResponseEntity(token,HttpStatus.OK);

    }*/


    @PostMapping(value = "/login")
    public ResponseEntity authenticate(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) throws ServletException, IOException {
        log.info("in UserController in AUTHENTICATE with username {}", username);
        String token = userAuthentication.successfulAuthentication(username,password);
        return new ResponseEntity(token,HttpStatus.OK);

    }



    @GetMapping(value="/users/account")
    public ResponseEntity<UserDto> getUserFromToken(@CookieValue(value ="jwtToken")String token){
        log.info("in UserController in getUserFromToken method where token is {}" ,token);
        String username = tokenUtil.checkTokenAndRetrieveUsernameFromIt(token);
        log.info("in UserController in getUserFromToken method where username is {}" ,username);
         User user = userService.findAUserByUsername(username);
        log.info("in UserController in getUserFromToken after retrieve user {} with role {}" ,user.getUsername(),user.getRole());
        return new ResponseEntity<>(userDtoMapper.userToUserDtoMapper(user), HttpStatus.OK);
    }







}
