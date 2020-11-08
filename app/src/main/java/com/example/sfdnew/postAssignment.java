package com.example.sfdnew;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class postAssignment extends AppCompatActivity {
    EditText editAssignmentName;
    Button btn_aup;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_assignment);

        editAssignmentName=(EditText)findViewById(R.id.assignmentnameupload);
        btn_aup=findViewById(R.id.uploadAssignment_btn);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Assignment");

        btn_aup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAssignmentFile();
            }
        });
    }

    private void selectAssignmentFile(){
        Intent intent=new Intent();
        String[] mimeTypes={"image/*","application/*"};
        //intent.setType("image/*||application/*");
        intent.setType(mimeTypes.length==1?mimeTypes[0]:"*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Assignment File"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uploadassignment(data.getData());
        }
    }

    private void uploadassignment(Uri data) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading Assignment......");
        progressDialog.show();

        StorageReference reference=storageReference.child("Assignment/"+System.currentTimeMillis());
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri= taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url=uri.getResult();

                        uploadAssignment uploadAssignment=new uploadAssignment(editAssignmentName.getText().toString(),url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadAssignment);
                        Toast.makeText(postAssignment.this,"Assignment Uploaded",Toast.LENGTH_SHORT ).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded : "+(int)progress+"%");


            }
        });
        notification();
    }

    private void notification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"n")
                .setContentText("Assignment")
                .setSmallIcon(R.drawable.assignment_icon)
                .setAutoCancel(true)
                .setContentText("New Assignment on SFD");
        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());
    }


    public void btn_actionassig(View view) {
        startActivity(new Intent(getApplicationContext(),professorViewAssignment.class));
    }
}