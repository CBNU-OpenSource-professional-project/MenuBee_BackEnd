package com.example.menubee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import menubee_backend.ImageToText;

public class Cafe extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe);
        String gptResult = getIntent().getStringExtra("gptResult");
        Log.d("Cafe Activity ", "onCreate: "+gptResult);
    }
}
