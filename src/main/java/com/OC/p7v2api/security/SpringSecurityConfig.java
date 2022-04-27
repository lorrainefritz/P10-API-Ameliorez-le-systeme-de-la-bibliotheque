package com.OC.p7v2api.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
       //A CHANGER EN PHASE II !!
        httpSecurity.authorizeRequests().anyRequest().permitAll();
        /*httpSecurity.authorizeRequests().antMatchers("/login").permitAll();*/
        /*httpSecurity.authorizeRequests().antMatchers( "/books", "/books/search", "/libraries").permitAll();*/
        /*httpSecurity.authorizeRequests().antMatchers("/users/account").authenticated();*/
        httpSecurity.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
    }





  /*  @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("login");
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests().antMatchers("/login").permitAll();
        httpSecurity.authorizeRequests().antMatchers( "/books", "/books/search", "/libraries").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/myAccount").authenticated();
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilter(customAuthenticationFilter);
    }*/


}
