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

        Runnable getPlayers = new GetPlayers();
        Thread getPlayersThread = new Thread(getPlayers);
        getPlayersThread.start();
        try {
            getPlayersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Runnable getTwitter = new GetTwitter();
        //Runnable getYoutube = new GetYoutube();

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
