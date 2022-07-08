package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.dtos.StockDto;
import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.mappers.StockDtoMapper;
import com.OC.p7v2api.services.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
public class StockController {
    private final StockService stockService;
    private final StockDtoMapper stockDtoMapper;


    @GetMapping(value = "/stocks")
    public ResponseEntity<List<StockDto>> findAllStocks(){
        log.info("HTTP GET request received at /stocks with findAllStocks");
        return new ResponseEntity<>(stockDtoMapper.stockToAllStockDto(stockService.findAllStocks()), HttpStatus.OK);
    }

    @PostMapping(value = "/stocks")
    public ResponseEntity<StockDto> saveAStock(@RequestBody @Validated StockDto stockDto, BindingResult bindingResult) throws Exception {
        log.info("HTTP POST request received at /stocks with saveAStock");
        if (stockDto == null) {
            log.info("HTTP POST request received at /stocks with saveAStock where stockDto is null");
            return new ResponseEntity<>(stockDto, HttpStatus.NO_CONTENT);
        }
        else if (bindingResult.hasErrors()){
            log.info("HTTP POST request received at /borrows with saveABorrow where libraryDto is not valid");
            return new ResponseEntity<>(stockDto, HttpStatus.FORBIDDEN);
        }
        else {
            stockService.saveAStock(stockDtoMapper.stockDtoToStock(stockDto));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(stockDto);
    }

    @DeleteMapping(value = "/stocks/delete/{id}")
    public ResponseEntity deleteAStock(@PathVariable Integer id) throws Exception {
        log.info("HTTP DELETE request received at /stocks/delete/" + id + " with deleteAStock");
        if (id == null) {
            log.info("HTTP DELETE request received at /stocks/delete/id where id is null");
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        }
        Stock stock = stockService.getOneStockById(id);
        stockService.deleteAStock(stock);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
