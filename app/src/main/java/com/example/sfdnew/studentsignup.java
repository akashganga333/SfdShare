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

public class studentsignup extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText regName, regEmail, regPhoneNo, regPassword;
    String regUsername;
    Button regBtn,signin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsignup);

        regName=findViewById(R.id.reg_studentName);
        // regUsername=findViewById(R.id.reg_username);
        regEmail=findViewById(R.id.reg_email);
        regPhoneNo=findViewById(R.id.reg_phoneNo);
        regPassword=findViewById(R.id.reg_password);
        regBtn=findViewById(R.id.reg_button);
        signin=findViewById(R.id.studentsignin_screen);

        fAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),studentDashboard1.class));
        }

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=regEmail.getText().toString().trim();
                final String password=regPassword.getText().toString().trim();
                final String name=regName.getText().toString().trim();
                // String username=regUsername.getText().toString().trim();
                final String phone=regPhoneNo.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    regEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    regPassword.setError("Password is Required");
                    return;
                }

                if(password.length()<6){
                    regPassword.setError("password must be >=6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(studentsignup.this,"User created",Toast.LENGTH_SHORT).show();
                            regUsername=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("Users").document(regUsername);
                            Map<String,Object> user=new HashMap<>();
                            user.put("name",name);
                            user.put("email",email);
                            user.put("password",password);
                            user.put("phoneNo",phone);
                            user.put("isstudent","1");
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for"+regUsername);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),studentDashboard1.class));
                        }
                        else{
                            Toast.makeText(studentsignup.this,"Error!!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentsignup.this,studentsignin.class);
                startActivity(intent);
                finish();
            }
        });

    }
}