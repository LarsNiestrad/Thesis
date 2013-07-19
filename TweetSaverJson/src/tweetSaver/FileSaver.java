/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        
    public void saveToJson (int fileNumber, List<TweetInterval> til) throws IOException{
        StringBuilder sb = new StringBuilder("{\"tweets\":{");
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
            sb.deleteCharAt(sb.length()-1);
            sb.append("]},");
            i++;
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}}");
        //save the Data to File
        pWriter = new PrintWriter(new FileWriter(fileNumber+".json" ));
        pWriter.print(sb.toString());
        pWriter.flush();
        System.out.println("File saved");       
        i=1;
    }
}
