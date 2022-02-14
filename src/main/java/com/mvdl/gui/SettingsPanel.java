package com.mvdl.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mvdl.model.Preferences;

public class SettingsPanel extends JPanel {
    
    private GUI gui;
    private Preferences prefs;

    private ButtonGroup  audioGrp = new ButtonGroup();
    private JRadioButton mp3 = new JRadioButton("mp3"),
                         wav = new JRadioButton("wav"),
                         m4a = new JRadioButton("m4a"),
                         webmAudio = new JRadioButton("webm");

    private ButtonGroup  videoGrp = new ButtonGroup();
    private JRadioButton mp4 = new JRadioButton("mp4"),
                         webmVideo = new JRadioButton("webm");

    public SettingsPanel(GUI gui, Preferences prefs) {
        this.gui = gui;
        this.prefs = prefs;

        mp3.setForeground(GUI.textColor1);
        mp3.setOpaque(false);
        wav.setForeground(GUI.textColor1);
        wav.setOpaque(false);
        m4a.setForeground(GUI.textColor1);
        m4a.setOpaque(false);
        webmAudio.setForeground(GUI.textColor1);
        webmAudio.setOpaque(false);
        mp4.setForeground(GUI.textColor1);
        mp4.setOpaque(false);
        webmVideo.setForeground(GUI.textColor1);
        webmVideo.setOpaque(false);

        JPanel audioPan = new JPanel();
        audioPan.setOpaque(false);
        this.audioGrp.add(mp3);
        audioPan.add(mp3);
        this.audioGrp.add(wav);
        audioPan.add(wav);
        this.audioGrp.add(m4a);
        audioPan.add(m4a);
        this.audioGrp.add(webmAudio);
        audioPan.add(webmAudio);

        JPanel videoPan = new JPanel();
        videoPan.setOpaque(false);
        this.videoGrp .add(mp4);
        videoPan.add(mp4);
        this.videoGrp .add(webmVideo);
        videoPan.add(webmVideo);
        
        this.setBackground(GUI.darkColor1);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(audioPan);
        this.add(videoPan);
    }

}
