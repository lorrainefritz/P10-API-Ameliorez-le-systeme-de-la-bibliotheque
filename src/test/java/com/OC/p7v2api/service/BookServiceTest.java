package com.OC.p7v2api.service;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.entities.*;

import com.OC.p7v2api.repositories.BookRepository;
import com.OC.p7v2api.services.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private AutoCloseable autoCloseable;
    private BookService bookServiceUnderTest;
    private Library library;
    private Stock stock;
    private User user;
    private Book book;


    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        bookServiceUnderTest = new BookService(bookRepository);
        library = new Library();
        stock = new Stock();
        user = new User();

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void checkFindAllBooks_shouldCallBookRepository() {
        //GIVEN WHEN
        bookServiceUnderTest.findAllBooks();
        //THEN
        verify(bookRepository).findAll();
    }

    @Test
    public void checkGetABookById_WhenIdIsValid_shouldCallBookRepository() throws Exception {
        //GIVEN WHEN
        bookServiceUnderTest.getABookById(1);
        //THEN
        verify(bookRepository).getById(1);
    }

    @Test
    public void checkGetABookById_WhenIdIsNull_shouldThrowAnException() throws Exception {

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.getABookById(null);
        }).withMessage("Invalid Id");


    }

    @Test
    public void checkSaveABook_WhenBookIsValid_shouldCallBookRepository() throws Exception {
        //GIVEN WHEN
        Book bookUnderTest = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        bookServiceUnderTest.saveABook(bookUnderTest);
        //THEN
        verify(bookRepository).save(bookUnderTest);
    }

    @Test
    public void checkSaveABook_WhenBookIsNull_shouldThrowAnException() throws Exception {
        Book bookUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.saveABook(bookUnderTest);
        }).withMessage("Book can't be null");
    }

    @Test
    public void checkSaveABook_WhenBookIsNotValid_shouldThrowAnException() throws Exception {
        Book bookUnderTest = new Book();
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.saveABook(bookUnderTest);
        });
    }


    @Test
    public void checkSettingTheNumberOfReservationPossibleForThisBook_whenGivenAValidBook_ShouldSetMaxReservationSize() throws Exception {
        stock = new Stock(1, 2, 0, 2, true, book);
        book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 0);
        Book bookUnderTest = bookServiceUnderTest.settingTheNumberOfReservationPossibleForThisBook(book);
        assertThat(bookUnderTest.getMaxReservationListSize()).isEqualTo(4);
    }

    @Test
    void checkSettingTheNumberOfReservationPossibleForThisBook_whenBookIsNull_ShouldThrowAnException() {
        Book bookUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.settingTheNumberOfReservationPossibleForThisBook(bookUnderTest);
        }).withMessage("Book can't be null");
    }

    @Test
    public void checkSettingTheNumberOfReservationPossibleForThisBook_WhenBookIsNotValid_shouldThrowAnException() throws Exception {
        Book bookUnderTest = new Book();
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.settingTheNumberOfReservationPossibleForThisBook(bookUnderTest);
        });
    }

    @Test
    public void checkDeleteABook_shouldCallBookRepository() throws Exception {
        //GIVEN WHEN
        Book bookUnderTest = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 0);
        bookServiceUnderTest.deleteABook(bookUnderTest);
        //THEN
        verify(bookRepository).delete(bookUnderTest);
    }


    @Test
    public void checkDeleteABook_WhenBookIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
        Book bookUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.deleteABook(bookUnderTest);
        }).withMessage("Book can't be null");
    }

    @Test
    public void checkFindBookByKeyword_shouldCallTheRepository() {
        //GIVEN WHEN
        bookServiceUnderTest.findBooksWithKeyword("Vampire");
        //THEN
        verify(bookRepository).findBooksByKeyword("Vampire");
    }


    @Test
    public void checkGetAscendingReservation_shouldReturnAscendingReservations_WhenGivenAValidBook() throws Exception {
        //GIVEN WHEN
        Date startDateUnderTest1 = new GregorianCalendar(2022, 06, 11).getTime();
        Date startDateUnderTest2 = new GregorianCalendar(2022, 06, 12).getTime();
        Date endDateUnderTest1 = new GregorianCalendar(2022, 06, 13).getTime();
        Date endDateUnderTest2 = new GregorianCalendar(2022, 06, 14).getTime();
        Book bookUnderTest = null;
        Reservation reservationUndertest1 = new Reservation(1, startDateUnderTest1, endDateUnderTest1, 1, user, bookUnderTest);
        Reservation reservationUndertest2 = new Reservation(2, startDateUnderTest2, endDateUnderTest2, 2, user, bookUnderTest);
        List<Reservation> reservationListUnderTest = new ArrayList<>();
        reservationListUnderTest.add(reservationUndertest2);
        reservationListUnderTest.add(reservationUndertest1);
        bookUnderTest = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, reservationListUnderTest, null, 0, 2);
        //THEN
        List<Reservation> listReservationUndertest = bookServiceUnderTest.getAscendingSortedReservations(bookUnderTest);
        assertThat(listReservationUndertest.size()).isEqualTo(2);
        assertThat(listReservationUndertest.get(0).getReservationPosition()).isEqualTo(1);
        assertThat(listReservationUndertest.get(0).getEndDate()).isEqualTo(endDateUnderTest1);
    }

    @Test
    public void checkGetAscendingReservation_WhenBookIsNotValid_shouldThrowAnException() throws Exception {
        Book bookUnderTest = new Book();
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            bookServiceUnderTest.getAscendingSortedReservations(bookUnderTest);
        });
    }


    @Test
    public void checkAllBooksSortedAscendingByID_shouldReturnAllBooksInAnAscendingList() throws Exception {
        //GIVEN

        Book book1 = new Book(2, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Book book2 = new Book(1, "Blackwater, tome 2 : La digue", "Michael McDowell", "Fantastique", "les Caskey reconstruisent", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Book book3 = new Book(3, "Blackwater, tome 3 : La maison", "Michael McDowell", "Fantastique", "les Caskey se déchirent", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        //WHEN
        when(bookRepository.findAll()).thenReturn(bookList);

        //THEN
        List<Book> booklistUnderTest = bookServiceUnderTest.getAllBooksSortedAscendingById();

        assertThat(booklistUnderTest.size()).isEqualTo(3);
        assertThat(booklistUnderTest.get(0).getId()).isEqualTo(1);
        assertThat(booklistUnderTest.get(1).getId()).isEqualTo(2);
        assertThat(booklistUnderTest.get(2).getId()).isEqualTo(3);
    }


}
