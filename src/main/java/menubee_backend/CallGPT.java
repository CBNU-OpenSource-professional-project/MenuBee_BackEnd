package menubee_backend;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menubee.R;

import org.json.JSONException;

import java.io.IOException;

public class CallGPT extends AppCompatActivity {
    private static chatgptAPI gptAPI = new chatgptAPI();
//    private static boolean isFirstQuestionDone = false;
//    private static boolean isSecondQuestionDone = false;
    private static String imgResult="";
    private static EditText GptResult;
    private static Activity GPTActivity;

    static String[] result1;

    public CallGPT(Activity activity){
        GPTActivity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        }
    public static void setText(String imgDetail) {
        if (imgDetail == null) {
            return;
        }
        imgResult = "가격없이 메뉴들만 분리해줘 그리고 결과값에 숫자가 없어야해 \n"+imgDetail;
        setGptText();
    }

    public static void setGptText() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return gptAPI.getResponse(imgResult);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(String result) { //백그라운드 작업후 호출

                Log.d("CALL GPT EXECUTE", "onPostExecute: "+result);
                super.onPostExecute(result);
                if (result != null) {
                    result1 = result.split("\n");
                    //저장값 확인
                    for(int i = 0 ; i<result1.length; i++)
                        System.out.println("배열 저장 값 : " + result1[i]);
                }
                else{
                    Log.d("Else CASE", "FAIELD!!!!!!!!!!!!");
                }

            }
        }.execute();
    }



}
