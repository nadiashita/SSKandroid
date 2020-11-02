package com.si_ware.neospectra.Network;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.si_ware.neospectra.Models.dbModule;

import java.io.File;
import java.util.UUID;

/**
 * Created by AmrWinter on 2/3/18.
 */

public class FireBaseIO {

    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FireBaseIO() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void downloadFile(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://neospectra-1517351078919.appspot.com/Modules/");
        StorageReference  islandRef = storageRef.child("Milk.txt");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "file_name");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"imageName.txt");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }

    public void uploadModuleData(final Context mContext, dbModule module) {
        byte[] moduleData = module.toString().getBytes();
        if(module != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Modules/" + module.getModuleName() + ".txt");
            ref.putBytes(moduleData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext,
                                "Module uploaded.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext,
                                "Uploading Failed."+e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot
                                .getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
        }
    }
}
