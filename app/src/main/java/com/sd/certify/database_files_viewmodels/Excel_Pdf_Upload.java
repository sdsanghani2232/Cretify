package com.sd.certify.database_files_viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Excel_Pdf_Upload {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference;

    public void uploadPdf(byte[] pdfBytes, String pdfName, String eventName, String userEmil, final OnPdfUploadListener listener) {
        reference = storage.getReference().child("events/" + eventName + "/" + pdfName);
        reference.putBytes(pdfBytes).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
            Map<String, Object> pdfUrl = new HashMap<>();
            pdfUrl.put("Email", userEmil);
            pdfUrl.put("url", uri.toString());

            CollectionReference eventsCollectionRef = db.collection("Events");
            eventsCollectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.isEmpty()) {
                    db.collection("Events").document(eventName)
                            .set(new HashMap<>())
                            .addOnSuccessListener(unused -> updateCertificate(eventName, pdfUrl, listener));
                } else {
                    updateCertificate(eventName, pdfUrl, listener);
                }
            });
        })).addOnFailureListener(e -> {
        });
    }

    private void updateCertificate(String eventName, Map<String, Object> pdfUrl, OnPdfUploadListener listener) {
        DocumentReference eventDocRef = db.collection("Events").document(eventName);

        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
            List<Map<String, Object>> certificates = (List<Map<String, Object>>) documentSnapshot.get("certificate");

            if (certificates == null) {
                certificates = new ArrayList<>();
            }
            certificates.add(pdfUrl);
            eventDocRef.update("certificate", certificates)
                    .addOnSuccessListener(unused -> listener.onUploadSuccess());
        });
    }


    public void uploadExcel(byte[] excel, String newExcel, String eventName, Context context)
    {
        reference = storage.getReference().child("events/"+eventName + "/"+newExcel);
        reference.putBytes(excel).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> db.collection("Events_Excels").document(eventName).
                set(new HashMap<String,Object>(){
                    {
                        put("ExcelFile",uri.toString());
                    }
                })
                .addOnSuccessListener(unused -> Toast.makeText(context, "All file Uploaded", Toast.LENGTH_SHORT).show()))).addOnFailureListener(e -> Log.d("code file","not add"));
    }

    public interface OnPdfUploadListener {
        void onUploadSuccess();
    }

}
