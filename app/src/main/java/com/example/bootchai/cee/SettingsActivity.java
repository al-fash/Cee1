package com.example.bootchai.cee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
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

import java.util.HashMap;


public class SettingsActivity extends ActionBarActivity {
    private String LOG_TAG = MyUtility.LOG_TAG;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private RelativeLayout drawer;
    ImageView toActivityMain;
    private LinearLayout toActivitySettings;
    private LinearLayout toActivityHelp;
    private LinearLayout toActivityAboutUs;

    private int homeTextSize;
    private boolean savedNotesAutoExport;
    private int helpTextSize;
    private String helpServerAddress;

    private SharedPreferences ceePref;

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

        ceePref = getSharedPreferences(MyUtility.preferencesFileName, MODE_PRIVATE);
        homeTextSize = ceePref.getInt("homeTextSize", MyUtility.defaultHomeTextSize);
        savedNotesAutoExport = ceePref.getBoolean("savedNotesAutoExport", MyUtility.defaultSavedNotesAutoExport);
        helpTextSize = ceePref.getInt("helpTextSize", MyUtility.defaultHelpTextSize);
        helpServerAddress = ceePref.getString("helpServerAddress", MyUtility.defaultHelpServerAddress);

        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            SettingsFragment settingsFragment = new SettingsFragment();

            Bundle args = new Bundle();
            args.putInt("homeTextSize", homeTextSize);
            args.putBoolean("savedNotesAutoExport", savedNotesAutoExport);
            args.putInt("helpTextSize", helpTextSize);
            args.putString("helpServerAddress", helpServerAddress);

            settingsFragment.setArguments(args);

            settingsFragment.setPreferencesViewChangedListener(new SettingsFragment.PreferencesViewChangedListener() {
                @Override
                public void onPreferencesViewChanged(int hts, boolean snae, int hets, String hsa) {
                    homeTextSize = hts;
                    savedNotesAutoExport = snae;
                    helpTextSize = hets;
                    helpServerAddress = hsa;
                }
            });

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_settings_container, settingsFragment)
                    .commit();
        }


        //****************************************************************************
        toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_settings_drawerlayout);
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

        drawer = (RelativeLayout) findViewById(R.id.activity_settings_drawer);
        drawer.setClickable(true);

        toActivityMain = (ImageView) findViewById(R.id.activity_settings_space);
        toActivityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivitySettings = (LinearLayout) findViewById(R.id.activity_settings_drawer_settings);
        toActivitySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityHelp = (LinearLayout) findViewById(R.id.activity_settings_drawer_help);
        toActivityHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityAboutUs = (LinearLayout) findViewById(R.id.activity_settings_drawer_aboutus);
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
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ceePrefEditor = ceePref.edit();
        ceePrefEditor.putInt("homeTextSize", homeTextSize);
        ceePrefEditor.putBoolean("savedNotesAutoExport", savedNotesAutoExport);
        ceePrefEditor.putInt("helpTextSize", helpTextSize);
        ceePrefEditor.putString("helpServerAddress", helpServerAddress);

        ceePrefEditor.commit();
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
