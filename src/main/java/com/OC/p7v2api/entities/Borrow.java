package com.OC.p7v2api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "BORROW")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
public class Borrow {

    @Id
    @Column(name="BORROW_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="RETURN_DATE")
    private Date returnDate;

    @Column(name="ALREADY_EXTENDED")
    private boolean alreadyExtended;

    @Column(name="IS_OUTDATED")
    private boolean IsOutdated;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;


}