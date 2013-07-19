/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetSaver;



/**
 * Main class, starts the relevant Methods
 *
 * @author Lars
 */
public class MainClass {
    public static void main(String[] args)  {
        TweetSaver ts = new TweetSaver();
        ts.startTimer();
        ts.CollectData();
    }
}
