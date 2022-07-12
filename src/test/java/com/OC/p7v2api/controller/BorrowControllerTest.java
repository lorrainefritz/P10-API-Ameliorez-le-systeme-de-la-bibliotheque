package com.OC.p7v2api.controller;

import com.OC.p7v2api.controllers.BorrowController;
import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.mappers.BorrowDtoMapper;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.BorrowService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {BorrowController.class})
@ExtendWith(SpringExtension.class)
public class BorrowControllerTest {
    @MockBean
    private BookService bookService;

    @Autowired
    private BorrowController borrowController;

    @MockBean
    private BorrowDtoMapper borrowDtoMapper;

    @MockBean
    private BorrowService borrowService;

    @MockBean
    private TokenUtil tokenUtil;

    @MockBean
    private UserService userService;


    @Test
    void checkBorrowList_ShouldReturnAListOfBorrows() throws Exception {

        //GIVEN
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Borrow borrow1 = new Borrow(1, startDate, returnDate, true, false, user1, book1);
        //WHEN
        when(this.userService.findAUserByUsername((String) any())).thenReturn(user1);
        when(this.tokenUtil.checkTokenAndRetrieveUsernameFromIt((String) any())).thenReturn("token");
        when(this.borrowDtoMapper.borrowToAllBorrowDto((java.util.List<Borrow>) any())).thenReturn(new ArrayList<>());
        //THEN
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/users/account/borrows");
        MockHttpServletRequestBuilder requestBuilder = getResult.cookie(new Cookie("jwtToken", "ABC123"));
        MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    void checkExtendABorrow() throws Exception {
        //GIVEN
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Borrow borrow1 = new Borrow(1, startDate, returnDate, true, false, user1, book1);
        //WHEN
        when(this.borrowService.findABorrowById((Integer) any())).thenReturn(borrow1);
        //THEN
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/users/account/borrows/extend");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("borrowId", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }


    @Test
    void checkIfBorrowsAreExpired_ShouldReturnstatus200IsOK() throws Exception {
        when(this.borrowService.findAllBorrows()).thenReturn(new ArrayList<>());
        when(this.borrowDtoMapper.borrowToAllBorrowDto((java.util.List<Borrow>) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allBorrows");
        MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void checkIfBorrowsAreExpired_WhithContext_ShouldReturnAstatus200IsOk() throws Exception {
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Borrow borrow1 = new Borrow(1, startDate, returnDate, true, false, user1, book1);

        Library library2 = new Library(2, "KatzenheimSud@gmail.com", "8 rue des camélias 57000 Katzenheim", "katzenheimSud@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock2 = new Stock(2, 2, 1, 3, true, book1);
        Book book2 = new Book(2, "Blackwater, tome 2 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF02", "cover", library2, stock2, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Role role2 = new Role(2, "ROLE_ADMIN", new ArrayList<>());
        User user2 = new User(2, "pierre@gmail.com", "Pierre", "Doe", "5 rue des fleurs", "0688997744", "123", role2, new ArrayList<>(), new ArrayList<>());
        Borrow borrow2 = new Borrow(2, startDate, returnDate, true, false, user2, book2);


        ArrayList<Borrow> borrowList = new ArrayList<>();
        borrowList.add(borrow1);
        borrowList.add(borrow2);
        when(this.borrowService.findAllBorrows()).thenReturn(borrowList);
        when(this.borrowDtoMapper.borrowToAllBorrowDto((java.util.List<Borrow>) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allBorrows");
        MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }



    @Test
    void checkGetABorrowById_shouldReturnOkStatus_WhenGivenAValidBorrow() throws Exception {
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Borrow borrow1 = new Borrow(1, startDate, returnDate, true, false, user1, book1);


        when(this.borrowService.findABorrowById((Integer) any())).thenReturn(borrow1);
        when(this.borrowDtoMapper.borrowToBorrowDto((Borrow) any())).thenReturn(new BorrowDto());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/borrowById");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("borrowId", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"startDate\":null,\"returnDate\":null,\"username\":null,\"lastName\":null,\"firstName\":null,"
                                        + "\"alreadyExtended\":false,\"outdated\":false,\"bookTitle\":null,\"bookAuthor\":null,\"libraryName\":null,"
                                        + "\"openingTime\":null}"));
    }

    @Test
    void checkSaveABorrow_WhenGivenABorrowShouldSaveItWithCreatedStatus() throws Exception {
        //GIVEN
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);


        Library library2 = new Library(2, "KatzenheimSud@gmail.com", "8 rue des camélias 57000 Katzenheim", "katzenheimSud@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock2 = new Stock(2, 2, 1, 3, true, book1);
        Book book2 = new Book(2, "Blackwater, tome 2 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF02", "cover", library2, stock2, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Library library3 = new Library(3, "KatzenheimSud@gmail.com", "9 rue des campanules 57000 Katzenheim", "katzenheimEst@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock3 = new Stock(3, 2, 0, 2, true, book2);
        Book book3 = new Book(3, "Blackwater, tome 3 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF03", "cover", library3, stock3, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Library library4 = new Library(4, "KatzenheimOuest@gmail.com", "10 rue des cactus 57000 Katzenheim", "katzenheimOuest@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock4 = new Stock(4, 2, 0, 2, true, book3);
        Book book4 = new Book(4, "Blackwater, tome 4 : La maison", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF04", "cover", library4, stock4, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);


        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Borrow borrow1 = new Borrow(1, startDate, returnDate, true, false, user1, book1);


        Role role2 = new Role(2, "ROLE_ADMIN", new ArrayList<>());
        User user2 = new User(2, "pierre@gmail.com", "Pierre", "Doe", "5 rue des fleurs", "0688997744", "123", role2, new ArrayList<>(), new ArrayList<>());
        Borrow borrow2 = new Borrow(2, startDate, returnDate, true, false, user2, book2);

        Role role3 = new Role(3, "ROLE_PERSONNEL", new ArrayList<>());
        User user3 = new User(3, "pamela@gmail.com", "Pamela", "Andersen", "6 rue des fleurs", "0688997744", "123", role3, new ArrayList<>(), new ArrayList<>());
        Borrow borrow3 = new Borrow(3, startDate, returnDate, true, false, user3, book3);

        Role role4 = new Role(4, "ROLE_PERSONNEL_TRAINEE", new ArrayList<>());
        User user4 = new User(4, "john@gmail.com", "John", "Arbogast", "8 rue des fleurs", "0688997744", "123", role4, new ArrayList<>(), new ArrayList<>());
        Borrow borrow4 = new Borrow(4, startDate, returnDate, true, false, user4, book4);

        BorrowDto borrowDtoUnderTest = new BorrowDto(3, startDate, returnDate, "pamela@gmail.com", "Pamela", "Andersen", true, false, "Blackwater, tome 3 : La Digue", "Michael McDowell", "Katzenheim Est", "10h-18h");
        String borrowDtoUnderTestAsAString = (new ObjectMapper()).writeValueAsString(borrowDtoUnderTest);


        //WHEN
        when(this.borrowService.saveABorrow((Borrow) any())).thenReturn(borrow1);
        when(this.borrowDtoMapper.borrowDtoToBorrow((BorrowDto) any())).thenReturn(borrow2);

        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/borrows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(borrowDtoUnderTestAsAString);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":3,\"startDate\":1656626400000,\"returnDate\":1656712800000,\"username\":\"pamela@gmail.com\",\"lastName\":\"Pamela\",\"firstName\":\"Andersen\","
                                        + "\"alreadyExtended\":true,\"outdated\":false,\"bookTitle\":\"Blackwater, tome 3 : La Digue\","
                                        + "\"bookAuthor\":\"Michael McDowell\",\"libraryName\":\"Katzenheim Est\",\"openingTime\":\"10h-18h\"}"));
    }



    @Test
    void checkDeleteABorrow_ShouldDeleteAValidBorrow_AndSendAIsAcceptedStatus() throws Exception {
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Borrow borrow1 = new Borrow(1, startDate, returnDate, true, false, user1, book1);

        when(this.borrowService.findABorrowById((Integer) any())).thenReturn(borrow1);
        doNothing().when(this.borrowService).deleteABorrow((Borrow) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/borrows/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.borrowController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }

}
