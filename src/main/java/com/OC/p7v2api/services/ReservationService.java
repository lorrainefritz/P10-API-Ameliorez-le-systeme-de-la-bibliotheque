package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Reservation;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookService bookService;
    private final UserService userService;

    public List<Reservation> findAllReservations() {
        log.info("in ReservationService in findAllReservations method");
        return reservationRepository.findAll();
    }

    public Reservation getAReservationById(Integer id) throws Exception {
        log.info("in ReservationService in getAReservationById method");
        if (id==null){
            log.info("in ReservationService in getAReservationById method where id is null");
            throw new Exception("Id can't be null");
        }
        return reservationRepository.getById(id);
    }

    public Reservation saveAReservation(Reservation reservation) throws Exception {
        if (reservation==null){
            log.info("in ReservationService in saveAReservation method where reservation is null");
            throw new Exception("Reservation can't be null");
        }
        return reservationRepository.save(reservation);
    }

    public void deleteAReservation(Reservation reservation) throws Exception {
        if (reservation==null){
            log.info("in ReservationService in deleteAReservation method where id is null");
            throw new Exception("Reservation can't be null");
        }
        log.info("in ReservationService in deleteAReservation method");
        reservationRepository.delete(reservation);
    }


    public void deleteAReservationById(Integer id) throws Exception {
        log.info("in ReservationService in deleteAReservationById method");
        if (id==null){
            log.info("in ReservationService in deleteAReservationById method where id is null");
            throw new Exception("Id can't be null");
        }
        Reservation reservation = getAReservationById(id);
        Book book = reservation.getBook();
        //Sort the list of reservations by ascending Ids
        List<Reservation> reservationListForTheCurrentBook = bookService.getAscendingSortedReservations(book);

        int positionOfTheCurrentReservationInListForTheCurrentBook = reservationListForTheCurrentBook.indexOf(reservation);
        log.info("in ReservationService in deleteAReservationById method where reservationListForTheCurrentBook size is {} and positionOfTheCurrentReservationInListForTheCurrentBook is {}", reservationListForTheCurrentBook.size(), positionOfTheCurrentReservationInListForTheCurrentBook);

        //case 1) the reservation is the only element of the list

        if (reservationListForTheCurrentBook.size() == 1) {
            log.info("in ReservationService in deleteAReservationById method in reservationListForTheCurrentBook size =1");

            deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book);

        } else {
            //case 2) the reservation is the first element of the list
            if (reservationListForTheCurrentBook.indexOf(reservation) == 0) {
                log.info("in ReservationService in deleteAReservationById method in reservation is the first of the list");
                for (Reservation currentReservation : reservationListForTheCurrentBook) {
                    log.info("in ReservationService in deleteAReservationById method in in reservation is the first of the list in for each where currentReservation id is {} and reservationPositionIs {}", currentReservation.getId(), currentReservation.getReservationPosition());
                    if (currentReservation.getReservationPosition() > 1) {
                        currentReservation.setReservationPosition(currentReservation.getReservationPosition() - 1);
                        log.info("in ReservationService in deleteAReservationById method in in reservation is the first of the list in for each after changin reservation position where currentReservation id is {} and reservationPositionIs {}", currentReservation.getId(), currentReservation.getReservationPosition());
                        saveAReservation(currentReservation);
                    }
                }
                deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book);


            }
            //case 3) the reservation is the last element of the list
            else if (positionOfTheCurrentReservationInListForTheCurrentBook == (reservationListForTheCurrentBook.size() - 1)) {
                log.info("in ReservationService in deleteAReservationById method in reservation is the last of the list");
                deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book);


            }
            //case 4) the reservation is somewhere in between
            else {
                log.info("in ReservationService in deleteAReservationById method in reservation is somewhere in between");
                for (Reservation currentReservation : reservationListForTheCurrentBook) {
                    log.info("in ReservationService in deleteAReservationById method in reservation is somewhere in between where currentReservation id is {} and reservationPositionIs {}", currentReservation.getId(), currentReservation.getReservationPosition());
                    if (currentReservation.getReservationPosition() > reservation.getReservationPosition()) {
                        log.info("in ReservationService in deleteAReservationById method in reservation is somewhere in between for each where currentReservation id is {} and reservationPositionIs {}", currentReservation.getId(), currentReservation.getReservationPosition());
                        currentReservation.setReservationPosition(currentReservation.getReservationPosition() - 1);
                        log.info("in ReservationService in deleteAReservationById method in reservation is somewhere in between for each after changin reservation position where currentReservation id is {} and reservationPositionIs {}", currentReservation.getId(), currentReservation.getReservationPosition());
                        saveAReservation(currentReservation);
                    }
                }

                deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book);

            }
        }
    }

    private void deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(Reservation reservation, Book book) throws Exception {
        log.info("in ReservationService in deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate method");
        if (reservation==null||book==null){
            log.info("in ReservationService in findReservationByBookId method where reservation or book are null");
            throw new Exception("Reservation and book can't be null");
        }

        book.setNumberOfReservation(book.getNumberOfReservation() - 1);

        reservationRepository.deleteById(reservation.getId());
        book.getReservations().remove(reservation);
        bookService.saveABook(book);
    }

    public Reservation makeAReservation(Integer bookId, Integer userId) throws Exception {
        log.info("in ReservationService in makeAReservation method");
        if (bookId==null||userId==null){
            log.info("in ReservationService in makeAreservation method where bookId userUd are null");
            throw new Exception("ReservationId and userId can't be null");
        }
        Book book = bookService.getABookById(bookId);
        User user = userService.getAUserById(userId);
        Date startDate = Date.from(Instant.now());

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setStartDate(startDate);

        reservation.setReservationPosition(book.getNumberOfReservation()+1);
        saveAReservation(reservation);
        book.setNumberOfReservation(book.getNumberOfReservation()+1);
        bookService.saveABook(book);
        return reservation;
    }


}
