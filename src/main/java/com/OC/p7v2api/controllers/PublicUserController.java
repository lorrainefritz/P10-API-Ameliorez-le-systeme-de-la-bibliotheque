package com.OC.p7v2api.controllers;

import com.OC.p7v2api.security.CustomAuthenticationFilter;
import com.OC.p7v2api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
@Transactional
public class PublicUserController {

/*    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final UserService userService;*/


    /*@PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGenerateAToken(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        log.info("HTTP POST request received at /authenticate with authenticateAndGenerateAToken");
        customAuthenticationFilter.attemptAuthentication()
        UserDetails user = userService.loadUserByUsername(username);
        log.info("HTTP POST request received at /authenticate with authenticateAndGenerateAToken after token generation token : {}");
        return ResponseEntity.ok();

    }*/





}
