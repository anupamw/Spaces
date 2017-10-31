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
        twitterStream.setOAuthConsumer("fbRnO97AjQaHa7PIff1CIhjjG", "pHN0yxaFLcS5djvNTSL4VMv2iGaDSjDMeYwVMzZ5E8wwoGev9y");
        // Twitter Access token & Access token Secret
        twitterStream.setOAuthAccessToken(new AccessToken("17674248-WfNtcYK8mGlHpVsTB9oM6BRIYPU6lddvgxBuSSi2e",
                "RI4Ixt3yUvQw7PeOg1opL1bBZ9XNdd4iS6XSqiXXm4Oan"));


        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
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
        String[] keywordsArray = null;

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
            int i =0;
            String name = null;
            keywordsArray = new String[rosterPlayers.size()];
            while (it.hasNext()) {
                name = it.next();
                System.out.println(name);
                keywordsArray[i++] = name;
            }
        }

        if (keywordsArray != null) {
            filter.track(keywordsArray);
            System.out.println("Now installing Twitter Stream function");
            twitterStream.filter(filter);
        }
        else {
            System.out.println("Did not install Twitter Stream function due to null filter");
        }

        //twitterStream.sample();
    }

    }
