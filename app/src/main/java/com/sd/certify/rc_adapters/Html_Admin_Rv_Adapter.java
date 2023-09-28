package com.sd.certify.rc_adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.certify.R;
import com.sd.certify.admin.activitys.Generate_Certificate;
import com.sd.certify.data_class.HtmlFiles;
import com.sd.certify.helper_class.Html_View;

import java.util.List;

public class Html_Admin_Rv_Adapter extends RecyclerView.Adapter<Html_Admin_Rv_Adapter.CodeFiles> {

    Context context;
    List<HtmlFiles> htmlFiles;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilesList(List<HtmlFiles> filesList) {
        this.htmlFiles = filesList;
        Log.d("data of ",htmlFiles.toString());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CodeFiles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Html_Admin_Rv_Adapter.CodeFiles(LayoutInflater.from(parent.getContext()).inflate(R.layout.html_files_layout,parent,false));

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull CodeFiles holder, int position) {

        holder.webView.setWebViewClient(new WebViewClient());
        holder.webSettings.setLoadWithOverviewMode(true);
        holder.webSettings.setJavaScriptEnabled(true);
        holder.webSettings.setUseWideViewPort(true);
        holder.url = htmlFiles.get(position).getFileDownloadUrl();
        new Html_View(code -> {
            if(code != null)
            {
                holder.code = code;
                holder.webView.loadData(code,"text/html", "UTF-8");
            }
        }).execute(holder.url);

        holder.use.setOnClickListener(view -> context.startActivity(new Intent(context, Generate_Certificate.class)
                .putExtra("code",holder.code)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

    }

    @Override
    public int getItemCount() {
        return  htmlFiles != null ? htmlFiles.size() : 0;
    }

    static class CodeFiles extends RecyclerView.ViewHolder {
        WebView webView;
        String url,code;
        Button use;
        WebSettings webSettings;
        public CodeFiles(@NonNull View itemView) {
            super(itemView);

            webView = itemView.findViewById(R.id.html_code_view);
            webSettings = webView.getSettings();
            use = itemView.findViewById(R.id.Use_file);
        }
    }
}
