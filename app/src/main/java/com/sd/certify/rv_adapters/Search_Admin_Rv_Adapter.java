package com.sd.certify.rv_adapters;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.certify.R;
import com.sd.certify.data_class.ExcelFiles;
import com.sd.certify.data_class.PdfFiles;
import com.sd.certify.helper_class.SendFiles;

import java.util.ArrayList;
import java.util.List;

public class Search_Admin_Rv_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PDF = 1;
    private static final int EXCEL = 2;

    private Context context;
    List<PdfFiles> pdfFiles = new ArrayList<>();
    List<ExcelFiles> excelFiles = new ArrayList<>();
    private String evName;

    @SuppressLint("NotifyDataSetChanged")
    public void setFileList(String evName, Context context) {
        this.context = context;
        this.evName = evName;
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setPdfList(List<PdfFiles> pdfFiles)
    {
        this.pdfFiles = pdfFiles;
        if (pdfFiles.isEmpty())
        {
            Toast.makeText(context, "No Pdf File found", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setExcelList(List<ExcelFiles> excelList)
    {
        this.excelFiles = excelList;
        if(excelFiles.isEmpty())
        {
            Toast.makeText(context, "No Excel File Found", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        if(position<pdfFiles.size())
        {
            return PDF;
        }
        else {
            return EXCEL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_view, parent, false);

        if (viewType == PDF) return new PdfViewHolder(view);
        else if (viewType == EXCEL) return new ExcelViewHolder(view);

        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PdfViewHolder) {
            PdfFiles pdfDetails = pdfFiles.get(position);
            String[] name = pdfDetails.getPdfName().split("@");
            ((PdfViewHolder) holder).pdfName.setText(name[0]);
            String url = pdfDetails.getPdfUrl();

            ((PdfViewHolder) holder).send.setOnClickListener(view -> {
                SendFiles files = new SendFiles();
                files.sendFile(context, url, evName);
            });

            ((PdfViewHolder) holder).download.setOnClickListener(view -> {
                SendFiles files = new SendFiles();
                files.downloadFile(context, evName, ".pdf", DIRECTORY_DOWNLOADS, url);
            });

        } else if (holder instanceof ExcelViewHolder) {
            int excelPosition = position - pdfFiles.size();
            ExcelFiles excelFile = excelFiles.get(excelPosition);
            ((ExcelViewHolder) holder).pdfName.setText(evName + "_Excel");
            String url = excelFile.getExcelFile();

            ((ExcelViewHolder) holder).send.setOnClickListener(view -> {
                SendFiles files = new SendFiles();
                files.sendFile(context, url, evName);
            });

            ((ExcelViewHolder) holder).download.setOnClickListener(view -> {
                SendFiles files = new SendFiles();
                files.downloadFile(context, evName, ".xlsx", DIRECTORY_DOWNLOADS, url);
            });
        }
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size() + excelFiles.size();
    }

    static class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView pdfName;
        ImageButton send, download;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdf_name);
            send = itemView.findViewById(R.id.Pdf_send);
            download = itemView.findViewById(R.id.Pdf_download);
        }
    }

    static class ExcelViewHolder extends RecyclerView.ViewHolder {
        TextView pdfName;
        ImageButton send, download;

        public ExcelViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdf_name);
            send = itemView.findViewById(R.id.Pdf_send);
            download = itemView.findViewById(R.id.Pdf_download);
        }
    }
}
