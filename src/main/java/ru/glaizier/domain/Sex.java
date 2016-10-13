package ru.glaizier.domain;

public enum Sex {
    UNKNOWN(0, "Unknown"),
    MALE(1, "Male"),
    FEMALE(2, "Female"),
    NOT_APLLICABLE(9, "NOT_APLLICABLE");

    private int isoCode;

    private String sex;

    Sex(int isoCode, String sex) {
        this.isoCode = isoCode;
        this.sex = sex;
    }

    public int getIsoCode() {
        return isoCode;
    }

    public String getSexName() {
        return sex;
    }

    public static Sex isoCodeToEnum(int isoCode) {
        switch (isoCode) {
            case 0:
                return UNKNOWN;
            case 1:
                return MALE;
            case 2:
                return FEMALE;
            default:
                return NOT_APLLICABLE;
        }

    }
}