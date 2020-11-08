package com.example.sfdnew;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

public class postPraticals extends AppCompatActivity {
    EditText editpName;
    Button btn_pup;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_praticals);

        editpName=(EditText)findViewById(R.id.pnameupload);
        btn_pup=findViewById(R.id.uploadp_btn);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Praticals");

        btn_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectpraticalFile();
            }
        });
    }

    private void selectpraticalFile(){
        Intent intent=new Intent();
        String[] mimeTypes={"image/*","application/*"};
        //intent.setType("image/*||application/*");
        intent.setType(mimeTypes.length==1?mimeTypes[0]:"*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uploadprac(data.getData());
        }
    }

    private void uploadprac(Uri data) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading Pratical......");
        progressDialog.show();

        StorageReference reference=storageReference.child("Praticals/"+System.currentTimeMillis());
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri= taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url=uri.getResult();

                        uploadPratical uploadPratical=new uploadPratical(editpName.getText().toString(),url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPratical);
                        Toast.makeText(postPraticals.this,"Pratical Uploaded",Toast.LENGTH_SHORT ).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded : "+(int)progress+"%");


            }
        });
    }


    public void btn_actionprac(View view) {
        startActivity(new Intent(getApplicationContext(),professorViewPraticals.class));
    }
}