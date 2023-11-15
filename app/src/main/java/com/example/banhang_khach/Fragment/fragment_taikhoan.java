package com.example.banhang_khach.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.Package_Bill.Activity.Billout_Activity;
import com.example.banhang_khach.Package_Bill.Activity.Giaohang_Activity;
import com.example.banhang_khach.Package_Bill.Activity.Hoanthanhdon_Activity;
import com.example.banhang_khach.Package_Bill.Activity.Layhang_Activity;
import com.example.banhang_khach.Package_Bill.Activity.Xacnhandon_Activity;
import com.example.banhang_khach.R;
import com.example.banhang_khach.activity.InformationActivity;
import com.example.banhang_khach.activity.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_taikhoan extends Fragment {
    String TAG = "fragmenttaikhoan";
    TextView tv_fullname, tvsoluongbill1, tvsoluongbill2, tvsoluongbill3;
    CardView information_id, carddonhuy, carddonhoanthanh, cardviewbill1, cardviewbill2, cardviewbill3;
    private FirebaseAuth auth;
    RelativeLayout rlxacnhandon, rllayhang, rldanggiao;
    ArrayList<BillDTO> list1;
    ArrayList<BillDTO> list2;
    ArrayList<BillDTO> list3;

    public fragment_taikhoan() {
    }

    public static fragment_taikhoan newInstance(){
        fragment_taikhoan fragment = new fragment_taikhoan();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_taikhoan, container, false);
        tv_fullname = viewok.findViewById(R.id.tv_fullname);
        rlxacnhandon = viewok.findViewById(R.id.rl_xacnhandon);
        rllayhang = viewok.findViewById(R.id.rl_layhang);
        rldanggiao = viewok.findViewById(R.id.rl_danggiao);
        information_id = viewok.findViewById(R.id.card2_infomation);
        carddonhuy = viewok.findViewById(R.id.card_donhuy);
        carddonhoanthanh = viewok.findViewById(R.id.card_donhoanthanh);
        cardviewbill1 = viewok.findViewById(R.id.cardviewbill1);
        tvsoluongbill1 = viewok.findViewById(R.id.tvsoluongbill1);
        tvsoluongbill2 = viewok.findViewById(R.id.tvsoluongbill2);
        tvsoluongbill3 = viewok.findViewById(R.id.tvsoluongbill3);
        cardviewbill2 = viewok.findViewById(R.id.cardviewbill2);
        cardviewbill3 = viewok.findViewById(R.id.cardviewbill3);
        rlxacnhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Xacnhandon_Activity.class);
                startActivity(intent);
            }
        });
        rllayhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Layhang_Activity.class);
                startActivity(intent);
            }
        });
        rldanggiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Giaohang_Activity.class);
                startActivity(intent);
            }
        });
        information_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InformationActivity.class);
                startActivity(intent);
            }
        });
        carddonhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Billout_Activity.class);
                startActivity(intent);
            }
        });

        carddonhoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Hoanthanhdon_Activity.class);
                intent.putExtra("status", 4);
                startActivity(intent);
            }
        });
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        getdata();
        return viewok;
    }
    public void getdata(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("BillProduct");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BillDTO billDTO = dataSnapshot.getValue(BillDTO.class);
                    if (billDTO.getStatus() == 1 && auth.getUid().equals(billDTO.getIduser())){
                        list1.add(billDTO);
                    }
                    if (billDTO.getStatus() == 2 && auth.getUid().equals(billDTO.getIduser())){
                        list2.add(billDTO);
                    }
                    if (billDTO.getStatus() == 3 && auth.getUid().equals(billDTO.getIduser())){
                        list3.add(billDTO);
                    }
                }
                if (list1.size() == 0){
                    cardviewbill1.setVisibility(View.GONE);
                }
                else {
                    cardviewbill1.setVisibility(View.VISIBLE);
                    tvsoluongbill1.setText(""+list1.size());
                }
                if (list2.size() == 0){
                    cardviewbill2.setVisibility(View.GONE);
                }
                else {
                    cardviewbill2.setVisibility(View.VISIBLE);
                    tvsoluongbill2.setText(""+list2.size());
                }
                if (list3.size() == 0){
                    cardviewbill3.setVisibility(View.GONE);
                }
                else {
                    cardviewbill3.setVisibility(View.VISIBLE);
                    tvsoluongbill3.setText(""+list3.size());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "err: " + error);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else{
            String name = user.getDisplayName();
            if (name == null){
                tv_fullname.setText("Hello Word !");
            }else{
                tv_fullname.setText(""+name);
            }
        }
        view.findViewById(R.id.btn_dangxuat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userUID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("userTokens").child(userUID);
                tokenRef.removeValue();
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(
                        getContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                LoginManager.getInstance().logOut();
            }
        });
    }
}
