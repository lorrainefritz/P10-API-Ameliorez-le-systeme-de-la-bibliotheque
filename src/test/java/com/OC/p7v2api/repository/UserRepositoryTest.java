package com.OC.p7v2api.repository;

import com.OC.p7v2api.entities.Role;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.repositories.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepositoryUnderTest;

    @After
    void tearDown() {
        userRepositoryUnderTest.deleteAll();
    }

    @Test
    void checkFindByUserName_whenGivenAValidUsername_shouldReturnAUser() {
        Role role = new Role(1, "ROLE_USER", null);
        User user = new User(1, "paul@gmail.com", "Paul", "Atreid", "3 rue des fleurs 57000 METZ", "0388776655", "123", role, null, null);
        userRepositoryUnderTest.save(user);

        User userUnderTest = userRepositoryUnderTest.findByUsername("paul@gmail.com");
        assertThat(userUnderTest.getFirstName()).isEqualTo(user.getFirstName());
    }


}
