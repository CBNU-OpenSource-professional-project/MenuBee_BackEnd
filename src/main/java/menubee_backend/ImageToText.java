package menubee_backend;
import static menubee_backend.APIKEYS.CloudVisionAPIKEY;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.example.menubee.R;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ImageToText extends AppCompatActivity {
    private static TextView mImageDetails;
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String CLOUD_VISION_API_KEY = CloudVisionAPIKEY;
    private static final int MAX_LABEL_RESULTS = 10;
    private Context OCRContext;
    private static Activity OCRActivity;
    private TextView mImageDetailes;
    private static String OCR_result;
    public ImageToText(Activity activity){
        OCRActivity= activity;

    }


    public Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = OCRActivity.getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(OCRActivity.getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature textDetection = new Feature();
                textDetection.setType("TEXT_DETECTION");
                textDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(textDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d("Vision API CALL", "created Cloud Vision request object, sending request");

        return annotateRequest;
    }


    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<ImageToText> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(ImageToText activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }


        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("BackGround Job", "created Cloud Vision request object, sending request"+mRequest);
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d("BackGround Job", "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d("BackGround Job", "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }
        // OCR 결과값 전송
        protected void onPostExecute(String result) {
            ImageToText activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                Log.d("OCR RESULT", "onPostExecute: "+result);
                OCR_result=result;
                Log.d("paramater", "onPostExecute: "+OCRActivity);
                CallGPT gpt=new CallGPT(OCRActivity);
                CallGPT.setText(result);
            }
        }
    }
    public static String getOcrResult(){
        return OCR_result;
    }
    public void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        Log.d("CALL OCR", "callCloudVision: "+bitmap);
//        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(ImageToText.this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d("Call OCT Func", "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }
    public static String convertResponseToString(BatchAnnotateImagesResponse response) { //여기에 있는 메시지를 전달해야함
        String message = "메뉴판 \n\n";

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        if (labels != null) {
            message += labels.get(0).getDescription(); //하나씩 출력함
        }
        else {
            message += "nothing";
        }

        return message;
    }
}

