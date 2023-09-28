package com.sd.certify.data_class;

public class PdfFiles {

    String pdfUrl,PdfName;

    public PdfFiles() {
    }

    public PdfFiles(String pdfUrl, String pdfName) {
        PdfName = pdfName;
        this.pdfUrl = pdfUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfName() {
        return PdfName;
    }

    public void setPdfName(String pdfName) {
        PdfName = pdfName;
    }
}
