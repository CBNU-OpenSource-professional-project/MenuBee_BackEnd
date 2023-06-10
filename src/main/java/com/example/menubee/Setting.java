package com.example.menubee;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    private TextView textSelectVoice;
    private TextToSpeech tts;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        textSelectVoice = findViewById(R.id.sound_TTS_change);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        tts.setSpeechRate(0.75f);
                        break;
                    case R.id.radioButton2:
                        tts.setSpeechRate(1.0f);
                        break;
                    case R.id.radioButton3:
                        tts.setSpeechRate(1.25f);
                        break;
                }
            }
        });
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.voice_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.voice_option1:
                        // Handle voice option 1 selection
                        return true;
                    case R.id.voice_option2:
                        // Handle voice option 2 selection
                        return true;
                    case R.id.voice_option3:
                        // Handle voice option 3 selection
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
