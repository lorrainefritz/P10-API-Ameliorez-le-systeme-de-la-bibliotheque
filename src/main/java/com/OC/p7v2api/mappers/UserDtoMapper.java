package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.UserDto;
import com.OC.p7v2api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    @Mapping(source = "user.id",target = "id")
    @Mapping(source = "user.username",target = "username")
    @Mapping(source = "role.name",target = "roleUser")
    UserDto userToUserDtoMapper(User user);
    User userDtoToUser(UserDto userDto);

}
