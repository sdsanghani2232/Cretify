package com.sd.certify.admin.fragments;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sd.certify.R;
import com.sd.certify.database_files_viewmodels.Html_Data;
import com.sd.certify.rv_adapters.Html_Admin_Rv_Adapter;


public class Admin_Home_fragment extends Fragment {

    private final static int HTML_REQUEST_CODE = 100;
    private FloatingActionButton addCode;
    private RecyclerView rv;
    private Html_Data model;
    public Admin_Home_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View infateview = inflater.inflate(R.layout.fragment_admin__home_fragment, container, false);

        addCode = infateview.findViewById(R.id.add_files);
        rv = infateview.findViewById(R.id.html_review);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        model = new ViewModelProvider(this).get(Html_Data.class);

        Html_Admin_Rv_Adapter adapter = new Html_Admin_Rv_Adapter();
        rv.setAdapter(adapter);

        model.getCodeFile().observe(getViewLifecycleOwner(), adapter::setFilesList);

        addCode.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/html");
            startActivityForResult(intent, HTML_REQUEST_CODE);
        });

        return  infateview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == HTML_REQUEST_CODE && resultCode ==RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            Log.d("data add ",uri.toString());
            model.UploadHtmlFile(uri,requireContext());
        }
    }
}