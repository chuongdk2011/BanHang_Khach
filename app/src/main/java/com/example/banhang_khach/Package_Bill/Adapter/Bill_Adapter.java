package com.example.banhang_khach.Package_Bill.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.Package_Bill.Activity.Chitietdonhang_Activity;
import com.example.banhang_khach.R;
import java.util.ArrayList;

public class Bill_Adapter extends BaseAdapter {
    String TAG = "xacnhandonadapter";
    int sl = 0;
    Context context;
    ArrayList<BillDTO> arrayList;

    public Bill_Adapter(Context context, ArrayList<BillDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        BillDTO billDTO = arrayList.get(i);
        return billDTO;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewok;
        if (view == null){
            viewok = View.inflate(viewGroup.getContext(), R.layout.item_xacnhandon, null);
        }else {
            viewok = view;
        }

        BillDTO billDTO = arrayList.get(i);
        TextView datedonhang,tonggiadonhang, chitietdons;
        datedonhang = viewok.findViewById(R.id.date_donhang);
        tonggiadonhang = viewok.findViewById(R.id.tonggia_donhang);
        chitietdons = viewok.findViewById(R.id.chitietdon);

        chitietdons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context1 = view.getContext();
                Intent intent = new Intent(context1, Chitietdonhang_Activity.class);
                intent.putExtra("id_bill", billDTO.getIdBill());
                intent.putExtra("status", billDTO.getStatus());
                intent.putExtra("id_user", billDTO.getIduser());
                intent.putExtra("idthanhtoan", billDTO.getIdthanhtoan());
                context1.startActivity(intent);
            }
        });

        datedonhang.setText("Ngày đặt: " +billDTO.getDateBuy());
        tonggiadonhang.setText("Tổng giá: " + billDTO.getTotalPrice()+ " VND" );
        return viewok;
    }
}
