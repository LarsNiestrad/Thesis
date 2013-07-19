/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private final Timer timer = new Timer();
    private boolean tenMinutesPassed = false;
    private int timeSecondsInterval = 600;
    
    public void startTimer() {
        System.out.println("Timer gestartet");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tenMinutesPassed = true;
                System.out.println(timeSecondsInterval+" seconds passed");
            }
        }, timeSecondsInterval*1000, timeSecondsInterval*1000);
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
            /*Counter the files that has been created to avoid naming conflicts*/
            int fileNumber = 1;        
            TweetInterval ti = new TweetInterval();
            List<TweetInterval> tweetList = new LinkedList<>();
            FileSaver fileSaver = new FileSaver();

            @Override
            public void onStatus(Status status) {

                //Array for checking the source of the Tweets
                String source[] = new String[5];
                source[0] = "android"; //1 android
                source[1] = "iphone"; //2 iphone
                source[2] = "blackberry"; //3 blackberry
                source[3] = "windows"; //4 windows
                source[4] = "other";

                //write the saved tweets in a json file if the given time has passed
                if (tenMinutesPassed == true) {
                    tweetList.add(ti);
                    System.out.println("Tweetliste erfolgreich hinzugefügt");
                    ti = new TweetInterval();
                    tenMinutesPassed = false;
                }

                if (tweetList.size() > 2) {
                    System.out.println("Aufruf savte2json");
                    try {
                        fileSaver.saveToJson(fileNumber, tweetList);
                    } catch (IOException ex) {
                        Logger.getLogger(TweetSaver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    fileNumber++;
                    tweetList.clear();
                }

                //only save Tweets with Geolocation information included
                if (status.getGeoLocation() != null) {

                    //save the ID of the Tweets
                    ti.setID(status.getId());

                    //save source(IPhone,Android..) 
                    for (int i = 0; i < 5; i++) {
                        if (i < 4 && status.getSource().contains(source[i])) {
                            ti.setSource(i + 1);
                            break;
                        } else if (i > 3) {
                            ti.setSource(5);
                        }
                    }

                    //save the Geolocation coordinates
                    ti.setLatitude(status.getGeoLocation().getLatitude());
                    ti.setLongtitude(status.getGeoLocation().getLongitude());

                    //search and save the HashTags
                    HashtagEntity hte[] = new HashtagEntity[status.getHashtagEntities().length];
                    hte = status.getHashtagEntities();
                    StringBuilder sBuilder = new StringBuilder();
                    if (status.getHashtagEntities() != null) {
                        for (int i = 0; status.getHashtagEntities().length > i; i++) {
                            if (i < 1) {
                                sBuilder.append(hte[i].getText());
                            } else {
                                sBuilder.append(" ,").append(hte[i].getText());
                            }
                        }
                    } else {
                        sBuilder.append("none");
                    }

                    ti.setHashtags(sBuilder.toString());
                    //clear StringBuilder 
                    sBuilder.delete(0, sBuilder.length());


                    //save the Links
                    URLEntity ue[] = new URLEntity[status.getURLEntities().length];
                    ue = status.getURLEntities();
                    if (status.getURLEntities() != null) {
                        for (int i = 0; status.getURLEntities().length > i; i++) {
                            if (i < 1) {
                                sBuilder.append(ue[i].getURL());
                            } else {
                                sBuilder.append(", ").append(ue[i].getURL());
                            }
                        }
                    } else {
                        sBuilder.append("none");
                    }
                    ti.setLinks(sBuilder.toString());
                    sBuilder.delete(0, sBuilder.length());

                    //save the date 
                    ti.setTimeStamp(status.getCreatedAt());
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
