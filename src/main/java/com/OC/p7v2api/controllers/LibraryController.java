package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.mappers.LibraryDtoMapper;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
public class LibraryController {

    private final LibraryService libraryService;
    private final LibraryDtoMapper libraryDtoMapper;

    @GetMapping(value = "/libraries")
    public ResponseEntity<List<LibraryDto>> findAllLibraries(){
        log.info("HTTP GET request received at /librariess with findAllLibraries");
        return new ResponseEntity<>(libraryDtoMapper.libraryToAllLibraryDto(libraryService.findAllLibraries()), HttpStatus.OK);
    }

    @PostMapping(value = "/libraries")
    public ResponseEntity<LibraryDto> saveALibrary(@RequestBody @Validated LibraryDto libraryDto, BindingResult bindingResult) throws Exception {
        log.info("HTTP POST request received at /libraries with saveALibrary");
        if (libraryDto == null) {
            log.info("HTTP POST request received at /libraries with saveABorrow where libraryDto is null");
            return new ResponseEntity<>(libraryDto, HttpStatus.NO_CONTENT);
        }
        else if (bindingResult.hasErrors()){
            log.info("HTTP POST request received at /borrows with saveABorrow where libraryDto is not valid");
            return new ResponseEntity<>(libraryDto, HttpStatus.FORBIDDEN);
        }
        else {
            libraryService.saveALibrary(libraryDtoMapper.libraryDtoToLibrary(libraryDto));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryDto);
    }

    @DeleteMapping(value = "/libraries/delete/{id}")
    public ResponseEntity deleteALibrary(@PathVariable Integer id) throws Exception {
        log.info("HTTP DELETE request received at /libraries/delete/" + id + " with deleteALibrary");
        if (id == null) {
            log.info("HTTP DELETE request received at /libraries/delete/id where id is null");
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        }
        libraryService.deleteALibrary(libraryService.getALibraryById(id));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
