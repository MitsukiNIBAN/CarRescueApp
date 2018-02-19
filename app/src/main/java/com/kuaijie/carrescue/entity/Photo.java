package com.kuaijie.carrescue.entity;

/**
 * Created by MitsukiSaMa on 12-15.
 */

public class Photo {
    private int id;
    private String fileName;//名称
    private String fileAddress;//全地址
    private String filePath;//路径

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public void setFileAddress() {
        this.fileAddress = filePath + fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
