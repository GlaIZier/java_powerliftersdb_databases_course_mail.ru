package ru.glaizier.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "city")
    private List<Powerlifter> powerlifters = new ArrayList<>();

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

    public List<Powerlifter> getPowerlifters() {
        return powerlifters;
    }

    public void setPowerlifters(List<Powerlifter> powerlifters) {
        this.powerlifters = powerlifters;
    }
}
