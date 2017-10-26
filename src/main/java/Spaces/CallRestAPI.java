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


public class CallRestAPI {

    public static void getREST(String urlstring) {

        try {

            //URL url = new URL("http://localhost:8080/RESTfulExample/json/product/get");
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

            jsonParser = new JSONParser();

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                System.out.println();




                try {
                    json = output.trim();
                    jobj = (JSONObject)jsonParser.parse(json);

                    Long count = (Long) jobj.get("count");
                    System.out.println(count);

                    // loop array
                    msg = (JSONArray) jobj.get("players");
                    Iterator<JSONObject> iterator = msg.iterator();
                    if (iterator != null)
                        while (iterator.hasNext()) {
                        jobj = (JSONObject)iterator.next();
                            String player = (String) jobj.get("name");
                            System.out.println(player);
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
