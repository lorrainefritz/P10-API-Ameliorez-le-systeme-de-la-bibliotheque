package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BorrowRepository extends JpaRepository<Borrow,Integer> {
    List<Borrow> findByBook(Book book);

/*
    @Query("FROM Borrow b WHERE b.user = :user")
    List<Borrow> findByUser(@Param("user") User user);
*/

   /* @Query(value = "SELECT b FROM Borrow b JOIN b.user.id=:userId")
    List<Borrow> findBorrowsByUserId(@Param("userId")Integer userId);*/
}
