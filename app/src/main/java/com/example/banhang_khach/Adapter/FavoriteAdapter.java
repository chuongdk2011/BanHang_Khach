package com.example.banhang_khach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.HolderFavorite>{
    private Context context;
    private ArrayList<DTO_QlySanPham> list;



    public FavoriteAdapter(Context context, ArrayList<DTO_QlySanPham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pro_faovrite,parent,false);
        return new HolderFavorite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFavorite holder, int position) {

        DTO_QlySanPham pham = list.get(position);

        loadPro(pham,holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.img_fave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFavorite(context,pham.getId());
            }
        });
    }

    private void loadPro(DTO_QlySanPham pham, HolderFavorite holder) {
        String proID = pham.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        ref.child(proID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String proName = ""+snapshot.child("name").getValue();
                String proInfo = ""+snapshot.child("information").getValue();
                String proCate = ""+snapshot.child("category").getValue();
                String proImage = ""+snapshot.child("image").getValue();
                String pronumber = ""+snapshot.child("number").getValue();
                String proPrice = ""+snapshot.child("price").getValue();
                String proId = ""+snapshot.child("id").getValue();

                pham.setFavorite(true);
                pham.setName(proName);
                pham.setInformation(proInfo);
                pham.setCategory(proCate);
                pham.setImage(proImage);
                pham.setNumber(Integer.parseInt(pronumber));
                pham.setPrice(proPrice);
                pham.setId(proId);

                holder.pro_name.setText(proName);
                holder.pro_info.setText(proInfo);
                holder.pro_cate.setText(proCate);
                Glide.with(context).load(proImage).centerCrop().into(holder.img_pro);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderFavorite extends RecyclerView.ViewHolder{
        TextView pro_name,pro_info,pro_cate;
        ImageView img_pro;
        ImageView img_fave;
        public HolderFavorite(@NonNull View itemView) {
            super(itemView);
            pro_name = itemView.findViewById(R.id.pro_name);
            pro_info = itemView.findViewById(R.id.pro_information);
            pro_cate = itemView.findViewById(R.id.pro_category);
            img_pro = itemView.findViewById(R.id.pro_img);
            img_fave = itemView.findViewById(R.id.img_remove_favorite);
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
