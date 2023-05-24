package com.example.menubee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class Tip extends AppCompatActivity {

    boolean viewtip;
    AppCompatButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
        CheckBox neverview = findViewById(R.id.neverview);

        viewtip = ((Global) getApplicationContext()).getTip_value();

        neverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewtip = !neverview.isChecked();
            }
        });

        ((Global) getApplicationContext()).setTip_value(viewtip);

        close = (AppCompatButton) findViewById(R.id.closetip);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent1 = new Intent(getApplicationContext(), Camera_capture.class);
                startActivity(intent1);
            }
        });
    }



}