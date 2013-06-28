/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Lars
 */
public class TweetSaver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException, IOException {

        /*
         * Authetication - the x are placeholders for my privat auth. keys - 
         * replace with the keys/ tokens twitter offers when creating a new
         * developer account
         */
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("xxxxxx");
        cb.setOAuthConsumerSecret("xxxxxx");
        cb.setOAuthAccessToken("xxxxxx-xxxxxx");
        cb.setOAuthAccessTokenSecret("xxxxxx");

        //Getting in Twitter Stream..
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
            /*Counter for automatical shutdown if a specific number of saved 
             tweets is reached*/
            int tweetNrGesamt = 1;
            //create new csv File for saving the Tweets
            PrintWriter pWriter = new PrintWriter(new FileWriter(tweetNrGesamt + ".csv"));

            @Override
            public void onStatus(Status status) {

                //Array for checking the source of the Tweet
                String source[] = new String[4];
                source[0] = "android";
                source[1] = "iphone";
                source[2] = "blackberry";
                source[3] = "windows";

                //shutdown after 0,5 Mio Tweets
                if (tweetNrGesamt == 5000000) {
                    System.exit(0);
                }

                //only save Tweets with Geolocation information included
                if (status.getGeoLocation() != null) {

                    //save the ID of the Tweet
                    pWriter.print(status.getId() + ", ");

                    //save source(IPhone,Android..) 
                    for (int i = 0; i < 4; i++) {
                        if (status.getSource().contains(source[i])) {
                            pWriter.print(source[i] + ", {");
                            break;
                        }

                        if (i > 2) {
                            pWriter.print("other, {");
                        }
                    }

                    //save the Geolocation coordinates
                    pWriter.print(status.getGeoLocation().getLatitude() + ", "
                            + status.getGeoLocation().getLongitude() + "}, (");

                    //save the HashTags
                    if (status.getHashtagEntities().length > 0) {
                        HashtagEntity hte[] = new HashtagEntity[status.getHashtagEntities().length];
                        hte = status.getHashtagEntities();

                        for (int i = 0; status.getHashtagEntities().length > i; i++) {
                            if (i < 1) {
                                pWriter.printf(hte[i].getText());
                            } else {
                                pWriter.printf(" ," + hte[i].getText());
                            }
                        }
                    }
                    pWriter.printf("),(");

                    //save the Links
                    if (status.getURLEntities().length > 0) {
                        //get as much URLEntities as URLs are in the Tweets
                        URLEntity ue[] = new URLEntity[status.getURLEntities().length];
                        ue = status.getURLEntities();
                        for (int i = 0; status.getURLEntities().length > i; i++) {
                            if (i < 1) {
                                pWriter.printf(ue[i].getURL());
                            } else {
                                pWriter.printf(", " + ue[i].getURL());
                            }
                        }
                    }

                    //save the date, clear the buffer and raise counter
                    pWriter.println("), " + status.getCreatedAt());
                    pWriter.flush();
                    tweetNrGesamt++;

                    //show saved tweet in console for checkup
                    System.out.println(status.getId());
                    System.out.println(status.getGeoLocation());
                    System.out.println(status.getCreatedAt());
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
                System.out.println("StatusDeletionNotice id:" + sdn.getStatusId());
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

        /*
         * Way to Filter the Twitterstream for own Keywords - maybe useful 
         * for later researches!?
         */
//        FilterQuery fq = new FilterQuery();
//        String keywords[] = {"a", "e", "i", "o", "u", "b", "c", "d", "f", "g", "h", "i", "j",
//            "k", "l", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3",
//            "4", "5", "6", "7", "8", "9"};
//        fq.track(keywords);

        twitterStream.addListener(listener);
//        twitterStream.filter(fq);
        twitterStream.sample();
    }
}
