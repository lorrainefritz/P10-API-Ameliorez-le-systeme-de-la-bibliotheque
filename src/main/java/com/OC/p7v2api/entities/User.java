package com.OC.p7v2api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "APPUSER")
public class User  {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "USERNAME")
    @Size(max = 65, message = "65 charactères maximum")
    @NotBlank(message = "Ce champ ne doit pas être vide")
    private String username;

    @Column(name = "FIRST_NAME")
    @Size(max = 65, message = "65 charactères maximum")
    @NotBlank(message = "Ce champ ne doit pas être vide")
    private String firstName;

    @Column(name = "LAST_NAME")
    @Size(max = 65, message = "65 charactères maximum")
    @NotBlank(message = "Ce champ ne doit pas être vide")
    private String lastName;

    @Column(name = "ADDRESS")
    @Size(max = 65, message = "65 charactères maximum")
    @NotBlank(message = "Ce champ ne doit pas être vide")
    private String address;

    @Column(name = "PHONE")
    @Size(max = 65, message = "65 charactères maximum")
    @NotBlank(message = "Ce champ ne doit pas être vide")
    private String phone;
    @Column(name = "PASSWORD")
    @Size(max = 65, message = "65 charactères maximum")
    @NotBlank(message = "Ce champ ne doit pas être vide")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "ID", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Borrow> borrows;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;
}
