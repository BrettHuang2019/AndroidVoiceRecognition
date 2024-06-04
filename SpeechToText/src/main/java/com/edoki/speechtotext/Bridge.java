package com.edoki.speechtotext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;

public class Bridge {

    private static SpeechRecognizer speech;
    private static Intent intent;
    private static String languageSpeech = "en-US";
    private static String unityGameObjectName = "SpeechToText"; // GameObject and Script name must be the same


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void sendToUnity(){
        UnityPlayer.UnitySendMessage(unityGameObjectName, "ShowText", "Hello from Android Studio");
    }

    public static void initSpeechRecognizer(Context context, String language){
        UnityPlayer.currentActivity.runOnUiThread(() -> {
            languageSpeech = language;
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);

            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        });
    }

    public static void onStartRecording(Context context) {

        UnityPlayer.currentActivity.runOnUiThread(() -> {
            speech = SpeechRecognizer.createSpeechRecognizer(context);
            speech.setRecognitionListener(recognitionListener);
            speech.startListening(intent);
        });

        UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "CallStart, Language:" + languageSpeech);
    }

    public static void onStopRecording() {
        UnityPlayer.currentActivity.runOnUiThread(() -> {
            speech.stopListening();
        });
        UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onStopRecording");
    }


    static RecognitionListener recognitionListener = new RecognitionListener() {
        private ArrayList<String> partials = new ArrayList<>();

        @Override
        public void onReadyForSpeech(Bundle params) {
            UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onReadyForSpeech");
            partials = new ArrayList<>();
        }

        @Override
        public void onBeginningOfSpeech() {
            UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onBeginningOfSpeech");

        }

        @Override
        public void onRmsChanged(float rmsdB) {
//            UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onRmsChanged:"+rmsdB);
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            UnityPlayer.UnitySendMessage(unityGameObjectName, "onError", String.valueOf(error));

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (text != null && !text.isEmpty()) {
                Log.d(unityGameObjectName, "onResults: " + text.get(0));
                UnityPlayer.UnitySendMessage(unityGameObjectName, "onResults", text.get(0));
            } else if (partials != null && !partials.isEmpty()) {
                Log.d(unityGameObjectName, "onResults: " + String.join(" ", partials));
                UnityPlayer.UnitySendMessage(unityGameObjectName, "onResults", String.join(" ", partials));
            } else {
                Log.d(unityGameObjectName, "onResults: No recognition results found");
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            ArrayList<String> text = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (text != null && !text.isEmpty()) {
                Log.d(unityGameObjectName, "onPartialResults: " + text.get(0));
                partials.add(text.get(0));
                UnityPlayer.UnitySendMessage(unityGameObjectName, "onPartialResults", text.get(0));
            } else {
                Log.d(unityGameObjectName, "onPartialResults: No recognition results found");
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            UnityPlayer.UnitySendMessage(unityGameObjectName, "onMessage", "onEvent");
        }
    };
}
