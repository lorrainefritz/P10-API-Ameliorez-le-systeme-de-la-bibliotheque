package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
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

    public Reservation getAReservationById(Integer id) {
        log.info("in ReservationService in getAReservationById method");
        return reservationRepository.getById(id);
    }

    public Reservation saveAReservation(Reservation reservation) {
        log.info("in ReservationService in saveAReservation method");
        return reservationRepository.save(reservation);
    }

    public void deleteAReservation(Reservation reservation) {
        log.info("in ReservationService in deleteAReservation method");
        reservationRepository.delete(reservation);
    }

    public List<Reservation> findReservationsByBookId(Integer id) {
        log.info("in ReservationService in findReservationsByBookId method");
        Book book = bookService.getABookById(id);
        return reservationRepository.findReservationsByBook(book);
    }

    public List<Reservation> findReservationsByBook(Book book) {
        log.info("in ReservationService in findReservationsByBookId method");
        return reservationRepository.findReservationsByBook(book);
    }

    public void deleteAReservationById(Integer reservationId) {
        log.info("in ReservationService in deleteAReservationById method");
        Reservation reservation = getAReservationById(reservationId);
        Book book = reservation.getBook();
        //Sort the list of reservations by ascending Ids
        List<Reservation> reservationListForTheCurrentBook = bookService.getAscendingSortedReservations(book);
        // Sort the borrows for this book

       /* List<Borrow> borrowsForTheCurrentBook = null;
        Date returnDate = null;*/
        /* try {
           borrowsForTheCurrentBook = bookService.getAscendingSortedBorrows(book);  
      } catch (Exception e){
          log.info("in ReservationService in deleteAReservationById method where borrowForTheCurrentBook Are Null");
      }
        // Retrieve the first returnDate of the list
        

            if (borrowsForTheCurrentBook!=null){
                returnDate = borrowsForTheCurrentBook.get(0).getReturnDate();
            }*/
            
        int positionOfTheCurrentReservationInListForTheCurrentBook = reservationListForTheCurrentBook.indexOf(reservation);
        log.info("in ReservationService in deleteAReservationById method where reservationListForTheCurrentBook size is {} and positionOfTheCurrentReservationInListForTheCurrentBook is {}", reservationListForTheCurrentBook.size(), positionOfTheCurrentReservationInListForTheCurrentBook);

        //case 1) the reservation is the only element of the list

        if (reservationListForTheCurrentBook.size() == 1) {
            log.info("in ReservationService in deleteAReservationById method in reservationListForTheCurrentBook size =1");
            /*reservationListForTheCurrentBook.remove(positionOfTheCurrentReservationInListForTheCurrentBook);*/
            /*deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book, returnDate);*/
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
                /*deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book, returnDate);*/

            }
            //case 3) the reservation is the last element of the list
            else if (positionOfTheCurrentReservationInListForTheCurrentBook == (reservationListForTheCurrentBook.size() - 1)) {
                log.info("in ReservationService in deleteAReservationById method in reservation is the last of the list");
                deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book);
                /*deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book, returnDate);*/

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
                /*deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book, returnDate);*/
                deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(reservation, book);

            }
        }
    }

    private void deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate(Reservation reservation, Book book) {
        log.info("in ReservationService in deleteAReservationAndUpdateBookForNumberOfReservationsAndEndDate method");
        book.setNumberOfReservation(book.getNumberOfReservation() - 1);
        /*book.setNearestReturnDate(returnDate);*/
        reservationRepository.deleteById(reservation.getId());
        book.getReservations().remove(reservation);
        bookService.saveABook(book);
    }

    public void makeAReservation(Integer bookId, Integer userId) {
        log.info("in ReservationService in makeAReservation method");
        Book book = bookService.getABookById(bookId);
        User user = userService.getAUserById(userId);
        Date startDate = Date.from(Instant.now());
        /*Date endDate = Date.from(Instant.now().plusSeconds(172800));//48h*/
        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setStartDate(startDate);
        /*reservation.setEndDate(endDate);*/
        reservation.setReservationPosition(book.getNumberOfReservation()+1);
        saveAReservation(reservation);
        book.setNumberOfReservation(book.getNumberOfReservation()+1);
        bookService.saveABook(book);
    }


}
