package ru.glaizier.simple;

import ru.glaizier.util.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class SimpleConnection {

    private Connection connection;

    public SimpleConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(Utils.getDbUrl(), Utils.getDbUserName(), Utils.getDbPassword());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        System.out.println("Opened database successfully");
    }

    public Connection getConnection() {
        return connection;
    }
}
