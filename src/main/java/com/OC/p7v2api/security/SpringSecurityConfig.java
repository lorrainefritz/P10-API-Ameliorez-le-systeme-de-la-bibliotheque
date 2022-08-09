package com.OC.p7v2api.security;

import com.OC.p7v2api.services.UserService;
import com.OC.p7v2api.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final TokenUtil tokenUtil;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("in SpringSecurityConfig in configure (AuthenticationManagerBuilder)");
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.info("in SpringSecurityConfig in AuthenticationManagerBean");
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info("in SpringSecurityConfig in configure (HttpSecurity)");
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests().antMatchers("/resources/**","/static/**","/css/**","/images/**","/logos/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/login").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/books", "/books/search", "/libraries","/allBorrows").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/users/account","/users/account/borrows").authenticated();
        httpSecurity.addFilterBefore(new CustomAuthenticationFilter(userService,tokenUtil), UsernamePasswordAuthenticationFilter.class);
    }


}
