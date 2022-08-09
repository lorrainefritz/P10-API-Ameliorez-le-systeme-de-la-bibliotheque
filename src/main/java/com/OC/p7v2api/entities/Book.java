package com.OC.p7v2api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter @AllArgsConstructor
@Table(name = "BOOK")
public class Book {
    @Id
    @Column(name="BOOK_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="TITLE")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String title;

    @Column(name="AUTHOR")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String author;

    @Column(name="TYPE")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String type;

    @Column(name="SUMMARY")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String summary;


    @Column(name="PUBLISHER")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String publisher;

    @Column(name="LANGUAGE")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String language;

    @Column(name="SHELVE")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String shelve;



    @Column(name="CREATION_DATE")
    private String creationDate;


    @Column (name = "COVER")
    private String cover;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LIBRARY_ID",nullable = false)
    private Library library;

   @OneToOne
   @JoinColumn(name = "STOCK_ID")
    private Stock stock;

    @OneToMany (fetch = FetchType.LAZY, mappedBy="book")
    private List<Borrow> borrows;

    @OneToMany (fetch = FetchType.LAZY, mappedBy="book")
    private List<Reservation> reservations;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="NEAREST_RETURN_DATE")
    private Date nearestReturnDate;

    @Column(name="NUMBER_OF_RESERVATION")
    private int numberOfReservation;

    @Column(name="MAX_RESERVATION_LIST_SIZE")
    private int maxReservationListSize;


}
