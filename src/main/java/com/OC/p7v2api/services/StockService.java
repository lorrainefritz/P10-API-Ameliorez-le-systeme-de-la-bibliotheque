package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Stock;
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
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> findAllStocks(){
        log.info("in StockService in findAllStocks method");
        return stockRepository.findAll();
    }

    public Stock getOneStockById(Integer id) {
        log.info("in StockService in getOneStockById method");
        return stockRepository.getById(id);
    }

    public Stock saveAStock (Stock stock) {
        log.info("in StockService in saveStock method");


        return stockRepository.save(stock);
    }

    public void deleteAStock (Stock stock) {
        log.info("in StockService in deleteAStock method" + stock.getId());
        stockRepository.delete(stock);
    }
}
