package com.OC.p7v2api.service;

import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.repositories.ReservationRepository;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.ReservationService;
import com.OC.p7v2api.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    private ReservationService reservationServiceUnderTest;
    private AutoCloseable autoCloseable;
    private Date startDate;
    private Date endDate;
    private Stock stock;
    private User user;
    private Library library;
    private Book book;
    private Role role;
    private Reservation reservation;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;


    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        reservationServiceUnderTest = new ReservationService(reservationRepository, bookService, userService);
        reservation = null;

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void checkFindAllReservations_shouldCallReservationRepository() {
        //GIVEN WHEN
        reservationServiceUnderTest.findAllReservations();
        //THEN
        verify(reservationRepository).findAll();
    }

    @Test
    public void checkGetAReservationById_WhenIdIsValid_shouldCallReservationRepository() throws Exception {
        //GIVEN WHEN
        reservationServiceUnderTest.getAReservationById(1);
        //THEN
        verify(reservationRepository).getById(1);
    }

    @Test
    public void checkGetAReservationById_WhenIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            reservationServiceUnderTest.getAReservationById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void checkSaveAReservation_WhenReserbationIsValid_shouldCallReservationRepository() throws Exception {
        //GIVEN WHEN
        user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs Katzenheim", "0666445599", "123", role, null, null);
        book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        startDate = new GregorianCalendar(2022, 06, 11).getTime();
        endDate = new GregorianCalendar(2022, 06, 12).getTime();
        Reservation reservationUnderTest = new Reservation(1, startDate, endDate, 1, user, book);
        reservationServiceUnderTest.saveAReservation(reservationUnderTest);
        //THEN
        verify(reservationRepository).save(reservationUnderTest);
    }

    @Test
    public void checkSaveAReservation_WhenReservationIsNull_shouldThrowAnException() throws Exception {

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            reservationServiceUnderTest.saveAReservation(reservation);
        }).withMessage("Reservation can't be null");
    }


    @Test
    public void checkDeleteAReservation_WhenGivenAValidId_WhenReservationIsTheOnlyReservation() throws Exception {
        //GIVEN
        startDate = new GregorianCalendar(2022, 06, 11).getTime();
        endDate = new GregorianCalendar(2022, 06, 12).getTime();
        Date returnDateUnderTest = new GregorianCalendar(2022, 06, 30).getTime();
        Library library = new Library();
        Stock stock = new Stock();
        User user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des roses 57000 METZ", "0383663320", "123", null, null, null);
        Book book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Reservation reservation = new Reservation(1, startDate, endDate, 1, user, book);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        book.setReservations(reservations);

        //WHEN
        Mockito.when(reservationServiceUnderTest.getAReservationById(1)).thenReturn(reservation);

        //THEN
        reservationServiceUnderTest.deleteAReservationById(1);
        Mockito.verify(reservationRepository, times(1)).deleteById(reservation.getId());
    }

    @Test
    public void checkDeleteAReservation_WhenGivenAValidId_WhenReservationIsTheFirstElementOfTheList() throws Exception {
        //GIVEN
        startDate = new GregorianCalendar(2022, 06, 11).getTime();
        endDate = new GregorianCalendar(2022, 06, 12).getTime();
        Date returnDateUnderTest = new GregorianCalendar(2022, 06, 30).getTime();
        Library library = new Library();
        Stock stock = new Stock();
        User user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des roses 57000 METZ", "0383663320", "123", null, null, null);
        Book book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Reservation reservation1 = new Reservation(1, startDate, endDate, 1, user, book);
        Reservation reservation2 = new Reservation(2, startDate, endDate, 2, user, book);
        Reservation reservation3 = new Reservation(3, startDate, endDate, 3, user, book);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        book.setReservations(reservations);

        //WHEN
        Mockito.when(reservationServiceUnderTest.getAReservationById(1)).thenReturn(reservation1);
        Mockito.when(bookService.getAscendingSortedReservations(book)).thenReturn(reservations);

        //THEN
        reservationServiceUnderTest.deleteAReservationById(1);
        Mockito.verify(reservationRepository, times(1)).deleteById(reservation1.getId());
        assertThat(book.getReservations().size()).isEqualTo(2);
        assertThat(book.getReservations().get(0).getId()).isEqualTo(2);
        assertThat(book.getReservations().get(0).getReservationPosition()).isEqualTo(1);
        assertThat(book.getReservations().get(1).getReservationPosition()).isEqualTo(2);
    }

    @Test
    public void checkDeleteAReservation_WhenGivenAValidId_WhenReservationIsSomewhereInTheMiddleOFTheList() throws Exception {
        //GIVEN
        Date startDate = new GregorianCalendar(2022, 06, 11).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 12).getTime();
        Date returnDateUnderTest = new GregorianCalendar(2022, 06, 30).getTime();
        Library library = new Library();
        Stock stock = new Stock();
        List<Reservation> reservations = new ArrayList<>();
        User user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des roses 57000 METZ", "0383663320", "123", null, null, null);
        Book book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Reservation reservation1 = new Reservation(1, startDate, returnDate, 1, user, book);
        Reservation reservation2 = new Reservation(2, startDate, returnDate, 2, user, book);
        Reservation reservation3 = new Reservation(3, startDate, returnDate, 3, user, book);
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        book.setReservations(reservations);

        //WHEN
        Mockito.when(reservationServiceUnderTest.getAReservationById(2)).thenReturn(reservation2);
        Mockito.when(bookService.getAscendingSortedReservations(book)).thenReturn(reservations);
        //THEN
        reservationServiceUnderTest.deleteAReservationById(2);
        Mockito.verify(reservationRepository, times(1)).deleteById(reservation2.getId());
        assertThat(book.getReservations().size()).isEqualTo(2);
        assertThat(book.getReservations().get(0).getId()).isEqualTo(1);
        assertThat(book.getReservations().get(1).getId()).isEqualTo(3);
        assertThat(book.getReservations().get(0).getReservationPosition()).isEqualTo(1);
        assertThat(book.getReservations().get(1).getReservationPosition()).isEqualTo(2);
    }

    @Test
    public void checkDeleteAReservation_WhenGivenAValidId_WhenReservationIsAtTheEndOfTheList() throws Exception {
        //GIVEN
        Date startDate = new GregorianCalendar(2022, 06, 11).getTime();
        Date returnDate = new GregorianCalendar(2022, 06, 12).getTime();
        Date returnDateUnderTest = new GregorianCalendar(2022, 06, 30).getTime();
        Library library = new Library();
        Stock stock = new Stock();
        List<Reservation> reservations = new ArrayList<>();
        User user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des roses 57000 METZ", "0383663320", "123", null, null, null);
        Book book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Reservation reservation1 = new Reservation(1, startDate, returnDate, 1, user, book);
        Reservation reservation2 = new Reservation(2, startDate, returnDate, 2, user, book);
        Reservation reservation3 = new Reservation(3, startDate, returnDate, 3, user, book);
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        book.setReservations(reservations);

        //WHEN
        Mockito.when(reservationServiceUnderTest.getAReservationById(3)).thenReturn(reservation3);
        Mockito.when(bookService.getAscendingSortedReservations(book)).thenReturn(reservations);
        //THEN
        reservationServiceUnderTest.deleteAReservationById(3);
        Mockito.verify(reservationRepository, times(1)).deleteById(reservation3.getId());
        assertThat(book.getReservations().size()).isEqualTo(2);
        assertThat(book.getReservations().get(0).getId()).isEqualTo(1);
        assertThat(book.getReservations().get(1).getId()).isEqualTo(2);
    }


    @Test
    public void checkDeleteAReservation_WhenReservationIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            reservationServiceUnderTest.deleteAReservationById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void ckeckMakeAReservation_WhenBookIdAndUserIdAreValid() throws Exception {
        //GIVEN
        startDate = new GregorianCalendar(2022, 06, 11).getTime();
        endDate = new GregorianCalendar(2022, 06, 12).getTime();
        Date returnDateUnderTest = new GregorianCalendar(2022, 06, 30).getTime();
        Library library = new Library();
        Stock stock = new Stock();
        User user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des roses 57000 METZ", "0383663320", "123", null, null, null);
        Book book = new Book(1, "Blackwater, tome 1 : La Crue", "Michael McDowell", "Fantastique", "les Caskey, doivent faire face à l'arrivée d'une femme mystérieuse", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stock, null, null, null, 0, 2);
        Reservation reservation = new Reservation(1, startDate, endDate, 1, user, book);
        //WHEN
        when(bookService.getABookById(1)).thenReturn(book);
        when(userService.getAUserById(1)).thenReturn(user);

        //THEN
        Reservation reservationUnderTest = reservationServiceUnderTest.makeAReservation(1, 1);
        assertThat(reservationUnderTest.getBook().getTitle()).isEqualTo(book.getTitle());
        assertThat(reservationUnderTest.getUser().getFirstName()).isEqualTo(user.getFirstName());

    }

    @Test
    public void checkMakeAReservation_WhenReservationIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des roses 57000 METZ", "0383663320", "123", null, null, null);
            reservationServiceUnderTest.makeAReservation(null, user.getId());
        }).withMessage("ReservationId and userId can't be null");
    }

    @Test
    public void checkMakeAReservation_WhenUserIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            reservation = new Reservation(1, startDate, endDate, 1, user, book);
            reservationServiceUnderTest.makeAReservation(reservation.getId(), null);
        }).withMessage("ReservationId and userId can't be null");
    }
}
