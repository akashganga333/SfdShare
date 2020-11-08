package com.example.sfdnew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class studentprofile extends AppCompatActivity {
    TextView fullname,email,phoneno;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentprofile);
        phoneno=findViewById(R.id.phone);
        fullname=findViewById(R.id.name);
        email=findViewById(R.id.email);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userId=fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference=fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phoneno.setText(documentSnapshot.getString("phoneNo"));
                fullname.setText(documentSnapshot.getString("name"));
                email.setText(documentSnapshot.getString("email"));

            }
        });



    }
}