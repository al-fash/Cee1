package com.example.bootchai.cee;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity {

    ViewPager viewPager;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
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
        setContentView(R.layout.activity_main);
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }*/

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), 2);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    myFragmentPagerAdapter.homeFragment.onResume_custom();
                    myFragmentPagerAdapter.savedNoteFragment.onPause_custom();
                }else if(position == 1){
                    myFragmentPagerAdapter.savedNoteFragment.onResume_custom();
                    myFragmentPagerAdapter.homeFragment.onPause_custom();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //****************************************************************************
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawerlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerlayout_description_open, R.string.drawerlayout_description_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                int currentViewPagerItem = viewPager.getCurrentItem();
                if (currentViewPagerItem == 0){
                    myFragmentPagerAdapter.homeFragment.onPause_custom();
                }else if(currentViewPagerItem == 1){
                    myFragmentPagerAdapter.savedNoteFragment.onPause_custom();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                int currentViewPagerItem = viewPager.getCurrentItem();
                if (currentViewPagerItem == 0){
                    myFragmentPagerAdapter.homeFragment.onResume_custom();
                }else if(currentViewPagerItem == 1){
                    myFragmentPagerAdapter.savedNoteFragment.onResume_custom();
                }
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (RelativeLayout) findViewById(R.id.activity_main_drawer);
        drawer.setClickable(true);

        toActivityMain = (ImageView) findViewById(R.id.activity_main_space);
        toActivityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivitySettings = (LinearLayout) findViewById(R.id.activity_main_drawer_settings);
        toActivitySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityHelp = (LinearLayout) findViewById(R.id.activity_main_drawer_help);
        toActivityHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitySettings = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intentToActivitySettings);
                drawerLayout.closeDrawer(drawer);
            }
        });

        toActivityAboutUs = (LinearLayout) findViewById(R.id.activity_main_drawer_aboutus);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

}
