package com.example.menubee;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class Choice_mode extends AppCompatActivity {

    TextView gotoorderbtn;
    TextView gotoplusrequestbtn;
    AppCompatButton gotomainbtn;

    TextView gotofastorderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_mode);

        Intent intent = getIntent();
        CharSequence order = intent.getCharSequenceExtra("order");

        gotoorderbtn = findViewById(R.id.pay_order);

        gotoorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdditionalOrder.class);
            }
        });

        gotoplusrequestbtn = findViewById(R.id.plus_order);

        gotoplusrequestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdditionalOrder.class);
                startActivity(intent);
            }
        });

        gotomainbtn = findViewById(R.id.gotomain);

        gotomainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        gotofastorderbtn = findViewById(R.id.quick_order);

        gotofastorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Choice_mode.class);
                intent.putExtra("order", (CharSequence) order);
                startActivity(intent);
            }
        });
    }

}
