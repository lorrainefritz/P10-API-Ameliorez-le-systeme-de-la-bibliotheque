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
        log.info("in LibraryService in findAllLibrary method");
        return libraryRepository.findAll();
    }

    public Library getALibraryById(Integer id) throws Exception {
        log.info("in LibraryService in getALibraryById method");
        if (id==null){
            log.info("in LibraryService in getALibraryById method where id is null");
            throw new Exception("Id can't be null");
        }
        return libraryRepository.getById(id);
    }

    public Library saveALibrary(Library library) throws Exception {
        log.info("in LibraryService in saveALibrary method");
        if (library==null){
            log.info("in LibraryService in saveALibrary method where library is null");
            throw new Exception("Library can't be null");
        }
        return libraryRepository.save(library);
    }

    public void deleteALibrary(Library library) throws Exception {
        log.info("in LibraryService in deleteALibrary method");
        if (library==null){
            log.info("in LibraryService in deleteALibrary method where id is null");
            throw new Exception("Id can't be null");
        }
        library.setBooks(null);
        libraryRepository.delete(library);
    }



}
