package com.example.banhang_khach.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.banhang_khach.Adapter.ProAdapter;
import com.example.banhang_khach.DTO.CategoryDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_trangchu extends Fragment {

    public fragment_trangchu() {
    }
    ArrayList<DTO_QlySanPham> list;
    ProAdapter adapter;
    RecyclerView rcv_pro;
    ImageSlider image_slide;
    ArrayList<String> listSpin;
    ArrayAdapter<String> adapter1;
    List<CategoryDTO> mlistCate;
    Spinner spn_locsp;
    SearchView sv_pro;
    public static fragment_trangchu newInstance(){
        fragment_trangchu fragment = new fragment_trangchu();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_trangchu, container, false);
        rcv_pro = viewok.findViewById(R.id.rcv_pro);
        spn_locsp = viewok.findViewById(R.id.spn_locsp);
        sv_pro = viewok.findViewById(R.id.sv_pro);
        sv_pro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getDataPro(s);
                return true;
            }
        });
        listSpin = new ArrayList<>();
        list= new ArrayList<>();

        getDataPro("");
        adapter = new ProAdapter(getContext(),list);
        rcv_pro.setAdapter(adapter);
        getDataCat();
        mlistCate = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(getContext(), R.layout.item_spinner_sp, listSpin);
        spn_locsp.setAdapter(adapter1);
        spn_locsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listSpin.get(i).equals("Tất cả")){
                    getDataPro("");
                }else{
                    getDataByCat(listSpin.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        image_slide = viewok.findViewById(R.id.image_slider);

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.item_pro,"Áo Đẹp",null));
        images.add(new SlideModel(R.drawable.item_pro,"Áo Đẹp",null));
        images.add(new SlideModel(R.drawable.item_pro,"Áo Đẹp",null));
        images.add(new SlideModel(R.drawable.item_pro,"Áo Đẹp",null));
        images.add(new SlideModel(R.drawable.item_pro,"Áo Đẹp",null));

        image_slide.setImageList(images, ScaleTypes.CENTER_CROP);

        image_slide.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Toast.makeText(getContext(), "Ảnh Slide  số "+i, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void doubleClick(int i) {

            }
        });

        return viewok;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getDataPro(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {

                    DTO_QlySanPham sanPham = dataSnapshot.getValue(DTO_QlySanPham.class);
                    if (sanPham.getName().contains(key)){
                        list.add(sanPham);
                    }

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDataByCat(String value) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Products").orderByChild("category").equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        DTO_QlySanPham dto_qlySanPham = issue.getValue(DTO_QlySanPham.class);
                        list.add(dto_qlySanPham);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getDataCat() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("category");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSpin.clear();

                listSpin.add("Tất cả");
                for (DataSnapshot snapshot1 :
                        snapshot.getChildren()) {

                    CategoryDTO categoryDTO = snapshot1.getValue(CategoryDTO.class);
                    listSpin.add(categoryDTO.getName());


                }
                adapter1.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
