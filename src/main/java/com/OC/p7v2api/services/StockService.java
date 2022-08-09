package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Stock;
import com.OC.p7v2api.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    public Stock getOneStockById(Integer id) throws Exception {
        log.info("in StockService in getOneStockById method");
        if (id==null){
            log.info("in StockService in getOneStockById method where id is null");
            throw new Exception("Id can't be null");
        }
        return stockRepository.getById(id);
    }

    public Stock saveAStock (Stock stock) throws Exception {
        log.info("in StockService in saveStock method");
        if (stock==null){
            log.info("in StockService in saveAStock method where stock is null");
            throw new Exception("Stock can't be null");
        }

        return stockRepository.save(stock);
    }

    public void deleteAStock (Stock stock) throws Exception {
        log.info("in StockService in deleteAStock method" + stock.getId());
        if (stock==null){
            log.info("in StockService in deleteAStock method where stock is null");
            throw new Exception("Stock can't be null");
        }
        stockRepository.delete(stock);
    }
}
