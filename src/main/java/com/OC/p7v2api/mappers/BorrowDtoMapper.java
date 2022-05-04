package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.BorrowDto;
import com.OC.p7v2api.entities.Borrow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowDtoMapper {
    @Mapping(source = "borrow.id",target = "id")
    @Mapping(source = "borrow.startDate",target = "startDate")
    @Mapping(source = "borrow.returnDate",target = "returnDate")
    @Mapping(source = "borrow.alreadyExtended", target = "alreadyExtended")
    @Mapping(source="user.username",target ="username")
    @Mapping(source = "book.title",target = "bookTitle")
    @Mapping(source = "book.author",target = "bookAuthor")
    BorrowDto borrowToBorrowDtoMapper(Borrow borrow);
    List<BorrowDto> borrowToAllBorrowDto(List<Borrow> borrows);

}
