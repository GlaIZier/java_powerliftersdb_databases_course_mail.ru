package ru.glaizier.simple;

import ru.glaizier.simple.domain.BiggestExercise;

import java.sql.*;

public class SimpleConnection {

    private Connection connection;

    public SimpleConnection(String dbUrl, String dbUserName, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(Utils.getDbUrl(), Utils.getDbUserName(), Utils.getDbPassword());
            connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // TODO can i do such things?
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }

    public BiggestExercise getBiggestSquat() {
        try (Statement statement = connection.createStatement();
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

    public Connection getConnection() {
        return connection;
    }
}
