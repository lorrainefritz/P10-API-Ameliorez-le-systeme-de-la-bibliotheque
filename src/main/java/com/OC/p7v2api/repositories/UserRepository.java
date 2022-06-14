package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
   /* @Query(value = "SELECT Borrow  FROM user" )
    List<Borrow> fetchAllBorrowsFromUser(User user);*/
}
