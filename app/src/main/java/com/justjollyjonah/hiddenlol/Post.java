package com.justjollyjonah.hiddenlol;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Post {
    public int Id;
    public String Title;
    public boolean IsVideo;
    public String ImageUrl;

    public Post(int id, String title, boolean video, String bitmap) {
        this.Id = id;
        this.Title = title;
        this.IsVideo = video;
        if(!video) {
            this.ImageUrl = bitmap;
        }
    }
}