package com.example.sailboatsapp.domain.boat.repository;

import com.example.sailboatsapp.domain.boat.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {

    Collection<Boat> findAllByOwnerId(Long ownerId);
}
