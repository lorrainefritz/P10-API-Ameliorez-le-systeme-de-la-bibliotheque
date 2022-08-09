package com.OC.p7v2api.service;

import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.repositories.BorrowRepository;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.BorrowService;
import com.OC.p7v2api.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {

    private BorrowService borrowServiceUnderTest;
    private AutoCloseable autoCloseable;
    private Borrow borrow;
    private Date startDate;
    private Date returnDate;
    private Stock stock;
    private User user;
    private Library library;
    private Book book;
    private Role role;

    @Mock
    private BorrowRepository borrowRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setup(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        borrowServiceUnderTest = new BorrowService(borrowRepository,bookService,userService);
        library = new Library();
        stock = new Stock();
        user = new User();
        role = new Role();

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void checkFindAllBorrows_shouldCallBorrowRepository() {
        //GIVEN WHEN
        borrowServiceUnderTest.findAllBorrows();
        //THEN
        verify(borrowRepository).findAll();
    }

    @Test
    public void checkFindABorrowById_WhenIdIsValid_shouldCallBorrowRepository() throws Exception {
        //GIVEN WHEN
        borrowServiceUnderTest.findABorrowById(1);
        //THEN
        verify(borrowRepository).getById(1);
    }

    @Test
    public void checkFindABorrowById_WhenIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            borrowServiceUnderTest.findABorrowById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void checkSaveABorrow_WhenBorrowIsValid_shouldCallBorrowRepository() throws Exception {
        //GIVEN WHEN
        user = new User(1,"paul@gmail.com","Paul","Atreid","3 rue des fleurs Katzenheim","0666445599","123",role,null,null);
        book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Borrow borrowUnderTest = new Borrow(1,startDate,returnDate,false,false,user,book);
        borrowServiceUnderTest.saveABorrow(borrowUnderTest);
        //THEN
        verify(borrowRepository).save(borrowUnderTest);
    }

    @Test
    public void checkSaveABorrow_WhenBorrowIsNull_shouldThrowAnException() throws Exception {

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            borrowServiceUnderTest.saveABorrow(borrow);
        }).withMessage("Borrow can't be null");
    }

    @Test
    public void checkDeleteABorrow_shouldCallBorrowRepository() throws Exception {
        //GIVEN WHEN
        user = new User(1,"paul@gmail.com","Paul","Atreid","3 rue des fleurs Katzenheim","0666445599","123",role,null,null);
        book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Borrow borrowUnderTest = new Borrow(1,startDate,returnDate,false,false,user,book);
        List<Borrow>borrows = new ArrayList<>();
        borrows.add(borrow);
        user.setBorrows(borrows);
        borrowServiceUnderTest.deleteABorrow(borrowUnderTest);
        //THEN
        verify(borrowRepository).delete(borrowUnderTest);
    }

    @Test
    public void checkDeleteABorrow_WhenBookIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
        Borrow borrowUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            borrowServiceUnderTest.deleteABorrow(borrowUnderTest);
        }).withMessage("Borrow can't be null");
    }

    @Test
    public void checkExtendABorrow_shouldExtendABorrowReturnABorrow_WhenGivenAValidBorrowId() throws Exception {
        //GIVEN WHEN
        startDate = new GregorianCalendar(2022, 06, 1).getTime();
        returnDate = new GregorianCalendar(2022, 06, 2).getTime();
        book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        user = new User(1,"paul@gmail.com","Paul","Atreid","3 rue des fleurs Katzenheim","0666445599","123",role,null,null);

        Date returnDateUnderTest = new GregorianCalendar(2022, 06, 30).getTime();
        Borrow borrow = new Borrow(1,startDate,returnDate,false,false,user,book);
        //WHEN
        when(borrowServiceUnderTest.findABorrowById(1)).thenReturn(borrow);

        //THEN
        Borrow borrowUnderTest = borrowServiceUnderTest.extendABorrow(1);
        assertThat(borrowUnderTest.isAlreadyExtended()).isTrue();
        assertThat(borrowUnderTest.getReturnDate()).isEqualTo(returnDateUnderTest);
    }

    @Test
    public void checkExtendABorrow_WhenBorrowIdIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
        Borrow borrowUnderTest = new Borrow();
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            borrowServiceUnderTest.extendABorrow(borrowUnderTest.getId());
        }).withMessage("Id can't be null");
    }
}
