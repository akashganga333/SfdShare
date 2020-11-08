package com.example.sfdnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class professorDashboard extends AppCompatActivity {
    CardView notice,assignment,notes,pratical,question,syllabus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_dashboard);

        notice=findViewById(R.id.post_pnotices);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(professorDashboard.this,postNotice.class);
                startActivity(intent);
            }
        });

        syllabus=findViewById(R.id.syllabus);


        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://collegecirculars.unipune.ac.in/sites/documents/Syllabus%202017/TE_Computer_Engg_Syllabus_2015_Course_10.072018.pdf"));
                startActivity(browserIntent);
            }
        });

        question=findViewById(R.id.question_papers);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/1et2oqPCg2lUDpNsoW_yEfb8xBL2NCSBW"));
                startActivity(browserIntent);
            }
        });

        assignment=findViewById(R.id.post_passignment);
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(professorDashboard.this,postAssignment.class);
                startActivity(intent);
            }
        });

        notes=findViewById(R.id.postnotes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(professorDashboard.this,postNotes.class);
                startActivity(intent);
            }
        });

        pratical=findViewById(R.id.post_praticals);
        pratical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(professorDashboard.this,postPraticals.class);
                startActivity(intent);
            }
        });

    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),professorSignUp.class));
        finish();

    }
}