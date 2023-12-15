package com.example.sailboatsapp.domain.boat.model;

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

}
