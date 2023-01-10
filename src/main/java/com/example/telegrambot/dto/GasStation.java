package com.example.telegrambot.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;



@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "gas_station")
public class GasStation {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "address")
    private String address;

    @OneToOne(mappedBy = "gasStation", fetch = FetchType.EAGER)
    private GasStationFuelType gasStationFuelType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GasStation that = (GasStation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
