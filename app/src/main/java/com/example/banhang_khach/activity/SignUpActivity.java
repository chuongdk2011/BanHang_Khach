package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.banhang_khach.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {


    String TAG = "zzzz";
    private ProgressDialog progressDialog;
    private EditText edemail;
    private EditText edpasswd;
    private EditText edrepasswd;
    private TextInputLayout tilemail;
    private TextInputLayout tilpass;
    private TextInputLayout tilrepass;

    private LinearLayout layout;

    int temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressDialog = new ProgressDialog(this);

        edemail = findViewById(R.id.signup_email);
        edpasswd = findViewById(R.id.signup_password);
        edrepasswd = findViewById(R.id.signup_repass);

        tilemail = findViewById(R.id.til_name_ad);
        tilpass = findViewById(R.id.til_price_add);
        tilrepass = findViewById(R.id.til_des_add);

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                if (temp==0){
                    clickSignUp();
                }else{
                    temp=0;
                }

            }
        });
    }



    private void clickSignUp() {
        String email = edemail.getText().toString().trim();
        String pass = edpasswd.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    String abc;
    void validate(){
        String pass = edpasswd.getText().toString();
        String repass = edrepasswd.getText().toString();
        if (edemail.getText().length() == 0){
            tilemail.setError("Don't leave your email blank!");
            temp++;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches()){
            tilemail.setError("Incorrect email format!");
            temp++;
        }else{
            tilemail.setError("");
        }

        if (edpasswd.getText().length() == 0){
            tilpass.setError("Don't leave your password blank!");
            temp++;
        }else if(edpasswd.getText().length() <= 6){
            tilpass.setError("Password greater than 6 characters!");
            temp++;
        }
        else{
            tilpass.setError("");
        }

        if (edrepasswd.getText().length() == 0){
            tilrepass.setError("Don't leave your Repassword blank!");
            temp++;
        }else if(!(repass.equalsIgnoreCase(pass))){
            tilrepass.setError("Repassword is incorrect!");
            temp++;
        }else{
            tilrepass.setError("");
        }
    }
}