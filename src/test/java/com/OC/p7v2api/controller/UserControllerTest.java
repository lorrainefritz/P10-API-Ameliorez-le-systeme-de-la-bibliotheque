package com.OC.p7v2api.controller;

import com.OC.p7v2api.controllers.UserController;
import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.mappers.*;
import com.OC.p7v2api.repositories.BookRepository;
import com.OC.p7v2api.repositories.BorrowRepository;
import com.OC.p7v2api.repositories.ReservationRepository;
import com.OC.p7v2api.repositories.UserRepository;
import com.OC.p7v2api.security.UserAuthentication;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.ReservationService;
import com.OC.p7v2api.services.UserService;
import com.OC.p7v2api.token.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private BorrowDtoMapper borrowDtoMapper;

    @MockBean
    private BorrowService borrowService;

    @MockBean
    private ReservationDtoMapper reservationDtoMapper;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private TokenUtil tokenUtil;

    @MockBean
    private UserAuthentication userAuthentication;

    @Autowired
    private UserController userController;

    @MockBean
    private UserDtoMapper userDtoMapper;

    @MockBean
    private UserService userService;



    /*@Test
    void checkFindBorrowsByUserId_ShouldReturnOkStatus() throws Exception {
        ArrayList<Borrow> borrowList = new ArrayList<>();
        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, borrowList, new ArrayList<>());
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getById((Integer) any())).thenReturn(user1);
        Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        BookService bookService = new BookService(mock(BookRepository.class));
        UserRepository userRepository1 = mock(UserRepository.class);
        Argon2PasswordEncoder passwordEncoder1 = new Argon2PasswordEncoder();
        ReservationService reservationService = new ReservationService(mock(ReservationRepository.class), null, null);

        ReservationService reservationService1 = new ReservationService(reservationRepository, bookService,
                new UserService(userRepository1, passwordEncoder1, reservationService,
                        new BorrowService(mock(BorrowRepository.class), null, null)));

        BorrowRepository borrowRepository = mock(BorrowRepository.class);
        BookService bookService1 = new BookService(mock(BookRepository.class));
        UserRepository userRepository2 = mock(UserRepository.class);
        Argon2PasswordEncoder passwordEncoder2 = new Argon2PasswordEncoder();
        ReservationService reservationService2 = new ReservationService(mock(ReservationRepository.class), null, null);

        UserService userService = new UserService(userRepository, passwordEncoder, reservationService1,
                new BorrowService(borrowRepository, bookService1, new UserService(userRepository2, passwordEncoder2,
                        reservationService2, new BorrowService(mock(BorrowRepository.class), null, null))));

        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        UserAuthentication userAuthentication = new UserAuthentication(authenticationManager, new TokenUtil());

        TokenUtil tokenUtil = new TokenUtil();
        UserDtoMapperImpl userDtoMapper = new UserDtoMapperImpl();
        BorrowDtoMapperImpl borrowDtoMapper = new BorrowDtoMapperImpl();
        ReservationDtoMapperImpl reservationDtoMapper = new ReservationDtoMapperImpl();
        ReservationRepository reservationRepository1 = mock(ReservationRepository.class);
        BookService bookService2 = new BookService(mock(BookRepository.class));
        UserRepository userRepository3 = mock(UserRepository.class);
        Argon2PasswordEncoder passwordEncoder3 = new Argon2PasswordEncoder();
        ReservationRepository reservationRepository2 = mock(ReservationRepository.class);
        BookService bookService3 = new BookService(mock(BookRepository.class));
        UserRepository userRepository4 = mock(UserRepository.class);
        ReservationService reservationService3 = new ReservationService(reservationRepository2, bookService3,
                new UserService(userRepository4, new Argon2PasswordEncoder(), null, null));

        BorrowRepository borrowRepository1 = mock(BorrowRepository.class);
        BookService bookService4 = new BookService(mock(BookRepository.class));
        UserRepository userRepository5 = mock(UserRepository.class);
        ReservationService reservationService4 = new ReservationService(reservationRepository1, bookService2,
                new UserService(userRepository3, passwordEncoder3, reservationService3, new BorrowService(borrowRepository1,
                        bookService4, new UserService(userRepository5, new Argon2PasswordEncoder(), null, null))));

        BookService bookService5 = new BookService(mock(BookRepository.class));
        BorrowRepository borrowRepository2 = mock(BorrowRepository.class);
        BookService bookService6 = new BookService(mock(BookRepository.class));
        UserRepository userRepository6 = mock(UserRepository.class);
        Argon2PasswordEncoder passwordEncoder4 = new Argon2PasswordEncoder();
        ReservationRepository reservationRepository3 = mock(ReservationRepository.class);
        BookService bookService7 = new BookService(mock(BookRepository.class));
        UserRepository userRepository7 = mock(UserRepository.class);
        ReservationService reservationService5 = new ReservationService(reservationRepository3, bookService7,
                new UserService(userRepository7, new Argon2PasswordEncoder(), null, null));

        BorrowRepository borrowRepository3 = mock(BorrowRepository.class);
        BookService bookService8 = new BookService(mock(BookRepository.class));
        UserRepository userRepository8 = mock(UserRepository.class);
        ResponseEntity<List<BorrowDto>> actualFindBorrowsByUserIdResult = (new UserController(tokenUtil, userService,
                userDtoMapper, userAuthentication, borrowDtoMapper, reservationDtoMapper, reservationService4, bookService5,
                new BorrowService(borrowRepository2, bookService6,
                        new UserService(userRepository6, passwordEncoder4, reservationService5,
                                new BorrowService(borrowRepository3, bookService8,
                                        new UserService(userRepository8, new Argon2PasswordEncoder(), null, null))))))
                .findBorrowsByUserId(123);
        assertEquals(borrowList, actualFindBorrowsByUserIdResult.getBody());
        assertEquals(HttpStatus.OK, actualFindBorrowsByUserIdResult.getStatusCode());
        assertTrue(actualFindBorrowsByUserIdResult.getHeaders().isEmpty());
        verify(userRepository).getById((Integer) any());
    }*/



    @Test
    void checkSaveAUser_shouldReturnAIsCreatedStatus() throws Exception {


        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());

        Role role2 = new Role(2, "ROLE_ADMIN", new ArrayList<>());
        User user2 = new User(2, "pamela@gmail.com", "Pamela", "Andersen", "10 rue des cactus", "0688997744", "123", role2, new ArrayList<>(), new ArrayList<>());


        UserDto userDto = new UserDto(1,"pamela@gmail.com", "Pamela", "Andersen", "10 rue des cactus", "0688997744");
        String userDtoContentAsAString = (new ObjectMapper()).writeValueAsString(userDto);
    //WHEN
        when(this.userDtoMapper.userDtoToUser((UserDto) any())).thenReturn(user2);
        when(this.userService.saveAUser((User) any())).thenReturn(user1);
    //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoContentAsAString);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"username\":\"pamela@gmail.com\",\"firstName\":\"Pamela\",\"lastName\":\"Andersen\",\"address\":\"10 rue des cactus\",\"phone\":"
                                        + "\"0688997744\"}"));
    }

    @Test
    void testAuthenticate() throws Exception {
        when(this.userAuthentication.successfulAuthentication((String) any(), (String) any()))
                .thenReturn("Successful Authentication");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                .param("password", "123")
                .param("username", "paul@gmail.com");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Successful Authentication"));
    }


    /*@Test
    void testDeleteAUser() throws Exception {
        //GIVEN
        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        //WHEN
        when(this.userService.getAUserById((Integer) any())).thenReturn(user1);
        //THEN
        doNothing().when(this.userService).deleteAUser((User) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }*/


    @Test
    void checkGenerateAreservationListToRetrieveFromLibrary() throws Exception {
        //GIVEN WHEN
        when(this.reservationDtoMapper
                .reservationsToAllReservationDto((java.util.List<com.OC.p7v2api.entities.Reservation>) any()))
                .thenReturn(new ArrayList<>());
        when(this.bookService.getAllBooksSortedAscendingById()).thenReturn(new ArrayList<>());
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/allReservationsToRetrieveFromLibrary");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void checkGetUserFromToken() throws Exception {
        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());

        when(this.userService.findAUserByUsername((String) any())).thenReturn(user1);
        when(this.userDtoMapper.userToUserDtoMapper((User) any())).thenReturn(new UserDto());
        when(this.tokenUtil.checkTokenAndRetrieveUsernameFromIt((String) any())).thenReturn("janedoe");
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/users/account");
        MockHttpServletRequestBuilder requestBuilder = getResult.cookie(new Cookie("jwtToken", "ABC123"));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"username\":null,\"firstName\":null,\"lastName\":null,\"address\":null,\"phone\":null}"));
    }

}
