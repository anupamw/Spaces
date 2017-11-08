package Spaces;


import java.io.IOException;



/**
 * Hello world!
 *
 */
public class App
{


    public static void main( String[] args ) {



        System.out.println("Entry point. This module fetches player data and writes to PG.");
        //

        AllData allData = new AllData();
        allData.pgC = new PgConn();
        System.out.println("Opened PG connection");


        Runnable getPlayers = new GetPlayers(allData);
        Thread getPlayersThread = new Thread(getPlayers);
        getPlayersThread.start();
        System.out.println("Updated Player database (not re-inserting to PG for now");
        //Todo: Change to insert into PG only on changes


        //Runnable pullTwitter = new PullTwitter(allData);
        //Thread pullTwitterThread = new Thread(pullTwitter);
        //pullTwitterThread.start();
        //Runnable getYoutube = new GetYoutube();

        //try {
        //pullTwitterThread.join(); // Stream tweets only after REST call to twitter for initial tweets is complete
        //System.out.println("Pulled initial set of tweets from players");
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        try {
            getPlayersThread.join(); //Todo: need to change to make this run and fetch infrequently for roster changes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*
            try {

                pgC.insertRecordsIntoTable("INSERT INTO PLAYERS (name, formerclub, age) VALUES ('DAVID DE GEA', 'Athletico Madrid', 25);");

                pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");

            } catch (SQLException e) {
                e.printStackTrace();
            }
*/
        //CallRestAPI.getREST("http://api.football-data.org/v1/teams/66/players");
    }

}
