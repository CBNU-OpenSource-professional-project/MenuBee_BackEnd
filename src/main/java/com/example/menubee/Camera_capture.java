package com.example.menubee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import menubee_backend.GetImage;

public class Camera_capture extends AppCompatActivity {

    AppCompatButton select_btn;
    GetImage getImage;
    static Bitmap cameraImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);
        getImage = new GetImage(this);
        getImage.startGalleryChooser();

        // 선택 버튼 클릭시
        select_btn = findViewById(R.id.selectimg);
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewPager2AutoScrollActivity.class);
                startActivity(intent);
            }
        });

        Button retry_btn=findViewById(R.id.retry);
        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, (dialog, which) -> getImage.startGalleryChooser())
                        .setNegativeButton(R.string.dialog_select_camera, (dialog, which) -> getImage.startCamera());
                builder.create().show();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getImage.onActivityResult(requestCode, resultCode, data);
    }
    public static void setBitmap(Bitmap bitmap){
        cameraImg=bitmap;
    }
}