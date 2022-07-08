package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Book;
import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.entities.User;
import com.OC.p7v2api.repositories.BorrowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class BorrowService {
    public final BorrowRepository borrowRepository;
    public final BookService bookService;
    public final  UserService userService;

    public List<Borrow> findAllBorrows(){
        log.info("in BorrowService in findAllBorrows method");
        return borrowRepository.findAll();
    }
    public Borrow findABorrowById(Integer id) throws Exception {
        log.info("in BorrowService in findABorrowById method");
        if (id==null){
            log.info("in BorrowService in findABorrowById method where id is null");
            throw new Exception("Id can't be null");
        }
        return borrowRepository.getById(id);
    }

    public Borrow saveABorrow(@Valid Borrow borrow) throws Exception {
        log.info("in BorrowService in saveABorrow method");
        if (borrow==null){
            log.info("in BorrowService in saveABorrow method where borrow is null");
            throw new Exception("Borrow can't be null");
        }
        return borrowRepository.save(borrow);
    }

    public void deleteABorrow(@Valid Borrow borrow) throws Exception {
        if (borrow==null){
            log.info("in BorrowService in deleteABorrow method where borrow is null");
            throw new Exception("Borrow can't be null");
        }
        log.info("in BorrowService in deleteABorrow method");
        User user = borrow.getUser();
        List<Borrow> borrows = user.getBorrows();
        borrows.remove(borrow);
        user.setBorrows(borrows);
        userService.saveAUser(user);
        borrow.setBook(null);
        borrowRepository.delete(borrow);
    }

    /*public List<Borrow>findBorrowsByBookId(Integer id) throws Exception {
        log.info("in BorrowService in findBorrowsByBookId method");
        if (id==null){
            log.info("in BorrowService in findABorrowByBookId where id is null");
            throw new Exception("Invalid Id");
        }
        Book book = bookService.getABookById(id);
        return borrowRepository.findByBook(book);
    }

    public List<Borrow>findBorrowsByBook(Book book) throws Exception {
        log.info("in BorrowService in findBorrowsByBook method");
        if (book==null){
            log.info("in BorrowService in findABorrowById method where book is null");
            throw new Exception("book can't be null");
        }
        return borrowRepository.findByBook(book);
    }*/

    public Borrow extendABorrow(Integer id) throws Exception {
        log.info("in BorrowService in extendABorrow method where borrow id is {}", id);
        if (id==null){
            log.info("in BorrowService in extendABorrow method where id is null");
            throw new Exception("Id can't be null");
        }
        Borrow borrow = findABorrowById(id);
        log.info("in BorrowService in extendABorrow method where borrow book is {} and return date is {} before extension", borrow.getBook().getTitle(), borrow.getReturnDate());
        Date returnDate = borrow.getReturnDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(returnDate);
        cal.add(Calendar.DATE, 28);
        Date date = (Date) cal.getTime();
        borrow.setReturnDate(date);
        borrow.setAlreadyExtended(true);
        saveABorrow(borrow);
        log.info("in BorrowService in extendABorrow method where borrow book is {} and return date is {} after extension", borrow.getBook().getTitle(), borrow.getReturnDate());
        return borrow;
    }


}
