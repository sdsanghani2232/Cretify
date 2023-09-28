package com.sd.certify.admin.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sd.certify.R;
import com.sd.certify.data_class.ExcelFiles;
import com.sd.certify.data_class.PdfFiles;
import com.sd.certify.database_files_viewmodels.Search_Excel_files;
import com.sd.certify.database_files_viewmodels.Search_Pdf_files;
import com.sd.certify.rv_adapters.Search_Admin_Rv_Adapter;

import java.util.Collections;
import java.util.List;

public class Admin_Search_fragment extends Fragment {

    private EditText event;
    private Search_Pdf_files pdfModel;
    private Search_Excel_files excelModel;
    private Search_Admin_Rv_Adapter adapter;


    public Admin_Search_fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_admin__search_fragment, container, false);

        event = inflate.findViewById(R.id.search_file_text_box);
        Button searchPdf = inflate.findViewById(R.id.search_file_pdf);
        Button searchExcel = inflate.findViewById(R.id.search_file_excel);
        RecyclerView rv = inflate.findViewById(R.id.search_rv);
        adapter = new Search_Admin_Rv_Adapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        pdfModel = new ViewModelProvider(this).get(Search_Pdf_files.class);
        excelModel = new ViewModelProvider(this).get(Search_Excel_files.class);

        searchPdf.setOnClickListener(view -> searchPdfFiles());

        searchExcel.setOnClickListener(view -> searchExcelFiles());

        return inflate;
    }


    private void searchPdfFiles() {

        String eventName = event.getText().toString().trim();
        if (eventName.isEmpty()) {
            Toast.makeText(requireContext(), "Enter Event name", Toast.LENGTH_SHORT).show();
            return;
        }

        pdfModel.getEventName(eventName.trim());
        pdfModel.getPdfFiles().observe(getViewLifecycleOwner(), pdfFiles -> {
            adapter.setFileList(eventName,requireContext());
            adapter.setPdfList(pdfFiles);
        });
    }

    private void searchExcelFiles() {
        String eventName = event.getText().toString().trim();
        if (eventName.isEmpty()) {
            Toast.makeText(requireContext(), "Enter Event name", Toast.LENGTH_SHORT).show();
            return;
        }

        excelModel.getEventName(eventName.trim());
        excelModel.getPdfFiles().observe(getViewLifecycleOwner(), excelFiles -> {
            adapter.setFileList(eventName,requireContext());
            adapter.setExcelList(excelFiles);
        });
    }
}