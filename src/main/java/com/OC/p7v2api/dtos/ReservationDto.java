package com.OC.p7v2api.dtos;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDto {
    private Integer id;
    private Date startDate;
    private Date endDate;
    private int reservationPosition;
    private String username;
    private String lastName;
    private String firstName;
    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private String libraryName;
    private String openingTime;

}
