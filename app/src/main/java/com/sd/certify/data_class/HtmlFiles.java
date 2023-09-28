package com.sd.certify.data_class;

public class HtmlFiles {

    String fileName;
    String fileDownloadUrl;

    public HtmlFiles() {
    }
    public HtmlFiles(String fileName, String fileDownloadUrl) {
        this.fileName = fileName;
        this.fileDownloadUrl = fileDownloadUrl;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }


}
