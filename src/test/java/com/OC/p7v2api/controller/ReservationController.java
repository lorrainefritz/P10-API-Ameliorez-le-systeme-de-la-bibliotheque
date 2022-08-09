package com.OC.p7v2api.controller;

import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.mappers.ReservationDtoMapper;
import com.OC.p7v2api.services.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {com.OC.p7v2api.controllers.ReservationController.class})
@ExtendWith(SpringExtension.class)
public class ReservationController {
    @Autowired
    private com.OC.p7v2api.controllers.ReservationController reservationController;

    @MockBean
    private ReservationDtoMapper reservationDtoMapper;

    @MockBean
    private ReservationService reservationService;

    @Test
    void checkIfReservationsAreExpired_ShouldReturnAIsOkStatus() throws Exception {
        //GIVEN WHEN
        when(this.reservationService.findAllReservations()).thenReturn(new ArrayList<>());
        when(this.reservationDtoMapper.reservationsToAllReservationDto((java.util.List<Reservation>) any()))
                .thenReturn(new ArrayList<>());
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allReservations");
        MockMvcBuilders.standaloneSetup(this.reservationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testMakeAReservation_shouldReturnAIsCreatedStatus() throws Exception {
        //GIVEN
        Date startDate = new GregorianCalendar(2022, 06, 1).getTime();
        Date endDate = new GregorianCalendar(2022, 06, 2).getTime();
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();


        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);


        Library library2 = new Library(2, "KatzenheimSud@gmail.com", "8 rue des camélias 57000 Katzenheim", "katzenheimSud@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock2 = new Stock(2, 2, 1, 3, true, book1);
        Book book2 = new Book(2, "Blackwater, tome 2 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF02", "cover", library2, stock2, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);


        Role role1 = new Role(1, "ROLE_USER", new ArrayList<>());
        User user1 = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs", "0688997744", "123", role1, new ArrayList<>(), new ArrayList<>());
        Reservation reservation1 = new Reservation(1, startDate, endDate, 1, user1, book1);

        //WHEN
        when(this.reservationService.makeAReservation((Integer) any(), (Integer) any())).thenReturn(reservation1);
        //THEN
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/books/reservation");
        MockHttpServletRequestBuilder paramResult = postResult.param("bookId", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("userId", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.reservationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    void testDeleteAReservation_ShouldReturnAIsAcceptedStatus() throws Exception {
        //GIVEN WHEN
        doNothing().when(this.reservationService).deleteAReservationById((Integer) any());
        //THEN
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/users/account/reservations/delete");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("reservationId", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.reservationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }




}
