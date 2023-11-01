package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhang_khach.Adapter.ChatAdapter;
import com.example.banhang_khach.DTO.ChatDTO;
import com.example.banhang_khach.DTO.FcmMessage;
import com.example.banhang_khach.Interface.FcmApiService;
import com.example.banhang_khach.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    ImageView img_back;
    CardView btn_send;
    EditText ed_chat;
    String senderRoom,reciverRoom,SenderUID;
    FirebaseDatabase  database;
    FirebaseAuth firebaseAuth;
    String reciverUid = "admin";
    ArrayList<ChatDTO> list;
    ChatAdapter adapter;
    RecyclerView rcv_chat;
    private ChildEventListener chatEventListener;
    DatabaseReference chatreference;

    TextView tv_nameShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        initView();


        SenderUID =  firebaseAuth.getUid();

        senderRoom = SenderUID+reciverUid;
        reciverRoom = reciverUid+SenderUID;
        list = new ArrayList<>();
        adapter = new ChatAdapter(this,list);
        getListChat();
        rcv_chat.setAdapter(adapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMessage();

                sendNotification(reciverUid, ed_chat.getText().toString());
            }
        });

    }
    private void initView() {
        img_back = findViewById(R.id.img_backChat);
        btn_send = findViewById(R.id.sendbtnn);
        ed_chat = findViewById(R.id.ed_msg);
        rcv_chat = findViewById(R.id.rcv_chat);
    }

    private void getListChat() {
        chatreference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatDTO newMessage = dataSnapshot.getValue(ChatDTO.class);
                list.add(newMessage);
                adapter.notifyDataSetChanged();

//                sendNotification(reciverUid, newMessage.getMessage());
//                sendNotification(newMessage);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                ChatDTO deletedMessage = dataSnapshot.getValue(ChatDTO.class);
                if (deletedMessage != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId().equals(deletedMessage.getId())) {
                            list.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ChatActivity", "Failed to read chats.", databaseError.toException());
            }
        };

        // Đặt ChildEventListener vào tham chiếu database
        chatreference.addChildEventListener(chatEventListener);
    }
    public void sendFcmData(String fcmToken, String content) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FcmApiService apiService = retrofit.create(FcmApiService.class);

        FcmMessage message = new FcmMessage();
        message.setTo(fcmToken);

        Map<String, String> data = new HashMap<>();
        data.put("title", "Có Tin Nhắn Mới");
        data.put("message", content);

        message.setData(data);

        Call<ResponseBody> call = apiService.sendFcmMessage(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Gửi thành công
                    Log.d("ChatActivity", "Gửi thông báo FCM thành công");
                } else {
                    // Gửi thất bại
                    Log.e("ChatActivity", "Gửi thông báo FCM thất bại");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xảy ra lỗi
                Log.e("ChatActivity", "Lỗi khi gửi thông báo FCM: " + t.getMessage());
            }
        });
    }

    private void sendNotification(String recipientUID, String message) {
        String notificationTitle = "Bạn có tin nhắn mới!";
        String notificationMessage = message;

        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("userTokens").child(recipientUID);
        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String recipientToken = dataSnapshot.getValue(String.class);
                if (recipientToken != null) {
                    Map<String, String> dataMap = new HashMap<>();
                    dataMap.put("title", notificationTitle);
                    dataMap.put("message", notificationMessage);

                    sendFcmData(recipientToken, notificationMessage);
                    Log.d("ChatActivity", "Gửi thông báo đến FCM Service");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatActivity", "Lỗi khi lấy mã FCM của người nhận: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatreference != null && chatEventListener != null) {
            chatreference.removeEventListener(chatEventListener);
        }
    }


    private void addMessage() {
        String message = ed_chat.getText().toString();
        if (message.isEmpty()){
            Toast.makeText(ChatActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
            return;
        }
        ed_chat.setText("");
        Date date = new Date();

        database= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("chats")
                .child(senderRoom)
                .child("messages");
        String id = databaseReference.push().getKey();
        ChatDTO messagess = new ChatDTO(id,message,SenderUID,date.getTime());
               databaseReference.child(id).setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .child(id).setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                });
    }


}