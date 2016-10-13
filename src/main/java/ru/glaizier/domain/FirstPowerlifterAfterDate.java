package ru.glaizier.domain;

import java.util.Date;

public class FirstPowerlifterAfterDate {

    private final int id;

    private final String lastName;

    private final String firstName;

    private final Sex sex;

    private final Date birthdate;

    private final String birthplace;

    public FirstPowerlifterAfterDate(int id, String lastName, String firstName, int sexIsoCode, Date birthdate, String birthplace) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.sex = Sex.isoCodeToEnum(sexIsoCode);
        this.birthdate = birthdate;
        this.birthplace = birthplace;
    }

    public int getId() {
        return id;
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

    public String getBirthplace() {
        return birthplace;
    }
}
