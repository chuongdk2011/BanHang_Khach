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
import android.widget.Toast;

import com.example.banhang_khach.Adapter.ChatAdapter;
import com.example.banhang_khach.DTO.ChatDTO;
import com.example.banhang_khach.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatDTO messages = dataSnapshot.getValue(ChatDTO.class);
                    list.add(messages);
                }
                Log.d("chuongdk", "onDataChange: "+list.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addMessage() {
        String message = ed_chat.getText().toString();
        if (message.isEmpty()){
            Toast.makeText(ChatActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
            return;
        }
        ed_chat.setText("");

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Trong Java, tháng bắt đầu từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        String timeCur = hour + ":" + minute + " " + day + "/" + month + "/" + year;
        ChatDTO messagess = new ChatDTO(message,SenderUID,timeCur);

        database= FirebaseDatabase.getInstance();
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                });
    }


}