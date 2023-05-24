package menubee_backend;

import static menubee_backend.APIKEYS.ChatGptAPIKEY;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chatgptAPI {
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final String API_KEY = ChatGptAPIKEY;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100,TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build();
    private final MediaType mediaType = MediaType.parse("application/json");

    public String getResponse(String prompt) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model","text-davinci-003");
        jsonObject.put("prompt", prompt);
        jsonObject.put("max_tokens", 1000);
        jsonObject.put("temperature", 0.5);

        RequestBody body = RequestBody.create(jsonObject.toString(), mediaType);

        System.out.print(body);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();
        JSONObject jsonResponse = new JSONObject(jsonString);
        System.out.println(jsonResponse);
        String choicesJson = jsonResponse.getJSONArray("choices").toString();
        JSONObject firstChoice = new JSONObject(choicesJson.substring(1, choicesJson.length() - 1));
        String text = firstChoice.getString("text");

        return text;
    }
}

