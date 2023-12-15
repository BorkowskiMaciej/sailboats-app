package com.example.sailboatsapp.domain.user.model;

import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.offer.model.Offer;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "user")
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private Boolean isCompany;
    private String companyName;
    private String tin;
    private String address;
    private String role;

    @OneToMany(mappedBy = "owner")
    private Set<Boat> boats;

    @OneToMany(mappedBy = "owner")
    private Set<Offer> offers;

}
