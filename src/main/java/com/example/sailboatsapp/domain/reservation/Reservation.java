package com.example.sailboatsapp.domain.reservation;

import com.example.sailboatsapp.domain.offer.Offer;
import com.example.sailboatsapp.domain.user.entity.AppUser;
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

    @Column(name = "landlord_id")
    private Long landlordId;

    @Transient
    private Offer offer;

    @Transient
    private AppUser tenant;

    @Transient
    private AppUser landlord;

}
