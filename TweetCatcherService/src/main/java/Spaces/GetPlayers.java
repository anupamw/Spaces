package Spaces;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

//Class to fetch player information from PG
public class GetPlayers implements Runnable {

    private AllData allData;

    public GetPlayers(AllData allData) {
        this.allData = allData;
        System.out.println("Fetched My team information ....\n");
        try {
            getTeamsFromTeamTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public void run() {
        String sqlstring;

        System.out.println("Fetched My Player information .... \n");

        try {
            getPlayersFromPlayersTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //Todo: No logic to delete players DELETE FROM PLAYERS where name = 'blah blah';
    }

    public void getPlayersFromPlayersTable() throws SQLException {

        PreparedStatement preparedStatement = null;
        final String selectSQL = "SELECT * FROM PLAYERSTABLE;";
        ResultSet rs = null;
        String name, position, jersey, country, club;
        String sqlstring;
        Long jerseyNumber;



        try {
            preparedStatement = allData.pgC.connection.prepareStatement(selectSQL);
            // execute select SQL stetement
            rs = preparedStatement.executeQuery();

            while (rs.next()) {

                name = rs.getString("name");
                position = rs.getString("position");
                jerseyNumber = rs.getLong("jersey");
                country = rs.getString("country");
                club = rs.getString("club");

                System.out.println(name + position + jerseyNumber + country + club);


                //Now load in AllData memory store
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.name = name.trim();
                playerInfo.country = country.trim();
                playerInfo.jersey = jerseyNumber;
                //playerInfo.dob = dob;
                playerInfo.position = position.trim();

                allData.playerDetails.replace(name.trim(), playerInfo);
                if (allData.clubsSignings.get(club.trim()) != null)
                    allData.clubsSignings.get(club.trim()).clubPlayers.add(name.trim());
                else
                    System.out.println(club.trim() + " not found in AllData. Skipping ...");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                //return;
            }
            /*
            if (connection != null) {
                connection.close();
            }
            */
        }

        allData.DumpAllData();
        allData.buildAllPlayers();

        return;
    }

    public void getTeamsFromTeamTable() throws SQLException {

        PreparedStatement preparedStatement = null;
        final String selectSQL = "SELECT * FROM TEAMTABLE;";
        ResultSet rs = null;
        String club, shortname, cresturl, enable;
        Set<String> clubPlayers;
        ClubPlayers clubPlayersObject;

        try {
            preparedStatement = allData.pgC.connection.prepareStatement(selectSQL);
            // execute select SQL stetement
            rs = preparedStatement.executeQuery();

            while (rs.next()) {

                club = rs.getString("name");
                shortname = rs.getString("shortname");
                cresturl = rs.getString("cresturl");
                enable = rs.getString("enable");

                System.out.println(club + shortname + cresturl + enable);

                if (enable != null && enable.trim().equalsIgnoreCase("yes")) {
                    clubPlayers = new HashSet<String>();
                    clubPlayersObject = new ClubPlayers();
                    clubPlayersObject.clubPlayers = clubPlayers;
                    allData.clubsSignings.putIfAbsent(club.trim(), clubPlayersObject);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                //return;
            }
            /*
            if (connection != null) {
                connection.close();
            }
            */
        }

        //allData.DumpAllData();
        //allData.buildAllPlayers();

        return;
    }
}
