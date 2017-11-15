package Spaces;

import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class StreamTwitter implements Runnable {

    private AllData allData;

    public StreamTwitter(AllData allData) {
        this.allData = allData;
    }

    public void run() {

        System.out.println();
        System.out.println("Now streaming tweets !");
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        // Twitter Consumer key & Consumer Secret
        twitterStream.setOAuthConsumer(allData.twitterAPIKey, allData.twitterAPISecret);
        // Twitter Access token & Access token Secret
        twitterStream.setOAuthAccessToken(new AccessToken(allData.twitterAccessToken, allData.twitterAccessSecret));


        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (status.isRetweet() == false) {
                    System.out.println(status.getCreatedAt() + "/" + status.getLang() + "/IsRT?" + status.isRetweet() + "/" + "@" + status.getUser().getScreenName() + " - " + status.getText());
                    insertIntoPlayerStream(status.getText());
                }
            }
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);

        // Filter

        FilterQuery filter = new FilterQuery();
        //String[] keywordsArray = { "WorldSeries" };
        String[] keywordsArray = new String[allData.allPlayers.size()];
        System.out.println("keywordsArray filter size is " + allData.allPlayers.size() + " players.");
        System.out.println("Adding following players to Twitter Stream API filter ....\n");
        String name;
        int i =0;

        Set set = allData.clubsSignings.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            //System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            //System.out.println(mentry.getValue());
            //System.out.println(mentry.getKey() + "Players:");
            ClubPlayers roster = (ClubPlayers) mentry.getValue();
            Set<String> rosterPlayers = roster.clubPlayers;
            //System.out.println(rosterPlayers);
            Iterator<String> it = rosterPlayers.iterator();

            name = null;
            while (it.hasNext()) {
                name = it.next();
                //System.out.println(name + " adding at " + i);
                keywordsArray[i++] = name;
            }
        }

        if (keywordsArray != null) {
            filter.track(keywordsArray);
            System.out.println("Now installing Twitter Stream function");
            twitterStream.filter(filter);
            //todo: just the name may be too wide. For example Pedro, Willian ...
        }
        else {
            System.out.println("Did not install Twitter Stream function due to null filter");
        }

        //twitterStream.sample();
    }

    void insertIntoPlayerStream(String tweet) {
        //Todo: this is missing a lot of matches. For example when tweet has only 1st name !
        for (String player: allData.allPlayers) {
            if (tweet.toLowerCase().contains(player.toLowerCase())) {
                System.out.println("Ingest: " + player + " matched in this tweet!");
                allData.jC.jedis.lpush(player.toLowerCase(), tweet);
            }
        }

        return;
    }

    }
