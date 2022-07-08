package com.OC.p7v2api.controller;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.CoreMatchers.is;


/*@TestPropertySource( locations = "classpath:application.properties")*/

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookSlimWithLibraryAndStockDtoMapper bookDtoMapper;
    @Autowired
    BookService bookService;
    @Autowired
    StockService stockService;
    @Autowired
    LibraryService libraryService;
    @Autowired
    UserService userService;
    @Autowired
    BorrowService borrowService;
    @Autowired
    ReservationService reservationService;




    @Test
    public void shouldReturnAllBooksWithAnOkStatus() throws Exception {

        mockMvc.perform(get("/libraries")).andExpect(status().isOk());
    }


    @Test
    void ShoulReturnAllBooksIntegrationTest() throws Exception {
        // Setup our mocked service

        BookSlimWithLibraryAndStockDto bookUnderTest1 = new BookSlimWithLibraryAndStockDto(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "MONSIEUR TOUSSAINT LOUVERTURE", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", 1, "Katzenheim Nord", null, 0, 2);
        BookSlimWithLibraryAndStockDto bookUnderTest2 = new BookSlimWithLibraryAndStockDto(2, "Blackwater, tome 2 : La digue", "Michael McDowell", "MONSIEUR TOUSSAINT LOUVERTURE", "Fantastique", "les Caskey reconstruisent", 1, "Katzenheim Nord", null, 0, 2);
        BookSlimWithLibraryAndStockDto bookUnderTest3 = new BookSlimWithLibraryAndStockDto(3, "Blackwater, tome 3 : La maison", "Michael McDowell", "MONSIEUR TOUSSAINT LOUVERTURE", "Fantastique", "les Caskey se déchirent", 1, "Katzenheim Nord", null, 0, 2);
        List<BookSlimWithLibraryAndStockDto> booksMock = new ArrayList<>(Arrays.asList(bookUnderTest1, bookUnderTest2, bookUnderTest3));

        doReturn(booksMock).when(bookDtoMapper.booksToAllBooksDto(bookService.getAllBooksSortedAscendingById()));

        // Execute the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)));
              /*  .andExpect(jsonPath("$[0].title", is("Blackwater, tome 1 : La Crue", "Michael McDowell")))
                .andExpect(jsonPath("$[0].author", is("Michael McDowell")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("tome 2 : La digue", "Michael McDowell", "MONSIEUR TOUSSAINT LOUVERTURE")))
                .andExpect(jsonPath("$[1]author", is("Michael McDowell")));*/
    }


}
