package com.example.sailboatsapp.domain.offer.repository;

import com.example.sailboatsapp.domain.offer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findAllByOwnerId(Long ownerId);

    List<Offer> findByBoatId(Long boatId);

}
