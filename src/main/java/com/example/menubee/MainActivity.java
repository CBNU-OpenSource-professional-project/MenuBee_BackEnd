package com.example.menubee;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private boolean tip_value;
    AppCompatButton start_btn;
    AppCompatButton setting_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tip_value = ((Global) getApplicationContext()).getTip_value();

        start_btn = findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnstartclicked(v);
            }
        });

        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });
    }

    public void btnstartclicked(View v) {
        if (tip_value) {
            Intent intent = new Intent(getApplicationContext(),Tip.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), Camera_capture.class);
            startActivity(intent);
        }
    }

}
