package com.sd.certify.database_files_viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sd.certify.data_class.ExcelFiles;
import java.util.ArrayList;
import java.util.List;

public class Search_Excel_files extends ViewModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<String> eventName = new MutableLiveData<>();
    private MutableLiveData<List<ExcelFiles>> excelList;

    public void getEventName(String EvName)
    {
        eventName.setValue(EvName);
    }
    public LiveData<List<ExcelFiles>> getPdfFiles()
    {

        excelList = new MutableLiveData<>();
        getFiles();
        return excelList;
    }

    private void getFiles() {
        if(eventName.getValue() != null)
        {
            db.collection("Events_Excels").document(eventName.getValue()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        ExcelFiles files = documentSnapshot.toObject(ExcelFiles.class);
                        List<ExcelFiles> list = new ArrayList<>();
                        list.add(files);
                        excelList.setValue(list);
                    }
                }
            });
        }
    }
}
