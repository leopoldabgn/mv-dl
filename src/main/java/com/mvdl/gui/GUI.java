package com.mvdl.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mvdl.model.Command;
import com.mvdl.model.Preferences;

public class GUI extends JFrame {
    
	private Preferences prefs;

    public GUI(Preferences prefs, int w, int h) {
		this.prefs = prefs;
		this.setTitle("MV downloader");
		this.setMinimumSize(new Dimension(w, h));
		
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		// La fermeture de la fenetre est gere dans le windowListener
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);

		setSearchPanel();
		
		// TODO: ONCLOSE WINDOW, on enregistre le fichier de prefs...
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
				if(Command.downloadsInProgress > 0) {
					GUI.showErrorWindow(GUI.this, "You cannot close the window"+
						"because downloads are still in progress.");
					return;
				}
                e.getWindow().dispose();
				System.exit(0);
            }
        });

		this.setVisible(true);
	}

    public void setSearchPanel() {
        this.getContentPane().removeAll();
        this.getContentPane().add(new SearchPanel(prefs));
    }

	public static void showErrorWindow(JFrame frame, String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
