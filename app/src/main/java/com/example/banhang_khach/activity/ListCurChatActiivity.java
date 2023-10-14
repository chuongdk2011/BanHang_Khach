package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banhang_khach.Adapter.ListChatAdapter;
import com.example.banhang_khach.DTO.ChatDTO;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListCurChatActiivity extends AppCompatActivity {
    ImageView img_back;
    ListChatAdapter adapter;
    ArrayList<ChatDTO> list;
    RecyclerView rcv_listchat;
    FirebaseAuth firebaseAuth;
    String reciverUid = "admin";
    String SenderUID, senderRoom;
    TextView tv_null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cur_chat_actiivity);
        firebaseAuth = FirebaseAuth.getInstance();

        SenderUID =  firebaseAuth.getUid();

        senderRoom = SenderUID+reciverUid;
        initView();
        list = new ArrayList<>();
        adapter = new ListChatAdapter(this,list);

        getLastMess();

        rcv_listchat.setAdapter(adapter);



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
        rcv_listchat = findViewById(R.id.rcv_listchat);
        tv_null = findViewById(R.id.tv_null);
    }
    private void getLastMess() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");

        messagesRef.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatDTO lastMessage = dataSnapshot.getValue(ChatDTO.class);

                list.add(lastMessage);
                adapter.notifyDataSetChanged();
                if (list.size()==0){
                    tv_null.setVisibility(View.VISIBLE);
                    rcv_listchat.setVisibility(View.GONE);
                }else{
                    tv_null.setVisibility(View.GONE);
                    rcv_listchat.setVisibility(View.VISIBLE);
                }
                // Xử lý tin nhắn ở đây...
            }

            // Phần còn lại của các sự kiện...
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


}