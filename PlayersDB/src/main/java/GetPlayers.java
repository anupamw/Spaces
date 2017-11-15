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


    }



    public void run() {


        String urlstring = "http://api.football-data.org//v1/competitions"; //all competitions

        long sleepinterval = 300000; //300s

        //while (true) {


        try {


            System.out.println("Use REST to fetch all competitions: " + urlstring);
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
            String caption;
            String league;
            JSONObject links;
            String sqlstring;
            Long id;
            String enabled;

            jsonParser = new JSONParser();

            System.out.println("Fetched competitions information .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                //System.out.println();


                try {
                    json = output.trim();

                    // loop array
                    msg = (JSONArray) jsonParser.parse(json);
                    Iterator<JSONObject> iterator = msg.iterator();
                    if (iterator != null)
                        while (iterator.hasNext()) {
                            jobj = (JSONObject) iterator.next();

                            links = (JSONObject)  jobj.get("_links");
                            System.out.println(links);

                            id = (Long) jobj.get("id");
                            System.out.println(id);

                            caption = (String) jobj.get("caption");
                            System.out.println(caption);

                            league = (String) jobj.get("league");
                            System.out.println(league);

                            try {
                                sqlstring = "INSERT INTO COMPTABLE (league, caption, id) VALUES ('" + league +
                                        "', '" + caption + "', " + id +  ") ON CONFLICT (league)" +
                                        " DO UPDATE SET caption = '" +  caption + "', id = " + id +";";
                                System.out.println(sqlstring);
                                allData.pgC.insertRecordsIntoTable(sqlstring);

                                //pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");

                            } catch (SQLException e) {

                                e.printStackTrace();
                            }


                            try {
                                sqlstring = "select enable from COMPTABLE where league = '" + league +"' and caption = '" +
                                        caption + "' and enable = 'Yes'";
                                System.out.println(sqlstring);
                                enabled = allData.pgC.isEnabledRecordFromTable(sqlstring);
                                System.out.println(enabled);

                                if (enabled.compareToIgnoreCase("Yes") == 0)
                                    fetchTeamsInComp(id, caption);




                            } catch (SQLException e) {

                                e.printStackTrace();
                            }

                            //Todo: No logic to delete players DELETE FROM PLAYERS where name = 'blah blah';



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


            /*
            try {
                Thread.sleep(sleepinterval); // go fetch again after sleepinterval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
        // } // End of while(1) loop

    }

    private void fetchTeamsInComp(long teamid, String caption) {
        //"http://api.football-data.org/v1/competitions/444/teams"
        System.out.println("Now looking at teams for id/caption:" + teamid + "/" + caption);
        final String urlstring = "http://api.football-data.org/v1/competitions/" + teamid + "/teams";
        try {


            System.out.println("Use REST to fetch all teams: " + urlstring);
            final URL url = new URL(urlstring);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String json;
            JSONObject jobj, jobj1;
            JSONArray msg;
            String league;
            JSONObject links;
            String sqlstring;
            Long id, count;
            String enabled;
            String club, shortname, crestUrl, code, sqmv;
            String selfURL, fixtureURL, playersURL=null;
            Iterator iter;
            String key, value;

            final JSONParser jsonParser = new JSONParser();

            System.out.println("Fetched teams information .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                System.out.println();


                try {
                    json = output.trim();
                    jobj = (JSONObject) jsonParser.parse(json);

                    links = (JSONObject)  jobj.get("_links");
                    System.out.println(links);

                    count = (Long) jobj.get("count");
                    System.out.println(count);

                    // loop array
                    msg = (JSONArray) jobj.get("teams");
                    Iterator<JSONObject> iterator = msg.iterator();
                    if (iterator != null)
                        while (iterator.hasNext()) {
                            jobj = (JSONObject) iterator.next();

                            links = (JSONObject)  jobj.get("_links");
                            System.out.println(links);


                            for(iter = links.keySet().iterator(); iter.hasNext();) {
                                key = (String) iter.next();
                                if (key.equalsIgnoreCase("players")) {
                                    System.out.println(links.get(key));
                                    jobj1 = (JSONObject) links.get(key);
                                    playersURL = (String) jobj1.get("href");
                                    System.out.println(playersURL);

                                }
                            }



                            club = (String) jobj.get("name");
                            System.out.println(club);

                            code = (String) jobj.get("code");
                            System.out.println(code);

                            shortname = (String) jobj.get("shortname");
                            System.out.println(shortname);

                            sqmv = (String) jobj.get("squadMarketValue");
                            System.out.println(sqmv);

                            crestUrl = (String) jobj.get("crestUrl");
                            System.out.println(crestUrl);


                            try {
                                sqlstring = "INSERT INTO TEAMTABLE (name, shortname, cresturl) VALUES ('" + club +
                                        "', '" + (shortname == null?"":shortname) + "', '" + crestUrl +  "') ON CONFLICT (name)" +
                                        " DO UPDATE SET shortname = '" +  (shortname == null?"":shortname) + "', cresturl = '" + crestUrl +"';";
                                System.out.println(sqlstring);
                                allData.pgC.insertRecordsIntoTable(sqlstring);

                                //pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");

                            } catch (SQLException e) {

                                e.printStackTrace();
                            }




                            try {
                                sqlstring = "select enable from TEAMTABLE where name = '" + club +"' and enable = 'Yes'";
                                System.out.println(sqlstring);
                                enabled = allData.pgC.isEnabledRecordFromTable(sqlstring);
                                System.out.println(enabled);

                                if (enabled.compareToIgnoreCase("Yes") == 0)
                                    fetchPlayersInClub(club, playersURL);




                            } catch (SQLException e) {

                                e.printStackTrace();
                            }

                            //Todo: No logic to delete players DELETE FROM PLAYERS where name = 'blah blah';



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

    }

    private void fetchPlayersInClub(String club, String playersURL) {


        System.out.println("Now looking at players for team: " + club + " @ " + playersURL);

        try {


            System.out.println("Use REST to fetch all players: " + playersURL);
            final URL url = new URL(playersURL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String json;
            JSONObject jobj, jobj1;
            JSONArray msg;
            String league;
            JSONObject links;
            String sqlstring;
            Long id, count;
            String enabled;


            Iterator iter;
            String key, value;
            String player, position, country, dob;
            Long jersey;

            final JSONParser jsonParser = new JSONParser();

            System.out.println("Fetched players  information .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                System.out.println();


                try {
                    json = output.trim();
                    jobj = (JSONObject) jsonParser.parse(json);

                    count = (Long) jobj.get("count");
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

                            try {
                                sqlstring = "INSERT INTO PLAYERSTABLE (name, position, jersey, country, club) VALUES ('" + player +
                                        "', '" + position + "', " + jersey +  ", '" + country + "',  '" + club + "') ON CONFLICT (name)" +
                                        " DO UPDATE SET position = '" + position + "', jersey = " + jersey + ", country = '" + country +
                                        "', club = '" + club + "';";
                                System.out.println(sqlstring);

                                // Todo: country = 'Cote d'Ivoire' is causing syntax error !!
                                allData.pgC.insertRecordsIntoTable(sqlstring);

                                //pgC.selectRecordsFromTable("SELECT * FROM PLAYERS;");

                            } catch (SQLException e) {

                                e.printStackTrace();
                            }


                            //Todo: No logic to delete players DELETE FROM PLAYERS where name = 'blah blah';



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

    }
}
