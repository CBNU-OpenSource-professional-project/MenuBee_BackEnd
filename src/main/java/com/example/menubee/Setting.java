package com.example.menubee;

import static android.speech.tts.TextToSpeech.ERROR;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import menubee_backend.TTS;

import menubee_backend.TTS;

public class Setting extends AppCompatActivity {
    private Spinner textSelectVoice;
    private TextToSpeech tts;
    private RadioGroup radioGroup;
    private SeekBar seekVolumn;
    private AudioManager audioManage;
    private int nCurrentVolumn;
    private int nMax;
    private RadioButton speedButton;
    private RadioButton speedButton2;
    private RadioButton speedButton3;
    public static float speed;
    public static Locale ttsLocale;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        speedButton = findViewById(R.id.radioButton);
        speedButton2 = (RadioButton) findViewById(R.id.radioButton2);
        speedButton3 = (RadioButton) findViewById(R.id.radioButton3);
        textSelectVoice = findViewById(R.id.sound_TTL_voice_change);

        TextView changeBGColor;
        TextView changeTextColor;

        String BGcolor;
        String Textcolor;
        database = new Database(this);

        boolean BGtoken = database.getBoolean("BGtoken",false);
        boolean Texttoken = database.getBoolean("Texttoken",false);


        if (BGtoken && !Texttoken)
        {
            String temp = database.getString("colorval","");

            database.storeString("BGcolor",temp);

            changeBGColor = findViewById(R.id.displayColor);

            int TextcolorInt = Color.parseColor(temp);

            changeBGColor.setBackgroundColor(TextcolorInt);
        }
        else if(!BGtoken && Texttoken)
        {
            String temp = database.getString("colorval","");

            database.storeString("Textcolor",temp);

            changeTextColor = findViewById(R.id.Menu_Text_Color);

            int TextcolorInt = Color.parseColor(temp);

            changeTextColor.setBackgroundColor(TextcolorInt);
        }


        AppCompatButton setBGcolor = findViewById(R.id.colorPicker_button);

        AppCompatButton showPopupButton = findViewById(R.id.colorPicker_button);
        setBGcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.storeBoolean("BGorText",true);
                Intent intent = new Intent(getApplicationContext(),selectcolor.class);
                startActivity(intent);
                finish();
            }
        });

        AppCompatButton setTextcolor = findViewById(R.id.colorPicker_button2);
        setTextcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.storeBoolean("BGorText",false);
                Intent intent = new Intent(getApplicationContext(),selectcolor.class);
                startActivity(intent);
                finish();
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(ttsLocale);
                }
            }
        });

        radioGroup = findViewById(R.id.radioGroup);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.voice_options_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSelectVoice.setAdapter(adapter);

        // textSelectVoice 뷰에 OnItemSelectedListener 설정
        textSelectVoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedVoice = parent.getItemAtPosition(position).toString();
                handleVoiceSelection(selectedVoice); // 아이템 선택 시 처리
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // tts 읽기 속도 및 음성 톤 설정
        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 0.1f;
                tts.setPitch(1.0f);         // 음성 톤
                tts.setSpeechRate(speed);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak("0.75배 설정이 완료되었습니다", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        speedButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 1.0f;
                tts.setPitch(1.0f);         // 음성 톤
                tts.setSpeechRate(speed);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak("평문 설정이 완료되었습니다", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        speedButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 2.5f;
                tts.setPitch(0.5f);         // 음성 톤
                tts.setSpeechRate(speed);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak("1.25배 설정이 완료되었습니다", TextToSpeech.QUEUE_FLUSH, null);

            }
        });
        setVolumnBar();
    }


    public void handleVoiceSelection(String selectedVoice) {
        switch (selectedVoice) {
            case "한국어":
                ttsLocale = Locale.KOREAN;
                tts.setLanguage(ttsLocale);
                tts.setSpeechRate(speed);
                tts.speak("한국어로 설정되었습니다.", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case "영어":
                ttsLocale = Locale.ENGLISH;
                tts.setLanguage(ttsLocale);
                tts.setSpeechRate(speed);
                tts.speak("Setting in English.", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case "중국어":
                ttsLocale = Locale.CHINESE;
                tts.setLanguage(ttsLocale);
                tts.setSpeechRate(speed);
                tts.speak("设定为中文.", TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }

    // 시스템 음량 연결
    public void setVolumnBar() {
        seekVolumn = findViewById(R.id.TTL_volum_seekBar);
        audioManage = (AudioManager) getSystemService(AUDIO_SERVICE);
        nMax = audioManage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        nCurrentVolumn = audioManage.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekVolumn.setMax(nMax);
        seekVolumn.setProgress(nCurrentVolumn);

        seekVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
