package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);

}
