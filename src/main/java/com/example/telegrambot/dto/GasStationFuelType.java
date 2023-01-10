package com.example.telegrambot.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "gas_station_fuel_type")
public class GasStationFuelType {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gas_station")
    private GasStation gasStation;

    @Column(name = "price_ai_92")
    private double Price_AI_92;

    @Column(name = "price_ai_95")
    private double Price_AI_95;

    @Column(name = "price_ai_98")
    private double Price_AI_98;

    @Column(name = "price_ai_100")
    private double Price_AI_100;

    @Column(name = "price_dt")
    private double Price_dt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GasStationFuelType that = (GasStationFuelType) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
