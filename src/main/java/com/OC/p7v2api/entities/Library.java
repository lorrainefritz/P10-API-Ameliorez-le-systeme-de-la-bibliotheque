package com.OC.p7v2api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Table(name = "LIBRARY")
@Entity
@NoArgsConstructor @Getter @Setter @AllArgsConstructor
public class Library {
    @Id
    @Column(name="LIBRARY_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="NAME")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String name;

    @Column(name="ADDRESS")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String address;

    @Column(name="EMAIL")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String email;

    @Column(name="PHONE")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String phone;

    @Column(name="OPENING")
    @Size(max=65, message="65 charactères maximum")
    @NotBlank(message="Ce champ ne doit pas être vide")
    private String openingTime;

    @OneToMany (mappedBy="library")
    private List<Book> books;
}