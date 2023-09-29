package com.sd.certify.database_files_viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sd.certify.data_class.Event_Details;

import java.util.List;

public class Event_model extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<Event_Details>> eventList;

    public LiveData<List<Event_Details>> getEventList()
    {
        eventList = new MutableLiveData<>();
        getEvList();
        return eventList;
    }

    private void getEvList() {
        db.collection("Events_Detail").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null )
                {
                    List<Event_Details> list = value.toObjects(Event_Details.class);
                    eventList.postValue(list);
                }

            }
        });
    }

}
