package com.example.menubee;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import static menubee_backend.CallGPT.resultforGPT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Timer;
import java.util.TimerTask;

import menubee_backend.ImageToText;

public class ViewPager2AutoScrollActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private int currentPage = 0;
    private Timer timer;
    private ProgressBar progressBar;
    private TextView textView;
    private static String GptResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2_auto_scroll);

        // OCR 객체 생성
        ImageToText OcrToGpt=new ImageToText(this);

        // OCR 시작
        OcrToGpt.callCloudVision(Camera_capture.getBitmap());
        viewPager2 = findViewById(R.id.viewPager2);
        textView = findViewById(R.id.text_loading);
        progressBar = findViewById(R.id.progressBar);
        ImageSliderAdapter adapter = new ImageSliderAdapter(this);
        viewPager2.setAdapter(adapter);

        // 페이지 전환을 위한 타이머 설정
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 마지막 이미지에 도달했는지 확인합니다.
                if (currentPage == adapter.getItemCount() - 1) {
                    // 현재 페이지를 0으로 설정합니다.
                    currentPage = 0;
                } else {
                    // 현재 페이지를 1 증가시킵니다.
                    currentPage++;
                }
                // ViewPager2에 현재 페이지를 설정합니다.
                viewPager2.setCurrentItem(currentPage, true);
            }
        }, 3000, 3000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(500);

                        // 핸들러를 사용하여 UI 스레드에서 텍스트를 업데이트합니다.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 텍스트를 "Loading..."으로 설정합니다.
                                textView.setText("Loading.");
                            }
                        });
                        Thread.sleep(500);

                        // 핸들러를 사용하여 UI 스레드에서 텍스트를 업데이트합니다.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 텍스트를 "Loading..."으로 설정합니다.
                                textView.setText("Loading..");
                            }
                        });
                        Thread.sleep(500);

                        // 핸들러를 사용하여 UI 스레드에서 텍스트를 업데이트합니다.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 텍스트를 "Loading..."으로 설정합니다.
                                textView.setText("Loading...");
                            }
                        });
                        Thread.sleep(500);

                        // 핸들러를 사용하여 UI 스레드에서 텍스트를 업데이트합니다.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 텍스트를 "Loading..."으로 설정합니다.
                                textView.setText("Loading....");
                            }
                        });

                        // Check if the GPT result is ready
                        if (resultforGPT != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // If the GPT result is ready, hide the progress bar and start the next activity.
                                    progressBar.setVisibility(View.GONE);
                                    Intent CafeActivity=new Intent(ViewPager2AutoScrollActivity.this, Cafe.class);
                                    CafeActivity.putExtra("gptResult", resultforGPT);
                                    startActivity(CafeActivity);
                                    finish();
                                }
                            });
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 종료될 때 타이머 취소
        timer.cancel();
    }
}
