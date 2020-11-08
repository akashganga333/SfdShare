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

public class viewNotices extends AppCompatActivity {

    ListView noticesListView;
    DatabaseReference databaseReference;
    List<uploadNotices> mynotices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notices);

        noticesListView=(ListView)findViewById(R.id.noticeView);
        mynotices =new ArrayList<>();


        viewAllFiles();

        noticesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadNotices uploadNotices=mynotices.get(i);

                Intent intent=new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadNotices.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void viewAllFiles() {

        databaseReference= FirebaseDatabase.getInstance().getReference("notices");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    uploadNotices uploadNotices=postSnapShot.getValue(com.example.sfdnew.uploadNotices.class);
                    mynotices.add(uploadNotices);

                }

                String[] uploads = new String[mynotices.size()];
                for(int i=0;i<uploads.length;i++){
                    uploads[i]=mynotices.get(i).getName();

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
                noticesListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}