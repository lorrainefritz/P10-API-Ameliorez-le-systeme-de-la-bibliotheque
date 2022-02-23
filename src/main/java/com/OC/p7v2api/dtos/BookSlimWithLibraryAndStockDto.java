package com.OC.p7v2api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
