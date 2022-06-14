package com.OC.p7v2api.repositories;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findReservationsByBook(Book book);
}
