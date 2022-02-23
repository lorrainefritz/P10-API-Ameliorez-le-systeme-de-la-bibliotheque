package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryDtoMapper {
    @Mapping(source = "library.id",target = "id")
    @Mapping(source = "library.name",target = "name")
    @Mapping(source = "library.address",target = "address")
    @Mapping(source = "library.email",target = "email")
    @Mapping(source = "library.phone",target = "phone")
    @Mapping(source = "library.openingTime",target = "openingTime")
    LibraryDto libraryToLibraryDto(Library library);
    List<LibraryDto> libraryToAllLibraryDto(List<Library>libraries);
}
