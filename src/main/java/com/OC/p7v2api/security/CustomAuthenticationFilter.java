package com.OC.p7v2api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("in CustomAuthenticationFilter in attemptAuthentication with username {} and password is {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }


/*
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("in CustomAuthenticationFilter in successfulAuthentication");
        User user = (User) authentication.getPrincipal();
        String access_token = createToken(request, user);
        Cookie cookie = createCookie(access_token);
        log.info("in CustomAuthentication in successfulAuthentication where created token is {} after creatingToken and creatingCookie and initialize it" , access_token);
       response.addCookie(cookie);
        log.info("Cookies {}",response.getHeaderNames().toString());
    }
*/






   /* @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("in CustomAuthenticationFilter in successfulAuthentication");
        User user = (User) authentication.getPrincipal();
        String access_token = createToken(request, user);
        *//* Cookie cookie = createCookie(access_token);*//*
        ResponseCookie springCookie = createCookie(access_token);
        log.info("in CustomAuthentication in successfulAuthentication where created token is {} after creatingToken and creatingCookie and initialize it" , access_token);
        response.setHeader(HttpHeaders.SET_COOKIE, springCookie.toString());
        *//*response.addHeader(HttpHeaders.SET_COOKIE, springCookie.toString());*//*
        *//*response.addCookie(cookie);*//*
        log.info("Cookies {}",response.getHeaderNames().toString());
      *//*  super.successfulAuthentication(request,response,chain,authentication);
        chain.doFilter(request,response);*//*
        *//*String body = (access_token);
        response.getWriter().write(body);
        response.getWriter().flush();*//*
    }*/

    private String createToken(HttpServletRequest request, User user) {
        log.info("in CustomAuthenticationFilter in createToken");
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // expires in 10 minutes
                .withIssuer(request.getRequestURL().toString()).withClaim("role", user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining())).sign(algorithm);
        log.info("in CustomAuthentication in createToken with token " + access_token);
        return access_token;
    }
    /*private Cookie createCookie(String access_token) {
        log.info("in CustomAuthenticationFilter in createCookie");
        Cookie cookie = new Cookie("jwtToken",access_token);
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        log.info("in CustomAuthenticationFilter in createCookie after initializing it cookie name {} cookie domain {} cookie path {}", cookie.getName(), cookie.getDomain(),cookie.getPath() );
        return cookie;
    }*/


    private ResponseCookie createCookie(String access_token) {
        log.info("in CustomAuthenticationFilter in createCookie");
        ResponseCookie springCookie = ResponseCookie.from("jwtToken", access_token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge (7* 24 * 60 * 60) // expires in 7 days
                .build();
        log.info("in CustomAuthenticationFilter in createCookie after initializing it cookie name {} cookie domain {} cookie path {}" ,springCookie.getName(),springCookie.getDomain(),springCookie.getPath());
        return springCookie;
    }


}
