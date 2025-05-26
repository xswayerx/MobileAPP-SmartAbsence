package com.example.profattendaceemsi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Assistant_virtuelActivity extends AppCompatActivity {

    private final String API_KEY = "AIzaSyArsJHoZCD8QE2FGlQ61BFm4Hx7UI0tm18";

    private TextView assistantTitle;
    private ScrollView scrollView;
    private LinearLayout messageContainer;
    private EditText messageInput;
    private Button sendButton;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_virtuel);

        assistantTitle = findViewById(R.id.assistantTitle);
        scrollView = findViewById(R.id.scrollView);
        messageContainer = findViewById(R.id.chatContainer);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                TextView userTextView = new TextView(this);
                userTextView.setText("You: " + userMessage);
                messageContainer.addView(userTextView);
                messageInput.setText("");
                scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));

                // ✅ Send to Gemini API
                sendMessageToGeminiAPI(userMessage);
            }
        });
    }

    private void sendMessageToGeminiAPI(String userMessage) {
        new Thread(() -> {
            try {
                JSONObject part = new JSONObject();
                part.put("text", userMessage);

                JSONArray partsArray = new JSONArray();
                partsArray.put(part);

                JSONObject content = new JSONObject();
                content.put("parts", partsArray);

                JSONArray contentsArray = new JSONArray();
                contentsArray.put(content);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("contents", contentsArray);

                RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
                Request request = new Request.Builder()
                        .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY)
                        .post(body)
                        .build();

                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();

                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    JSONObject contentResp = candidates.getJSONObject(0).getJSONObject("content");
                    JSONArray partsResp = contentResp.getJSONArray("parts");
                    String botReply = partsResp.getJSONObject(0).getString("text");

                    runOnUiThread(() -> {
                        TextView botTextView = new TextView(this);
                        botTextView.setText("Bot: " + botReply);
                        messageContainer.addView(botTextView);
                        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                    });
                } else {
                    android.util.Log.e("GeminiAPI", "Response code: " + response.code());
                    android.util.Log.e("GeminiAPI", "Response body: " + (response.body() != null ? response.body().string() : "null"));
                    showError("Erreur: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur lors de la communication avec l'API: " + e.getMessage());
            }
        }).start();
    }


    private void showError(String errorMessage) {
        runOnUiThread(() -> {
            TextView errorText = new TextView(this);
            errorText.setText("Bot: " + errorMessage);
            messageContainer.addView(errorText);
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        });
    }
}
