package com.example.sfdnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class studentFinalDashboard extends AppCompatActivity {
    CardView viewNotes,cardViewquestion,cardViewsyllabus,cardNotice,cardAssignment,cardPratical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_final_dashboard);

        viewNotes=findViewById(R.id.view_notes);

        viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentFinalDashboard.this,viewNotes.class);
                startActivity(intent);
            }
        });


        cardViewquestion = findViewById(R.id.view_questionpapers);

        cardViewquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/1SlF6pQLP5Ax4ZvY26EL8h90izWPDZEfT"));
                startActivity(browserIntent);
            }
        });

        cardViewsyllabus = findViewById(R.id.view_syllabus);

        cardViewsyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://collegecirculars.unipune.ac.in/sites/documents/Syllabus%202017/TE_Computer_Engg_Syllabus_2015_Course_10.072018.pdf"));
                startActivity(browserIntent);
            }
        });

        cardAssignment=findViewById(R.id.view_assignment);

        cardAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentFinalDashboard.this,viewAssignment.class);
                startActivity(intent);
            }
        });

        cardNotice=findViewById(R.id.view_notices);

        cardNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentFinalDashboard.this,viewNotices.class);
                startActivity(intent);
            }
        });

        cardPratical=findViewById(R.id.view_praticals);

        cardPratical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(studentFinalDashboard.this,viewPraticals.class);
                startActivity(intent);
            }
        });




    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),studentsignup.class));
        finish();

    }
}