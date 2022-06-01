package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    @Mapping(source = "user.id",target = "id")
    @Mapping(source = "user.username",target = "username")
    @Mapping(source = "user.firstName",target = "firstName")
    @Mapping(source = "user.lastName",target = "lastName")
    @Mapping(source = "user.address",target = "address")
    @Mapping(source = "user.phone",target = "phone")
    UserDto userToUserDtoMapper(User user);
    User userDtoToUser(UserDto userDto);

}
