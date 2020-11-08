package com.example.sfdnew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class studentDashboard1 extends AppCompatActivity {
    CardView cardcourse,cardViewcircular,cardProfile,cardtest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard1);


        cardcourse=findViewById(R.id.courses);

        cardcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentDashboard1.this,studentDashboard2.class);
                startActivity(intent);
            }
        });

        cardProfile=findViewById(R.id.profile);

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentDashboard1.this,studentprofile.class);
                startActivity(intent);
            }
        });

        cardtest=findViewById(R.id.aboutus);

        cardtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentDashboard1.this,aboutUs.class);
                startActivity(intent);
            }
        });



        cardViewcircular = findViewById(R.id.circulars);

        cardViewcircular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://collegecirculars.unipune.ac.in/Important%20Circulars/Forms/Active%20Circulars.aspx"));
                startActivity(browserIntent);
            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),studentsignup.class));
        finish();

    }
}