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

public class fragment_taikhoan extends Fragment {
    String TAG = "fragmenttaikhoan";
    TextView tv_fullname;
    CardView information_id, carddonhuy;
    private FirebaseAuth auth;
    RelativeLayout rlxacnhandon, rllayhang, rldanggiao, rlhoanthanh;

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
        rlhoanthanh = viewok.findViewById(R.id.rl_hoanthanh);
        carddonhuy = viewok.findViewById(R.id.card_donhuy);
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
        rlhoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Hoanthanhdon_Activity.class);
                intent.putExtra("status", 4);
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
        return viewok;
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
            Log.e("zzz", "checkUser: "+ name);
        }
        view.findViewById(R.id.btn_dangxuat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
