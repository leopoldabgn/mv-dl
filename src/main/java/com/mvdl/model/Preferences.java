package com.mvdl.model;

import java.io.File;
import java.io.Serializable;

public class Preferences implements Serializable {
    
    private File downloadFolder;

    public Preferences(File downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public void setDownloadFolder(File downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public File getDownloadFolder() {
        return downloadFolder;
    }

}
