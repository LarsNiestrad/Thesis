/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lars
 */
public class TweetInterval {

    private List<Long> allIDs = new ArrayList<>();
    private List<Double> allLatitude = new ArrayList<>();
    private List<Double> allLongtitude = new ArrayList<>();
    private List<Integer> allSources = new ArrayList<>();
    private List<String> allHashtags = new ArrayList<>();
    private List<String> allLinks = new ArrayList<>();
    private List<Date> allTimeStamps = new ArrayList<>();
    private List<Integer> allFollowers = new ArrayList<>();
    private Map<String, Integer> cCodes = new HashMap<>();

    public TweetInterval() {
        //generate the CountryCodes map with 0 as default value for all countries
        try {
            BufferedReader br = new BufferedReader(new FileReader("CountryCodes.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                this.cCodes.put(line, 0);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //getter & setter
    public List<Long> getAllIDs() {
        return allIDs;
    }

    public void setID(long id) {
        this.allIDs.add(id);
    }

    public List<Double> getAllLatitude() {
        return allLatitude;
    }

    public void setLatitude(double latitude) {
        this.allLatitude.add(latitude);
    }

    public List<Double> getAllLongtitude() {
        return allLongtitude;
    }

    public void setLongtitude(double longtitude) {
        this.allLongtitude.add(longtitude);
    }

    public List<Integer> getAllSources() {
        return allSources;
    }

    public void setSource(int source) {
        this.allSources.add(source);
    }

    public List<String> getAllHashtags() {
        return allHashtags;
    }

    public void setHashtags(String hashtags) {
        this.allHashtags.add(hashtags);
    }

    public List<String> getAllLinks() {
        return allLinks;
    }

    public void setLinks(String links) {
        this.allLinks.add(links);
    }

    public List<Date> getAllTimeStamps() {
        return allTimeStamps;
    }

    public void setTimeStamp(Date timeStamps) {
        this.allTimeStamps.add(timeStamps);
    }

    public List<Integer> getAllFollowers() {
        return allFollowers;
    }

    public void setFollowers(int followers) {
        allFollowers.add(followers);
    }

    public Map<String, Integer> getcCodes() {
        return cCodes;
    }

    public void setcCode(String cCode) {
       System.out.println(this.cCodes.put(cCode, cCodes.get(cCode) + 1));
    }
}
