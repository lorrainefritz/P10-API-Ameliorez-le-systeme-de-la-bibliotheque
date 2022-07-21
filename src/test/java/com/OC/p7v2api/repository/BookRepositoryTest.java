package com.OC.p7v2api.repository;


import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.repositories.BookRepository;
import com.OC.p7v2api.repositories.LibraryRepository;
import com.OC.p7v2api.repositories.StockRepository;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class BookRepositoryTest {



@After
    void tearDown() {
        bookRepositoryUnderTest.deleteAll();
    }

    private Library library;
    private List<Borrow> borrows;
    private List<Reservation> reservations;
    private List<Book> books;
    private Book book;
    private Stock stoc1;
    private Stock stoc2;
    private Stock stoc3;
    @Autowired
    private BookRepository bookRepositoryUnderTest;

    @Test
    void checkFindBooksByKeyword_shouldReturnBooksWithTitleContainingKeyword_WhenGivenAKeyword() {
        //GIVEN

        List<Borrow> borrows = null;
        List<Reservation> reservations = null;
        Book book1 = null;
        Book book2 = null;
        Book book3 = null;

        Library library = new Library(1, "Katzenheim-Nord", "3 rue des fleurs 57000 METZ", "kathenheimNord@gmail.com", "0383333333", "10h-18h", books);

        Stock stock1 = new Stock(1, 2, 0, 2, true, book1);
        Stock stock2 = new Stock(1, 2, 0, 2, true, book2);
        Stock stock3 = new Stock(1, 2, 0, 2, true, book3);

        book1 = new Book(1, "Blackwater, tome 1", "Michael McDowell", "Horreur", "les Caskey et l'arrivée d'une femme", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc1, borrows, reservations, null, 0, 2);
        book2 = new Book(2, "Blackwater, tome 2", "John Doe", "Fantastique", "les Caskey et des réparations", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc1, null, null, null, 0, 2);
        book3 = new Book(3, "Lestat le Vampire", "Anne Rice", "Fantastique", "l'histoire d'un vampire", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc1, null, null, null, 0, 2);

        stock1.setBook(book1);
        stock2.setBook(book2);
        stock3.setBook(book3);

        String keywordUnderTest = "Vampire";

        bookRepositoryUnderTest.save(book1);
        bookRepositoryUnderTest.save(book2);
        bookRepositoryUnderTest.save(book3);

        //WHEN
        List<Book> bookListUnderTest = bookRepositoryUnderTest.findBooksByKeyword(keywordUnderTest);
        //THEN
        assertThat(bookListUnderTest.get(0).getTitle()).isEqualTo(book3.getTitle());

    }

    @Test
    void checkFindBooksByKeyword_shouldReturnBooksWithAuthorContainingKeyword_WhenGivenAKeyword() {
        //GIVEN

        List<Borrow> borrows = null;
        List<Reservation> reservations = null;
        Book book1 = null;
        Book book2 = null;
        Book book3 = null;

        Library library = new Library(1, "Katzenheim-Nord", "3 rue des fleurs 57000 METZ", "kathenheimNord@gmail.com", "0383333333", "10h-18h", books);

        Stock stock1 = new Stock(1, 2, 0, 2, true, book1);
        Stock stock2 = new Stock(1, 2, 0, 2, true, book2);
        Stock stock3 = new Stock(1, 2, 0, 2, true, book3);

        book1 = new Book(1, "Blackwater, tome 1", "Michael McDowell", "Horreur", "les Caskey et l'arrivée d'une femme", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc2, borrows, reservations, null, 0, 2);
        book2 = new Book(2, "Blackwater, tome 2", "John Doe", "Fantastique", "les Caskey et des réparations", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc2, null, null, null, 0, 2);
        book3 = new Book(3, "Lestat le Vampire", "Anne Rice", "Fantastique", "l'histoire d'un vampire", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc2, null, null, null, 0, 2);

        stock1.setBook(book1);
        stock2.setBook(book2);
        stock3.setBook(book3);

        String keywordUnderTest = "Doe";

        bookRepositoryUnderTest.save(book1);
        bookRepositoryUnderTest.save(book2);
        bookRepositoryUnderTest.save(book3);

        //WHEN
        List<Book> bookListUnderTest = bookRepositoryUnderTest.findBooksByKeyword(keywordUnderTest);
        //THEN
        assertThat(bookListUnderTest.get(0).getAuthor()).isEqualTo(book2.getAuthor());

    }

    @Test
    void checkFindBooksByKeyword_shouldReturnBooksWithTypsContainingKeyword_WhenGivenAKeyword() {
        List<Borrow> borrows = null;
        List<Reservation> reservations = null;
        Book book1 = null;
        Book book2 = null;
        Book book3 = null;

        Library library = new Library(1, "Katzenheim-Nord", "3 rue des fleurs 57000 METZ", "kathenheimNord@gmail.com", "0383333333", "10h-18h", books);

        Stock stock1 = new Stock(1, 2, 0, 2, true, book1);
        Stock stock2 = new Stock(1, 2, 0, 2, true, book2);
        Stock stock3 = new Stock(1, 2, 0, 2, true, book3);

        book1 = new Book(1, "Blackwater, tome 1", "Michael McDowell", "Horreur", "les Caskey et l'arrivée d'une femme", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc3, borrows, reservations, null, 0, 2);
        book2 = new Book(2, "Blackwater, tome 2", "John Doe", "Fantastique", "les Caskey et des réparations", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc3, null, null, null, 0, 2);
        book3 = new Book(3, "Lestat le Vampire", "Anne Rice", "Fantastique", "l'histoire d'un vampire", "MONSIEUR TOUSSAINT LOUVERTURE", "Français", "MCF01", "07/04/2022", null, library, stoc3, null, null, null, 0, 2);

        stock1.setBook(book1);
        stock2.setBook(book2);
        stock3.setBook(book3);

        String keywordUnderTest = "Horreur";

        bookRepositoryUnderTest.save(book1);
        bookRepositoryUnderTest.save(book2);
        bookRepositoryUnderTest.save(book3);

        //WHEN
        List<Book> bookListUnderTest = bookRepositoryUnderTest.findBooksByKeyword(keywordUnderTest);
        //THEN
        assertThat(bookListUnderTest.get(0).getType()).isEqualTo(book1.getType());

    }
}
