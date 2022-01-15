package com.mvdl.launcher;

import java.io.File;

import com.mvdl.gui.GUI;
import com.mvdl.model.Preferences;

public class Launcher {

    public static void main(String[] args) {
        // On recupere le fichier de prefs si il existe.
		// Sinon, on en cree un nouveau
		Preferences prefs = new Preferences(new File("music"));
        new GUI(prefs, 800, 600);
    }

}