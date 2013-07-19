/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    
}
