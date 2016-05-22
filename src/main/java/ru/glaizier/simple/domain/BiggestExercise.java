package ru.glaizier.simple.domain;

import java.util.Date;

public class BiggestExercise {

    private final String lastName;

    private final String firstName;

    private final int sex;

    private final Date birthday;

    private final int resultInKg;


    public BiggestExercise(String lastName, String firstName, int sex, Date birthday, int resultInKg) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.sex = sex;
        this.birthday = birthday;
        this.resultInKg = resultInKg;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getResultInKg() {
        return resultInKg;
    }
}
