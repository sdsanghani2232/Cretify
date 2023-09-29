package com.sd.certify.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sd.certify.R;
import com.sd.certify.data_class.Event_Details;
import com.sd.certify.database_files_viewmodels.Event_model;
import com.sd.certify.rv_adapters.User_Home_Rv_Adapter;

import java.util.List;

public class User_Home_fragment extends Fragment {

    RecyclerView rv;
    User_Home_Rv_Adapter adapter;
    Event_model model;
    public User_Home_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__home_fragment, container, false);

        rv = view.findViewById(R.id.user_home_rv);
        adapter = new User_Home_Rv_Adapter();
        model = new ViewModelProvider(this).get(Event_model.class);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        model.getEventList().observe(getViewLifecycleOwner(), new Observer<List<Event_Details>>() {
            @Override
            public void onChanged(List<Event_Details> eventDetails) {
                Log.d("list data ",eventDetails.toString());
                adapter.setEventList(eventDetails);
            }
        });
        return view;
    }
}