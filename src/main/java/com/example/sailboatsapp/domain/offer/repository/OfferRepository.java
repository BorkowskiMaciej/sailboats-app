package com.example.sailboatsapp.domain.offer.repository;

import com.example.sailboatsapp.domain.offer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
