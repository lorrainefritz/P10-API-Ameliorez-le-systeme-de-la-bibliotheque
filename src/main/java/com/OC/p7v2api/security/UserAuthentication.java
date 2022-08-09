package com.OC.p7v2api.security;

import com.OC.p7v2api.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthentication {
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;


    public String successfulAuthentication(String username,String password) throws IOException, ServletException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("in CustomAuthenticationFilter in successfulAuthentication");
        User user = (User) authentication.getPrincipal();
        String access_token = tokenUtil.createToken(user);
        log.info("in CustomAuthentication in successfulAuthentication where created token is {} after creatingToken and creatingCookie and initialize it" , access_token);
        return access_token;
    }

}
