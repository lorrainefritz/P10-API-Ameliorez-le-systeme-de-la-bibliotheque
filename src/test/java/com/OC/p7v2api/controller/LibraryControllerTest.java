package com.OC.p7v2api.controller;

import com.OC.p7v2api.controllers.LibraryController;
import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.mappers.LibraryDtoMapper;
import com.OC.p7v2api.services.LibraryService;
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

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {LibraryController.class})
@ExtendWith(SpringExtension.class)
public class LibraryControllerTest {

    @Autowired
    private LibraryController libraryController;

    @MockBean
    private LibraryDtoMapper libraryDtoMapper;

    @MockBean
    private LibraryService libraryService;


    @Test
    void checkFindAllLibraries_ShouldReturnAnOkStatus() throws Exception {
        //GIVEN WHEN
        when(this.libraryService.findAllLibraries()).thenReturn(new ArrayList<>());
        when(this.libraryDtoMapper.libraryToAllLibraryDto((java.util.List<Library>) any())).thenReturn(new ArrayList<>());
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/libraries");
        MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void checkSaveALibrary_shouldReturnAIsCreatedStatus() throws Exception {
        //GIVEN
        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        Library library2 = new Library(2, "KatzenheimSud@gmail.com", "8 rue des camélias 57000 Katzenheim", "katzenheimSud@gmail.com", "0388997766", "10-19h", new ArrayList<>());
        LibraryDto libraryDto = new LibraryDto(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h");
        String libraryDtoAsAStringValue = (new ObjectMapper()).writeValueAsString(libraryDto);
        //WHEN
        when(this.libraryService.saveALibrary((Library) any())).thenReturn(library1);
        when(this.libraryDtoMapper.libraryDtoToLibrary((LibraryDto) any())).thenReturn(library2);
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/libraries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(libraryDtoAsAStringValue);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"KatzenheimNord@gmail.com\",\"address\":\"3 rue des fleurs 57000 Katzenheim\",\"email\":\"katzenheimNord@gmail.com\",\"phone\":\"0388997744\",\"openingTime\":\"10-18h\"}"));

    }


    @Test
    void checkDeleteALibrary_shouldReturnAIsAcceptedStatus() throws Exception {
        //GIVEN
        Library library1 = new Library(1, "KatzenheimNord@gmail.com", "3 rue des fleurs 57000 Katzenheim", "katzenheimNord@gmail.com", "0388997744", "10-18h", new ArrayList<>());
        //WHEN
        when(this.libraryService.getALibraryById((Integer) any())).thenReturn(library1);
        //THEN
        doNothing().when(this.libraryService).deleteALibrary((Library) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/libraries/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.libraryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }


}
