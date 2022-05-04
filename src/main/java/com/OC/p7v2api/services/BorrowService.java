package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Borrow;
import com.OC.p7v2api.repositories.BorrowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class BorrowService {
    public final BorrowRepository borrowRepository;

    public List<Borrow> findAllBorrows(){
        log.info("in BorrowService in findAllBorrows method");
        return borrowRepository.findAll();
    }
    public Borrow findABorrowById(Integer id){
        log.info("in BorrowService in findABorrowById method");
        return borrowRepository.getById(id);
    }

    public Borrow saveABorrow(Borrow borrow){
        log.info("in BorrowService in saveABorrow method");
        return borrowRepository.save(borrow);
    }

    public void deleteABorrowById(Integer id){
        log.info("in BorrowService in deleteABorrowById method");
        borrowRepository.deleteById(id);
    }

}
