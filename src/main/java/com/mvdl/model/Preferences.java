package com.mvdl.model;

import java.io.File;
import java.io.Serializable;

public class Preferences implements Serializable {
    
    private File downloadFolder;
    private String audioFormat = "mp3",
                   videoFormat = "mp4";

    public Preferences(File downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public void setDownloadFolder(File downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public File getDownloadFolder() {
        return downloadFolder;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

}
