package com.example.sailboatsapp.domain.boat.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Boat {

    @Id
    private Long id;
    private String name;
    private String type;

}
