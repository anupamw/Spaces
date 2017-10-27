package Spaces;



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

public class GetPlayers implements Runnable {

        /*
    private PgConn pgC;

    public void GetPlayers(PgConn pgC) {
        this.pgC = pgC;
    }
*/
    public void run() {

        PgConn pgC = new PgConn();
        String urlstring = "http://api.football-data.org/v1/teams/66/players";
        long sleepinterval = 30000; //30s

        while (true) {


            try {


                System.out.println("Use REST to fetch player info from: " + urlstring);
                //URL url = new URL("http://api.football-data.org/v1/teams/66/players");
                URL url = new URL(urlstring);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                String json;
                JSONObject jobj;
                JSONParser jsonParser;
                JSONArray msg;
                String player, position, country, dob;
                String sqlstring;
                Long jersey;

                jsonParser = new JSONParser();

                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                    System.out.println();


                    try {
                        json = output.trim();
                        jobj = (JSONObject) jsonParser.parse(json);

                        Long count = (Long) jobj.get("count");
                        System.out.println(count); //print for debug HTTP response.

                        // loop array
                        msg = (JSONArray) jobj.get("players");
                        Iterator<JSONObject> iterator = msg.iterator();
                        if (iterator != null)
                            while (iterator.hasNext()) {
                                jobj = (JSONObject) iterator.next();
                                player = (String) jobj.get("name");
                                System.out.println(player);

                                position = (String) jobj.get("position");
                                System.out.println(position);

                                jersey = (Long) jobj.get("jerseyNumber");
                                System.out.println(jersey);

                                country = (String) jobj.get("nationality");
                                System.out.println(country);

                                dob = (String) jobj.get("dateOfBirth");
                                System.out.println(dob);

                                try {
                                    sqlstring = "INSERT INTO PLAYERSTABLE (name, position, jersey, country) VALUES ('" + player +
                                            "', '" + position + "', " + jersey +  ", '" + country + "');";
                                    System.out.println(sqlstring);
                                    pgC.insertRecordsIntoTable(sqlstring);

                                    //pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                }


                conn.disconnect();




            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            try {
                Thread.sleep(sleepinterval); // go fetch again after sleepinterval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // End of while(1) loop

    }
}
