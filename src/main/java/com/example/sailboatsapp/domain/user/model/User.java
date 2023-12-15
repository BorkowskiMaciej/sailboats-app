package com.example.sailboatsapp.domain.user.model;

import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.offer.model.Offer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Nazwa użytkownika nie może być pusta.")
    private String username;
    @Size(min = 8, message = "Hasło musi miec co najmniej 8 znaków.")
    @Size(max = 30, message = "Hasło nie może mie więcej niż 30 znaków.")
    private String password;
    @NotBlank(message = "Nazwa użytkownika nie może być pusta.")
    private String name;
    @NotEmpty(message = "Nazwa użytkownika nie może być pusta.")
    private String surname;
    @NotEmpty
    @Column(name = "phone_number")
    private String phoneNumber;
    @NotEmpty
    private String email;
    @NotEmpty
    private String role;
    @Column(name = "is_company")
    private Boolean isCompany;
    @Column(name = "company_name")
    private String companyName;
    private String tin;
    private String address;

    @OneToMany(mappedBy = "owner")
    private Set<Boat> boats;

    @OneToMany(mappedBy = "owner")
    private Set<Offer> offers;

}
