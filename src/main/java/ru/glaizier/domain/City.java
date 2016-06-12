package ru.glaizier.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="city",
        uniqueConstraints={@UniqueConstraint(columnNames={"city_id"})})
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="city_id", nullable=false, unique=true)
    private int cityId;

    @Column(name="city_name")
    private String cityName;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
