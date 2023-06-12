package com.example.menubee;

import static menubee_backend.CallGPT.resultforGPT;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;


public class Cafe extends AppCompatActivity {
    ScrollView menuView;
    LinearLayout menu;
    LinearLayout orderResult;
    AppCompatButton orderbtn, addorderbtn;
    Database database;

    class Result {
        LinearLayout selected = new LinearLayout(getApplicationContext());
        TextView menu = new TextView(getApplicationContext());
        TextView num = new TextView(getApplicationContext());
        int number = 0;
        AppCompatButton plus;
        AppCompatButton minus;

        Result() {}
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe);
        menuView = (ScrollView) findViewById(R.id.menuView);
        orderbtn = (AppCompatButton) findViewById(R.id.orderbtn);

        //result 배열 예시
        String[] result1 = resultforGPT;

        ArrayList<TextView> menuLists = new ArrayList<TextView>();
        ArrayList<Result> resultList = new ArrayList<Result>();

        menu = (LinearLayout) findViewById(R.id.menu1);

        orderResult = (LinearLayout) findViewById(R.id.orderResult);

        for(String next : result1) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText(next);
            menuStyle(tv);
            menuLists.add(tv);
            menu.addView(tv);
        }

        Iterator<TextView> menulistIterator = menuLists.iterator();
        while (menulistIterator.hasNext()) {
            TextView tnext = menulistIterator.next();
            tnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Iterator<Result> iterator = resultList.iterator();
                    while (iterator.hasNext()) {
                        Result next = iterator.next();
                        if (next.menu.getText().toString().equals(tnext.getText().toString())) {
                            next.number += 1;
                            String curNum = Integer.toString(next.number);
                            next.num.setText(curNum);
                            return;
                        }
                    }
                    Result result = new Result();
                    result.menu.setText(tnext.getText().toString());
                    result.number = 1;
                    result.num.setText("1");
                    selectedLayout(result);
                    resultList.add(result);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 5, 0, 0);
                    result.selected.setLayoutParams(layoutParams);
                    orderResult.addView(result.selected);

                    for (Result pmresult : resultList) {
                        pmresult.plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pmresult.number += 1;
                                String curNum = Integer.toString(pmresult.number);
                                pmresult.num.setText(curNum);
                            }
                        });

                        pmresult.minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (pmresult.number == 1) {
                                    orderResult.removeView(pmresult.selected);
                                    resultList.remove(pmresult);
                                } else {
                                    pmresult.number -= 1;
                                    String curNum = Integer.toString(pmresult.number);
                                    pmresult.num.setText(curNum);
                                }
                            }
                        });
                    }
                }
            });
        }

        //database = new Database(this);

        //String BGcolor = database.getString("BGcolor","");
        //String Textcolor = database.getString("Textcolor","");

        //int BGcolor_int = Color.parseColor(BGcolor);
        //int Textcolor_int = Color.parseColor(Textcolor);

        // if(BGcolor.equals("")){
        //      BGcolor_int = Color.parseColor("#FFFFFF");
        // }
        // if(Textcolor.equals("")) {
        //      Textcolor_int = Color.parseColor("#000000");
        //  }
        int BGcolor_int = Color.parseColor("#FFFFFF");
        int Textcolor_int = Color.parseColor("#000000");
        changeBGColor(BGcolor_int);
        changeTextColor(Textcolor_int);

        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Result> iterator = resultList.iterator();
                StringBuilder ordermsg = new StringBuilder("");
                if (resultList.isEmpty()) {
                    Toast.makeText(Cafe.this, "주문할 메뉴를 선택하세요", Toast.LENGTH_SHORT).show();
                } else {
                    while (iterator.hasNext()) {
                        Result next = iterator.next();
                        if (!iterator.hasNext()) {
                            ordermsg.append(next.menu.getText().toString());
                            ordermsg.append(" ");
                            ordermsg.append(next.num.getText().toString());
                            ordermsg.append("개 주세요");
                        } else {
                            ordermsg.append(next.menu.getText().toString());
                            ordermsg.append(" ");
                            ordermsg.append(next.num.getText().toString());
                            ordermsg.append("개, ");
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), Choice_mode.class);
                    intent.putExtra("order", (CharSequence) ordermsg);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void menuStyle(TextView tv) {
        Typeface typeFace = ResourcesCompat.getFont(this, R.font.bitbit);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(10);
        tv.setLayoutParams(layoutParams);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setTextSize(35);
        tv.setLetterSpacing(0.05f);
        tv.setPadding(0, 20, 0,10);
        tv.setTypeface(typeFace);
    }

    public void selectedLayout(Result result) {
        Typeface typeFace = ResourcesCompat.getFont(this, R.font.bitbit);
        result.menu.setTextSize(25);
        result.menu.setLetterSpacing(0.1f);
        result.menu.setTypeface(typeFace);
        LinearLayout.LayoutParams menuparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuparams.weight=0.7f;
        result.menu.setLayoutParams(menuparams);
        result.num.setTextSize(25);
        result.num.setTypeface(typeFace);
        LinearLayout.LayoutParams numparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        numparams.weight=0.3f;
        result.num.setLayoutParams(numparams);
        result.plus = new AppCompatButton(getApplicationContext());
        result.plus.setBackgroundResource(R.drawable.plus1);
        LinearLayout.LayoutParams plusparams = new LinearLayout.LayoutParams(60, 60);
        plusparams.setMargins(0,-18,20,0);
        result.plus.setLayoutParams(plusparams);
        result.minus = new AppCompatButton(getApplicationContext());
        result.minus.setBackgroundResource(R.drawable.minus1);
        LinearLayout.LayoutParams minusparams = new LinearLayout.LayoutParams(60, 60);
        minusparams.setMargins(0,-18,20,0);
        result.minus.setLayoutParams(minusparams);
        result.selected.setWeightSum(1f);
        result.selected.addView(result.menu, menuparams);
        result.selected.addView(result.num, numparams);
        result.selected.addView(result.plus, plusparams);
        result.selected.addView(result.minus, minusparams);
    }

    public void changeBGColor(int color) {
        ScrollView menuView = (ScrollView) findViewById(R.id.menuView);
        menuView.setBackgroundColor(color);
    }

    public void changeTextColor(int color) {
        TextView menuHead = (TextView) findViewById(R.id.menuHead);
        View menuLine = (View) findViewById(R.id.menuLine);
        menuHead.setTextColor(color);
        menuLine.setBackgroundColor(color);
    }
}