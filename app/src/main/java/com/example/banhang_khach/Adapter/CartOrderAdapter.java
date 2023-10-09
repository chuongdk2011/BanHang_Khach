package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartOrderAdapter extends BaseAdapter {
    String TAG = "cartorderadapter";
    Context context;
    ArrayList<CartOrderDTO> list;

    CartOrderAdapter.OnclickCheck onclickCheck;
    ArrayList<String> arr = new ArrayList<>();
    int tonggia = 0;

    public CartOrderAdapter(Context context, ArrayList<CartOrderDTO> list, OnclickCheck onclickCheck) {
        this.context = context;
        this.list = list;
        this.onclickCheck = onclickCheck;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        CartOrderDTO cartOrderDTO = list.get(i);
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
            viewok = View.inflate(viewGroup.getContext(), R.layout.item_cartorder, null);
        }else {
            viewok = view;
        }

        CartOrderDTO cartOrderDTO = list.get(i);
        CheckBox cbkcart;
        ImageView imgitemgio, imgtru, imgcong;
        TextView tvname, tvprice, tvsoluong;
        cbkcart = viewok.findViewById(R.id.cbk_giohang);
        imgitemgio = viewok.findViewById(R.id.img_itemgio);
        tvname = viewok.findViewById(R.id.tvnamesp);
        tvprice = viewok.findViewById(R.id.tvgia);
        tvsoluong = viewok.findViewById(R.id.tv_soluong);
        imgtru = viewok.findViewById(R.id.imgtru);
        imgcong = viewok.findViewById(R.id.imgcong);


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvname.setText(cartOrderDTO.getNamesp());
        tvprice.setText(decimalFormat.format(cartOrderDTO.getPrice()));
        tvsoluong.setText(String.valueOf(cartOrderDTO.getSoluong()));
        Glide.with(context).load(cartOrderDTO.getImage()).centerCrop().into(imgitemgio);
        cbkcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbkcart.isChecked()){
                    arr.add(cartOrderDTO.getIdCart());
                    tonggia += cartOrderDTO.getPrice();
                    Log.d(TAG, "tonggia + : " + tonggia);
                    onclickCheck.onCheckboxTrue(tonggia);
                }else {
                    arr.remove(cartOrderDTO.getIdCart());
                    tonggia -= cartOrderDTO.getPrice();
                    Log.d(TAG, "tong gia -: " + tonggia);
                    onclickCheck.onCheckboxFalse(tonggia);
                }
                Log.d(TAG, "onClick: " + arr);
                onclickCheck.onQuality(arr);
            }
        });
        imgcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(tvsoluong.getText().toString().trim()) + 1;
                int giachia = (int) ((cartOrderDTO.getPrice()) / Integer.parseInt(tvsoluong.getText().toString().trim()));
                int gianew = giachia * soluong;
                if (soluong < 101){
                    String slmoi = String.valueOf(soluong);
                    String pricenew = String.valueOf(gianew);
                    tonggia += giachia;
                    Log.d(TAG, "tonggia = gianew + : " + tonggia);
                    onclickCheck.onCheckboxTrue(tonggia);
                    tvprice.setText(pricenew);
                    tvsoluong.setText(slmoi);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("CartOrder/" + cartOrderDTO.getId_product());
                    Map<String, Object> mapcartoder = new HashMap<>();
                    mapcartoder.put("price", gianew);
                    mapcartoder.put("soluong", soluong);
                    myRef.updateChildren(mapcartoder);
                }
            }
        });
        imgtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(tvsoluong.getText().toString().trim()) - 1;
                int giachia = (int) ((cartOrderDTO.getPrice()) / Integer.parseInt(tvsoluong.getText().toString().trim()));
                int gianew = giachia * soluong;
                if (soluong > 0){
                    String slmoi = String.valueOf(soluong);
                    String pricenew = String.valueOf(gianew);
                    tonggia -= giachia;
                    Log.d(TAG, "tonggia = gianew - : " + tonggia);
                    onclickCheck.onCheckboxFalse(tonggia);
                    tvprice.setText(pricenew);
                    tvsoluong.setText(slmoi);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("CartOrder/" + cartOrderDTO.getId_product());
                    Map<String, Object> mapcartoder = new HashMap<>();
                    mapcartoder.put("price", gianew);
                    mapcartoder.put("soluong", soluong);
                    myRef.updateChildren(mapcartoder);
                }
            }
        });
        return viewok;
    }

    public interface OnclickCheck{
        void onQuality(ArrayList<String> idcart);
        void onCheckboxTrue(int tongtien);
        void onCheckboxFalse(int tongtien);

    }
}
