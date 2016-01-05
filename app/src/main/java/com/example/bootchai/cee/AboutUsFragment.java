package com.example.bootchai.cee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AboutUsFragment extends Fragment {

    String LOG_TAG = MyUtility.LOG_TAG;

    OnEmailUsClickedListener onEmailUsClickedListener;

    interface OnEmailUsClickedListener{
        public void onEmailUs();
    }

    public void setOnEmailUsClickedListener(OnEmailUsClickedListener oecl){
        onEmailUsClickedListener = oecl;
    }

    public AboutUsFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_fragment_aboutus_email){
            if(onEmailUsClickedListener != null) {
                onEmailUsClickedListener.onEmailUs();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);


        return rootView;
    }
}