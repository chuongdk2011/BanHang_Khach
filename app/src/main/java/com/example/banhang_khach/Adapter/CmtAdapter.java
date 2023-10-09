package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang_khach.DTO.CommentDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CmtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<CommentDTO> list;
    Context context;

    public CmtAdapter(ArrayList<CommentDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bl,parent,false);

        return new CmtAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CmtAdapter.ItemViewHolder viewHolder = (CmtAdapter.ItemViewHolder) holder;
        CommentDTO dto = list.get(position);

        if (dto.getUserDTO().getFullname().isEmpty()){
            viewHolder.tv_nameBL.setText("null");
        }else {
            viewHolder.tv_nameBL.setText(dto.getUserDTO().getFullname());
        }
        viewHolder.tv_timeBL.setText(dto.getDate());
        viewHolder.tv_contentBL.setText(dto.getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameBL,tv_contentBL,tv_timeBL;



        public ItemViewHolder(View view) {
            super(view);

            tv_nameBL = view.findViewById(R.id.tv_nameBL);
            tv_contentBL = view.findViewById(R.id.tv_contentBL);
            tv_timeBL = view.findViewById(R.id.tv_timeBL);
        }
    }

}
