package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.activity.Chitietsanpham;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "proadapter";
    Context context;
    ArrayList<DTO_QlySanPham> list;
    boolean isMyFavorite = false;


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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() == null){
////            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
//        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(sanPham.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isMyFavorite = snapshot.exists();
                            if (isMyFavorite){
                                viewHolder.img_fave.setImageResource(R.drawable.favorite_24);
                            }else{
                                viewHolder.img_fave.setImageResource(R.drawable.no_favorite_24);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//        }

        viewHolder.img_fave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
                }else{
                    if (isMyFavorite){
                        removeFavorite(context,sanPham.getId());
                    }else {
                        addToFavorite(context,sanPham.getId());
                    }
                }
            }
        });
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
        ImageView img_fave;

        LinearLayout ll_chitietsp;



        public ItemViewHolder(View view) {
            super(view);

            tv_gia = view.findViewById(R.id.tv_giatien);
            img_pro = view.findViewById(R.id.img_pro);
            ll_chitietsp = view.findViewById(R.id.id_chitietsp);
            tv_namepro = view.findViewById(R.id.tv_namepro);
            img_fave = view.findViewById(R.id.img_fave);
        }
    }

//    public void checkIsFavorive(Context context,String idProduct,boolean isMyFavorite){
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() == null){
////            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
//        }else{
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//            reference.child(firebaseAuth.getUid()).child("Favorites").child(idProduct)
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                isMyFavorite(true)
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//        }
//
//    }

    public void addToFavorite(Context context,String idProduct){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        }else{
            long timestamp = System.currentTimeMillis();

            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("idProduct",idProduct);
            hashMap.put("timeStamp",timestamp);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(idProduct)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Added to your favorites list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "failed to add to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void removeFavorite(Context context,String idProduct ){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(idProduct)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Removed to your favorites list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "failed to remove to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
