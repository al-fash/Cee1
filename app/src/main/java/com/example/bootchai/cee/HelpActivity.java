package com.example.bootchai.cee;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class HelpActivity extends ActionBarActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    RelativeLayout drawer;
    ImageView toActivityMain;
    LinearLayout toActivitySettings;
    LinearLayout toActivityHelp;
    LinearLayout toActivityAboutUs;

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
        setContentView(R.layout.activity_help);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_help_container, new HelpFragment())
                    .commit();
        }

        //****************************************************************************
        toolbar = (Toolbar) findViewById(R.id.help_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_help_drawerlayout);
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

        drawer = (RelativeLayout) findViewById(R.id.activity_help_drawer);
        drawer.setClickable(true);

        toActivityMain = (ImageView) findViewById(R.id.activity_help_space);
        toActivityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivitySettings = (LinearLayout) findViewById(R.id.activity_help_drawer_settings);
        toActivitySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityHelp = (LinearLayout) findViewById(R.id.activity_help_drawer_help);
        toActivityHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityAboutUs = (LinearLayout) findViewById(R.id.activity_help_drawer_aboutus);
        toActivityAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //****************************************************************************
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
