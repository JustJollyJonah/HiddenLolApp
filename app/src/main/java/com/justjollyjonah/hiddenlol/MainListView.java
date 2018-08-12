package com.justjollyjonah.hiddenlol;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
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

public class MainListView extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    private List<HDLPostFragment> fragments = new ArrayList<>(3);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottombaritem_front:
                        switchFragment(0, "iets");
                        Log.i("Switched List", "Front");
                        break;
                    case R.id.bottombaritem_fresh:
                        switchFragment(1, "iets");
                        Log.i("Switched List", "Fresh");
                        break;
                    case R.id.bottombaritem_rising:
                        switchFragment(2, "iets");
                        Log.i("Switched List", "Rising");
                        break;
                }
                return true;
            }
        });

        buildFragmentsList();

        switchFragment(0, "iets");

    }

    private void buildFragmentsList() {
        HDLPostFragment frontFragment = buildFragment(Page.Front);
        HDLPostFragment freshFragment = buildFragment(Page.Fresh);
        HDLPostFragment risingFragment = buildFragment(Page.Rising);

        fragments.add(frontFragment);
        fragments.add(freshFragment);
        fragments.add(risingFragment);
    }

    private HDLPostFragment buildFragment(Page type) {
        HDLPostFragment fragment = new HDLPostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("arg_type", type.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void switchFragment(int pos, String tag) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in,android.R.anim.fade_out);
        ft.replace(R.id.frame_fragmentholder, fragments.get(pos), tag);
        ft.commit();
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