package Spaces;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



/**
 * Hello world!
 *
 */
public class App 
{


    public static void main( String[] args ) {



        System.out.println("Entry point.");
        //

        AllData allData = new AllData();
        allData.pgC = new PgConn();
        System.out.println("Opened PG connection");
        allData.jC = new JedisConn();
        System.out.println("Opened connection pool to REDIS");

        System.out.println("Read api keys from file: api-keys.txt");
        //Expecting a file called api-keys.txt in current dir of .jar that has each of these
        // in 1 line each: twitterAPIKey, twitterAPISecret, twitterAccessToken, twitterAccessTockenSecret
        BufferedReader br = null;
        FileReader fr = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader("api-keys.txt");
            br = new BufferedReader(fr);

            String sCurrentLine;

            if ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                allData.twitterAPIKey = sCurrentLine;
            }
            if ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                allData.twitterAPISecret = sCurrentLine;
            }
            if ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                allData.twitterAccessToken = sCurrentLine;
            }
            if ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                allData.twitterAccessSecret = sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Runnable getPlayers = new GetPlayers(allData);
        Thread getPlayersThread = new Thread(getPlayers);
        getPlayersThread.start();
        System.out.println("Updated Player database (not re-inserting to PG for now");
        //Todo: Change to insert into PG only on changes


        Runnable pullTwitter = new PullTwitter(allData);
        Thread pullTwitterThread = new Thread(pullTwitter);
        pullTwitterThread.start();
        //Runnable getYoutube = new GetYoutube();

        try {
            pullTwitterThread.join(); // Stream tweets only after REST call to twitter for initial tweets is complete
            System.out.println("Pulled initial set of tweets from players");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stream tweets only after REST call to twitter for initial tweets is complete
        System.out.println("Now tuning to stream of tweets ...");
        Runnable streamTwitter = new StreamTwitter(allData);
        Thread streamTwitterThread = new Thread(streamTwitter);
        streamTwitterThread.start();


        try {
            getPlayersThread.join();
            streamTwitterThread.join();
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
