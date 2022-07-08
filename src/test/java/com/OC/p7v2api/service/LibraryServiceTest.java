package com.OC.p7v2api.service;

import com.OC.p7v2api.entities.*;
import com.OC.p7v2api.repositories.LibraryRepository;
import com.OC.p7v2api.services.BookService;
import com.OC.p7v2api.services.LibraryService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest  {
    private AutoCloseable autoCloseable;
    private LibraryService libraryServiceUndertest;
    private Library library;
    @Mock
    LibraryRepository libraryRepository;



    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        libraryServiceUndertest = new LibraryService(libraryRepository);

    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void checkFindAllLibraries_shouldCallLibraryRepository() {
        //GIVEN WHEN
        libraryServiceUndertest.findAllLibraries();
        //THEN
        verify(libraryRepository).findAll();
    }

    @Test
    public void checkFindALibraryById_WhenIdIsValid_shouldCallLibraryRepository() throws Exception {
        //GIVEN WHEN
        libraryServiceUndertest.getALibraryById(1);
        //THEN
        verify(libraryRepository).getById(1);
    }

    @Test
    public void checkFindALibraryById_WhenIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            libraryServiceUndertest.getALibraryById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void checkSaveALibrary_WhenLibraryIsValid_shouldCallLibraryRepository() throws Exception {
        //GIVEN WHEN
         Library libraryUnderTest = new Library(1,"Katzenheim Nord","3 rue des Fleurs 57000 Katzenheim","katzenheim@gmail.com","0388779988","10h-18h",null);
        libraryServiceUndertest.saveALibrary(libraryUnderTest);
        //THEN
        verify(libraryRepository).save(libraryUnderTest);
    }

    @Test
    public void checkSaveALibrary_WhenLibraryIsNull_shouldThrowAnException() throws Exception {

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            libraryServiceUndertest.saveALibrary(library);
        }).withMessage("Library can't be null");
    }

    @Test
    public void checkDeleteALibrary_shouldCallLibraryRepository() throws Exception {
        List<Book>books = new ArrayList<>();
        Library libraryUnderTest = new Library(1,"Katzenheim Nord","3 rue des Fleurs 57000 Katzenheim","katzenheim@gmail.com","0388779988","10h-18h",books);
        //GIVEN WHEN
        libraryServiceUndertest.deleteALibrary(libraryUnderTest);
        //THEN
        verify(libraryRepository).delete(libraryUnderTest);
    }

    @Test
    public void checkDeleteALibrary_WhenLibraryIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
        Library libraryUnderTest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            libraryServiceUndertest.deleteALibrary(libraryUnderTest);
        });
    }

}
