package com.example.menubee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

public class AdditionalOrder extends AppCompatActivity {

    ConstraintLayout line1, line2, line3, line4;
    AppCompatButton btn1, btn2, btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);


    }
}