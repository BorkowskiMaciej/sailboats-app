package com.example.sailboatsapp.domain.offer.model;

import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.user.model.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name = "boat_id")
    private Boat boat;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser ownerId;

    private String port;
    private Integer price;
    private Integer deposit;
    private Date startDate;
    private Date endDate;
    private String description;

}
