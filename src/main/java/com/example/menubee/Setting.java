package com.example.menubee;

import static android.speech.tts.TextToSpeech.ERROR;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import menubee_backend.TTS;

public class Setting extends AppCompatActivity {
    private TextView textSelectVoice;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        speedButton = findViewById(R.id.radioButton);
        speedButton2 = (RadioButton) findViewById(R.id.radioButton2);
        speedButton3 = (RadioButton) findViewById(R.id.radioButton3);
        textSelectVoice = findViewById(R.id.sound_TTS_change);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        radioGroup = findViewById(R.id.radioGroup);
        // tts 읽기 속도 및 음성 톤 설정
        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed=0.1f;
                tts.setPitch(1.0f);         // 음성 톤
                tts.setSpeechRate(speed);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak("0.75배 설정이 완료되었습니다",TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        speedButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed=1.0f;
                tts.setPitch(1.0f);         // 음성 톤
                tts.setSpeechRate(speed);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak("평문 설정이 완료되었습니다",TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        speedButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed=2.5f;
                tts.setPitch(0.5f);         // 음성 톤
                tts.setSpeechRate(speed);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak("1.25배 설정이 완료되었습니다",TextToSpeech.QUEUE_FLUSH, null);

            }
        });
        setVolumnBar();
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
