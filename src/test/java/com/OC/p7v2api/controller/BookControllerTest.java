package com.OC.p7v2api.controller;

import com.OC.p7v2api.controllers.BookController;
import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.services.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;


@ContextConfiguration(classes = {BookController.class})
@ExtendWith(SpringExtension.class)

public class BookControllerTest {


    @Autowired
    private BookController bookController;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookSlimWithLibraryAndStockDtoMapper bookSlimWithLibraryAndStockDtoMapper;

    @MockBean
    private LibraryService libraryService;

    @MockBean
    private StockService stockService;


    @Test
    void checkBookList_shouldReturnAListOfBook() throws Exception {
        when(this.bookSlimWithLibraryAndStockDtoMapper.booksToAllBooksDto((java.util.List<Book>) any()))
                .thenReturn(new ArrayList<>());
        when(this.bookService.getAllBooksSortedAscendingById()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books");
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void checkBooksListWithAKeyword_whenGivenAKeyword_shouldReturnABookListWhoMatchToTheKeyword() throws Exception {
        when(this.bookSlimWithLibraryAndStockDtoMapper.booksToAllBooksDto((java.util.List<Book>) any()))
                .thenReturn(new ArrayList<>());
        when(this.bookService.findBooksWithKeyword((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/search").param("keyword", "vampire");
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void checkSaveABook() throws Exception {
        //GIVEN
        Date nearestDateOfReturn = new GregorianCalendar(2022, 06, 3).getTime();

        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(),  nearestDateOfReturn, 0, 4);

        Library library2 = new Library(2, "KatzenheimSud@gmail.com", "8 rue des camélias 57000 Katzenheim", "katzenheimSud@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock2 = new Stock(2, 2, 1, 3, true, book1);
        Book book2 = new Book(2, "Blackwater, tome 2 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF02", "cover", library2, stock2, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Library library3 = new Library(3, "KatzenheimSud@gmail.com", "9 rue des campanules 57000 Katzenheim", "katzenheimEst@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock3 = new Stock(3, 2, 0, 2, true, book2);
        Book book3 = new Book(3, "Blackwater, tome 3 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF03", "cover", library3, stock3, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        Library library4 = new Library(4, "KatzenheimOuest@gmail.com", "10 rue des cactus 57000 Katzenheim", "katzenheimOuest@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock4 = new Stock(4, 2, 0, 2, true, book3);
        Book book4 = new Book(4, "Blackwater, tome 4 : La maison", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF04", "cover", library3, stock3, new ArrayList<>(), new ArrayList<>(), nearestDateOfReturn, 0, 4);

        BookSlimWithLibraryAndStockDto bookDto = new BookSlimWithLibraryAndStockDto(1,"Blackwater, tome 1 : La Digue","Michael McDowell","MONSIEUR TOUSSAINT LOUVERTURE","Fantastique","summary",2,"Katzenheim Nord",nearestDateOfReturn,0,4);

        String bookDtoWriteAsAString = (new ObjectMapper()).writeValueAsString(bookDto);


        //WHEN
        when(this.libraryService.saveALibrary((Library) any())).thenReturn(library3);
        when(this.stockService.saveAStock((Stock) any())).thenReturn(stock2);
        when(this.bookSlimWithLibraryAndStockDtoMapper.bookDtoToBook((BookSlimWithLibraryAndStockDto) any()))
                .thenReturn(book4);
        when(this.bookSlimWithLibraryAndStockDtoMapper.bookDtoToStock((BookSlimWithLibraryAndStockDto) any()))
                .thenReturn(stock4);
        when(this.bookService.saveABook((Book) any())).thenThrow(new Exception("An error occurred"));

        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookDtoWriteAsAString);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"title\":\"Blackwater, tome 1 : La Digue\",\"author\":\"Michael McDowell\",\"publisher\":\"MONSIEUR TOUSSAINT LOUVERTURE\",\"type\":\"Fantastique\",\"summary\":\"summary\","
                                        + "\"numberOfCopiesAvailable\":2,\"libraryName\":\"Katzenheim Nord\",\"nearestReturnDate\":1656799200000,\"numberOfReservation"
                                        + "\":0,\"maxReservationListSize\":4}"));
    }

    @Test
    void checkDeleteABook() throws Exception {
        doNothing().when(this.stockService).deleteAStock((Stock) any());

        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date date = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());

        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Stock stock1 = new Stock(1, 2, 0, 2, true, new Book());
        Book book1 = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF01", "cover", library1, stock1, new ArrayList<>(), new ArrayList<>(), date, 0, 4);

        Library library2 = new Library(2, "KatzenheimSud@gmail.com", "8 rue des camélias 57000 Katzenheim", "katzenheimSud@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock2 = new Stock(2, 2, 1, 3, true, book1);
        Book book2 = new Book(2, "Blackwater, tome 2 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF02", "cover", library2, stock2, new ArrayList<>(), new ArrayList<>(), date, 0, 4);

        Library library3 = new Library(3, "KatzenheimSud@gmail.com", "9 rue des campanules 57000 Katzenheim", "katzenheimEst@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock3 = new Stock(3, 2, 0, 2, true, book2);
        Book book3 = new Book(3, "Blackwater, tome 3 : La Digue", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF03", "cover", library3, stock3, new ArrayList<>(), new ArrayList<>(), date, 0, 4);

        Library library4 = new Library(4, "KatzenheimOuest@gmail.com", "10 rue des cactus 57000 Katzenheim", "katzenheimOuest@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        Stock stock4 = new Stock(4, 2, 0, 2, true, book3);
        Book book4 = new Book(4, "Blackwater, tome 4 : La maison", "Michael McDowell", "Fantaisie", "summary", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "", "MCF04", "cover", library3, stock3, new ArrayList<>(), new ArrayList<>(), date, 0, 4);

        doNothing().when(this.bookService).deleteABook((Book) any());
        when(this.bookService.getABookById((Integer) any())).thenReturn(book2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }


}
