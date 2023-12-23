package com.example.sailboatsapp.domain.reservation.model;

import com.example.sailboatsapp.domain.offer.model.Offer;
import com.example.sailboatsapp.domain.user.model.AppUser;
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

    @Column(name = "offer_id")
    private Long offerId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Transient
    private Offer offer;

    @Transient
    private AppUser tenant;

}
