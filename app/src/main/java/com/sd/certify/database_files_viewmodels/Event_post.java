package com.sd.certify.database_files_viewmodels;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;

public class Event_post {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference;
    public void postEvent(String evName, String evDate, Uri selectedImageUri, Context context, OnEventPostListener listener) {

        reference = storage.getReference().child("Events_Details/"+evName);
        reference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String,Object> event = new HashMap<>();
                        event.put("event_name",evName);
                        event.put("img",uri.toString());
                        event.put("date",evDate);
                        db.collection("Events_Detail").document(evName).set(event)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(context, "Event Posted", Toast.LENGTH_SHORT).show();
                                            listener.onEventPostSuccess();
                                        }
                                        else {
                                            listener.onError();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    public interface OnEventPostListener {
        void onEventPostSuccess();
        void onError();
    }
}

