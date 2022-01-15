package com.mvdl.gui;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import com.mvdl.model.Preferences;

public class GUI extends JFrame {
    
	private Preferences prefs;

    public GUI(int w, int h) {
		this.setTitle("MV downloader");
		this.setMinimumSize(new Dimension(w, h));
		
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);

		// On recupere le fichier de prefs si il existe.
		// Sinon, on en cree un nouveau
		this.prefs = new Preferences(new File("music"));

		setSearchPanel();
		
		// TODO: ONCLOSE WINDOW, on enregistre le fichier de prefs...

		this.setVisible(true);
	}

    public void setSearchPanel() {
        this.getContentPane().removeAll();
        this.getContentPane().add(new SearchPanel(prefs));
    }

}
