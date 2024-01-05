package com.example.sailboatsapp.domain.offer;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import com.example.sailboatsapp.domain.user.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    @Length(max = 30, message = "Nazwa portu nie może być dłuższa niż 30 znaków")
    private String port;
    @Min(value = 0, message = "Cena nie może być ujemna")
    @Max(value = 1000000, message = "Cena nie może być większa niż 1 000 000")
    private Integer price;
    @Min(value = 0, message = "Kaucja nie może być ujemna")
    @Max(value = 1000000, message = "Kaucja nie może być większa niż 1 000 000")
    private Integer deposit;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotBlank(message = "Opis nie może być pusty")
    @Length(max = 1000, message = "Opis nie może być dłuższy niż 5 000 znaków")
    private String description;

    @Transient
    private Boat boat;

    @Transient
    private AppUser owner;

}
