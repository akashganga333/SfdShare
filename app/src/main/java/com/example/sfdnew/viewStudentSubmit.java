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

public class viewStudentSubmit extends AppCompatActivity {

    ListView myPdfListView;
    FirebaseStorage mStorage;
    DatabaseReference databaseReference;
    ValueEventListener mDbListener;
    List<submitAssignment> submitAssignments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_submit);

        myPdfListView = (ListView)findViewById(R.id.sView);
        submitAssignments = new ArrayList<>();

        viewAllPdf();

        myPdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                submitAssignment submitAssignment= submitAssignments.get(i);

                Intent intent = new Intent();
                intent.setData(Uri.parse(submitAssignment.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void viewAllPdf() {
        mStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Submissions");

        mDbListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                submitAssignments.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){

                    submitAssignment submitAssignment = postSnapshot.getValue(submitAssignment.class);
                    submitAssignment.setKey(postSnapshot.getKey());
                    submitAssignments.add(submitAssignment);
                }
                String[] uploads = new String[submitAssignments.size()];
                for(int i =0; i<uploads.length;i++){
                    uploads[i] = submitAssignments.get(i).getName();
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
                        final submitAssignment selectedItem =submitAssignments.get(i);
                        new AlertDialog.Builder(viewStudentSubmit.this)
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
                                                Toast.makeText(viewStudentSubmit.this, "Assignment Deleted", Toast.LENGTH_SHORT).show();
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