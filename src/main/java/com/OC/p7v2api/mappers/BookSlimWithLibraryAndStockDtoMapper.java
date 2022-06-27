package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")//,uses = {BorrowDtoMapper.class,ReservationDto.class}
public interface BookSlimWithLibraryAndStockDtoMapper {
    @Mapping(source = "book.id",target = "id")
    @Mapping(source = "book.title",target = "title")
    @Mapping(source = "book.author",target = "author")
    @Mapping(source = "book.publisher",target = "publisher")
    @Mapping(source = "book.type",target = "type")
    @Mapping(source = "book.summary",target = "summary")
    @Mapping(source="book.nearestReturnDate",target = "nearestReturnDate")
    @Mapping(source ="book.numberOfReservation",target = "numberOfReservation")
    @Mapping(source ="book.maxReservationListSize",target = "maxReservationListSize")
    @Mapping(source = "stock.numberOfCopiesAvailable", target = "numberOfCopiesAvailable")
    @Mapping(source = "library.name",target = "libraryName")

    BookSlimWithLibraryAndStockDto bookToBookDto(Book book);
    List<BookSlimWithLibraryAndStockDto> booksToAllBooksDto(List<Book>books);
    List<Book> booksDtoToAllBooks(List<BookSlimWithLibraryAndStockDto>books);
    Book bookDtoToBook(BookSlimWithLibraryAndStockDto bookSlimWithLibraryAndStockDto);
    Stock bookDtoToStock(BookSlimWithLibraryAndStockDto bookSlimWithLibraryAndStockDto);
    Library bookDtoToLibrary(BookSlimWithLibraryAndStockDto bookSlimWithLibraryAndStockDto);


}
