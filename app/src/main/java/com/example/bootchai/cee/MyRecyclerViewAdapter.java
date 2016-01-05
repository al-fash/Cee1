package com.example.bootchai.cee;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MankinG on 4/13/2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
    private String LOG_TAG = MyUtility.LOG_TAG;
    private ArrayList<HashMap> filesList;
    public int filesCheckedCount = 0;

    private RecyclerViewItemClickedListener rvItemClickedListener;

    public interface RecyclerViewItemClickedListener{
        public void onItemClicked(int filesCheckedCount);
        public void onItemLongClicked(int positionChecked);
        public void onFileNameLongClicked(int positionChecked);
    }

    public MyRecyclerViewAdapter(RecyclerViewItemClickedListener rvicl, ArrayList<HashMap> fl){
        rvItemClickedListener = rvicl;
        filesList = fl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_saved_note_rv_entry, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HashMap fileInfo = filesList.get(i);
        viewHolder.tvFileName.setText((String) fileInfo.get("displayedFileName"));
        viewHolder.tvLastModifiedDate.setText((String) fileInfo.get("lastModifiedDate"));
        viewHolder.cbChecked.setChecked((boolean) fileInfo.get("checked"));

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFileName;
        public TextView tvLastModifiedDate;
        public CheckBox cbChecked;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            tvFileName = (TextView) itemView.findViewById(R.id.fragment_saved_note_rv_entry_tv_filename);
            tvLastModifiedDate = (TextView) itemView.findViewById(R.id.fragment_saved_note_rv_entry_tv_lastmodifieddate);
            cbChecked = (CheckBox) itemView.findViewById(R.id.fragment_saved_note_rv_entry_cb_checked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap fileTemp = filesList.get(getPosition());

                    boolean oldCheckedFileTemp = (boolean) fileTemp.get("checked");

                    //Log.i(LOG_TAG, "lastClickedposition = " + getPosition());

                    if(!oldCheckedFileTemp){
                        filesCheckedCount++;
                    }else{
                        filesCheckedCount--;
                    }

                    fileTemp.put("checked", !oldCheckedFileTemp);
                    cbChecked.setChecked(!oldCheckedFileTemp);

                    rvItemClickedListener.onItemClicked(filesCheckedCount);
                    //Log.i(LOG_TAG, "count = " + filesCheckedCount);
                }
            });

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rvItemClickedListener.onItemLongClicked(getPosition());
                    return true;
                }
            });
            tvFileName.setLongClickable(true);
            tvFileName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rvItemClickedListener.onFileNameLongClicked(getPosition());
                    return true;
                }
            });
        }
    }
}
