package com.example.bootchai.cee;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class AboutUsActivity extends ActionBarActivity {

    String LOG_TAG = MyUtility.LOG_TAG;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private RelativeLayout drawer;
    ImageView toActivityMain;
    private LinearLayout toActivitySettings;
    private LinearLayout toActivityHelp;
    private LinearLayout toActivityAboutUs;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        if (savedInstanceState == null) {

            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            aboutUsFragment.setOnEmailUsClickedListener(new AboutUsFragment.OnEmailUsClickedListener() {
                @Override
                public void onEmailUs() {
                    String[] TO = {"nbootcha@syr.edu"};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");


                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi!");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Email Us:"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Log.i(LOG_TAG, "No email client");
                    }
                }
            });



            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_aboutus_container, aboutUsFragment)
                    .commit();
        }

        //****************************************************************************
        toolbar = (Toolbar) findViewById(R.id.aboutus_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_aboutus_drawerlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerlayout_description_open, R.string.drawerlayout_description_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (RelativeLayout) findViewById(R.id.activity_aboutus_drawer);
        drawer.setClickable(true);

        toActivityMain = (ImageView) findViewById(R.id.activity_aboutus_space);
        toActivityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivitySettings = (LinearLayout) findViewById(R.id.activity_aboutus_drawer_settings);
        toActivitySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityHelp = (LinearLayout) findViewById(R.id.activity_aboutus_drawer_help);
        toActivityHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityAboutUs = (LinearLayout) findViewById(R.id.activity_aboutus_drawer_aboutus);
        toActivityAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawer);
            }
        });

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //****************************************************************************
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
