package com.intive.patronage.toz.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload";

    private int amountFilesInFolder = 998;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAmountFilesInFolder() {
        return amountFilesInFolder;
    }

    public void setAmountFilesInFolder(int amountFilesInFolder) {
        this.amountFilesInFolder = amountFilesInFolder;
    }
}
