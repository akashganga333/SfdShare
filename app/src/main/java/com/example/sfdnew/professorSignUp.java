package com.example.sfdnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class professorSignUp extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText regName, regEmail, regPhoneNo, regPassword,regUserCode;
    String regUsername;
    Button regBtn, signin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_sign_up);

        regName = findViewById(R.id.reg_pName);

        regEmail = findViewById(R.id.reg_pemail);
        regPhoneNo = findViewById(R.id.reg_pphoneNo);
        regPassword = findViewById(R.id.reg_ppassword);
        regBtn = findViewById(R.id.reg_buttonp);
        signin = findViewById(R.id.psignin_screen);
        regUserCode=findViewById(R.id.user_code);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), professorDashboard.class));
        }

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = regEmail.getText().toString().trim();
                final String password = regPassword.getText().toString().trim();
                final String name = regName.getText().toString().trim();
                // String username=regUsername.getText().toString().trim();
                final String phone = regPhoneNo.getText().toString().trim();
                final String usercode = regUserCode.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6) {
                    regPassword.setError("password must be >=6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                if(usercode.equals("professor2020")) {
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(professorSignUp.this, "User created", Toast.LENGTH_SHORT).show();
                                regUsername = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("Users").document(regUsername);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("email", email);
                                user.put("password", password);
                                user.put("phoneNo", phone);
                                user.put("isprofessor", "1");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user profile is created for" + regUsername);
                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), professorDashboard.class));
                            } else {
                                Toast.makeText(professorSignUp.this, "Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }

                        }
                    });

                }
                else{
                    regUserCode.setError("Empty or Wrong UserCode");
                    return;
                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(professorSignUp.this, professorSignIn.class);
                startActivity(intent);
                finish();
            }
        });

    }
}