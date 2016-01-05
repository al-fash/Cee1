package com.example.bootchai.cee;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class SavedNoteFragment extends Fragment implements HomeFragment.ButtonClicked, MyRecyclerViewAdapter.RecyclerViewItemClickedListener{
    public boolean savedNotesAutoExport;

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences ceePref = getActivity().getSharedPreferences(MyUtility.preferencesFileName, getActivity().MODE_PRIVATE);
        savedNotesAutoExport = ceePref.getBoolean("savedNotesAutoExport", MyUtility.defaultSavedNotesAutoExport);

        //Toast.makeText(getActivity(), "savedNotesAutoExport = " + savedNotesAutoExport, Toast.LENGTH_SHORT).show();
        //Log.i(LOG_TAG, "savedNotesAutoExport = " + savedNotesAutoExport);
    }

    @Override
    public void onSavedNoteButtonClicked(String fileName, String lastModifiedDateString) {
        HashMap file = new HashMap();
        String fileNameWithoutExtension = fileName.substring(0, fileName.length() - 4);
        file.put("fileName", fileNameWithoutExtension);

        String displayedFilesName;
        if(fileNameWithoutExtension.length() > 20){
            displayedFilesName = fileNameWithoutExtension.substring(0, 19) + "...";

        }else{
            displayedFilesName = fileNameWithoutExtension;
        }
        file.put("displayedFileName", displayedFilesName);

        file.put("checked", false);
        file.put("lastModifiedDate", lastModifiedDateString);

        filesList.add(0, file);
        allFilesListCache.add(0, file);

        if(myRecyclerViewAdapter != null){
            myRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private String LOG_TAG = MyUtility.LOG_TAG;
    private ArrayList<HashMap> filesList;
    private ArrayList<HashMap> allFilesListCache;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView rvFilesList;
    private RecyclerView.LayoutManager layoutManager;
    private ActionMode filesCheckedCAB;

    public SavedNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        File folder = MyUtility.documentStorageDir;
        File[] files = folder.listFiles();

        filesList = new ArrayList();
        String fileName;
        String ext;
        HashMap fileHashMap;
        String fileNameWithoutExtension;
        for(int i = 0 ; i < files.length ; i++){
            File file = files[i];
            fileName = file.getName();
            ext = fileName.substring(fileName.length() - 4);
            if(ext.equals(".txt")){
                //Log.d(LOG_TAG, "FileName: " + fileName);
                fileHashMap = new HashMap();
                fileNameWithoutExtension = fileName.substring(0, fileName.length() - 4);
                fileHashMap.put("fileName", fileNameWithoutExtension);

                String displayedFilesName;
                if(fileNameWithoutExtension.length() > 20){
                    displayedFilesName = fileNameWithoutExtension.substring(0, 19) + "...";

                }else{
                    displayedFilesName = fileNameWithoutExtension;
                }
                fileHashMap.put("displayedFileName", displayedFilesName);

                fileHashMap.put("checked", false);
                String lastModifiedDateString = MyUtility.getLastModifiedDateString(file);
                fileHashMap.put("lastModifiedDate", lastModifiedDateString);

                filesList.add(fileHashMap);
            }
        }

        allFilesListCache = (ArrayList<HashMap>) filesList.clone();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_savednote, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_fragment_home_search).getActionView();

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    filesList.clear();
                    filesList.addAll((ArrayList<HashMap>) allFilesListCache.clone());
                    ArrayList<HashMap> filesToRemove = new ArrayList<HashMap>();
                    for(int i = 0 ; i < filesList.size() ; i++){
                        HashMap fileHashMapTemp = filesList.get(i);
                        String fileName = (String) fileHashMapTemp.get("fileName");
                        if(!fileName.contains(s)){
                            filesToRemove.add(fileHashMapTemp);
                        }else{
                            //Log.i(LOG_TAG, "found = " + fileName);
                        }
                    }
                    filesList.removeAll(filesToRemove);
                    myRecyclerViewAdapter.notifyDataSetChanged();

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
            searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    //Log.i(LOG_TAG, "in OnViewAttachedToWindow()");
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    //Log.i(LOG_TAG, "in OnViewDetachedFromWindow()");

                    filesList.clear();
                    filesList.addAll((ArrayList<HashMap>) allFilesListCache.clone());
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_fragment_home_selectall){
            for(int i = 0 ; i < filesList.size() ; i++) {
                HashMap fileTemp = filesList.get(i);
                fileTemp.put("checked", true);
            }
            myRecyclerViewAdapter.filesCheckedCount = filesList.size();
            myRecyclerViewAdapter.notifyDataSetChanged();
            onItemClicked(filesList.size());
        }else if(id == R.id.action_fragment_home_selectnone){
            for(int i = 0 ; i < filesList.size() ; i++){
                HashMap fileTemp = filesList.get(i);
                fileTemp.put("checked", false);
            }
            myRecyclerViewAdapter.filesCheckedCount = 0;
            myRecyclerViewAdapter.notifyDataSetChanged();
            if(filesCheckedCAB != null){
                filesCheckedCAB.finish();
            }
        }else if(id == R.id.action_fragment_home_sort){
            DialogSort dialogSort = new DialogSort();
            dialogSort.setTargetFragment(this, 1);
            dialogSort.show(getFragmentManager(), "SavedNoteFragment_DialogSort");

            //MyUtility.sortFilesList(filesList, 0, 0);
            //myRecyclerViewAdapter.notifyDataSetChanged();
            /*if(filesCheckedCAB != null){
                filesCheckedCAB.finish();
            }*/
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d(LOG_TAG, "in OnCreatView");

        final View rootView = inflater.inflate(R.layout.fragment_saved_note, container, false);

        rvFilesList = (RecyclerView) rootView.findViewById(R.id.savednote_filelist);
        rvFilesList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvFilesList.setLayoutManager(layoutManager);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, filesList);
        rvFilesList.setAdapter(myRecyclerViewAdapter);

        return  rootView;
    }

    @Override
    public void onItemClicked(int filesCheckedCount) {

        if(filesCheckedCount == 0) {
            if(filesCheckedCAB != null){
                filesCheckedCAB.finish();
            }
        }else if(filesCheckedCount == 1) {
            filesCheckedCAB = ((ActionBarActivity)getActivity()).startSupportActionMode(new CABCallBack());
        }else if(filesCheckedCount > 1) {
            filesCheckedCAB = ((ActionBarActivity)getActivity()).startSupportActionMode(new CABCallBack2(filesCheckedCount));
        }
    }

    @Override
    public void onFileNameLongClicked(int positionChecked) {
        showDialogRenameFile(positionChecked);
    }

    @Override
    public void onItemLongClicked(int positionChecked) {
    }

    public void onResume_custom() {
        if(myRecyclerViewAdapter != null) {
            if(myRecyclerViewAdapter.filesCheckedCount == 0) {
                if(filesCheckedCAB != null){
                    filesCheckedCAB.finish();
                }
            }else if(myRecyclerViewAdapter.filesCheckedCount == 1) {
                filesCheckedCAB = ((ActionBarActivity)getActivity()).startSupportActionMode(new CABCallBack());
            }else if(myRecyclerViewAdapter.filesCheckedCount > 1) {
                filesCheckedCAB = ((ActionBarActivity)getActivity()).startSupportActionMode(new CABCallBack2(myRecyclerViewAdapter.filesCheckedCount));
            }
        }
    }

    public void onPause_custom() {
        if(filesCheckedCAB != null){
            filesCheckedCAB.finish();
            //Log.i(LOG_TAG, "filesCheckedCAB.finish()");
        }
    }

    public class CABCallBack2 implements ActionMode.Callback{
        private int filesCheckedCount;

        public CABCallBack2(int fcc){
            filesCheckedCount = fcc;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_fragment_savednote_itemchecked_cab, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Selected : " + filesCheckedCount);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            if(id == R.id.action_fragment_home_itemchecked_cab_export){

            }else if(id == R.id.action_fragment_home_itemchecked_cab_delete){
                ArrayList<HashMap> filesToDelete = new ArrayList();

                for(int i = 0 ; i < filesList.size() ; i++) {
                    HashMap fileTemp = filesList.get(i);
                    String fileName = (String) fileTemp.get("fileName");
                    if(!((boolean) fileTemp.get("checked"))){
                        //Log.i(LOG_TAG, "file not checked = " + fileName);
                        continue;
                    }
                    //Log.i(LOG_TAG, "file to delete = " + fileName);
                    if(MyUtility.deleteFile(fileName)) {
                        filesToDelete.add(fileTemp);
                    }
                }

                filesList.removeAll(filesToDelete);
                myRecyclerViewAdapter.filesCheckedCount = 0;
                myRecyclerViewAdapter.notifyDataSetChanged();
                filesCheckedCAB.finish();
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }

    public class CABCallBack implements ActionMode.Callback{
        private int positionChecked;
        private ShareActionProvider shareActionProvider;

        public CABCallBack(){
            for(int i = 0 ; i < filesList.size() ; i++) {
                HashMap fileTemp = filesList.get(i);
                boolean fileChecked = (boolean) fileTemp.get("checked");
                if(fileChecked){
                    positionChecked = i;
                    break;
                }
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_fragment_savednote_itemchecked_cab2, menu);

            MenuItem shareItem = menu.findItem(R.id.action_fragment_home_itemchecked_cab2_share);
            shareActionProvider = new ShareActionProvider(getActivity());
            MenuItemCompat.setActionProvider(shareItem, shareActionProvider);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle((String) filesList.get(positionChecked).get("fileName"));
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            if(id == R.id.action_fragment_home_itemchecked_cab2_edit){
                try {
                    HashMap fileTemp = filesList.get(positionChecked);
                    String fileName = (String) fileTemp.get("fileName");

                    Intent share_intent = new Intent(Intent.ACTION_VIEW);
                    share_intent.setDataAndType(Uri.parse("file://" + MyUtility.documentStorageDir + "/" + fileName + ".txt"), "text/plain");
                    startActivity(share_intent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(getActivity(), "No text editor found", Toast.LENGTH_SHORT).show();
                }

            }else if(id == R.id.action_fragment_home_itemchecked_cab2_rename){
                showDialogRenameFile(positionChecked);
                HashMap fileTemp = filesList.get(positionChecked);
                fileTemp.put("checked", false);
                myRecyclerViewAdapter.filesCheckedCount = 0;
                filesCheckedCAB.finish();
            }else if(id == R.id.action_fragment_home_itemchecked_cab2_delete){
                HashMap fileTemp = filesList.get(positionChecked);
                String fileName = (String) fileTemp.get("fileName");
                if(MyUtility.deleteFile(fileName)) {
                    filesList.remove(fileTemp);
                    myRecyclerViewAdapter.filesCheckedCount = 0;
                    myRecyclerViewAdapter.notifyItemRemoved(positionChecked);
                }

                filesCheckedCAB.finish();
            }else if(id == R.id.action_fragment_home_itemchecked_cab2_share){
                if(shareActionProvider != null) {
                    HashMap fileTemp = filesList.get(positionChecked);
                    String fileName = (String) fileTemp.get("fileName");

                    /*
                    Intent intentShare = new Intent(Intent.ACTION_SEND);
                    intentShare.setType("text/plain");
                    intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + MyUtility.documentStorageDir + "/" + fileName + ".txt"));
                    shareActionProvider.setShareIntent(intentShare);*/

                    String name = "Cee_" + fileName;
                    String content = MyUtility.openFile(fileName);

                    if(content != null) {
                        Intent share_intent = new Intent(android.content.Intent.ACTION_SEND);
                        share_intent.setType("text/plain");
                        share_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, name);
                        share_intent.putExtra(android.content.Intent.EXTRA_TEXT, content);

                        shareActionProvider.setShareIntent(share_intent);
                    }else{
                        Toast.makeText(getActivity(), "Cannot retrieve the content of the file", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i(LOG_TAG, "shareActionProvider == null");
                }
            }else if(id == R.id.action_fragment_home_itemchecked_cab2_export){

            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }

    private void showDialogRenameFile(int positionChecked){
        HashMap fileTemp = filesList.get(positionChecked);
        String oldFileName = (String) fileTemp.get("fileName");

        DialogRenameFile dialogRenameFile = new DialogRenameFile();
        Bundle args = new Bundle();
        args.putString("oldFileName", oldFileName);
        args.putInt("positionChecked", positionChecked);
        dialogRenameFile.setArguments(args);

        dialogRenameFile.setTargetFragment(this, 0);
        dialogRenameFile.show(getFragmentManager(), "SavedNoteFragment_DialogRename");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            //DialogRenameFile
            if(resultCode != Activity.RESULT_OK) return;
            String potentialNewFileName = data.getStringExtra("newFileName");
            int positionChecked = data.getIntExtra("positionChecked", -1);

            if(positionChecked > -1) {
                HashMap fileTemp = filesList.get(positionChecked);
                String oldFileName = (String) fileTemp.get("fileName");
                String[] newFileInfo = MyUtility.renameFile(oldFileName, potentialNewFileName);
                fileTemp.put("fileName", newFileInfo[0]);

                String displayedFilesName;
                if(newFileInfo[0].length() > 20){
                    displayedFilesName = newFileInfo[0].substring(0, 19) + "...";

                }else{
                    displayedFilesName = newFileInfo[0];
                }
                fileTemp.put("displayedFileName", displayedFilesName);

                fileTemp.put("lastModifiedDate", newFileInfo[1]);

                myRecyclerViewAdapter.notifyItemChanged(positionChecked);
            }else{
                Log.i(LOG_TAG, "Cannot get position of the file back from DialogRename");
            }
        }else if(requestCode == 1){
            //DialogSort
            if(resultCode != Activity.RESULT_OK) return;

            int sortby = data.getIntExtra("sortby", -1);
            int orderby = data.getIntExtra("orderby", -1);

            if(sortby == -1 || orderby == -1){
                return;
            }
            MyUtility.sortFilesList(filesList, sortby, orderby);
            myRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
