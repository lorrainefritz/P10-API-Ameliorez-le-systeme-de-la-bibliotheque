package com.OC.p7v2api.controllers;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping(value = "/libraries")
    public List<Library> findAllLibraries(){
        log.info("HTTP GET request received at /librariess with findAllLibraries");
        return libraryService.findAllLibraries();
    }
}
