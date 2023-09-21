package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.activity.Chitietsanpham;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "proadapter";
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
        viewHolder.tv_namepro.setText(sanPham.getName());
        Glide.with(context).load(sanPham.getImage()).centerCrop().into(viewHolder.img_pro);

        viewHolder.ll_chitietsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context1 = view.getContext();
                Intent intent = new Intent(context1, Chitietsanpham.class);
                intent.putExtra("id_product", sanPham.getId());
                Log.d(TAG, "idproduct: " + sanPham.getId());
                intent.putExtra("name", sanPham.getName());
                intent.putExtra("price", sanPham.getPrice());
                intent.putExtra("image", sanPham.getImage());
                intent.putExtra("information", sanPham.getInformation());
                intent.putExtra("soluongkho", sanPham.getNumber());
                context1.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gia,tv_namepro;
        ImageView img_pro;

        LinearLayout ll_chitietsp;

        public ItemViewHolder(View view) {
            super(view);

            tv_gia = view.findViewById(R.id.tv_giatien);
            img_pro = view.findViewById(R.id.img_pro);
            ll_chitietsp = view.findViewById(R.id.id_chitietsp);
            tv_namepro = view.findViewById(R.id.tv_namepro);
        }

    }
}
