package com.example.sailboatsapp.domain.boat.repository;

import com.example.sailboatsapp.domain.boat.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {

}
