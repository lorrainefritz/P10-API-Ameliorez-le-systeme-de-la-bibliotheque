package com.OC.p7v2api.controller;

import com.OC.p7v2api.controllers.StockController;
import com.OC.p7v2api.dtos.StockDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.mappers.StockDtoMapper;
import com.OC.p7v2api.services.StockService;
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

@ContextConfiguration(classes = {StockController.class})
@ExtendWith(SpringExtension.class)
public class StockControllerTest {
    @Autowired
    private StockController stockController;

    @MockBean
    private StockDtoMapper stockDtoMapper;

    @MockBean
    private StockService stockService;

    @Test
    void checkFindAllStocks_shouldReturnAIsOkStatus() throws Exception {
        //GIVEN WHEN
        when(this.stockService.findAllStocks()).thenReturn(new ArrayList<>());
        when(this.stockDtoMapper.stockToAllStockDto((java.util.List<Stock>) any())).thenReturn(new ArrayList<>());
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/stocks");
        MockMvcBuilders.standaloneSetup(this.stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void checkSaveAStock_shouldReturnAIsCreatedStatus() throws Exception {
        //GIVEN
        Stock stock1 = new Stock(1,2,1,3,true,new Book());
        Stock stock2 = new Stock(2,1,1,2,true,new Book());
        StockDto stockDto = new StockDto(1,2,1,3,true);
        String stockDtoAsAStringValue = (new ObjectMapper()).writeValueAsString(stockDto);
        //WHEN
        when(this.stockService.saveAStock((Stock) any())).thenReturn(stock1);
        when(this.stockDtoMapper.stockDtoToStock((StockDto) any())).thenReturn(stock2);
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stockDtoAsAStringValue);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.stockController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"numberOfCopiesAvailable\":2,\"numberOfCopiesOut\":1,\"totalOfCopies\":3,\"bookIsAvailable\":true}"));

    }


    @Test
    void checkDeleteAStock_shouldReturnAIsAcceptedStatus() throws Exception {
        //GIVEN
        Stock stock1 = new Stock(1,2,1,3,true,new Book());
        //WHEN
        when(this.stockService.getOneStockById((Integer) any())).thenReturn(stock1);
        //THEN
        doNothing().when(this.stockService).deleteAStock((Stock) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/stocks/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.stockController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }


}
