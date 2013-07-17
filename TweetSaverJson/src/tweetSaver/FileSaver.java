/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Lars
 */
public class FileSaver {

    int i = 1;
    PrintWriter pWriter;

    public void saveToJson(int fileNumber, List<TweetInterval> til) {
        StringBuilder sb = new StringBuilder("{\"tweets\":{");
        for (TweetInterval ti : til) {
            sb.append("\"").append(i).append("\" : { \"ids\" : ").append(ti.allIDs).append(",");
            sb.append("\"sources\" : ").append(ti.allSources).append(",");
            sb.append("\"geolocation\" :");
            for (int u = 0; u < ti.allLatitude.size(); u++) {
                if (u > 0) {
                    sb.append(",[").append(ti.allLatitude.get(u)).append(",").append(ti.allLongtitude.get(u)).append("]");
                } else {
                    sb.append("[").append(ti.allLatitude.get(u)).append(",").append(ti.allLongtitude.get(u)).append("]");
                }
            }
            sb.append("},");
            sb.append("\"hashtags\" : ").append(ti.allHashtags).append(",");
            sb.append("\"links\" : ").append(ti.allLinks).append(",");
            sb.append("\"timestamps\" : ").append(ti.allTimeStamps).append("},");
            i++;
        }
        //the last comma has to be deleted because of the json format
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb);

    }
}
