package com.example.sailboatsapp.domain.user;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import com.example.sailboatsapp.domain.offer.Offer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @NotBlank(message = "Imię nie może być puste ani składać się z samych białych znaków.")
    private String name;
    @NotBlank(message = "Nazwisko nie może być puste ani składać się z samych białych znaków.")
    private String surname;
    @Column(name = "phone_number")
    @Pattern(regexp = "\\d{9}", message = "Numer telefonu musi składać się z 9 cyfr.")
    private String phoneNumber;
    @Column(name = "is_company")
    private Boolean isCompany;
    @Column(name = "company_name")
    private String companyName;
    private String tin;
    private String address;
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;
    @Column(name = "confirmation_code")
    private String confirmationCode;
    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @OneToMany(mappedBy = "ownerId")
    private Set<Boat> boats;

    @OneToMany(mappedBy = "ownerId")
    private Set<Offer> offers;

}
