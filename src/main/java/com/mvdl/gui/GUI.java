package com.mvdl.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GUI extends JFrame {
    
    public GUI(int w, int h) {
		this.setTitle("MV downloader");
		this.setMinimumSize(new Dimension(w, h));
		
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);

		setSearchPanel();
		
		this.setVisible(true);
	}

    public void setSearchPanel() {
        this.getContentPane().removeAll();
        this.getContentPane().add(new SearchPanel());
    }

}
