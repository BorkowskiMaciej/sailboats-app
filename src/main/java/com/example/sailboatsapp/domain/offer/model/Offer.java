package com.example.sailboatsapp.domain.offer.model;

import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.user.model.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "offer")
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "boat_id")
    private Long boatId;

    @Column(name = "owner_id")
    private Long ownerId;

    @NotBlank(message = "Nazwa portu nie może być pusta")
    private String port;
    @Min(value = 0, message = "Cena nie może być ujemna")
    private Integer price;
    @Min(value = 0, message = "Kaucja nie może być ujemna")
    private Integer deposit;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotBlank(message = "Opis nie może być pusty")
    private String description;

    @Transient
    private Boat boat;

    @Transient
    private AppUser owner;

}
