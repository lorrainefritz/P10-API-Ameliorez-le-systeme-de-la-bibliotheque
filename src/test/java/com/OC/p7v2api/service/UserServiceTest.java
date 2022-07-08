package com.OC.p7v2api.service;

import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.repositories.UserRepository;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.ReservationService;
import com.OC.p7v2api.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private AutoCloseable autoCloseable;
    private UserService userServiceUndertest;
    private Role role;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ReservationService reservationService;
    @Mock
    private BorrowService borrowService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
       userServiceUndertest = new UserService(userRepository,passwordEncoder,reservationService,borrowService);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
        role = new Role();
    }

    @Test
    public void checkFindAllUsers_shouldCallUserRepository() {
        //GIVEN WHEN
        userServiceUndertest.findAllUsers();
        //THEN
        verify(userRepository).findAll();
    }

    @Test
    public void checkFindAUserById_WhenIdIsValid_shouldCallUserRepository() throws Exception {
        //GIVEN WHEN
        userServiceUndertest.getAUserById(1);
        //THEN
        verify(userRepository).getById(1);
    }

    @Test
    public void checkFindAUserById_WhenIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            userServiceUndertest.getAUserById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void checkSaveAUser_WhenUserIsValid_shouldCallUserRepository() throws Exception {
        //GIVEN WHEN
        User userUnderTest = new User(1,"paul@gmail.com","Paul","Atreid","3 rue des fleurs Katzenheim","0666445599","123",role,null,null);
        userServiceUndertest.saveAUser(userUnderTest);
        //THEN
        verify(userRepository).save(userUnderTest);
    }

    @Test
    public void checkUserABorrow_WhenUserIsNull_shouldThrowAnException() throws Exception {
        User userUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            userServiceUndertest.saveAUser(userUnderTest);
        }).withMessage("User can't be null");
    }

    @Test
    public void checkDeleteAUser_shouldCallUserRepository() throws Exception {
        //GIVEN WHEN
        List<Borrow> borrows = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        role = new Role(1,"ROLE_USER",null);
        User userUnderTest = new User(1,"paul@gmail.com","Paul","Atreid","3 rue des fleurs Katzenheim","0666445599","123",role,borrows,reservations);
        List<User> users = new ArrayList<>();
        users.add(userUnderTest);
        role.setUsers(users);
        userServiceUndertest.deleteAUser(userUnderTest);
        //THEN
        verify(userRepository).delete(userUnderTest);
    }

    @Test
    public void checkDeleteAUser_WhenUserIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
       User userUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            userServiceUndertest.deleteAUser(userUnderTest);
        }).withMessage("User can't be null");
    }

    @Test
    public void checkLoadUserByUserName_whenGivenAValidUsername_shouldReturnAUser() throws Exception {
        //GIVEN
        List<Borrow> borrows = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        role = new Role(1,"ROLE_USER",null);
        User userUnderTest = new User(1,"paul@gmail.com","Paul","Atreid","3 rue des fleurs Katzenheim","0666445599","123",role,borrows,reservations);
        String username ="paul@gmail.com";
        //WHEN
        when(userRepository.findByUsername(username)).thenReturn(userUnderTest);

        //THEN
        userServiceUndertest.findAUserByUsername(username);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void checkLoadUserByUserName_WhenUserIsNull_ShouldUsernameNotFoundException() throws Exception {
        //GIVEN WHEN
        assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
            userServiceUndertest.loadUserByUsername("");
        }).withMessage("Invalid email or Password");
    }

}
