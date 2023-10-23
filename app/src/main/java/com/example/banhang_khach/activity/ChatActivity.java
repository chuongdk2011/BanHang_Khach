package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.google.firebase.database.ChildEventListener;
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
    private ChildEventListener chatEventListener;
    DatabaseReference chatreference;
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
        chatreference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatDTO newMessage = dataSnapshot.getValue(ChatDTO.class);
                list.add(newMessage);
                adapter.notifyDataSetChanged();

                // Gửi thông báo cho người nhận
                sendNotification(newMessage);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ChatActivity", "Failed to read chats.", databaseError.toException());
            }
        };

        // Đặt ChildEventListener vào tham chiếu database
        chatreference.addChildEventListener(chatEventListener);
    }

    private void sendNotification(ChatDTO newMessage) {

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