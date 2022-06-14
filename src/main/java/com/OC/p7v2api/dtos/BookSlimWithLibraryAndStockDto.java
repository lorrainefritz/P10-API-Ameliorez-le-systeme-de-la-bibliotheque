package com.OC.p7v2api.dtos;

import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookSlimWithLibraryAndStockDto {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String type;
    private String summary;
    private int numberOfCopiesAvailable;
    private String libraryName;
    private Date nearestReturnDate;
    private int numberOfReservation;
    private int maxReservationListSize;

    /*public void addBorrow(BorrowDto borrowDto) {
        if (borrows == null) {
            borrows = new ArrayList<>();
        }

        borrows.add(borrowDto);
    }

    public void addReservation(ReservationDto reservationDto) {
        if (reservations == null) {
            reservations = new ArrayList<>();

        }
        reservations.add(reservationDto);
    }*/

}
