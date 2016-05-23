package ru.glaizier.pool.postgres;

import org.postgresql.ds.PGPoolingDataSource;
import ru.glaizier.domain.BiggestExercise;

import javax.sql.DataSource;
import java.sql.*;

public class PostgresConnectionPool {

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

    public BiggestExercise getBiggestSquat() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "select last_name, first_name, sex, birthdate, result_kg as max_squat_result\n" +
                             "from exercise_result\n" +
                             "join competition_result\n" +
                             "using (competition_result_id)\n" +
                             "join powerlifter\n" +
                             "using (powerlifter_id)\n" +
                             "where exercise_result_id = \n" +
                             "  (select exercise_result_id\n" +
                             "  from exercise_result\n" +
                             "  join competition_result\n" +
                             "  using (competition_result_id)\n" +
                             "  join exercise\n" +
                             "  using (exercise_id)\n" +
                             "  join powerlifter\n" +
                             "  using (powerlifter_id)\n" +
                             "  where last_name = 'Power500'\n" +
                             "    and exercise_name = 'Squat'\n" +
                             "  order by result_kg desc\n" +
                             "  limit 1);")) {
            if (rs.next()) {
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                int sex = rs.getInt("sex");
                Date birthdate = rs.getDate("birthdate");
                int resultInKg = rs.getInt("max_squat_result");
                return new BiggestExercise(lastName, firstName, sex, birthdate, resultInKg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
