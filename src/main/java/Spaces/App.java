package Spaces;


import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

            System.out.println("Entry point.");
            PgConn pgC = new PgConn();

        try {

            pgC.insertRecordsIntoTable("INSERT INTO PLAYERS (name, formerclub, age) VALUES ('DAVID DE GEA', 'Athletico Madrid', 25);");

            pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");




        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
