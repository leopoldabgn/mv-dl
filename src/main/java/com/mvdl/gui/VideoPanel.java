package com.mvdl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.mvdl.model.Command;
import com.mvdl.model.Video;

public class VideoPanel extends JPanel {

    private SearchPanel searchPan;

    private Video video;
    private Image image;
    private DescriptionPanel desc;
    private DownloadPanel downloadPan;

    public VideoPanel(SearchPanel searchPan, Video video) {
        this.searchPan = searchPan;
        this.video = video;
        this.desc = new DescriptionPanel();
        this.downloadPan = new DownloadPanel();

        int border = 10;
        setBorder(BorderFactory.createEmptyBorder(border, border, 0, border));
        setOpaque(false);
        this.setLayout(new BorderLayout());
        this.add(desc, BorderLayout.CENTER);
        this.add(downloadPan, BorderLayout.EAST);
    }

    private class DescriptionPanel extends JPanel {

        private JTextArea title;
        private JLabel duration, views, date;

        private DescriptionPanel() {
            setOpaque(false);
            this.title = new JTextArea(video.getTitle());
            title.setForeground(new Color(200, 210, 220));
            title.setEditable(false);
            title.setOpaque(false);
            title.setLineWrap(true); // Pour un retour à ligne automatique
            title.setWrapStyleWord(true); // Pour que les mots ne soient pas coupés
            Font font1 = new Font("Arial", Font.BOLD, 16);
            Font font2 = new Font("Arial", Font.PLAIN, 13);
            Color descColor = new Color(100, 100, 80);
            title.setFont(font1);
            this.duration = new JLabel(video.getDuration());
            duration.setForeground(descColor);
            duration.setFont(font2);
            this.views = new JLabel(video.getViews());
            views.setForeground(descColor);
            views.setFont(font2);
            this.date = new JLabel(video.getDate());
            date.setForeground(descColor);
            date.setFont(font2);

            JPanel westPan = new JPanel();
            westPan.setOpaque(false);
            westPan.setLayout(new GridLayout(4, 1));
            westPan.add(title);
            westPan.add(duration);
            westPan.add(views);
            westPan.add(date);

            this.setLayout(new BorderLayout()); // Probleme quand on retrecit la fenetre. A regler.
            this.add(westPan);
        }

    }

    private class DownloadPanel extends JPanel {

        private IconPanel download;

        private DownloadPanel() {
            setOpaque(false);
            this.download = new IconPanel("download_icon", 32);
            download.addMouseListener( new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        download.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Command.downloadMusic(video);
                                download.setEnabled(true);
                            }
                        }).start();
                    }
                }
            );
            
            setLayout(new GridBagLayout());
            add(download);
        }

        
    }

}