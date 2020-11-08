package com.example.sfdnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginAs extends AppCompatActivity {
    private Button moveToAdmin, moveToStudents;
    FirebaseAuth fAuth;
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as);






        moveToAdmin = findViewById(R.id.log_admin);
       moveToAdmin.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                moveToAdminPage();
          }
        });

        moveToStudents=findViewById(R.id.log_student);
        moveToStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToStudentPage();
            }
        });
    }
    private void moveToAdminPage(){
        Intent intent=new Intent(loginAs.this,professorSignUp.class);
        startActivity(intent);

    }

    private void moveToStudentPage(){
        Intent intent=new Intent(loginAs.this,studentsignup.class);
        startActivity(intent);

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isprofessor") != null) {
                        startActivity(new Intent(getApplicationContext(), professorDashboard.class));
                        finish();
                    }
                    if (documentSnapshot.getString("isstudent") != null) {
                        startActivity(new Intent(getApplicationContext(), studentDashboard1.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), userSignIn.class));
                    finish();
                }
            });
        }
    }
}