package com.OC.p7v2api.service;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.Role;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.repositories.RoleRepository;
import com.OC.p7v2api.services.LibraryService;
import com.OC.p7v2api.services.RoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest  {
    private AutoCloseable autoCloseable;
    private RoleService roleServiceUnderTest;

    @Mock
    RoleRepository roleRepository;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        roleServiceUnderTest = new RoleService(roleRepository);

    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void checkFindAllRoles_shouldCallRoleRepository() {
        //GIVEN WHEN
        roleServiceUnderTest.findAllRoles();
        //THEN
        verify(roleRepository).findAll();
    }

    @Test
    public void checkFindARoleById_WhenIdIsValid_shouldCallRoleRepository() throws Exception {
        //GIVEN WHEN
        roleServiceUnderTest.getARoleById(1);

        //THEN
        verify(roleRepository).getById(1);
    }

    @Test
    public void checkFindARoleById_WhenIdIsNull_shouldThrowAnException() throws Exception {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            roleServiceUnderTest.getARoleById(null);
        }).withMessage("Id can't be null");
    }

    @Test
    public void checkSaveARole_WhenRoleIsValid_shouldCallRoleRepository() throws Exception {
        //GIVEN WHEN
        Role roleUndertest = new Role(1,"ADMIN",null);
        roleServiceUnderTest.saveARole(roleUndertest);
        //THEN
        verify(roleRepository).save(roleUndertest);
    }

    @Test
    public void checkSaveARole_WhenRoleIsNull_shouldThrowAnException() throws Exception {
        Role roleUndertest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            roleServiceUnderTest.saveARole(roleUndertest);
        }).withMessage("Role can't be null");
    }

    @Test
    public void checkDeleteARole_shouldCallRoleRepository() throws Exception {
        //GIVEN WHEN
        Role roleUndertest = new Role(1,"ADMIN",null);
        roleServiceUnderTest.deleteARole(roleUndertest);
        //THEN
        verify(roleRepository).delete(roleUndertest);
    }

    @Test
    public void checkDeleteARole_WhenRoleIsNull_ShouldThrowAnException() throws Exception {
        //GIVEN WHEN
        Role roleUndertest = null;
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            roleServiceUnderTest.deleteARole(roleUndertest);
        }).withMessage("Role can't be null");
    }
}
