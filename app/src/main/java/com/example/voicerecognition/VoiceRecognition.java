package com.example.voicerecognition;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class VoiceRecognition {
    private Context context;

    public VoiceRecognition(Context context) {
        this.context = context;
    }

    public String recognizeSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onBeginningOfSpeech() {
                // Called when the recognition starts
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                // Called when the recognition ends
            }

            @Override
            public void onReadyForSpeech(Bundle results) {
                // Called when the recognition is ready
            }

            @Override
            public void onResults(Bundle results) {
                // Called when the recognition is complete
                String recognizedText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                // return recognizedText;
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }

            @Override
            public void onError(int errorCode) {
                // Called when an error occurs
            }
        });

        speechRecognizer.startListening(intent);
        return null;
    }
}