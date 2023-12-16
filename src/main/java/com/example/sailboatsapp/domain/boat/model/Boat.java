package com.example.sailboatsapp.domain.boat.model;

import com.example.sailboatsapp.domain.user.model.AppUser;
import jakarta.persistence.*;
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

    private String name;
    private String type;
    private String model;
    private Integer maxHeadcount;
    private Integer cabinsNumber;
    private Integer prodYear;
    private Integer enginePower;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

}
