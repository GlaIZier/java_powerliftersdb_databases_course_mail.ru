package ru.glaizier.domain;

import java.util.Date;

public class BiggestExercise {

    private final String lastName;

    private final String firstName;

    private final Sex sex;

    private final Date birthdate;

    private final int resultInKg;


    public BiggestExercise(String lastName, String firstName, int sexIsoCode, Date birthdate, int resultInKg) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.sex = Sex.isoCodeToEnum(sexIsoCode);
        this.birthdate = birthdate;
        this.resultInKg = resultInKg;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Sex getSex() {
        return sex;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public int getResultInKg() {
        return resultInKg;
    }
}
