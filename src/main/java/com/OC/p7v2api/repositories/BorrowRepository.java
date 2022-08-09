package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BorrowRepository extends JpaRepository<Borrow,Integer> {



}
