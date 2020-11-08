package com.example.sfdnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
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

public class viewAssignment extends AppCompatActivity {
    ListView assigListView;
    DatabaseReference databaseReference;
    List<uploadAssignment> myassig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        assigListView=(ListView)findViewById(R.id.aView);
        myassig =new ArrayList<>();


        viewAllFiles();

        assigListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadAssignment uploadAssignment=myassig.get(i);

                Intent intent=new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadAssignment.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void viewAllFiles() {

        databaseReference= FirebaseDatabase.getInstance().getReference("Assignment");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    uploadAssignment uploadAssignment=postSnapShot.getValue(com.example.sfdnew.uploadAssignment.class);
                    myassig.add(uploadAssignment);

                }

                String[] uploads = new String[myassig.size()];
                for(int i=0;i<uploads.length;i++){
                    uploads[i]=myassig.get(i).getName();

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
                assigListView.setAdapter(adapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}