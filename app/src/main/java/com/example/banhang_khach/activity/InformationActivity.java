package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity {
    TextView tv_email, tv_sdt, tv_fullname, tv_tuoi, tv_diachi;
    ArrayList<UserDTO> mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        tv_email = findViewById(R.id.tv_email);
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_tuoi = findViewById(R.id.tv_tuoi);
        tv_diachi = findViewById(R.id.tv_diachi);
        ImageView sua1 = findViewById(R.id.sua1);
        ImageView sua2 = findViewById(R.id.sua2);
        ImageView sua3 = findViewById(R.id.sua3);
        ImageView sua4 = findViewById(R.id.sua4);
        ImageView img_backuser = findViewById(R.id.img_backuser);
        img_backuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        sua1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua1();
            }
        });
        sua2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua2();
            }
        });
        sua3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua3();
            }
        });
        sua4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua4();
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Đặt ID của người dùng bạn muốn lấy thông tin
        DatabaseReference userRef = databaseReference.child("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Dữ liệu người dùng được tìm thấy
                    UserDTO user = dataSnapshot.getValue(UserDTO.class);

                    // Kiểm tra xem có dữ liệu số điện thoại, tuổi và địa chỉ không
                    if (user != null) {
                        tv_email.setText("Email:"+user.getEmail());

                        if (user != null) {
                            int phone = user.getPhone();
                            int defaultValue = -1; // Sử dụng -1 làm giá trị mặc định

                            if (phone != defaultValue) {
                                String phoneText = "Phone:(+84) " + String.valueOf(phone);
                                tv_sdt.setText(phoneText);
                            } else {
                                tv_sdt.setVisibility(View.GONE);
                            }
                        } else {
                            // Người dùng không tồn tại hoặc lỗi xảy ra
                        }
                        if (user.getFullname() != null) {
                            String fullname = user.getFullname();
                            String fullnameText = "FullName: " + fullname;
                            tv_fullname.setText(fullnameText);
                            tv_fullname.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu full name chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_fullname.setText("FullName: chưa điền");
                            tv_fullname.setVisibility(View.VISIBLE);
                        }
                        if (user.getAge() != null) {
                            String tuoi = user.getAge();
                            String tuoiText = "Tuoi: " + tuoi;
                            tv_tuoi.setText(tuoiText);
                            tv_tuoi.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu Age chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_tuoi.setText("Tuoi: chưa điền");
                            tv_tuoi.setVisibility(View.VISIBLE); // Hiển thị TextView
                        }
                        if (user.getAdress() != null) {
                            String diachi = user.getAdress();
                            String diachiText = "Diachi: " + diachi;
                            tv_diachi.setText(diachiText);
                            tv_diachi.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu Address chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_diachi.setText("Diachi: chưa điền");
                            tv_diachi.setVisibility(View.VISIBLE); // Hiển thị TextView
                        }
                    } else {
                        // Người dùng không tồn tại hoặc lỗi xảy ra
                    }
                } else {
                    // Người dùng không
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });




    }
    private void dialogsua1() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = databaseReference.child("Users").child(userId);

        final Dialog editDialog = new Dialog(InformationActivity.this);
        editDialog.setContentView(R.layout.edit_dialog);

        EditText tv_suafullname = editDialog.findViewById(R.id.tv_suafullname);
        Button saveButton = editDialog.findViewById(R.id.saveButton);

        // Truyền tham chiếu đến TextView trong Activity cho Dialog
        TextView tv_fullname = InformationActivity.this.findViewById(R.id.tv_fullname);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedValue = tv_suafullname.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                databaseReference.child("fullname").setValue(editedValue);
                editDialog.dismiss();

                // Cập nhật TextView trong Activity
                tv_fullname.setText("Fullname:"+ editedValue);
            }
        });

        userRef.child("fullname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String updatedFullname = dataSnapshot.getValue(String.class);
                    tv_suafullname.setText(updatedFullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý bất kỳ lỗi nào trong quá trình truy xuất dữ liệu
            }
        });

        editDialog.show();
    }
    private void dialogsua2() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = databaseReference.child("Users").child(userId);

        final Dialog editDialog = new Dialog(InformationActivity.this);
        editDialog.setContentView(R.layout.edit_dialog);

        EditText tv_suaphone = editDialog.findViewById(R.id.tv_suafullname);
        Button saveButton = editDialog.findViewById(R.id.saveButton);
        TextView tv_phone = InformationActivity.this.findViewById(R.id.tv_sdt);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedValue = tv_suaphone.getText().toString();  //
                int editedPhone = Integer.parseInt(editedValue);  // Convert the edited value to an int
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                databaseReference.child("phone").setValue(editedPhone);
                // Add "0" to the beginning of the edited value
                tv_suaphone.setText("0" + editedValue);
                editDialog.dismiss();


                // Update the phone TextView in your activity
                tv_phone.setText("Phone:(+84) " + editedPhone);  // Update the TextView with "Phone: " prefix
            }
        });


        userRef.child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int updatedPhone = dataSnapshot.getValue(Integer.class);
                    tv_suaphone.setText(String.valueOf(updatedPhone));  // Set the EditText in the dialog as a String
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors during data retrieval
            }
        });

        editDialog.show();
    }
    private void dialogsua3() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = databaseReference.child("Users").child(userId);

        final Dialog editDialog = new Dialog(InformationActivity.this);
        editDialog.setContentView(R.layout.edit_dialog);

        EditText tv_suafullname = editDialog.findViewById(R.id.tv_suafullname);
        Button saveButton = editDialog.findViewById(R.id.saveButton);

        // Truyền tham chiếu đến TextView trong Activity cho Dialog
        TextView tv_age = InformationActivity.this.findViewById(R.id.tv_tuoi);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedValue = tv_suafullname.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                databaseReference.child("age").setValue(editedValue);
                editDialog.dismiss();

                // Cập nhật TextView trong Activity
                tv_age.setText("Tuoi:"+ editedValue);
            }
        });

        userRef.child("age").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String updatedFullname = dataSnapshot.getValue(String.class);
                    tv_suafullname.setText(updatedFullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý bất kỳ lỗi nào trong quá trình truy xuất dữ liệu
            }
        });

        editDialog.show();
    }
    private void dialogsua4() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = databaseReference.child("Users").child(userId);

        final Dialog editDialog = new Dialog(InformationActivity.this);
        editDialog.setContentView(R.layout.edit_dialog);

        EditText tv_suafullname = editDialog.findViewById(R.id.tv_suafullname);
        Button saveButton = editDialog.findViewById(R.id.saveButton);

        // Truyền tham chiếu đến TextView trong Activity cho Dialog
        TextView tv_adress = InformationActivity.this.findViewById(R.id.tv_diachi);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedValue = tv_suafullname.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                databaseReference.child("adress").setValue(editedValue);
                editDialog.dismiss();

                // Cập nhật TextView trong Activity
                tv_adress.setText("Diachi:"+editedValue);
            }
        });

        userRef.child("adress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String updatedFullname = dataSnapshot.getValue(String.class);
                    tv_suafullname.setText(updatedFullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý bất kỳ lỗi nào trong quá trình truy xuất dữ liệu
            }
        });

        editDialog.show();
    }


}
//                        if (user.getAdress() != null) {
//                            String diachiText = "Diachi: " + user.getAdress();
//                            tv_diachi.setText(diachiText);
//                        } else {
//                            tv_diachi.setVisibility(View.GONE);
//                        }