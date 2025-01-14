package com.example.software2.ocrhy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static int firstTime = 0;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private TextView mVoiceInputTv;
    float x1, x2, y1, y2;
    private TextView mSpeakBtn;
    private SurfaceView surfaceView;

    private static TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(1f);
                    if (firstTime == 0)
                        textToSpeech.speak("Welcome to My App. Swipe left to listen the features of the app and swipe right and say what you want", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);

    }
    public boolean onTouchEvent(MotionEvent touchEvent) {
        firstTime = 1;
        switch (touchEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2) {
                    firstTime = 1;
                    Intent intent = new Intent(MainActivity.this, MainActivity7.class);
                    startActivity(intent);

                }
                if (x1 > x2) {
                    startVoiceInput();
                    break;
                }

                break;
        }

        return false;
    }


    private void startVoiceInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mVoiceInputTv.setText(result.get(0));
                if (mVoiceInputTv.getText().toString().equals("exit")) {
                    finishAffinity();
                    System.exit(0);
                }
                if (mVoiceInputTv.getText().toString().equals("read")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                    mVoiceInputTv.setText(null);
                } else {
                    textToSpeech.speak("Do not understand just Swipe right Say again", TextToSpeech.QUEUE_FLUSH, null);

                }
                if (mVoiceInputTv.getText().toString().equals("calculator")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                    startActivity(intent);
                    mVoiceInputTv.setText(null);
                } else {
                    textToSpeech.speak("Do not understand just Swipe right Say again", TextToSpeech.QUEUE_FLUSH, null);
                }
                if (mVoiceInputTv.getText().toString().equals("time and date")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                    startActivity(intent);
                    mVoiceInputTv.setText(null);
                } else {
                    textToSpeech.speak("Do not understand just Swipe right  Say again", TextToSpeech.QUEUE_FLUSH, null);
                }
                if (mVoiceInputTv.getText().toString().equals("weather")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity5.class);
                    startActivity(intent);
                    mVoiceInputTv.setText(null);
                } else {
                    textToSpeech.speak("Do not understand just Swipe right Say again", TextToSpeech.QUEUE_FLUSH, null);
                }

                if (mVoiceInputTv.getText().toString().equals("battery")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity6.class);
                    startActivity(intent);
                    mVoiceInputTv.setText(null);
                } else {
                    textToSpeech.speak("Do not understand Swipe right Say again", TextToSpeech.QUEUE_FLUSH, null);
                }
                if (mVoiceInputTv.getText().toString().equals("yes")) {
                    textToSpeech.speak("  Say Read for reading,  calculator for calculator,  time and date,  weather for weather,  battery for battery. Do you want to listen again", TextToSpeech.QUEUE_FLUSH, null);
                    mVoiceInputTv.setText(null);
                } else if ((mVoiceInputTv.getText().toString().equals("no"))) {
                    textToSpeech.speak("then Swipe right and say what you want", TextToSpeech.QUEUE_FLUSH, null);

                } else if (mVoiceInputTv.getText().toString().equals("location")) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity8.class);
                    startActivity(intent);
                    mVoiceInputTv.setText(null);
                }


                if (mVoiceInputTv.getText().toString().contains("exit")) {
                  mVoiceInputTv.setText(null);

                    finishAffinity();
                }

            }
        }

    }

    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();

    }
}
