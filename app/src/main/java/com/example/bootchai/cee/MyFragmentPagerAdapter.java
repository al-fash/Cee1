package com.example.bootchai.cee;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    public HomeFragment homeFragment;
    public SavedNoteFragment savedNoteFragment;
    public int size;



    public MyFragmentPagerAdapter(FragmentManager fm, int s) {
        super(fm);
        size = s;

        savedNoteFragment = new SavedNoteFragment();
        homeFragment = new HomeFragment();
        homeFragment.setOnButtonClickedListener(savedNoteFragment);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return homeFragment;
        }else{
            return savedNoteFragment;
        }
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "HOME";
        }else{
            return "SAVED NOTES";
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
