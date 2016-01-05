package com.example.bootchai.cee;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MankinG on 4/12/2015.
 */
public class MyUtility {
    public static String LOG_TAG = "Cee_MainActivity";
    public static File documentStorageDir = getDocumentStorageDir("Cee");

    public static final int defaultHomeTextSize = 14;
    public static final boolean defaultSavedNotesAutoExport = false;
    public static final int defaultHelpTextSize = 14;
    public static final String defaultHelpServerAddress = "http://192.168.29.117";

    public static final String preferencesFileName = "ceePref";

    public static String openFile(String fileName){
        String text = "";
        try {
            File file = new File(MyUtility.documentStorageDir, fileName + ".txt");
            BufferedReader input = new BufferedReader(new FileReader(file));

            String dummy;
            while((dummy = input.readLine()) != null) {
                text += dummy + "\n";
            }

            input.close();
            return text;
        }catch(FileNotFoundException e){
            Log.i(LOG_TAG, "In openFile: File not found");
            return null;
        }catch (IOException e){
            Log.i(LOG_TAG, "In openFile: IO exception");
            return null;
        }

    }

    public static String getLastModifiedDateString(File file){
        Date lastModifiedDate = new Date(file.lastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
        String lastModifiedDateString = simpleDateFormat.format(lastModifiedDate);

        return lastModifiedDateString;
    }

    public static void sortFilesList(ArrayList<HashMap> filesList, int sortby, int orderby){

        if(sortby == 0 && orderby == 0) {
            Collections.sort(filesList, new Comparator<HashMap>() {
                @Override
                public int compare(HashMap lhs, HashMap rhs) {
                    String lhsFileName = (String) lhs.get("fileName");
                    String rhsFileName = (String) rhs.get("fileName");
                    return lhsFileName.compareTo(rhsFileName);
                }
            });
        }else if(sortby == 1 && orderby == 0) {
            Collections.sort(filesList, new Comparator<HashMap>() {
                @Override
                public int compare(HashMap lhs, HashMap rhs) {
                    String lhsFileName = (String) lhs.get("lastModifiedDate");
                    String rhsFileName = (String) rhs.get("lastModifiedDate");
                    return lhsFileName.compareTo(rhsFileName);
                }
            });
        }else if(sortby == 0 && orderby == 1) {
            Collections.sort(filesList, new Comparator<HashMap>() {
                @Override
                public int compare(HashMap lhs, HashMap rhs) {
                    String lhsFileName = (String) lhs.get("fileName");
                    String rhsFileName = (String) rhs.get("fileName");
                    return rhsFileName.compareTo(lhsFileName);
                }
            });
        }else {
            Collections.sort(filesList, new Comparator<HashMap>() {
                @Override
                public int compare(HashMap lhs, HashMap rhs) {
                    String lhsFileName = (String) lhs.get("lastModifiedDate");
                    String rhsFileName = (String) rhs.get("lastModifiedDate");
                    return rhsFileName.compareTo(lhsFileName);
                }
            });
        }

    }

    public static boolean deleteFile(String fileName){
        File file = new File(MyUtility.documentStorageDir, fileName + ".txt");
        if(!file.delete()){
            Log.i(LOG_TAG, "Cannot delete the file " + fileName);
            return false;
        }
        return true;
    }

    public static String[] renameFile(String oldFileName, String newFileName){
        String[] returnValues = new String[2];

        File oldFile = new File(MyUtility.documentStorageDir, oldFileName + ".txt");
        File newFile = new File(MyUtility.documentStorageDir, newFileName + ".txt");
        if(!oldFile.renameTo(newFile)){
            Log.i(LOG_TAG, "Cannot rename the file to " + newFileName);
            returnValues[0] = new String(oldFileName);
            returnValues[1] = getLastModifiedDateString(oldFile);
            return returnValues;
        }

        returnValues[0] = new String(newFileName);
        returnValues[1] = getLastModifiedDateString(newFile);
        return returnValues;
    }

    public static File getDocumentStorageDir(String folderName) {
        // Get the directory for the user's public directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), folderName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static ArrayList downloadQADataJson(String serverUrl){

        String q = null;
        String a = null;

        JSONArray qasJsonArray = null;
        JSONObject qaJsonObj = null;

        ArrayList qaPosts = new ArrayList<Map<String,?>>();

        String qasArray = MyUtility2.downloadJSON(serverUrl);

        try {
            qasJsonArray = new JSONArray(qasArray);
            for (int i = 0; i < qasJsonArray.length(); i++) {
                qaJsonObj = (JSONObject) qasJsonArray.get(i);
                if (qaJsonObj != null) {
                    q = (String) qaJsonObj.get("question");
                    a = (String) qaJsonObj.get("answer");
                }
                qaPosts.add(createQA(q, a));
            }
        }catch(JSONException e){

            Log.i(LOG_TAG, "Catch JSONException in MyUtility.downloadQADataJson " + e);
            return null;
        }

        return qaPosts;
    }

    private static HashMap createQA(String q, String a) {
        HashMap qa = new HashMap();
        qa.put("q", q);
        qa.put("a", a);

        return qa;
    }
}
