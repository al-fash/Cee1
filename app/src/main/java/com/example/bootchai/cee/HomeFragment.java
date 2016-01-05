package com.example.bootchai.cee;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by MankinG on 4/12/2015.
 */
public class HomeFragment extends Fragment implements RecognitionListener {

    private EditText returnedText;
    private EditText inputText;
    private CardView toggleButtonMe;
    private MyCustomView customViewButton;
    private ButtonClicked buttonClicked;
    private View toastRoot;
    private TextView toastText;
    private Toast toast;

    private TextToSpeech textToSpeech;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = MyUtility.LOG_TAG;

    private int currentCursorPosition = 0;

    private boolean toggledButtonChecked = false;
    private boolean toggledButtonMeChecked = false;

    public int homeTextSize;

    public interface ButtonClicked{
        public abstract void onSavedNoteButtonClicked(String fileName, String lastModifiedDateString);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_fragment_home_clear) {
            clearNoteRoutine();
        }else if(id == R.id.action_fragment_home_save) {
            saveNoteRoutine();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences ceePref = getActivity().getSharedPreferences(MyUtility.preferencesFileName, getActivity().MODE_PRIVATE);
        homeTextSize = ceePref.getInt("homeTextSize", MyUtility.defaultHomeTextSize);
        if(returnedText != null){
            returnedText.setTextSize((float) homeTextSize);
        }
        if(inputText != null){
            inputText.setTextSize((float) homeTextSize);
        }
    }

    public HomeFragment() {
    }

