package com.example.menubee;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Locale;

public class TextOrder extends AppCompatActivity {
    TextView orderText;
    AppCompatButton voiceOrder, back;
    int code;

    private TextToSpeech tts; // Added this line
    public static Locale locale;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textorder);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        String reqText = intent.getStringExtra("req");
        CharSequence order = intent.getCharSequenceExtra("order");
        orderText = (TextView) findViewById(R.id.orderText);
        float speed=Setting.speed;

        if(reqText != null) {
            orderText.setText(reqText);
        }
        else {
            orderText.setText(order);
        }

        // Initialize TextToSpeech object
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                locale=Setting.ttsLocale;
                Log.d("TTS SPEED", "onInit: "+speed);
                if (status != TextToSpeech.ERROR) {
                    tts.setSpeechRate(speed);
                    tts.setLanguage(locale);
                }
            }
        });

        voiceOrder = (AppCompatButton) findViewById(R.id.voiceOrder);
        back = (AppCompatButton) findViewById(R.id.back);

        // Set OnClickListener for voiceOrder button
        voiceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = orderText.getText().toString();

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); // Edited this line
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        // Shutting down TTS
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
