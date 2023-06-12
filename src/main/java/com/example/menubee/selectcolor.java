package com.example.menubee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class selectcolor extends AppCompatActivity {

    TextView colorTextView;
    View colorView;

    Database database;
    String selectcolorval;

    boolean BGorText = false;

    AppCompatButton selectbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcolor);

        database = new Database(this);

        BGorText = database.getBoolean("BGorText",false);

        colorTextView = findViewById(R.id.color_text_view);

        colorView = findViewById(R.id.color_view);

        ColorPickerView colorPickerView = findViewById(R.id.colorPickerView);
        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {

                colorTextView.setText(envelope.getHexCode());

                colorView.setBackgroundColor(envelope.getColor());
            }
        });

        //밝기 조절 슬라이더
        BrightnessSlideBar brigSlideBar = findViewById(R.id.brig_slideBar);
        colorPickerView.attachBrightnessSlider(brigSlideBar);

        //투명도 조절 슬라이더
        AlphaSlideBar alphaSlideBar = findViewById(R.id.alph_slideBar);
        colorPickerView.attachAlphaSlider(alphaSlideBar);

        selectcolorval = colorTextView.getText().toString();

        selectbtn = findViewById(R.id.selectcolor_btn);

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BGorText){
                    database.storeBoolean("BGtoken",true);
                    database.storeBoolean("Texttoken",false);
                    database.storeString("colorval",selectcolorval);
                }else{
                    database.storeBoolean("BGtoken",false);
                    database.storeBoolean("Texttoken",true);
                    database.storeString("colorval",selectcolorval);
                }
                finish();
                Intent intent = new Intent(getApplicationContext(),Setting.class);
                startActivity(intent);

            }
        });
    }


}