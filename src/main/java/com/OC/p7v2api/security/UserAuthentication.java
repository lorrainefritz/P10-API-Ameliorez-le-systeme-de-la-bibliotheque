package com.OC.p7v2api.security;

import com.OC.p7v2api.token.TokenUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthentication {
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;


    public Authentication attemptAuthentication(String username, String password) throws AuthenticationException {
        log.info("in TestAuthentication in attemptAuthentication with username {} and password is {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    public String successfulAuthentication(Authentication authentication) throws IOException, ServletException {
        log.info("in CustomAuthenticationFilter in successfulAuthentication");
        User user = (User) authentication.getPrincipal();
        String access_token = tokenUtil.createToken(user);
        log.info("in CustomAuthentication in successfulAuthentication where created token is {} after creatingToken and creatingCookie and initialize it" , access_token);
        return access_token;
    }

}
