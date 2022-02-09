package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LibraryRepository extends JpaRepository <Library,Integer> {
}
