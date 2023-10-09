package com.example.banhang_khach.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang_khach.Adapter.FavoriteAdapter;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class fragment_yeuthich extends Fragment {

    FirebaseAuth firebaseAuth;
    private ArrayList<DTO_QlySanPham> list;
    private FavoriteAdapter adapter;
    private RecyclerView recyclerView;

    public fragment_yeuthich() {
    }

    public static fragment_yeuthich newInstance(){
        fragment_yeuthich fragment = new fragment_yeuthich();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_yeuthich, container, false);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_favorite);
        firebaseAuth = FirebaseAuth.getInstance();
        loadFavorite();
    }

    private void loadFavorite() {
        list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String proId =""+ ds.child("idProduct").getValue();
                            DTO_QlySanPham pham = new DTO_QlySanPham();
                            pham.setId(proId);

                            list.add(pham);

                        }

                        adapter = new FavoriteAdapter(getContext(),list);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