    public void setOnButtonClickedListener(ButtonClicked bc){
        buttonClicked = bc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            setToggleButtonMeChecked(true);
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            setToggleButtonMeChecked(false);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.i(LOG_TAG, "An error has occurred with TextToSpeech Engine");
                        }
                    });
                }
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        textToSpeech.shutdown();

        super.onDestroy();
    }

    @Override
    public void onPause() {
        if(textToSpeech != null){
            textToSpeech.stop();
        }
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        inputText = (EditText) rootView.findViewById(R.id.main_et_in);
        returnedText = (EditText) rootView.findViewById(R.id.main_et_out);
        customViewButton = (MyCustomView) rootView.findViewById(R.id.main_cv_cee);
        toggleButtonMe = (CardView) rootView.findViewById(R.id.fragment_help_cv_help);

        //********************************************************
        toastRoot = inflater.inflate(R.layout.custom_toast, (ViewGroup) getActivity().findViewById(R.id.fl_toastroot));
        toastText = (TextView) toastRoot.findViewById(R.id.tv_toast);

        toast = new Toast(getActivity());
        toast.setView(toastRoot);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT, 40, 430);

        //********************************************************

        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        setMyCustomViewChecked(false);
        /*customViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(LOG_TAG, "in onClicked");
                if (!toggledButtonChecked) {
                    //returnedText.removeTextChangedListener(textWatcherForEditText);
                    speech.startListening(recognizerIntent);
                    setMyCustomViewChecked(true);
                } else {
                    //returnedText.addTextChangedListener(textWatcherForEditText);
                    speech.stopListening();
                    setMyCustomViewChecked(false);
                }
            }
        });*/

        customViewButton.setOnCustomViewMotionListener(new MyCustomView.OnCustomViewMotionListener() {
            @Override
            public void onCustomViewSingleTab() {
                if (!toggledButtonChecked) {
                    //returnedText.removeTextChangedListener(textWatcherForEditText);
                    speech.startListening(recognizerIntent);
                    setMyCustomViewChecked(true);
                } else {
                    //returnedText.addTextChangedListener(textWatcherForEditText);
                    speech.stopListening();
                    setMyCustomViewChecked(false);
                }
            }

            @Override
            public void onCustomViewFlingUp() {
                clearNoteRoutine();
            }

            @Override
            public void onCustomViewFlingDown() {
                saveNoteRoutine();
            }
        });

        setToggleButtonMeChecked(false);
        toggleButtonMe.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!toggledButtonMeChecked) {
                    String toSpeak = inputText.getText().toString();
                    if(!toSpeak.equals("")) {
                        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, toSpeak.substring(0, 1));
                    }
                } else {
                    textToSpeech.stop();
                    setToggleButtonMeChecked(false);
                }
            }
        });

        return rootView;
    }

    private void clearNoteRoutine(){
        currentCursorPosition = 0;
        returnedText.setText("");
        returnedText.setSelection(currentCursorPosition);

        inputText.setText("");
    }

    private void saveNoteRoutine(){
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd_HH-mm-ss");
            String fileName = simpleDateFormat.format(calendar.getTime());

            showToast("Save: " + fileName);

            fileName += ".txt";

            File file = new File(MyUtility.documentStorageDir, fileName);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            String text = returnedText.getText().toString();
            output.write(text);

            output.close();

            String lastModifiedDateString = MyUtility.getLastModifiedDateString(file);

            buttonClicked.onSavedNoteButtonClicked(fileName, lastModifiedDateString);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void setMyCustomViewChecked(boolean checked){
        toggledButtonChecked = checked;
        customViewButton.setClicked(checked);
    }

    private void setToggleButtonMeChecked(boolean checked){
        toggledButtonMeChecked = checked;
        if(toggledButtonMeChecked){
            toggleButtonMe.setCardBackgroundColor(getResources().getColor(R.color.me_press));
        }else{
            toggleButtonMe.setCardBackgroundColor(getResources().getColor(R.color.me_default));
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        customViewButton.setProgress(100);
        currentCursorPosition = returnedText.getText().length();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        customViewButton.setProgress((int) (rmsdB * 10));
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        setMyCustomViewChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED: " + errorMessage);
        showToast("FAILED: " + errorMessage);

        SpannableStringBuilder text = (SpannableStringBuilder) returnedText.getText();
        if(currentCursorPosition > text.length()){
            currentCursorPosition = text.length();
            Log.d(LOG_TAG, "WEIRD CONDITION in onError: currentCursorPosition > text.length()");
        }
        text.replace(currentCursorPosition, text.length(), "");

        returnedText.setText(text);
        returnedText.setSelection(currentCursorPosition);

        setMyCustomViewChecked(false);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        SpannableStringBuilder text = (SpannableStringBuilder) returnedText.getText();

        //float[] matchConfidences = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
        //String text = "";
            /*for (int i = 0 ; i < matches.size() ; i++) {
                text += matches.get(i);
                if(matchConfidences != null){
                    text += "...." + matchConfidences[i];
                }
                text += '\n';
            }*/

        if(currentCursorPosition > text.length()){
            currentCursorPosition = text.length();
            Log.d(LOG_TAG, "WEIRD CONDITION in onResults: currentCursorPosition > text.length()");
        }
        text.replace(currentCursorPosition, text.length(), matches.get(0) + "\n");
        Log.i(LOG_TAG, "onResults: " + matches.get(0));

        returnedText.setText(text);
        currentCursorPosition = text.length();
        returnedText.setSelection(currentCursorPosition);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(LOG_TAG, "onPartialResults");

        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        SpannableStringBuilder text = (SpannableStringBuilder) returnedText.getText();
        if(currentCursorPosition > text.length()){
            //currentCursorPosition = text.length();
            Log.d(LOG_TAG, "WEIRD CONDITION in onPartialResults: currentCursorPosition > text.length()");
        }
        text.replace(currentCursorPosition, text.length(), "[" + matches.get(0) + "]");

        returnedText.setText(text);
        returnedText.setSelection(text.length());
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(LOG_TAG, "onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public void onResume_custom() {
    }

    public void onPause_custom() {
        if(getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager =
                    (InputMethodManager) getActivity().
                            getSystemService(getActivity().getBaseContext().INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showToast(String s){
        toastText.setText(s);
        toast.show();
    }

}
