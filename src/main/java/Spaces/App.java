package Spaces;




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

        Runnable getPlayers = new GetPlayers(allData);
        Thread getPlayersThread = new Thread(getPlayers);
        getPlayersThread.start();


        Runnable pullTwitter = new PullTwitter(allData);
        Thread pullTwitterThread = new Thread(pullTwitter);
        pullTwitterThread.start();
        //Runnable getYoutube = new GetYoutube();

        try {
            pullTwitterThread.join(); // Stream tweets only after REST call to twitter for initial tweets is complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stream tweets only after REST call to twitter for initial tweets is complete
        Runnable streamTwitter = new StreamTwitter(allData);
        Thread streamTwitterThread = new Thread(streamTwitter);
        streamTwitterThread.start();


        try {
            getPlayersThread.join();
            //pullTwitterThread.join();
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
