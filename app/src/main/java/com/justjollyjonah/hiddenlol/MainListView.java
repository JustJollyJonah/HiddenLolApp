package com.justjollyjonah.hiddenlol;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainListView extends AppCompatActivity implements AsyncResponse {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Post[] Posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mRecyclerView = (RecyclerView) findViewById(R.id.postList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        GetPosts posts = new GetPosts();
        posts.delegate = this;
        try {
            URL url = new URL("http://www.hiddenlol.com");
            posts.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(Post[] output) {
        Posts = output;
        mAdapter = new PostAdapter(Posts);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class GetPosts extends AsyncTask<URL, Void, Post[]> {

        public AsyncResponse delegate = null;

        @Override
        protected Post[] doInBackground(URL... urls) {
            ArrayList<Post> posts = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(urls[0].toString()).get();
                Elements elements = doc.getElementsByClass("post-container");
                for (Element c: elements) {
                    int postId = Integer.parseInt(c.attr("data-id"));
                    boolean isVideo = false;
                    String mediaUrl = "";
                    Log.i("Post ID", String.valueOf(postId));
                    Log.i("Post Title", c.getElementById("post-"+ String.valueOf(postId)).getElementsByClass("title").first().text());
                    Element image =  doc.getElementById("post-image-" + String.valueOf(postId));
                    if(image != null) {
                        Log.i("Image source", image.attr("src"));
                        mediaUrl = image.attr("src");
                    } else {
                        isVideo = true;
                        mediaUrl = doc.getElementById("post-video-" + String.valueOf(postId)).getElementsByAttributeValue("type", "video/mp4").first().attr("src");
                        Log.i("Video source", mediaUrl);
                    }

                    posts.add(new Post(
                            postId,
                            c.getElementById("post-"+ String.valueOf(postId)).getElementsByClass("title").first().text(),
                            isVideo,
                            mediaUrl
                    ));
                }
            } catch (IOException e) {

            }
            Post[] postArray = new Post[posts.size()];
            return posts.toArray(postArray);
        }

        @Override
        protected void onPostExecute(Post[] result) {
            delegate.processFinish(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... somethhing) {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}