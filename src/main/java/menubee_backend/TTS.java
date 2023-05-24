package menubee_backend;

import static android.speech.tts.TextToSpeech.ERROR;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TTS extends AppCompatActivity {
    Button btn_tts;
    EditText image_datails;
    static TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        btn_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // editText에 있는 문장을 읽는다.
                tts.speak(image_datails.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }


}
