package com.OC.p7v2api.controller;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.mappers.BookSlimWithLibraryAndStockDtoMapper;
import com.OC.p7v2api.mappers.LibraryDtoMapper;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@ActiveProfiles("test")*/
@SpringBootTest
@TestPropertySource( locations = "classpath:application.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LibraryController {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LibraryDtoMapper libraryDtoMapper;

    @Autowired
    LibraryService libraryService;


    @Test
    public void shouldReturnAllLibrariesWithAnOkStatus() throws Exception {

        mockMvc.perform(get("/libraries")).andExpect(status().isOk());
    }

    @Test
    void ShoulReturnAllLibrariesIntegrationTest() throws Exception {
        //GIVEN

        Library library1 = new Library(1,"Katzenheim-Nord","3 rue des fleurs 57000 KATZENHEIM","kathenheimNord@gmail.com","0383333333","10h-18h",null);
        Library library2 = new Library(13,"Katzenheim-Sud","11 rue des camelias 57000 KATZENHEIM","kathenheimSud@gmail.com","0383333333","10h-18h",null);
        Library library3 = new Library(14,"Katzenheim-Est","20 rue des paquerrettes 57000 KATZENHEIM","kathenheimEst@gmail.com","0383333333","10h-18h",null);

        libraryService.saveALibrary(library1);
        libraryService.saveALibrary(library2);
        libraryService.saveALibrary(library3);

        List<LibraryDto> librariesListUnderTest= new ArrayList<>();
        librariesListUnderTest.add(libraryDtoMapper.libraryToLibraryDto(library1));
        librariesListUnderTest.add(libraryDtoMapper.libraryToLibraryDto(library2));
        librariesListUnderTest.add(libraryDtoMapper.libraryToLibraryDto(library3));

        //WHEN
        /*when(libraryDtoMapper.libraryToAllLibraryDto(libraryService.findAllLibraries())).thenReturn(librariesListUnderTest);*/


        // THEN
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/libraries")).andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(librariesListUnderTest));


                /*.andExpect((jsonPath("$", hasSize(3))));*/
                /*.andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(13)));*/
    }



}
