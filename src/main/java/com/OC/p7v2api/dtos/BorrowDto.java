package com.OC.p7v2api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BorrowDto {
    private Integer id;
    private Date startDate;
    private Date returnDate;
    private String username;
    private boolean alreadyExtended;
    private boolean isOutdated;
    private String bookTitle;
    private String bookAuthor;

}
