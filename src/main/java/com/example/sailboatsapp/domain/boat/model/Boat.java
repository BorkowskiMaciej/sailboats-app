package com.example.sailboatsapp.domain.boat.model;

import com.example.sailboatsapp.domain.user.model.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity(name = "boat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Boat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa nie może być pusta")
    private String name;
    @NotNull(message = "Typ nie może być pusty")
    private BoatType type;
    @NotBlank(message = "Model nie może być pusty")
    private String model;
    @Min(value = 1, message = "Minimalna liczba osób to 1")
    private Integer maxHeadcount;
    @Min(value = 1, message = "Minimalna liczba kabin to 1")
    private Integer cabinsNumber;
    @Min(value = 1900, message = "Rok produkcji nie może być mniejszy niż 1900")
    @Max(value = 2023, message = "Rok produkcji nie może być większy niż 2023")
    private Integer prodYear;
    @Min(value = 1, message = "Minimalna moc silnika to 1 KM")
    private Integer enginePower;
    @Column(name = "owner_id")
    private Long ownerId;

}
