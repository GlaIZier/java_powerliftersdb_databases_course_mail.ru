package ru.glaizier.simple;

import ru.glaizier.dao.BiggestSquatDao;
import ru.glaizier.domain.BiggestExercise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnection extends BiggestSquatDao {

    private final String dbUrl;
    private final String dbUserName;
    private final String dbPassword;

    private Connection connection;

    private final Object lock = new Object();

    public SimpleConnection(String dbUrl, String dbUserName, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException(e);
        }
    }

    @Override
    public BiggestExercise getBiggestSquat() {
        synchronized (lock) {
            return super.getBiggestSquat();
        }
    }

}
