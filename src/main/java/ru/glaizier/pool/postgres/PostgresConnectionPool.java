package ru.glaizier.pool.postgres;

import org.postgresql.ds.PGPoolingDataSource;
import ru.glaizier.dao.BiggestSquatDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresConnectionPool extends BiggestSquatDao {

    private DataSource dataSource;

    public PostgresConnectionPool(String dbServerName, String dbName, String dbUserName, String dbPassword) {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setDataSourceName("postgresConnectionPool");
        dataSource.setServerName(dbServerName);
        dataSource.setDatabaseName(dbName);
        dataSource.setUser(dbUserName);
        dataSource.setPassword(dbPassword);
        dataSource.setInitialConnections(4);
        dataSource.setMaxConnections(10);
        this.dataSource = dataSource;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
