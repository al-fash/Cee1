package com.example.bootchai.cee;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MankinG on 4/13/2015.
 */
public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.ViewHolder>{
    private String LOG_TAG = MyUtility.LOG_TAG;
    private ArrayList<HashMap> qaList;
    public int helpTextSize;

    public MyRecyclerViewAdapter2(ArrayList<HashMap> fl, int hets){
        qaList = fl;
        helpTextSize = hets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_help_rv_entry, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HashMap qa = qaList.get(i);

        viewHolder.tvQ.setText((String) qa.get("q"));
        viewHolder.tvQ.setTextSize(helpTextSize);
        String a = (String) qa.get("a");
        if(a.equals("")){
            viewHolder.llA.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            viewHolder.llA.setVisibility(View.INVISIBLE);
        }else {
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lllp.setMargins(4, 4, 4, 8);
            viewHolder.llA.setLayoutParams(lllp);
            viewHolder.llA.setVisibility(View.VISIBLE);
            viewHolder.tvA.setText(a);
            viewHolder.tvA.setTextSize(helpTextSize);
        }
    }

    @Override
    public int getItemCount() {
        return qaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvQ;
        public TextView tvA;
        public LinearLayout llA;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            tvQ = (TextView) itemView.findViewById(R.id.fragment_help_rv_entry_tv_q);
            tvA = (TextView) itemView.findViewById(R.id.fragment_help_rv_entry_tv_a);
            llA = (LinearLayout) itemView.findViewById(R.id.fragment_help_rv_entry_ll_a);
        }
    }
}
