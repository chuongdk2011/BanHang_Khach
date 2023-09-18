package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    ArrayList<DTO_QlySanPham> list;


    public ProAdapter(Context context, ArrayList<DTO_QlySanPham> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pro,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_QlySanPham sanPham = list.get(position);

        ProAdapter.ItemViewHolder viewHolder = (ProAdapter.ItemViewHolder) holder;

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        float giatien = Float.parseFloat(sanPham.getPrice());
        viewHolder.tv_gia.setText(decimalFormat.format(giatien)+"đ");

        Glide.with(context).load(sanPham.getImage()).centerCrop().into(viewHolder.img_pro);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gia;
        ImageView img_pro;



        public ItemViewHolder(View view) {
            super(view);

            tv_gia = view.findViewById(R.id.tv_giatien);
            img_pro = view.findViewById(R.id.img_pro);

        }

    }
}
