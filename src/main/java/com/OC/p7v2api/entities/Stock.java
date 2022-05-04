package com.OC.p7v2api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Table(name = "STOCK")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Stock {
    @Id
    @Column(name = "STOCK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NUMBER_AVAILABLE")
    private int numberOfCopiesAvailable;

    @Column(name = "NUMBER_OUT")
    private int numberOfCopiesOut;

    @Column(name = "TOTAL_COPIES")
    @Min(value = 0, message = "doit ête compris entre 1 et 10")
    @Max(value = 10, message = "doit ête compris entre 1 et 10")
    @NotNull(message = "Ce champ ne doit pas être vide")
    private int totalOfCopies;
    @Column(name = "BOOK_IS_AVAILABLE")
    private boolean bookIsAvailable;

    @OneToOne(mappedBy = "stock")
    private Book book;



}