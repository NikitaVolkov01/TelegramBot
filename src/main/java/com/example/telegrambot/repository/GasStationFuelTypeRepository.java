package com.example.telegrambot.repository;

import com.example.telegrambot.dto.GasStationFuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GasStationFuelTypeRepository extends JpaRepository<GasStationFuelType, Long> {

    @Query(value = "select gas_station.address from gas_station_fuel_type join gas_station " +
            "on gas_station.id = gas_station_fuel_type.id_gas_station where gas_station_fuel_type.price_ai_92 = 44.3", nativeQuery = true)
    String getByAddress92();

    @Query(value = "select gas_station.address from gas_station_fuel_type join gas_station " +
            "on gas_station.id = gas_station_fuel_type.id_gas_station where gas_station_fuel_type.price_ai_95 = 48.4", nativeQuery = true)
    String getByAddress95();

    @Query(value = "select gas_station.address from gas_station_fuel_type join gas_station " +
            "on gas_station.id = gas_station_fuel_type.id_gas_station where gas_station_fuel_type.price_ai_98 = 61.9", nativeQuery = true)
    String getByAddress98();

    @Query(value = "select gas_station.address from gas_station_fuel_type join gas_station " +
            "on gas_station.id = gas_station_fuel_type.id_gas_station where gas_station_fuel_type.price_ai_100 = 60.07", nativeQuery = true)
    String getByAddress100();

    @Query(value = "select gas_station.address from gas_station_fuel_type join gas_station " +
            "on gas_station.id = gas_station_fuel_type.id_gas_station where gas_station_fuel_type.price_dt = 52.69", nativeQuery = true)
    String getByAddressDT();

}
