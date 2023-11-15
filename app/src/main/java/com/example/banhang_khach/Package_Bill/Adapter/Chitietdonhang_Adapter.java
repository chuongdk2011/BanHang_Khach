package com.example.banhang_khach.Package_Bill.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.R;

import java.util.ArrayList;

public class Chitietdonhang_Adapter extends BaseAdapter {
    String TAG = "Chitietdonhang_Adapter";
    Context context;
    ArrayList<CartOrderDTO> arrayList;

    public Chitietdonhang_Adapter(Context context, ArrayList<CartOrderDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        CartOrderDTO cartOrderDTO = arrayList.get(i);
        return cartOrderDTO;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewok;
        if (view == null){
            viewok = View.inflate(viewGroup.getContext(), R.layout.item_chitietdonhang, null);
        }else {
            viewok = view;
        }
        CartOrderDTO cartOrderDTO = arrayList.get(i);
        ImageView img_itemgio;
        TextView tvnamesp, tvgia, tv_soluong;
        img_itemgio = viewok.findViewById(R.id.img_itemgio);
        tvgia = viewok.findViewById(R.id.tvgia);
        tvnamesp = viewok.findViewById(R.id.tvnamesp);
        tv_soluong = viewok.findViewById(R.id.tv_soluong);

        Glide.with(context).load(cartOrderDTO.getImage()).centerCrop().into(img_itemgio);
        tvnamesp.setText("Tên sản phẩm: " + cartOrderDTO.getNamesp());
        tvgia.setText("Giá: " + cartOrderDTO.getPrice());
        tv_soluong.setText("Số lượng: " + cartOrderDTO.getSoluong() + "");
        return viewok;
    }
}
