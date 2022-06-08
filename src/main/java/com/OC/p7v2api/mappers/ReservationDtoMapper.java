package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.BookSlimWithLibraryAndStockDto;
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
    @Mapping(source = "user.username",target = "username")
    @Mapping(source = "book.title",target = "title")
    ReservationDtoMapper reservationToReservationDtoMapper(Reservation reservation);
    List<ReservationDtoMapper> reservationsToAllReservationDtoMapper(List<Reservation>reservations);
    Reservation reservationDtoMapperToReservation(ReservationDtoMapper reservationDtoMapper);
}
