package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Preferences implements Serializable {
    
    private static final long serialVersionUID = 1264551928456287364L;
    public transient static final String DEFAULT_PREF_FILE = "prefs.ini";

    private File downloadFolder = new File("music");
    private String audioFormat = "mp3",
                   videoFormat = "mp4";

    public Preferences() {}

    public Preferences(File downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public void save() throws FileNotFoundException, IOException {
        save(DEFAULT_PREF_FILE);
    }

    public void save(String path) throws FileNotFoundException, IOException {
        save(this, path);
    }

    public static void save(Preferences prefs, String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(prefs);
        oos.close();
    }

    public static Preferences load() throws FileNotFoundException, ClassNotFoundException, IOException {
        return load(DEFAULT_PREF_FILE);
    }

    public static Preferences load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        File file =  new File(path) ;

        // ouverture d'un flux sur un fichier
       ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(file)) ;
               
        // désérialization de l'objet
       return (Preferences)ois.readObject();
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
