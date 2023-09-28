package com.sd.certify.database_files_viewmodels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sd.certify.data_class.HtmlFiles;

import java.util.List;

public class Html_Data extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference = storage.getReference();

    // get html and txt files
    private MutableLiveData<List<HtmlFiles>> CodeFileList;

    public LiveData<List<HtmlFiles>> getCodeFile()
    {
        if(CodeFileList == null)
        {
            CodeFileList = new MutableLiveData<>();
            getFiles();
        }
        return CodeFileList;
    }

    private void getFiles() {

        db.collection("Codes").addSnapshotListener((value, error) -> {
            if(error == null)
            {
                assert value != null;
                List<HtmlFiles> code = value.toObjects(HtmlFiles.class);
                Log.d("data of list",code.toString());
                CodeFileList.postValue(code);
                Log.d("data of list",CodeFileList.toString());

            }
        });
    }


    public void UploadHtmlFile(Uri uri, Context context)
    {
        String name = "html" +System.currentTimeMillis();
        reference = storage.getReference().child("html/"+name);

        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
            HtmlFiles dataModel = new HtmlFiles(name, uri1.toString());
            db.collection("Codes").document(name)
                    .set(dataModel)
                    .addOnSuccessListener(unused -> Toast.makeText(context, "File uploaded ", Toast.LENGTH_SHORT).show());
        }));
    }

}
