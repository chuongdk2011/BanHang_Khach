package com.example.banhang_khach.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang_khach.Adapter.ProAdapter;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_trangchu extends Fragment {

    public fragment_trangchu() {
    }
    ArrayList<DTO_QlySanPham> list;
    ProAdapter adapter;
    RecyclerView rcv_pro;
    public static fragment_trangchu newInstance(){
        fragment_trangchu fragment = new fragment_trangchu();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_trangchu, container, false);
        rcv_pro = viewok.findViewById(R.id.rcv_pro);

        list= new ArrayList<>();
        getDataPro();
        adapter = new ProAdapter(getContext(),list);
        rcv_pro.setAdapter(adapter);

        return viewok;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getDataPro() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {

                    DTO_QlySanPham sanPham = dataSnapshot.getValue(DTO_QlySanPham.class);
                    list.add(sanPham);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
