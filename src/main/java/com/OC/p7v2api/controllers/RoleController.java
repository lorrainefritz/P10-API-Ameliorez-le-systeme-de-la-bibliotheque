package com.OC.p7v2api.controllers;

import com.OC.p7v2api.dtos.RoleDto;
import com.OC.p7v2api.entities.Role;
import com.OC.p7v2api.mappers.RoleDtoMapper;
import com.OC.p7v2api.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Log4j2
public class RoleController {
     private final RoleService roleService;
     private final RoleDtoMapper roleDtoMapper;

    @GetMapping(value = "/roles")
    public ResponseEntity<List<RoleDto>> findAllRoles(){
        log.info("HTTP GET request received at /roles with findAllRoles");
        return new ResponseEntity<>(roleDtoMapper.roleToAllRoleDto(roleService.findAllRoles()), HttpStatus.OK);
    }

    @PostMapping(value = "/roles")
    public ResponseEntity<RoleDto> saveARole(@RequestBody @Validated RoleDto roleDto, BindingResult bindingResult) throws Exception {
        log.info("HTTP POST request received at /roles with saveARole");
        if (roleDto == null) {
            log.info("HTTP POST request received at /roles with saveARole where roleDto is null");
            return new ResponseEntity<>(roleDto, HttpStatus.NO_CONTENT);
        }
        else if (bindingResult.hasErrors()){
            log.info("HTTP POST request received at /roles with saveARole where roleDto is not valid");
            return new ResponseEntity<>(roleDto, HttpStatus.FORBIDDEN);
        }
        else {
            roleService.saveARole(roleDtoMapper.roleDtoToRole(roleDto));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDto);
    }

    @DeleteMapping(value = "/roles/delete/{id}")
    public ResponseEntity deleteARole(@PathVariable Integer id) throws Exception {
        log.info("HTTP DELETE request received at /roles/delete/" + id + " with deleteARole");
        if (id == null) {
            log.info("HTTP DELETE request received at /roles/delete/id where id is null");
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        }
        Role role = roleService.getARoleById(id);
        roleService.deleteARole(role);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }



}
