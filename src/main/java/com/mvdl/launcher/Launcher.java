package com.mvdl.launcher;

import java.io.File;

import com.mvdl.gui.GUI;
import com.mvdl.model.Command;
import com.mvdl.model.Preferences;

public class Launcher {

    public static void main(String[] args) {
        Command.readJSON("res/result3.json");
        
        boolean ok = true;
        if(!Command.checkPrgm("curl")) {
            System.out.println("- You need to install curl   (ubuntu : sudo apt install curl)");
            ok = false;
        }
        if(!Command.checkPrgm("yt-dlp")) {
            System.out.println("- You need to install yt-dlp (ubuntu : sudo apt install yt-dlp)");
            ok = false;
        }
        if(!ok)
            return;
        // On recupere le fichier de prefs si il existe.
		// Sinon, on en cree un nouveau
		Preferences prefs = new Preferences(new File("music"));
       // new GUI(prefs, 800, 600);
    }

}