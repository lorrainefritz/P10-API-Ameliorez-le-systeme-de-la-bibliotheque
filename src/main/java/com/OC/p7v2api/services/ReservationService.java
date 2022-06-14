package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Reservation;
import com.OC.p7v2api.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookService bookService;

    public List<Reservation> findAllReservations(){
        log.info("in ReservationService in findAllReservations method");
        return reservationRepository.findAll();
    }

    public Reservation getAReservationById (Integer id){
        log.info("in ReservationService in getAReservationById method");
        return reservationRepository.getById(id);
    }

    public Reservation saveAReservation(Reservation reservation){
        log.info("in ReservationService in saveAReservation method");
        return reservationRepository.save(reservation);
    }

    public void deleteAReservation(Reservation reservation){
        log.info("in ReservationService in deleteAReservation method");
        reservationRepository.delete(reservation);
    }
    public List<Reservation>findReservationsByBookId(Integer id){
        log.info("in ReservationService in findReservationsByBookId method");
        Book book = bookService.getABookById(id);
        return reservationRepository.findReservationsByBook(book);
    }

    public List<Reservation>findReservationsByBook(Book book){
        log.info("in ReservationService in findReservationsByBookId method");
        return reservationRepository.findReservationsByBook(book);
    }

    public void deleteAReservationById(Integer reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
