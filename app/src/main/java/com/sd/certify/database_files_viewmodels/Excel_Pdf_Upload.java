package com.sd.certify.database_files_viewmodels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Excel_Pdf_Upload {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference;

    public void uploadPdf(byte[] pdfBytes, String pdfname, String eventName, String userEmil, OnPdfUploadListener listener) {
        reference = storage.getReference().child("events/" + eventName + "/" + pdfname);
        reference.putBytes(pdfBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> pdfUrl = new HashMap<>();
                        pdfUrl.put("Email", userEmil);
                        pdfUrl.put("url", uri.toString());

                        DocumentReference eventDocRef = db.collection("Events").document(eventName);
                        eventDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        updateCertificate(eventName, pdfUrl, listener);
                                    } else {
                                        createNewCertificate(eventName, pdfUrl, listener);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
            }
        });
    }

    private void createNewCertificate(String eventName, Map<String, Object> pdfUrl, OnPdfUploadListener listener) {

        db.collection("Events").document(eventName)
                .set(pdfUrl)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onUploadSuccess();
                    }
                });
    }

    private void updateCertificate(String eventName, Map<String, Object> pdfUrl, OnPdfUploadListener listener) {
        DocumentReference eventDocRef = db.collection("Events").document(eventName);

        eventDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<Map<String, Object>> certificates = (List<Map<String, Object>>) documentSnapshot.get("certificate");

                if (certificates == null) {
                    certificates = new ArrayList<>();
                }

                certificates.add(pdfUrl);

                // Update the "certificate" array
                eventDocRef.update("certificate", certificates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                listener.onUploadSuccess();
                            }
                        });
            }
        });
    }


    public void uploadExcel(byte[] excel, String newExcel, String eventName, Context context)
    {
        reference = storage.getReference().child("events/"+eventName + "/"+newExcel);
        reference.putBytes(excel).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("Events_Excels").document(eventName).
                                set(new HashMap<String,Object>(){
                                    {
                                        put("ExcelFile",uri.toString());
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "All file Uploaded", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("code file","not add");
            }
        });
    }

    public interface OnPdfUploadListener {
        void onUploadSuccess();
    }

}
