package com.justjollyjonah.hiddenlol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Post[] Posts;
    private Context Context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public ProgressBar loader;
        public PlayerView video;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.post_title);
            image = (ImageView) itemView.findViewById(R.id.post_image);
            loader = (ProgressBar) itemView.findViewById(R.id.post_loader);
            video = (PlayerView) itemView.findViewById(R.id.post_video);
        }
    }

    public PostAdapter(Post[] posts, Context context) {
        Posts = posts;
        Context = context;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder vh) {
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.loader.setVisibility(View.VISIBLE);
        holder.image.setVisibility(View.VISIBLE);
        final ProgressBar progressView = holder.loader;

        holder.title.setText(Posts[position].Title);
        if(!Posts[position].IsVideo) {
            Picasso.get().load(Posts[position].ImageUrl).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    progressView.setVisibility(View.GONE);
                    holder.video.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(Context, trackSelector);

            holder.video.setPlayer(player);

            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                    "Mozilla/5.0",
                    null /* listener */,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true /* allowCrossProtocolRedirects */
            );

            DefaultBandwidthMeter bandwidthMeter2 = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Context, null, httpDataSourceFactory);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(Posts[position].ImageUrl));
            player.prepare(videoSource);
            holder.loader.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Posts.length;
    }
}
