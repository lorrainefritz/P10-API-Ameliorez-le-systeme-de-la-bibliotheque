package com.OC.p7v2api.util;

import com.OC.p7v2api.entities.Borrow;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
@Log4j2
public class BorrowReturnDatesComparator implements Comparator<Borrow> {


    @Override
    public int compare(Borrow b1, Borrow b2) {
        log.info("in BorrowdatesComparator in compare => ascending order");
        //sort by ascending order
        if (b1.getReturnDate() == null || b2.getReturnDate() == null) {
            return 0;
        }
        return b1.getReturnDate().compareTo(b2.getReturnDate());
    }
}
