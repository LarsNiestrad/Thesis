/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Lars
 */
public class TweetSaver {

    final Timer timer = new Timer();
    boolean tenMinutesPassed = false;

    public void startTimer() {
        System.out.println("Timer gestartet");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tenMinutesPassed = true;
                System.out.println("10 seconds passed");

            }
        }, 15000, 15000);
    }

    public void CollectData() {

        /*
         * Authetication - the are x placeholders for my privat auth. keys - 
         * replace with the keys/ tokens twitter offers when creating a new
         * developer account
         */
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("xxx");
        cb.setOAuthConsumerSecret("xxx");
        cb.setOAuthAccessToken("xxx-xxx");
        cb.setOAuthAccessTokenSecret("xxx");

        //Getting in Twitter Stream..
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
            /*Counter for automatical shutdown if a specific number of saved 
             tweets is reached*/
            int tweetNrGesamt = 0,
                    fileNumber = 1,
                    tweetInterval = 0;
            TweetInterval ti = new TweetInterval();
            List<TweetInterval> tweetList = new LinkedList<>();
            FileSaver fileSaver = new FileSaver();

            @Override
            public void onStatus(Status status) {

                //Array for checking the source of the Tweets
                String source[] = new String[4];
                source[0] = "android"; //1 android
                source[1] = "iphone"; //2 iphone
                source[2] = "blackberry"; //3 blackberry
                source[3] = "windows"; //4 windows

                //write the saved tweets in a json file if 20 minutes have passed
                if (tenMinutesPassed == true) {
                    tweetList.add(ti);
                    ti = new TweetInterval();
                    fileSaver.saveToJson(fileNumber, tweetList);
                    fileNumber++;
                    tweetList.clear();
                    tweetNrGesamt = 0;
                    tenMinutesPassed = false;
                }

                //only save Tweets with Geolocation information included
                if (status.getGeoLocation() != null) {

                    //save the ID of the Tweets
                    ti.setID(status.getId());

                    //save source(IPhone,Android..) 
                    for (int i = 0; i < 4; i++) {
                        if (status.getSource().contains(source[i])) {
                            ti.setSource(i + 1);
                            break;
                        } else {
                            ti.setSource(5);
                        }
                    }

                    //save the Geolocation coordinates
                    ti.setLatitude(status.getGeoLocation().getLatitude());
                    ti.setLongtitude(status.getGeoLocation().getLongitude());

                    //search and save the HashTags
                    if (status.getHashtagEntities().length > 0) {
                        HashtagEntity hte[] = new HashtagEntity[status.getHashtagEntities().length];
                        hte = status.getHashtagEntities();
                        StringBuffer sBuffer = new StringBuffer();
                        for (int i = 0; status.getHashtagEntities().length > i; i++) {
                            if (i < 1) {
                                sBuffer.append(hte[i].getText());
                            } else {
                                sBuffer.append(" ," + hte[i].getText());
                            }
                        }
                        ti.setHashtags(sBuffer.toString());
                    }


                    //save the Links
                    if (status.getURLEntities().length > 0) {
                        //get as much URLEntities as URLs are in the Tweets
                        URLEntity ue[] = new URLEntity[status.getURLEntities().length];
                        ue = status.getURLEntities();
                        StringBuffer sBuffer = new StringBuffer();
                        for (int i = 0; status.getURLEntities().length > i; i++) {
                            if (i < 1) {
                                sBuffer.append(ue[i].getURL());
                            } else {
                                sBuffer.append(", " + ue[i].getURL());
                            }
                        }
                        ti.setLinks(sBuffer.toString());
                    }

                    //save the date 
                    ti.setTimeStamp(status.getCreatedAt());

                    //show saved tweet in console for checkup
                    System.out.println(status.getId());
                    System.out.println(status.getGeoLocation());
                    System.out.println(status.getCreatedAt());
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
            }

            @Override
            public void onTrackLimitationNotice(int i) {
                System.out.println("limitationNotice:" + i);;
            }

            @Override
            public void onScrubGeo(long l, long l1) {
                System.out.println("ScrubGeo " + l + l1);
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onException(Exception excptn) {
                excptn.printStackTrace();
            }
        };

        twitterStream.addListener(listener);
        twitterStream.sample();
    }
}
