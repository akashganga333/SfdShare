package com.example.sfdnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class professorViewAssignment extends AppCompatActivity {

    ListView myPdfListView;
    FirebaseStorage mStorage;
    DatabaseReference databaseReference;
    ValueEventListener mDbListener;
    List<uploadAssignment> uploadAssignment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_view_notice);

        myPdfListView = (ListView)findViewById(R.id.myListView);
        uploadAssignment = new ArrayList<>();

        viewAllPdf();

        myPdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadAssignment uploadAssign = uploadAssignment.get(i);

                Intent intent = new Intent();
                intent.setData(Uri.parse(uploadAssign.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void viewAllPdf() {
        mStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Assignment");

        mDbListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadAssignment.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){

                    uploadAssignment uploadAssignments = postSnapshot.getValue(uploadAssignment.class);
                    uploadAssignments.setKey(postSnapshot.getKey());
                    uploadAssignment.add(uploadAssignments);
                }
                String[] uploads = new String[uploadAssignment.size()];
                for(int i =0; i<uploads.length;i++){
                    uploads[i] = uploadAssignment.get(i).getName();
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){

                    @Override
                    public View getView(int position, View convertView,  ViewGroup parent) {
                        View view=super.getView(position, convertView, parent);
                        TextView myText = (TextView) view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLACK);
                        return view;

                    }

                };
                myPdfListView.setAdapter(adapter);

                myPdfListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final uploadAssignment selectedItem = uploadAssignment.get(i);
                        new AlertDialog.Builder(professorViewAssignment.this)
                                .setIcon(android.R.drawable.ic_delete)
                                .setTitle("Are You Sure ?")
                                .setMessage("Do you want to delete this File")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final String selectedKey = selectedItem.getKey();
                                        StorageReference fileRef = mStorage.getReferenceFromUrl(selectedItem.getUrl());
                                        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                databaseReference.child(selectedKey).removeValue();
                                                Toast.makeText(professorViewAssignment.this, "Assignment Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("NO",null)
                                .show();
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}