package com.justjollyjonah.hiddenlol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HDLPostFragment extends Fragment implements AsyncResponse {
    public static final String ARG_TYPE = "arg_type";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Post[] Posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View rootView = inflater.inflate(R.layout.post_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.postList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        GetPosts posts = new GetPosts(Page.values()[getArguments().getInt(ARG_TYPE, 0)]);
        posts.delegate = this;
        posts.execute();

        return rootView;
    }

    @Override
    public void processFinish(Post[] output) {
        Posts = output;
        mAdapter = new PostAdapter(Posts, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private class GetPosts extends AsyncTask<Void, Void, Post[]> {

        public AsyncResponse delegate = null;
        public Page type;

        public GetPosts(Page type) {
            this.type = type;
        }

        @Override
        protected Post[] doInBackground(Void... urls) {
            ArrayList<Post> posts = new ArrayList<>();
            URL url = null;
            Log.i("Type",type.name());
            try {
                url = new URL("http://hiddenlol.com");
                switch (type) {
                    case Front:
                        url = new URL("http://hiddenlol.com");
                        break;
                    case Rising:
                        url = new URL("http://hiddenlol.com/rising");
                        break;
                    case Fresh:
                        url = new URL("http://hiddenlol.com/fresh");
                        break;
                }
                Log.i("Connection String", url.toString());
            }
            catch (MalformedURLException e) {

            }
            try {
                Document doc = Jsoup.connect(url.toString()).userAgent("Mozilla/5.0").get();
                Elements elements = doc.getElementsByClass("post-container");
                for (Element c: elements) {
                    int postId = Integer.parseInt(c.attr("data-id"));
                    boolean isVideo = false;
                    String mediaUrl = "";
                    Element image =  doc.getElementById("post-image-" + String.valueOf(postId));
                    if(image != null) {
                        mediaUrl = image.attr("src");
                    } else {
                        isVideo = true;
                        mediaUrl = doc.getElementById("post-video-" + String.valueOf(postId)).getElementsByAttributeValue("type", "video/mp4").first().attr("src");
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

}
