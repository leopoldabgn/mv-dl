package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Command;
import model.Preferences;

public class GUI extends JFrame {
    
    public static Color darkColor1 = new Color(70, 70, 70),
                        darkColor2 = new Color(37, 37, 38);
	public static Color textColor1 = new Color(200, 210, 220),
						textColor2 = new Color(10, 15, 15);

	private Preferences prefs;
	private SearchPanel searchPanel;

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
		
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
				if(Command.downloadsInProgress > 0) {
					GUI.showErrorWindow(GUI.this, "You cannot close the window"+
						" because downloads are still in progress.");
					return;
				}
				e.getWindow().dispose();
				try {
					prefs.save();
				} catch (IOException e1) {
					System.out.println("Error: can't save preferences file");
				}
				System.exit(0);
            }
        });

		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					searchPanel.setSearchButtonEnabled(false);
					searchPanel.startSearch();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					searchPanel.setSearchButtonEnabled(true);
				}
			}

		});

		this.requestFocus();
		this.setVisible(true);
	}

    public void setSearchPanel() {
        this.getContentPane().removeAll();
		if(searchPanel == null)
			searchPanel = new SearchPanel(this, prefs);
        this.getContentPane().add(searchPanel);
		revalidate();
		repaint();
    }

	public void setSettingsPanel() {
		this.getContentPane().removeAll();
		this.getContentPane().add(new SettingsPanel(this, prefs));
		revalidate();
		repaint();
	}

	public static void showErrorWindow(JFrame frame, String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
