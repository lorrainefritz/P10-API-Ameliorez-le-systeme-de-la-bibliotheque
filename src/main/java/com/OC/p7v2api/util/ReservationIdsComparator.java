package com.OC.p7v2api.util;

import com.OC.p7v2api.entities.Reservation;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
@Log4j2
public class ReservationIdsComparator implements Comparator <Reservation> {

    public int compare(Reservation r1, Reservation r2){
        log.info("in ReservationIdsComparator in compare => ascending order");
        //sort by ascending order
        return r1.getId()-r2.getId();
    }


}
