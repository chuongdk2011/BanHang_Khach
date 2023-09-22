package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "cartorderadapter";
    Context context;
    ArrayList<CartOrderDTO> list;

    OnclickCheck onclickCheck;

    public CartOrderAdapter(Context context, ArrayList<CartOrderDTO> list, OnclickCheck onclickCheck) {
        this.context = context;
        this.list = list;
        this.onclickCheck = onclickCheck;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewok = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartorder, parent, false);
        return new ItemViewHolder(viewok);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartOrderDTO cartOrderDTO = list.get(position);
        CartOrderAdapter.ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        itemViewHolder.tvname.setText(cartOrderDTO.getNamesp());
        itemViewHolder.tvprice.setText(decimalFormat.format(cartOrderDTO.getPrice()));
        itemViewHolder.tvsoluong.setText(String.valueOf(cartOrderDTO.getSoluong()));
        Glide.with(context).load(cartOrderDTO.getImage()).centerCrop().into(itemViewHolder.imgitemgio);
        itemViewHolder.cbkcart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    onclickCheck.onCheckbox(cartOrderDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbkcart;
        ImageView imgitemgio;
        TextView tvname, tvprice, tvsoluong;
        public ItemViewHolder(View view) {
            super(view);
            cbkcart = view.findViewById(R.id.cbk_giohang);
            imgitemgio = view.findViewById(R.id.img_itemgio);
            tvname = view.findViewById(R.id.tvnamesp);
            tvprice = view.findViewById(R.id.tvgia);
            tvsoluong = view.findViewById(R.id.tv_soluong);
        }

    }

    public interface OnclickCheck{
        void onCheckbox(CartOrderDTO cartOrderDTO);
    }
}
