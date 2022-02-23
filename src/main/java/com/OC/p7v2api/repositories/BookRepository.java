package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BookRepository extends JpaRepository<Book,Integer> {
    @Query(value = "SELECT * FROM book book WHERE CONCAT (book.title, book.author, book.type) LIKE %?1%", nativeQuery=true)
    List<Book> findBooksByKeyword(@Param("keyword")String keyword);
/*
 List<Book> findByTitleContaining(String title);
*/



}
