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
    private static boolean isFirstQuestionDone = false;
    private static boolean isSecondQuestionDone = false;
    private static String imgResult="";
    private static EditText GptResult;
    private static Activity GPTActivity;

    static String[] result1;

    public CallGPT(Activity activity){
        GPTActivity=activity;
//        GptResult = GPTActivity.findViewById(R.id.img_text);
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
            protected void onPostExecute(String result) {

                Log.d("CALL GPT EXECUTE", "onPostExecute: "+result);
                super.onPostExecute(result);
                if (GptResult != null) {
                    GptResult.setText(result);
                    result1 = result.split("\n");
                }
                else{
                    Log.d("Else CASE", "FAIELD!!!!!!!!!!!!");
                }

                if (!isFirstQuestionDone) {
                    isFirstQuestionDone = true;
                    imgResult = result +" \n 위 메뉴를 사용하는 업장 한 가지를 추측해줘" ;// 첫 번째 질문의 답변을 두 번째 질문에 포함한다.
                    setGptText();
                }
                if(!isSecondQuestionDone){
                    isSecondQuestionDone=true;
                    imgResult=result+"\n 위 업장에서 고객이 메뉴 혹은 업장에 요청할 수 있는 질문 알려줘";
                    Log.d("SecondQuestion", "onPostExecute: "+imgResult);
                    setGptText();

                }
            }
        }.execute();
    }



}
