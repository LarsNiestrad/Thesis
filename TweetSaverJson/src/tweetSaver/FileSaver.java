/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * This class reads the information out of the list the tweetSaver class
 * collected in an Interval. The data gets serialized in a String which the json
 * format. Then this String will be saved to a file.
 *
 *
 * @author Lars
 */
public class FileSaver {

    //counter for the interval numbers
    private int i = 1;
    private PrintWriter pWriter;

    //saves the collected Data in a json File
    public void saveToJson(int fileNumber, List<TweetInterval> til, int totalAmount, int totalIntervalAmount) throws IOException {
        StringBuilder sb = new StringBuilder("{\"totalAmount\":["+totalAmount+
                "],\"totalIntervalAmount\":["+totalIntervalAmount+"],\"tweets\":{");
        
        //go through the saved data a build a string in json Format
        for (TweetInterval ti : til) {

            /*note: the ids have to be saved in quotes because jvectormap needs
             * them as a String representation to show them correctly
             */
            //read the ids
            sb.append("\"").append(i).append("\":{\"ids\":[");
            for (long a : ti.getAllIDs()) {
                sb.append("\"").append(a).append("\",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("],");
            
            //read the sources
            sb.append("\"sources\":").append(ti.getAllSources()).append(",");

            //read the geolocation
            sb.append("\"geolocation\":[");
            for (int u = 0; u < ti.getAllLatitude().size(); u++) {
                if (u > 0) {
                    sb.append(",[").append(ti.getAllLatitude().get(u)).append(",").append(ti.getAllLongtitude().get(u)).append("]");
                } else {
                    sb.append("[").append(ti.getAllLatitude().get(u)).append(",").append(ti.getAllLongtitude().get(u)).append("]");
                }
            }
            
            //read the Hashtags
            sb.append("],").append("\"hashtags\":[");
            for (String str : ti.getAllHashtags()) {
                sb.append("\"").append(str).append("\",");
            }
            if (ti.getAllHashtags() != null) {
                sb.deleteCharAt(sb.length() - 1);
            }
            
            //read the links
            sb.append("],\"links\":[");
            for (String str : ti.getAllLinks()) {
                sb.append("\"").append(str).append("\",");
            }
            if (ti.getAllLinks() != null) {
                sb.deleteCharAt(sb.length() - 1);
            }
            
            //read the timestamps
            sb.append("],\"timestamps\":[");
            for (Date str : ti.getAllTimeStamps()) {
                sb.append("\"").append(str).append("\",");
            }
            sb.deleteCharAt(sb.length() - 1);
            
            //read the usernames
            sb.append("],\"usernames\":[");
            for (String str : ti.getAllUsernames()) {
                sb.append("\"").append(str).append("\",");
            }
            if (ti.getAllUsernames() != null) {
                sb.deleteCharAt(sb.length() - 1);
            }
            
            //read the tweet-links
            sb.append("],\"tweetlinks\":[");
            for (String str : ti.getAllUrls()) {
                sb.append("\"").append(str).append("\",");
            }
            if (ti.getAllUrls() != null) {
                sb.deleteCharAt(sb.length() - 1);
            }
            
            //read the followers
            sb.append("],\"followers\":").append(ti.getAllFollowers()).append(",");

            //get the countrycodes
            sb.append("\"countrycodes\":{");
            for (String cCode : ti.getcCodes().keySet()) {
                sb.append("\"").append(cCode).append("\":").append(ti.getcCodes().get(cCode)).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}},");
            i++;
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}}");
        
        //save the Data to File
        pWriter = new PrintWriter(new FileWriter(fileNumber + ".json"));
        pWriter.print(sb.toString());
        pWriter.flush();
        System.out.println("File saved");
        i = 1;
    }
}
