package com.example.menubee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AdditionalOrder extends AppCompatActivity {

    ConstraintLayout line1, line2, line3, line4;
    AppCompatButton btnlist[] = new AppCompatButton[8];;
    AppCompatButton btnReq, btnBack;
    EditText reqText;
    CharSequence order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);

        btnlist[0]=(AppCompatButton) findViewById(R.id.btn1);
        btnlist[1]=(AppCompatButton) findViewById(R.id.btn2);
        btnlist[2]=(AppCompatButton) findViewById(R.id.btn3);
        btnlist[3]=(AppCompatButton) findViewById(R.id.btn4);
        btnlist[4]=(AppCompatButton) findViewById(R.id.btn5);
        btnlist[5]=(AppCompatButton) findViewById(R.id.btn6);
        btnlist[6]=(AppCompatButton) findViewById(R.id.btn7);
        btnlist[7]=(AppCompatButton) findViewById(R.id.btn8);
        reqText=(EditText) findViewById(R.id.reqText);
        btnReq = (AppCompatButton) findViewById(R.id.btnReq);
        btnBack = (AppCompatButton) findViewById(R.id.btnBack);

        Intent intent = getIntent();
        order = intent.getCharSequenceExtra("order");
        reqText.setText(order);

        btnlist[0].setText("물 한 잔 주세요.");
        btnlist[1].setText("냅킨 좀 주세요.");
        btnlist[2].setText("화장실은 어디 있나요?");

        for(int i = 0; i < 8; i++) {
            final int index;
            index = i;
            btnlist[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = reqText.getText().toString();
                    String btnText = btnlist[index].getText().toString();
                    reqText.setText(text + " " + btnText);
                }
            });
        }

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderIntent = new Intent(getApplicationContext(), TextOrder.class);
                orderIntent.putExtra("req", reqText.getText().toString());
                startActivity(orderIntent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}