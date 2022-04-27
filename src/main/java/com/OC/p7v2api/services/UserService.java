package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers(){
        log.info("in UserService in findAllUsers method ");
        return userRepository.findAll();
    }
    public User getAUserById(Integer id){
        log.info("in UserService in findById method ");
        return userRepository.getById(id);
    }

    public User findAUserByUsername(String username) throws UsernameNotFoundException {
        log.info("in UserService in findAUserByUsername method ");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid email or Password");
        }
        return user;
    }

    public void deleteAUser(Integer id) {
        log.info("in UserService in deleteAUser method");
        User user = userRepository.getById(id);
        user.setRole(null);
       /* List <Borrow> borrows = (List<Borrow>)libraryUser.getBorrows();
        for (Borrow borrow : borrows) {
            borrowService.deleteBorrow(borrow);
        }*/
        saveAUser(user);
        userRepository.delete(user);;
    }

    public User saveAUser(User user) {
        log.info("in UserService in saveAUser method");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("in UserService in loadUserByUsername method ");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("in UserService in loadUserByUsername method User not found in DB ");
            throw new UsernameNotFoundException("Invalid email or Password");
        }else{
            log.info("in UserService in loadUserByUsername method, user {} found ",username);
        }
        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authority);
    }
}
