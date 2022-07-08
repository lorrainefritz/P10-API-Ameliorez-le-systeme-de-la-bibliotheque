package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.dtos.StockDto;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.entities.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockDtoMapper {
    @Mapping(source = "stock.id",target = "id")
    @Mapping(source = "stock.numberOfCopiesAvailable",target = "numberOfCopiesAvailable")
    @Mapping(source = "stock.numberOfCopiesOut",target = "numberOfCopiesOut")
    @Mapping(source = "stock.totalOfCopies",target = "totalOfCopies")
    @Mapping(source = "stock.bookIsAvailable",target = "bookIsAvailable")
    StockDto stockToStockDto(Stock stock);
    List<StockDto> stockToAllStockDto(List<Stock>stocks);
    Stock stockDtoToStock(StockDto stockDto);
}
