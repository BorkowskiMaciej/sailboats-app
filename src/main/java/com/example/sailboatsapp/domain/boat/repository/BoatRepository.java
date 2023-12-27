package com.example.sailboatsapp.domain.boat.repository;

import com.example.sailboatsapp.domain.boat.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {

    List<Boat> findAllByOwnerId(Long ownerId);

    boolean existsByName(String name);
}
