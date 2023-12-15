package com.example.sailboatsapp.domain.boat.repository;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatRepository extends CrudRepository<Boat, Long> {

    Boat findByName(String name);

}
