package com.OC.p7v2api.dtos;

import com.OC.p7v2api.entities.Borrow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
