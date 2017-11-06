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

    private AllData allData;

    public GetPlayers(AllData allData) {
        this.allData = allData;

        //Hardcode for Man U
        Set<String> clubPlayers = new HashSet<String>();
        ClubPlayers clubPlayersObject = new ClubPlayers();
        clubPlayersObject.clubPlayers = clubPlayers;
        allData.clubsSignings.putIfAbsent("Manchester United", clubPlayersObject);
    }



    public void run() {


        String urlstring = "http://api.football-data.org/v1/teams/66/players"; //Man U hardcoded

        long sleepinterval = 300000; //300s

        //while (true) {


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

                System.out.println("Fetched Player information .... \n");
                while ((output = br.readLine()) != null) {
                    //System.out.println(output);
                    //System.out.println();


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
                                //System.out.println(player);

                                position = (String) jobj.get("position");
                                //System.out.println(position);

                                jersey = (Long) jobj.get("jerseyNumber");
                                //System.out.println(jersey);

                                country = (String) jobj.get("nationality");
                                //System.out.println(country);

                                dob = (String) jobj.get("dateOfBirth");
                                //System.out.println(dob);

                                //TODO: fix DOB to convert to age and insert into PG
                                //This is for PG 9.5+
                                // INSERT INTO PLAYERSTABLE (name, position, jersey, country) VALUES ('Juan Mata',
                                // 'Attacking Midfield', 1000, 'Spain') ON CONFLICT (name) DO UPDATE SET jersey=1000;

                                try {
                                    sqlstring = "INSERT INTO PLAYERSTABLE (name, position, jersey, country) VALUES ('" + player +
                                            "', '" + position + "', " + jersey +  ", '" + country + "') ON CONFLICT (name)" +
                                            " DO UPDATE SET position = '" + position + "', jersey = " + jersey + ", country = '" + country +"';";
                                    System.out.println(sqlstring);
                                    allData.pgC.insertRecordsIntoTable(sqlstring);

                                    //pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");

                                } catch (SQLException e) {

                                    e.printStackTrace();
                                }


                                //Todo: No logic to delete players DELETE FROM PLAYERS where name = 'blah blah';

                                //Now load in AllData memory store
                                PlayerInfo playerInfo = new PlayerInfo();
                                playerInfo.name = player;
                                playerInfo.country = country;
                                playerInfo.jersey = jersey;
                                playerInfo.dob = dob;
                                playerInfo.position = position;

                                allData.playerDetails.replace(player, playerInfo);
                                allData.clubsSignings.get("Manchester United").clubPlayers.add(player);


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

            allData.DumpAllData();
            allData.buildAllPlayers();
            /*
            try {
                Thread.sleep(sleepinterval); // go fetch again after sleepinterval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
       // } // End of while(1) loop

    }
}
