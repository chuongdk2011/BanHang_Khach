package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banhang_khach.Adapter.ChatBotAdapter;
import com.example.banhang_khach.DTO.ChatBotDTO;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBotActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private List<ChatBotDTO> messageList = new ArrayList<>();
    private ChatBotAdapter messageAdapter;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private DatabaseReference mDatabase;
    private String senderRoom = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        initializeViews();
        setupRecyclerView();
        listenForMessages();

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) {
                saveToFirebase(question,senderRoom);
                messageEditText.setText("");
                callAPI(question);

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        img_back = findViewById(R.id.img_backChat);
    }
    private void setupRecyclerView() {
        messageAdapter = new ChatBotAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
    }

    private void listenForMessages() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chatbots").child(senderRoom).child("messages");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ChatBotDTO message = messageSnapshot.getValue(ChatBotDTO.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatBotActivity", "Failed to read messages.", databaseError.toException());
            }
        });
    }
    private void saveToFirebase(String message, String sentBy, String messageId) {
        ChatBotDTO newMessage = new ChatBotDTO(message, sentBy);
        if (messageId == null) {
            mDatabase.push().setValue(newMessage);
        } else {
            mDatabase.child(messageId).setValue(newMessage);
        }
    }
    private void saveToFirebase(String message, String sentBy) {
        saveToFirebase(message, sentBy, null);
    }
    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        saveToFirebase(response.trim(), ChatBotDTO.SENT_BY_BOT);
    }

    void callAPI(String question){

        String loadingMessageId = mDatabase.push().getKey();  // Lưu trữ ID của tin nhắn "Đang nhập..."
        saveToFirebase("Đang Nhập...", ChatBotDTO.SENT_BY_BOT, loadingMessageId);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Xử lý khi gặp lỗi kết nối
                e.printStackTrace();
                addResponse("Failed to load response due to network issue: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        mDatabase.child(loadingMessageId).removeValue();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.optJSONArray("choices"); // Sử dụng optJSONArray để xử lý null
                        if (jsonArray != null && jsonArray.length() > 0) {
                            String result = jsonArray.getJSONObject(0).optString("text", ""); // Sử dụng optString để xử lý null
                            addResponse(result.trim());
                        } else {
                            addResponse("No response choices found in the JSON.");
                        }
                    } else {
                        addResponse("Failed to load response due to: " + response.code() + " " + response.message());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    addResponse("Failed to parse the response: " + e.getMessage());
                }
            }
        });
    }
}