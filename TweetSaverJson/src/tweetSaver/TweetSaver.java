/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * This Application connects to the Twitterstream, looks for relevant Tweets and
	 * saves them to json files
     */
    public static void main(String[] args) throws TwitterException, IOException {

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
            int tweetQuantity=200; //amount of Tweets a json file should contain
            int tweetNrGesamt = 0; //counts the number of Tweets for a single file
            int fileNumber = 1; //counts the number of json files the app created 
            long allIDs[] = new long[tweetQuantity]; //Array for the tweet id´s
            double allLatitude[] = new double[tweetQuantity]; //array for the Lat values of the tweets
            double allLongtitude[] = new double[tweetQuantity]; //array for the Longt values of the tweets
            int allSources[] = new int[tweetQuantity]; //array for the source of the tweets
            String allHashtags[] = new String[tweetQuantity]; //array for the hashtags
            String allLinks[] = new String[tweetQuantity]; //array for the links
            Date allTimeStamps[] = new Date[tweetQuantity]; //array for the timestamps

            @Override
            public void onStatus(Status status) {



                //Array for checking the source of the Tweet
                String source[] = new String[4];
                source[0] = "android"; 
                source[1] = "iphone"; 
                source[2] = "blackberry"; 
                source[3] = "windows"; 


                //write the saved tweets in a json file if a specific number of tweets is reached
                if (tweetNrGesamt == tweetQuantity) {
                    tweetNrGesamt=0;
                    //create new csv File for saving the Tweets
                    PrintWriter pWriter;
					
					//save the information in json coloum array format
                    try {
                        pWriter = new PrintWriter(new FileWriter("DataFile" + fileNumber + ".json"));
                        fileNumber++;
                        pWriter.print("{\"tweets\":{\"id\":[");
                        
                        for (int i = 0; i < allIDs.length; i++) {
                            if (i > 0) {
                                pWriter.print("," + allIDs[i]);
                            } else {
                                pWriter.print(allIDs[i]);
                            }
                        }

                        pWriter.print("],\"source\":[");

                        for (int i = 0; i < allSources.length; i++) {
                            if (i > 0) {
                                pWriter.print("," + allSources[i]);
                            } else {
                                pWriter.print(allSources[i]);
                            }
                        }

                        pWriter.print("],\"geolocation\":[");

                        for (int i = 0; i < allLatitude.length; i++) {
                            if (i > 0) {
                                pWriter.print(",[" + allLatitude[i] + "," + allLongtitude[i] + "]");
                            } else {
                                pWriter.print("[" + allLatitude[i] + "," + allLongtitude[i] + "]");
                            }
                        }

                        pWriter.print("],\"hashtags\":[");

                        for (int i = 0; i < allHashtags.length; i++) {
                            if (i > 0) {
                                pWriter.print("," + "\""+allHashtags[i]+"\"");
                            } else {
                                pWriter.print("\""+allHashtags[i]+"\"");
                            }
                        }

                        pWriter.print("],\"links\":[");
                        for (int i = 0; i < allLinks.length; i++) {
                            if (i > 0) {
                                pWriter.print("," + "\""+allLinks[i]+"\"");
                            } else {
                                pWriter.print("\""+allLinks[i]+"\"");
                            }
                        }

                        pWriter.print("],\"timestamps\":[");

                        for (int i = 0; i < allTimeStamps.length; i++) {
                            if (i > 0) {
                                pWriter.print("," + "\""+allTimeStamps[i]+"\"");
                            } else {
                                pWriter.print("\""+allTimeStamps[i]+"\"");
                            }
                        }
                        pWriter.print("]}}");
                        pWriter.flush();
                        


                    } catch (IOException ex) {
                        Logger.getLogger(TweetSaver.class.getName()).log(Level.SEVERE, null, ex);
                    }



                }

                //only save Tweets with Geolocation information included
                if (status.getGeoLocation() != null) {

                    //get the ID of the Tweet
                    allIDs[tweetNrGesamt] = status.getId();

                    //get source(IPhone,Android..) 
                    for (int i = 0; i < 4; i++) {
                        if (status.getSource().contains(source[i])) {
                            allSources[tweetNrGesamt] = i+1;
                            break;
                        } else {
                            allSources[tweetNrGesamt] = 5;
                        }
                    }

                    //get the Geolocation coordinates
                    allLatitude[tweetNrGesamt] = status.getGeoLocation().getLatitude();
                    allLongtitude[tweetNrGesamt] = status.getGeoLocation().getLongitude();

                    //get the HashTags
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
                        allHashtags[tweetNrGesamt] = sBuffer.toString();
                    }


                    //get the Links
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
                        allLinks[tweetNrGesamt]=sBuffer.toString();
                    }

                    //save the date and raise counter
                    allTimeStamps[tweetNrGesamt]=status.getCreatedAt();   
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
