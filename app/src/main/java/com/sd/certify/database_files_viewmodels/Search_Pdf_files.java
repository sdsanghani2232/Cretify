package com.sd.certify.database_files_viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sd.certify.data_class.PdfFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search_Pdf_files extends ViewModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<String> eventName = new MutableLiveData<>();
    private MutableLiveData<List<PdfFiles>> pdfList;

    public void getEventName(String EvName)
    {
        eventName.setValue(EvName);
    }
    public LiveData<List<PdfFiles>> getPdfFiles()
    {

        pdfList = new MutableLiveData<>();
        getFiles();
        return pdfList;
    }

    private void getFiles() {
        if(eventName.getValue() != null)
        {
            DocumentReference eventDocRef = db.collection("Events").document(eventName.getValue());
            eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
                List<PdfFiles> certificateList = new ArrayList<>();

                List<HashMap<String, String>> certificatesData = (List<HashMap<String, String>>) documentSnapshot.get("certificate");

                if (certificatesData != null) {
                    for (HashMap<String, String> certificateData : certificatesData) {
                        String email = certificateData.get("Email");
                        String url = certificateData.get("url");
                        certificateList.add(new PdfFiles(url, email));
                    }
                }
                pdfList.setValue(certificateList);
            });
        }
    }
}
