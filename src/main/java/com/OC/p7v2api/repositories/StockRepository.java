package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StockRepository extends JpaRepository<Stock,Integer> {
}
