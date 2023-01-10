package com.example.telegrambot.repository;

import com.example.telegrambot.dto.GasStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GasStationRepository extends CrudRepository<GasStation, Long> {

    @Query("select latitude from GasStation where id = :id")
    double findByIdX(Long id);

    @Query("select longitude from GasStation where id = :id")
    double findByIdIY(Long id);

}
