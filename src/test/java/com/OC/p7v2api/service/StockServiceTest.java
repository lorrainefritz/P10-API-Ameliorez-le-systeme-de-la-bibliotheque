package com.OC.p7v2api.service;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.repositories.StockRepository;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    private AutoCloseable autoCloseable;
    private StockService stockServiceUnderTest;
    private Book book;

    @Mock
    private StockRepository stockRepository;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        stockServiceUnderTest = new StockService(stockRepository);
        book = new Book();

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void checkFindAllStocks_shouldCallStockRepository() {
        //GIVEN WHEN
        stockServiceUnderTest.findAllStocks();
        //THEN
        verify(stockRepository).findAll();
    }

    @Test
    public void checkFindAStockById_WhenIdIsValid_shouldCallStockRepository() throws Exception {
        //GIVEN WHEN
        stockServiceUnderTest.getOneStockById(1);
        //THEN
        verify(stockRepository).getById(1);
    }

    @Test
    public void checkFindAStockById_WhenIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            stockServiceUnderTest.getOneStockById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void checkSaveAStock_WhenStockIsValid_shouldCallStockRepository() throws Exception {
        //GIVEN WHEN
        Stock stockUnderTest = new Stock(1, 2, 0, 2, true, book);
        stockServiceUnderTest.saveAStock(stockUnderTest);
        //THEN
        verify(stockRepository).save(stockUnderTest);
    }

    @Test
    public void checkSaveAStock_WhenStockIsNull_shouldThrowAnException() throws Exception {
        Stock stockUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            stockServiceUnderTest.saveAStock(stockUnderTest);
        }).withMessage("Stock can't be null");
    }

    @Test
    public void checkDeleteAStock_shouldCallBookRepository() throws Exception {
        //GIVEN WHEN
        Stock stockUnderTest = new Stock(1, 2, 0, 2, true, book);
        stockServiceUnderTest.deleteAStock(stockUnderTest);
        //THEN
        verify(stockRepository).delete(stockUnderTest);
    }

    @Test
    public void checkDeleteAStock_WhenStockIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
        Stock stockUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            stockServiceUnderTest.deleteAStock(stockUnderTest);
        });
    }
}
