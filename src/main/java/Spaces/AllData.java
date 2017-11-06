package Spaces;
import java.util.*;
import redis.clients.jedis.Jedis;


//Class that holds references to all data in our store

public class AllData {

    PgConn pgC;
    JedisConn jC;
    HashMap<String, ClubPlayers> clubsSignings; //Clubname gets a PlayerList (set)
    HashMap<String, PlayerInfo> playerDetails; //Playername gets a player-meta-object (object)
    Set<String> allPlayers;
    String twitterAPIKey;
    String twitterAPISecret;
    String twitterAccessToken;
    String twitterAccessSecret;


    public AllData() {
        clubsSignings = new HashMap<String, ClubPlayers>();
        playerDetails = new HashMap<String, PlayerInfo>();
        allPlayers = new HashSet<String>();
    }

    public void DumpAllData() {
        //prints everything on the screen
        //First iterate for each club over its roster:

        Set set = clubsSignings.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            //System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            //System.out.println(mentry.getValue());
            System.out.println(mentry.getKey() + "Players:");
            ClubPlayers roster = (ClubPlayers) mentry.getValue();
            Set<String> rosterPlayers = roster.clubPlayers;
            System.out.println(rosterPlayers);

        }

    }

    public void buildAllPlayers() {

        Set set = clubsSignings.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            //System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            //System.out.println(mentry.getValue());
            //System.out.println(mentry.getKey() + "Players:");
            ClubPlayers roster = (ClubPlayers) mentry.getValue();
            Set<String> rosterPlayers = roster.clubPlayers;
            //System.out.println(rosterPlayers);
            for (String players: rosterPlayers) {
                allPlayers.add(players);
                System.out.println("Added " + players + " to allPlayers");
            }

        }

    }

}
