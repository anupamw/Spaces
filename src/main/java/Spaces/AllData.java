package Spaces;
import java.util.*;


//Class that holds references to all data in our store

public class AllData {

    PgConn pgC;
    HashMap<String, ClubPlayers> clubsSignings; //Clubname gets a PlayerList (set)
    HashMap<String, PlayerInfo> playerDetails; //Playername gets a player-meta-object (object)

    public AllData() {
        clubsSignings = new HashMap<String, ClubPlayers>();
        playerDetails = new HashMap<String, PlayerInfo>();
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


}
