package com.example.banhang_khach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.banhang_khach.Fragment.fragment_trangchu;
import com.example.banhang_khach.Fragment.fragment_yeuthich;
import com.example.banhang_khach.Fragment.fragment_thongbao;
import com.example.banhang_khach.Fragment.fragment_taikhoan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Code giao diện menu botton navigation
        manager = getSupportFragmentManager();
        fragment_trangchu fragmentTrangchu = new fragment_trangchu();
        manager.beginTransaction().add(R.id.id_frame, fragmentTrangchu).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.id_botton_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_trangchu){
                    relaceFragment(fragment_trangchu.newInstance());
                }else
                if (id == R.id.nav_yeuthich) {
                    relaceFragment(fragment_yeuthich.newInstance());
                }else if (id == R.id.nav_thongbao){
                    relaceFragment(fragment_thongbao.newInstance());
                } else if (id == R.id.nav_taikhoan) {
                    relaceFragment(fragment_taikhoan.newInstance());
                }
                return true;
            }
        });

    }
    public void relaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_frame, fragment);
        transaction.commit();
    }
}