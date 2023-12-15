package com.example.sailboatsapp.domain.reservation.model;

import com.example.sailboatsapp.domain.offer.model.Offer;
import com.example.sailboatsapp.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private User tenant;
}
