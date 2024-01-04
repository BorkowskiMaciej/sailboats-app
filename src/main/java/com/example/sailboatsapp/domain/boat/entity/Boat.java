package com.example.sailboatsapp.domain.boat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.hibernate.validator.constraints.Length;

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

    @NotBlank(message = "Nazwa nie może być pusta ani składać się z samych białych znaków")
    @Length(max = 30, message = "Nazwa nie może być dłuższa niż 30 znaków")
    private String name;
    @NotNull(message = "Typ nie może być pusty")
    private BoatType type;
    @NotBlank(message = "Model nie może być pusty ani składać się z samych białych znaków")
    @Length(max = 30, message = "Model nie może być dłuższy niż 30 znaków")
    private String model;
    @Min(value = 1, message = "Minimalna liczba osób to 1")
    @Max(value = 100, message = "Maksymalna liczba osób to 100")
    private Integer maxHeadcount;
    @Min(value = 1, message = "Minimalna liczba kabin to 1")
    @Max(value = 10, message = "Maksymalna liczba kabin to 10")
    private Integer cabinsNumber;
    @Min(value = 1900, message = "Rok produkcji nie może być mniejszy niż 1900")
    @Max(value = 2023, message = "Rok produkcji nie może być większy niż 2023")
    private Integer prodYear;
    @Min(value = 1, message = "Minimalna moc silnika to 1 KM")
    @Max(value = 1000, message = "Maksymalna moc silnika to 1000 KM")
    private Integer enginePower;
    @Column(name = "owner_id")
    private Long ownerId;
    @JdbcType(VarbinaryJdbcType.class)
    @Column(name = "image", columnDefinition="bytea")
    private byte[] image;
    @Column(name = "image_name")
    private String imageName;

}
