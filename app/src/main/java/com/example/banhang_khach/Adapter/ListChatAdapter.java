package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang_khach.DTO.ChatDTO;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.example.banhang_khach.activity.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.viewholder> {
    Context context;
    ArrayList<ChatDTO> usersArrayList;
    public ListChatAdapter(Context context, ArrayList<ChatDTO> usersArrayList) {
        this.context=context;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public ListChatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_chat,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.viewholder holder, int position) {

        ChatDTO chatDTO = usersArrayList.get(position);
        holder.userstatus.setText(chatDTO.getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
