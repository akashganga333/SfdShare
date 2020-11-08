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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class professorSignIn extends AppCompatActivity {
    EditText sEmail, sPassword;
    Button loginBtn, signUp;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_sign_in);

        sEmail = findViewById(R.id.professor_email);
        sPassword = findViewById(R.id.professor_password);
        progressBar = findViewById(R.id.progressBar5);
        fAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.btnLogin2);
        signUp = findViewById(R.id.psignup_screen);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String email = sEmail.getText().toString().trim();
                final String password = sPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    sEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    sPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6) {
                    sPassword.setError("password must be >=6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                Log.d("TAG","onClick" +sEmail.getText().toString());
                fAuth.signInWithEmailAndPassword(sEmail.getText().toString(),sPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        checkUserAccessLevel(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(professorSignIn.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                });




            }
        });









        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(professorSignIn.this, professorSignUp.class);
                startActivity(intent);
                finish();
            }
        });



    }
    private void checkUserAccessLevel(String uid){
        DocumentReference df=fStore.collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.getString("isprofessor")!=null){
                    Toast.makeText(professorSignIn.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),professorDashboard.class));
                    finish();
                }
                if(documentSnapshot.getString("isstudent")!=null){
                    Toast.makeText(professorSignIn.this,"Wrong Email or Password entered",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;

                }

            }
        });

    }


}