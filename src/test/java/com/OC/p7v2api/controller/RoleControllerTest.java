package com.OC.p7v2api.controller;


import com.OC.p7v2api.controllers.LibraryController;
import com.OC.p7v2api.controllers .RoleController;
import com.OC.p7v2api.dtos.LibraryDto;
import com.OC.p7v2api.dtos.RoleDto;
import com.OC.p7v2api.entities.Library;
import com.OC.p7v2api.entities.Role;
import com.OC.p7v2api.mappers.LibraryDtoMapper;
import com.OC.p7v2api.mappers.RoleDtoMapper;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RoleController.class})
@ExtendWith(SpringExtension.class)
public class RoleControllerTest {
    @Autowired
    private RoleController roleController;

    @MockBean
    private RoleDtoMapper roleDtoMapper;

    @MockBean
    private RoleService roleService;

    @Test
    void checkFindAllStocks_ShouldReturnAnOkStatus() throws Exception {
        //GIVEN WHEN
        when(this.roleService.findAllRoles()).thenReturn(new ArrayList<>());
        when(this.roleDtoMapper.roleToAllRoleDto((java.util.List<Role>) any())).thenReturn(new ArrayList<>());
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/roles");
        MockMvcBuilders.standaloneSetup(this.roleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void checkSaveARole_shouldReturnAIsCreatedStatus() throws Exception {
        //GIVEN
        Role role1 = new Role(1,"ROLE_ADMIN",new ArrayList<>());
        Role role2 = new Role(2,"ROLE_ROLE",new ArrayList<>());
        RoleDto roleDto = new RoleDto(1,"ROLE_ADMIN");
        String roleDtoAsAStringValue = (new ObjectMapper()).writeValueAsString(roleDto);
        //WHEN
        when(this.roleService.saveARole((Role) any())).thenReturn(role1);
        when(this.roleDtoMapper.roleDtoToRole((RoleDto) any())).thenReturn(role2);
        //THEN
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleDtoAsAStringValue);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.roleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"ROLE_ADMIN\"}"));

    }


    @Test
    void checkDeleteARole_shouldReturnAIsAcceptedStatus() throws Exception {
        //GIVEN
        Role role1 = new Role(1,"ROLE_ADMIN",new ArrayList<>());
        //WHEN
        when(this.roleService.getARoleById((Integer) any())).thenReturn(role1);
        //THEN
        doNothing().when(this.roleService).deleteARole((Role) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/roles/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.roleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }

}
