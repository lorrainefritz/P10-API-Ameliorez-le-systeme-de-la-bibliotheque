package com.OC.p7v2api.mappers;

import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.dtos.RoleDto;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleDtoMapper {
    @Mapping(source = "role.id",target = "id")
    @Mapping(source = "role.name",target = "name")
    RoleDto roleToRoleDto(Role role);
    List<RoleDto> roleToAllRoleDto(List<Role> roles);
    Role roleDtoToRole(RoleDto roleDto);

}
