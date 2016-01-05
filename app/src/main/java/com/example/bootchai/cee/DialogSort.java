package com.example.bootchai.cee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

/**
 * Created by MankinG on 4/14/2015.
 */
public class DialogSort extends DialogFragment {
    private String LOG_TAG = MyUtility.LOG_TAG;
    private int sortbyPos = 0;
    private int orderbyPos = 0;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_sort, null);

        Spinner spSortby = (Spinner) v.findViewById(R.id.dialog_sort_sp_sortby);
        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"File name", "Last modified date"});
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSortby.setAdapter(stringArrayAdapter);

        Spinner spOrderby = (Spinner) v.findViewById(R.id.dialog_sort_sp_orderby);
        final ArrayAdapter<String> stringArrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"Ascending", "Descending"});
        stringArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrderby.setAdapter(stringArrayAdapter2);

        spSortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortbyPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOrderby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderbyPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(v)
                .setTitle("Sort notes")
                //.setMessage("Enter new title:")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getTargetFragment() != null) {
                                    Intent i = new Intent();
                                    i.putExtra("sortby", sortbyPos);
                                    i.putExtra("orderby", orderbyPos);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                                } else {
                                    Log.i(LOG_TAG, "no need to return results");
                                }
                            }
                        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(getTargetFragment() != null){
                    Intent i = new Intent();
                    i.putExtra("sortby", -1);
                    i.putExtra("orderby", -1);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                }else{
                    Log.i(LOG_TAG, "no need to return results");
                }
            }
        });

        return alertDialogBuilder.create();
    }
}
