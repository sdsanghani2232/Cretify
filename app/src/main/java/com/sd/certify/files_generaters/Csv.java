package com.sd.certify.files_generaters;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;

import com.sd.certify.data_class.FilesDetails;
import com.sd.certify.data_class.UserDetails;
import com.sd.certify.database_files_viewmodels.Excel_Pdf_Upload;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    Context context;
    WebView webView;
    View view;
    Workbook workbook;
    List<String> rowdataOfCav;
    int currentIndex = 0,row = 1,height,width;
    List<List<String>> dataOfCsv;
    Sheet sheet;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Uri uri;
    String userName,userEmil,changeCode,newExcel,certificateName,oldCode;
    FilesDetails filesDetails;
    UserDetails userDetails = new UserDetails();

    public Csv(Context context, FilesDetails filesDetails) {
        this.context = context;
        this.filesDetails = filesDetails;
    }

    public void csvToPdf(Uri uri, WebView webView, View view) {
        this.view = view;
        this.webView = webView;
        this.uri = uri;
        oldCode = filesDetails.getCode();

        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            dataOfCsv = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\,");
                rowdataOfCav = new ArrayList<>();

                for (String part : parts)
                    rowdataOfCav.add(part.trim());

                dataOfCsv.add(rowdataOfCav);
            }
            CreateNewExcel();
            timeDelay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CreateNewExcel() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("sheet1");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("email");
        headerRow.createCell(2).setCellValue("certificate Ids");
    }

    private void timeDelay() {
        if(currentIndex<dataOfCsv.size())
        {
            List<String> row = dataOfCsv.get(currentIndex);
            userName= row.get(0);
            userEmil = row.get(1);
            userDetails.setName(userName);
            userDetails.setEmail(userEmil);
            changeCode = oldCode.replace("Joe Nathan",userName);
            setWebView();
        }else
        {
            currentIndex = 0;
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Excel_Pdf_Upload().uploadExcel(outputStream.toByteArray(), newExcel, filesDetails.getEventName(), context);
            webView.loadData(oldCode,"text/html","UTF-8");
        }
    }

    private void setWebView() {

        webView.loadData(changeCode,"text/html","UTF-8");
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                width = webView.getMeasuredWidth();
                height = webView.getMeasuredHeight();
                PdfCreate();
            }
        }, 400);
    }

    private void PdfCreate() {
        PdfDocument pd;
        PdfDocument.Page page;
        pd = new PdfDocument();
        PdfDocument.PageInfo pg = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        page = pd.startPage(pg);
        view.draw(page.getCanvas());
        pd.finishPage(page);

        certificateName = userName + " : " + System.currentTimeMillis() + ".pdf";
        filesDetails.setNewCerti(certificateName);

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pd.writeTo(outputStream);
            new Excel_Pdf_Upload().uploadPdf(outputStream.toByteArray(), certificateName, filesDetails.getEventName(), userEmil,new Excel_Pdf_Upload.OnPdfUploadListener() {
                @Override
                public void onUploadSuccess() {
                    addInExcel();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void addInExcel() {


        Row row1 = sheet.createRow(row++);
        row1.createCell(0).setCellValue(userName);
        row1.createCell(1).setCellValue(userEmil);
        row1.createCell(2).setCellValue(userName + " : " + System.currentTimeMillis());
        completeExcel();
    }

    private void completeExcel() {

        newExcel = filesDetails.getEventName() + ".xlsx";
        filesDetails.setNewExcel(newExcel);

        try {
            outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentIndex++;
                timeDelay();
            }
        },400);
    }

}
