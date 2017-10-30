package Spaces;

/*


        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.lang.String;
        import java.util.*;


        import org.json.simple.JSONArray;
        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;
        import org.json.simple.parser.ParseException;

        import java.sql.SQLException;
*/
import twitter4j.*;
import twitter4j.auth.AccessToken;
import java.util.*;


public class PullTwitter implements Runnable {


    //Do a REST on Twitter to pull the first batch of tweets. Then rely on Stream API.

    private AllData allData;

    public PullTwitter(AllData allData) {
        this.allData = allData;

    }


    public void run() {

        //PgConn pgC = new PgConn();
        //String urlstring = "http://api.football-data.org/v1/teams/66/players";
        long sleepinterval = 30000; //30s

        try {
            Thread.sleep(sleepinterval); // go fetch again after sleepinterval
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //while (true) {
            Twitter twitter = new TwitterFactory().getInstance();
            // Twitter Consumer key & Consumer Secret
            twitter.setOAuthConsumer("fbRnO97AjQaHa7PIff1CIhjjG", "pHN0yxaFLcS5djvNTSL4VMv2iGaDSjDMeYwVMzZ5E8wwoGev9y");
            // Twitter Access token & Access token Secret
            twitter.setOAuthAccessToken(new AccessToken("17674248-WfNtcYK8mGlHpVsTB9oM6BRIYPU6lddvgxBuSSi2e",
                    "RI4Ixt3yUvQw7PeOg1opL1bBZ9XNdd4iS6XSqiXXm4Oan"));

            try {
                /*
                // Getting Twitter Timeline using Twitter4j API
                //ResponseList statusReponseList = twitter.getUserTimeline(new Paging(1, 5));
                List<Status> statusReponseList = twitter.getUserTimeline(new Paging(1, 5));
                for (twitter4j.Status status : statusReponseList) {
                    System.out.println(status.getText());
                }
                */
                //Twitter twitter = TwitterFactory.getSingleton();

                Set set = allData.clubsSignings.entrySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry)iterator.next();
                    //System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
                    System.out.print("Tweets from: "+ mentry.getKey());
                    //System.out.println(mentry.getValue());
                    //System.out.println(mentry.getKey() + "Players:");
                    ClubPlayers roster = (ClubPlayers) mentry.getValue();
                    Set<String> rosterPlayers = roster.clubPlayers;
                    //System.out.println(rosterPlayers);
                    Iterator<String> it = rosterPlayers.iterator();
                    while(it.hasNext()){
                        System.out.println(it.next());

                        Query query = new Query(it.next());
                        QueryResult result = twitter.search(query);
                        for (Status status : result.getTweets()) {
                            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                        }
                    }
                }
                /*
                Query query = new Query("Paul Pogba");
                QueryResult result = twitter.search(query);
                for (Status status : result.getTweets()) {
                    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                }

                System.out.println("Got something from (T)witter");
                */

            } catch (Exception e) {
            }


            try {
                Thread.sleep(sleepinterval); // go fetch again after sleepinterval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
    }

}
