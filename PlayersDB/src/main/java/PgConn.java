package Spaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;




public class PgConn {

    public Connection connection; //No encapsulation for now

    public PgConn() {
        System.out.println("Now the PG part ...");

        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Found!");

        connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/mydb", "postgres",
                    "postgres");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectRecordsFromTable(String selectSQL) throws SQLException {

        PreparedStatement preparedStatement = null;

        //String selectSQL = "SELECT * FROM PLAYERS;";

        try {
            preparedStatement = connection.prepareStatement(selectSQL);

            // execute select SQL stetement
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                String name = rs.getString("name");
                String fclub = rs.getString("formerclub");
                //String fclub = rs.getString("age");

                System.out.println();
                System.out.println("name : " + name);
                System.out.println("formerclub : " + fclub);

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            /*
            if (connection != null) {
                connection.close();
            }
            */

        }
    }

    public void insertRecordsIntoTable(String selectSQL) throws SQLException {

        PreparedStatement preparedStatement = null;

        //String selectSQL = "SELECT * FROM PLAYERS;";

        try {
            preparedStatement = connection.prepareStatement(selectSQL);

            // execute select SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into DBUSER table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            /*
            if (connection != null) {
                connection.close();
            }
            */

        }



    }

}
