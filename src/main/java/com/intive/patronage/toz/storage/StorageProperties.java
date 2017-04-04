package com.intive.patronage.toz.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
public class StorageProperties {


    private String storagePathRoot = "storage";

    private int maxAmountFilesInDir = 998;


    public String getStoragePathRoot() {
        return storagePathRoot;
    }

    public void setStoragePathRoot(String storagePathRoot) {
        this.storagePathRoot = storagePathRoot;
    }

    public int getMaxAmountFilesInDir() {
        return maxAmountFilesInDir;
    }

    public void setMaxAmountFilesInDir(int maxAmountFilesInDir) {
        this.maxAmountFilesInDir = maxAmountFilesInDir;
    }
}
