package com.OC.p7v2api.util;

import com.OC.p7v2api.entities.Book;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;

@Log4j2
public class BookIdComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        log.info("in BookIdComparator in compare => ascending order");
        //sort by ascending order
        return b1.getId()-b2.getId();
    }
}
