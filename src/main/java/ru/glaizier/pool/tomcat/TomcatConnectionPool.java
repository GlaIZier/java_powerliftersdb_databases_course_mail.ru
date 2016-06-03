package ru.glaizier.pool.tomcat;

import ru.glaizier.dao.BiggestSquatDao;
import ru.glaizier.util.Utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TomcatConnectionPool extends BiggestSquatDao {

    private DataSource dataSource;

    public TomcatConnectionPool() {
        dataSource = Utils.getDataSource();
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
