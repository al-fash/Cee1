package com.example.bootchai.cee;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by MankinG on 4/21/2015.
 */
public class SettingsFragment extends Fragment {

    private String LOG_TAG = MyUtility.LOG_TAG;

    private int homeTextSize;
    private boolean savedNotesAutoExport;
    private int helpTextSize;
    private String helpServerAddress;

    private SeekBar sbHomeTextSize;
    private Switch swSavedNotesAutoExport;
    private SeekBar sbHelpTextSize;
    private EditText etHelpServerAddress;

    private TextView tvHomeTextSizeShow;
    private TextView tvHelpTextSizeShow;

    private Button btDefault;

    private PreferencesViewChangedListener preferencesViewChangedListener;

    public interface PreferencesViewChangedListener{
        public void onPreferencesViewChanged(int hts, boolean snae, int hets, String hsa);
    }

    public void setPreferencesViewChangedListener(PreferencesViewChangedListener pvcl){
        preferencesViewChangedListener = pvcl;
    }

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        homeTextSize = getArguments().getInt("homeTextSize");
        savedNotesAutoExport = getArguments().getBoolean("savedNotesAutoExport");
        helpTextSize = getArguments().getInt("helpTextSize");
        helpServerAddress = getArguments().getString("helpServerAddress");

        sbHomeTextSize = (SeekBar) rootView.findViewById(R.id.fragment_settings_sb_hometextsize);
        swSavedNotesAutoExport = (Switch) rootView.findViewById(R.id.fragment_settings_sw_autoexport);
        sbHelpTextSize = (SeekBar) rootView.findViewById(R.id.fragment_settings_sb_helptextsize);
        etHelpServerAddress = (EditText) rootView.findViewById(R.id.fragment_settings_et_helpserveraddress);
        tvHomeTextSizeShow = (TextView) rootView.findViewById(R.id.fragment_settings_tv_hometextsizeshow);
        tvHelpTextSizeShow = (TextView) rootView.findViewById(R.id.fragment_settings_tv_helptextsizeshow);
        btDefault = (Button) rootView.findViewById(R.id.fragment_settings_bt_default);

        sbHomeTextSize.setProgress(homeTextSize - 12);
        swSavedNotesAutoExport.setChecked(savedNotesAutoExport);
        sbHelpTextSize.setProgress(helpTextSize - 12);
        etHelpServerAddress.setText(helpServerAddress);
        tvHomeTextSizeShow.setText("[" + homeTextSize + "]");
        tvHelpTextSizeShow.setText("[" + helpTextSize + "]");

        sbHomeTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHomeTextSizeShow.setText("[" + (progress + 12) + "]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbHelpTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHelpTextSizeShow.setText("[" + (progress + 12) + "]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbHomeTextSize.setProgress(MyUtility.defaultHomeTextSize - 12);
                swSavedNotesAutoExport.setChecked(MyUtility.defaultSavedNotesAutoExport);
                sbHelpTextSize.setProgress(MyUtility.defaultHelpTextSize - 12);
                etHelpServerAddress.setText(MyUtility.defaultHelpServerAddress);

                tvHomeTextSizeShow.setText("[" + MyUtility.defaultHomeTextSize + "]");
                tvHelpTextSizeShow.setText("[" + MyUtility.defaultHelpTextSize + "]");
            }
        });

        /*Log.i(LOG_TAG, "homeTextSize = " + homeTextSize);
        Log.i(LOG_TAG, "savedNotesAutoExport = " + savedNotesAutoExport);
        Log.i(LOG_TAG, "helpTextSize = " + helpTextSize);
        Log.i(LOG_TAG, "helpServerAddress = " + helpServerAddress);*/

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        homeTextSize = sbHomeTextSize.getProgress() + 12;
        savedNotesAutoExport = swSavedNotesAutoExport.isChecked();
        helpTextSize = sbHelpTextSize.getProgress() + 12;
        helpServerAddress = etHelpServerAddress.getText().toString();

        preferencesViewChangedListener.onPreferencesViewChanged(homeTextSize, savedNotesAutoExport, helpTextSize, helpServerAddress);
    }
}
