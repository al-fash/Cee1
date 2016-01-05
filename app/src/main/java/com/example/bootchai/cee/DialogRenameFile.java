package com.example.bootchai.cee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by MankinG on 4/14/2015.
 */
public class DialogRenameFile extends DialogFragment {
    private String LOG_TAG = MyUtility.LOG_TAG;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String oldFileName = getArguments().getString("oldFileName");
        final int positionChecked = getArguments().getInt("positionChecked");

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_renamefile, null);
        final EditText etNewFileName = (EditText) v.findViewById(R.id.dialog_renamefile_et_newname);
        etNewFileName.setText(oldFileName);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(v)
                .setTitle("Rename note")
                .setMessage("Enter new title:")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(getTargetFragment() != null){
                                    Intent i = new Intent();
                                    i.putExtra("newFileName", etNewFileName.getText().toString());
                                    i.putExtra("positionChecked", positionChecked);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                                }else{
                                    Log.i(LOG_TAG, "no need to return results");
                                }
                            }
                        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(getTargetFragment() != null){
                    Intent i = new Intent();
                    i.putExtra("newFileName", oldFileName);
                    i.putExtra("positionChecked", positionChecked);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                }else{
                    Log.i(LOG_TAG, "no need to return results");
                }
            }
        });

        return alertDialogBuilder.create();
    }
}
