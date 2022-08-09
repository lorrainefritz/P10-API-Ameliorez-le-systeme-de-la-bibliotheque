package com.OC.p7v2api.controller;

import com.OC.p7v2api.controllers.UserController;
import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.Role;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.mappers.BorrowDtoMapper;
import com.OC.p7v2api.mappers.ReservationDtoMapper;
import com.OC.p7v2api.mappers.UserDtoMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
