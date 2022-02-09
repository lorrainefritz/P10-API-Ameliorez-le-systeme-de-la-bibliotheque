package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.repositories.BookRepository;
import com.OC.p7v2api.repositories.LibraryRepository;
import com.OC.p7v2api.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public List<Library> findAllLibraries(){
        log.info("in LibraryService in getAllLibrary method");
        return libraryRepository.findAll();
    }

    public Library getALibraryById(Integer id) {
        log.info("in LibraryService in getALibraryById method");
        return libraryRepository.getById(id);
    }
    /*public Library  getOneLibraryByName(String name) {
        logger.info("in LibraryService in getOneLibraryByName method");
        return libraryRepository.findByName(name);
    }*/

    public Library saveALibrary(Library library) {
        log.info("in LibraryService in saveALibrary method");
        return libraryRepository.save(library);
    }

    public void deleteALibrary(Integer id) {
        log.info("in LibraryService in deleteALibrary method");
        libraryRepository.deleteById(id);
    }



}
