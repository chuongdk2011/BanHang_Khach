package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.banhang_khach.MainActivity;
import com.example.banhang_khach.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 20;


    String TAG = "zzzz";
    private ProgressDialog progressDialog;

    private EditText edemail;
    private EditText edpasswd;
    private TextInputLayout tilemail;
    private TextInputLayout tilpass;


    int temp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edemail = findViewById(R.id.login_email);
        edpasswd = findViewById(R.id.login_password);
        tilemail = findViewById(R.id.til_email);
        tilpass = findViewById(R.id.til_password);
        progressDialog = new ProgressDialog(this);


        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,options);

        String abc;
//        findViewById(R.id.tv_signup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.tv_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                if (temp == 0) {
                    clickLogin();
                } else {
                    temp = 0;
                }

            }
        });

        findViewById(R.id.img_gg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGG();

            }

        });


    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    private void loginWithGG() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                fireWithGG(account.getIdToken());

            } catch (Exception e) {
                Log.e(TAG, "onActivityResult: " + e.getMessage());

            }
        }



    }

    private void fireWithGG(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("fullname", user.getDisplayName());
                            map.put("email",user.getEmail());
                            map.put("role","User");

                            database.getReference().child("Users").child(user.getUid()).setValue(map);
                            Toast.makeText(LoginActivity.this, "Login Email Success.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();
                        }else{
                            Log.e(TAG, "onComplete: something!!" );
                        }
                    }
                });
    }


    private void validate() {
        if (edemail.getText().length() == 0) {
            tilemail.setError("Don't leave your email blank!");
            temp++;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches()) {
            tilemail.setError("Incorrect email format!");
            temp++;
        } else {
            tilemail.setError("");
        }

        if (edpasswd.getText().length() == 0) {
            tilpass.setError("Don't leave your password blank!");
            temp++;
        } else if (edpasswd.getText().length() <= 6) {
            tilpass.setError("Password greater than 6 characters!");
            temp++;
        } else {
            tilpass.setError("");
        }


    }

    private void clickLogin() {
        String email = edemail.getText().toString().trim();
        String password = edpasswd.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = auth.getCurrentUser();
//                            Log.e(TAG, "onComplete: " + user.getEmail());
                            Log.e(TAG, "onComplete: " + user.getUid());
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference myRefId = database.getReference("Users/" + user.getUid() + "/id");
                            myRefId.setValue(user.getUid());

                            DatabaseReference myRefEmail = database.getReference("Users/" + user.getUid() + "/email");
                            myRefEmail.setValue(user.getEmail());

//                            DatabaseReference myRefPassword = database.getReference("Users/" + user.getUid() + "/password");
//                            myRefPassword.setValue(password);

//                            DatabaseReference myRefFullname = database.getReference("Users/"+user.getUid()+"/fullname");
//                            myRefFullname.setValue("");

//                            DatabaseReference myRefPhone = database.getReference("Users/"+user.getUid()+"/phone");
//                            myRefPhone.setValue("");

                            DatabaseReference myRefRole = database.getReference("Users/" + user.getUid() + "/role");
                            myRefRole.setValue("User");


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
