package ru.glaizier.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "powerlifter",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"powerlifter_id"})})
public class Powerlifter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "powerlifter_id", nullable = false, unique = true)
    private int powerlifterId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "sex")
    private short sex;

    @Column(name = "birthdate")
    @Type(type = "date")
    private Date birthdate;

    @Column(name = "country_id")
    private int countryId;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public int getPowerlifterId() {
        return powerlifterId;
    }

    public void setPowerlifterId(int powerlifterId) {
        this.powerlifterId = powerlifterId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public short getSex() {
        return sex;
    }

    public void setSex(short sex) {
        this.sex = sex;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City cityId) {
        this.city = cityId;
    }
}

