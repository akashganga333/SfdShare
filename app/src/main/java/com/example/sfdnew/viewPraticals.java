package com.example.sfdnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewPraticals extends AppCompatActivity {
    ListView pListView;
    DatabaseReference databaseReference;
    List<uploadPratical> myprac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_praticals);

        pListView=(ListView)findViewById(R.id.pView);
        myprac =new ArrayList<>();


        viewAllFiles();

        pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadPratical uploadPratical=myprac.get(i);

                Intent intent=new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadPratical.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void viewAllFiles() {

        databaseReference= FirebaseDatabase.getInstance().getReference("Praticals");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    uploadPratical uploadPratical=postSnapShot.getValue(com.example.sfdnew.uploadPratical.class);
                    myprac.add(uploadPratical);

                }

                String[] uploads = new String[myprac.size()];
                for(int i=0;i<uploads.length;i++){
                    uploads[i]=myprac.get(i).getName();

                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){

                    @Override
                    public View getView(int position,  View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView myText=(TextView) view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLUE);
                        return view;
                    }
                };
                pListView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}