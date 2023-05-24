package menubee_backend;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.menubee.Camera_capture;
import com.example.menubee.R;

import java.io.File;
import java.io.IOException;

public class GetImage extends AppCompatActivity {
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 101;
    private static final int CAMERA_PERMISSIONS_REQUEST = 2;
    private static final int CAMERA_IMAGE_REQUEST = 3;

    public static final String FILE_NAME = "temp.jpg";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;
    private ImageView mMainImage;
    private EditText mImageDetails;
    private final Activity mActivity;
    private Bitmap bitmap;

    public GetImage(Activity activity) {
        mActivity = activity;
        mMainImage=mActivity.findViewById(R.id.menucapture);
//        mImageDetails=mActivity.findViewById(R.id.img_text);
    }
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(mActivity, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_MEDIA_IMAGES)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            Log.d("Intent result", "startGalleryChooser: "+mActivity);
            mActivity.startActivityForResult(intent, GALLERY_IMAGE_REQUEST);
        }
    }



    public void startCamera() {
        if (PermissionUtils.requestPermission(
                mActivity,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mActivity.startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(GetImage.this, mActivity.getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    private void uploadImage(Uri uri) {

        if (uri != null) {
            try {
                bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri), MAX_DIMENSION);
                mMainImage.setImageBitmap(bitmap);
                Camera_capture.setBitmap(bitmap);
                // OCR func //
                ImageToText img2Text=new ImageToText(mActivity);
                img2Text.callCloudVision(bitmap);

            } catch (IOException e) {
                Log.d("Get image", "Image picking failed because " + e.getMessage());
//                Toast.makeText(mActivity, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("Get image", "Image picker gave us a null image.");
//            Toast.makeText(mActivity, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }
    public Bitmap getMenuImage(){
        return bitmap;
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


}
