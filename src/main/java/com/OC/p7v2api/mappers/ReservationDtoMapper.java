package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
import com.OC.p7v2api.dtos.ReservationDto;
import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Reservation;
import com.OC.p7v2api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationDtoMapper {

    @Mapping(source = "reservation.id",target = "id")
    @Mapping(source = "reservation.startDate",target = "startDate")
    @Mapping(source = "reservation.endDate",target = "endDate")
    @Mapping(source = "reservation.reservationPosition",target = "reservationPosition")
    @Mapping(source="user.username",target ="username")
    @Mapping(source="user.firstName",target ="firstName")
    @Mapping(source="user.lastName",target ="lastName")
    @Mapping(source = "book.id",target = "bookId")
    @Mapping(source = "book.title",target = "bookTitle")
    @Mapping(source = "book.author",target = "bookAuthor")
    @Mapping(source = "book.library.name",target = "libraryName")
    @Mapping(source = "book.library.openingTime",target = "openingTime")
    ReservationDto reservationToReservationDto(Reservation reservation);
    List<ReservationDto> reservationsToAllReservationDto(List<Reservation>reservations);

}
