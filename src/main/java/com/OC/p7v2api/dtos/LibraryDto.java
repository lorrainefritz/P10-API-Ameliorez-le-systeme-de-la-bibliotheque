package com.OC.p7v2api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryDto {
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String openingTime;
}
