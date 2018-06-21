package com.justjollyjonah.hiddenlol;

import java.net.MalformedURLException;
import java.net.URL;

public class Post {
    public int Id;
    public String Title;
    public boolean IsVideo;
    public URL ImageURL;

    public Post(int id, String title, boolean video, String URL) {
        this.Id = id;
        this.Title = title;
        this.IsVideo = video;
        if(!video) {
            try {
                this.ImageURL = new URL(URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}