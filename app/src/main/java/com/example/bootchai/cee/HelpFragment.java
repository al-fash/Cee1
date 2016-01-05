package com.example.bootchai.cee;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class HelpFragment extends Fragment {

    private String LOG_TAG = MyUtility.LOG_TAG;

    ArrayList qaPosts;
    private MyRecyclerViewAdapter2 myRecyclerViewAdapter;
    private RecyclerView rvQaPosts;
    private RecyclerView.LayoutManager layoutManager;

    private EditText etHelp;
    private CardView cvHelp;

    public int helpTextSize;
    private String helpServerAddress;

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences ceePref = getActivity().getSharedPreferences(MyUtility.preferencesFileName, getActivity().MODE_PRIVATE);
        helpTextSize = ceePref.getInt("helpTextSize", MyUtility.defaultHelpTextSize);
        helpServerAddress = ceePref.getString("helpServerAddress", MyUtility.defaultHelpServerAddress);
        //helpServerAddress = MyUtility.defaultHelpServerAddress;

        //Toast.makeText(getActivity(), "onResume()", Toast.LENGTH_SHORT).show();

        myRecyclerViewAdapter.helpTextSize = helpTextSize;
        myRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences ceePref = getActivity().getSharedPreferences(MyUtility.preferencesFileName, getActivity().MODE_PRIVATE);
        helpTextSize = ceePref.getInt("helpTextSize", MyUtility.defaultHelpTextSize);
        helpServerAddress = ceePref.getString("helpServerAddress", MyUtility.defaultHelpServerAddress);
        //helpServerAddress = MyUtility.defaultHelpServerAddress;

        qaPosts = new ArrayList();
    }

    public HelpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.d(LOG_TAG, "in OnCreatView");

        final View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        rvQaPosts = (RecyclerView) rootView.findViewById(R.id.fragment_help_rv_qaposts);
        etHelp = (EditText) rootView.findViewById(R.id.fragment_help_et_in);
        cvHelp = (CardView) rootView.findViewById(R.id.fragment_help_cv_help);

        rvQaPosts.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvQaPosts.setLayoutManager(layoutManager);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter2(qaPosts, helpTextSize);
        rvQaPosts.setAdapter(myRecyclerViewAdapter);

        executeDownloadJSON();

        cvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String escapedHelp = URLEncoder.encode(etHelp.getText().toString(), "utf-8");

                    MyUploadQAsyncTask downloadJson = new MyUploadQAsyncTask();
                    String url = helpServerAddress + "/test2/QandA/" + escapedHelp;
                    downloadJson.execute(new String[]{url});

                    cvHelp.setCardBackgroundColor(getResources().getColor(R.color.me_press));
                }catch(UnsupportedEncodingException e){
                    Log.i(LOG_TAG, "UnsupportedEncodingException: for uploading question");
                }
            }
        });

        return  rootView;
    }

    private void executeDownloadJSON(){
        MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(myRecyclerViewAdapter);
        String url = helpServerAddress + "/test2/QandA/";
        downloadJson.execute(new String[]{url});
    }

    private class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, ArrayList<HashMap>> {
        private final WeakReference<MyRecyclerViewAdapter2> adapterWeakReference;
        public MyDownloadJsonAsyncTask(MyRecyclerViewAdapter2 adapter){
            adapterWeakReference = new WeakReference<MyRecyclerViewAdapter2>(adapter);
        }

        @Override
        protected ArrayList doInBackground(String... urls) {
            ArrayList qaPosts = null;
            for(String url : urls){
                qaPosts = MyUtility.downloadQADataJson(url);
            }
            return qaPosts;
        }

        @Override
        protected void onPostExecute(ArrayList qaPostsTemp) {
            qaPosts.clear();
            qaPosts.addAll(qaPostsTemp);

            /*for(int i = 0 ; i < qaPosts.size() ; i++){
                Log.i(LOG_TAG, ((HashMap) (qaPosts.get(i))).get("name").toString());
            }*/

            if(adapterWeakReference != null){
                //System.out.println("+++ adapterWeakReference != null");
                final MyRecyclerViewAdapter2 adapter = adapterWeakReference.get();
                if(adapter != null){
                    //System.out.println("+++ adapter != null");
                    adapter.notifyDataSetChanged();
                    rvQaPosts.scrollToPosition(myRecyclerViewAdapter.getItemCount() - 1);
                }else{
                    //System.out.println("+++ adapter == null");
                }
            }else{
                //System.out.println("+++ adapterWeakReference == null");
            }
        }
    }

    private class MyUploadQAsyncTask extends AsyncTask<String, Void, String> {
        public MyUploadQAsyncTask(){
        }

        @Override
        protected String doInBackground(String... urls) {
            String returnedString = null;
            for(String url : urls){
                returnedString = MyUtility2.downloadJSON(url);
            }
            return returnedString;
        }

        @Override
        protected void onPostExecute(String returnedString) {
            if(returnedString != null){
                int endPosition = returnedString.length();
                if(endPosition > 10){
                    endPosition = 10;
                }
                Toast.makeText(getActivity(), "Posted: " + returnedString.substring(0, endPosition) + "...", Toast.LENGTH_SHORT).show();
                etHelp.setText("");
                cvHelp.setCardBackgroundColor(getResources().getColor(R.color.me_default));
                executeDownloadJSON();
            }
        }
    }
}
