package com.mvdl.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mvdl.model.Preferences;

public class SettingsPanel extends JPanel {

    private GUI gui;
    private Preferences prefs;

    private ButtonGroup audioGrp = new ButtonGroup(), videoGrp = new ButtonGroup();
    private HashMap<String, JRadioButton> audioFormats, videoFormats;

    public SettingsPanel(GUI gui, Preferences prefs) {
        this.gui = gui;
        this.prefs = prefs;

        String[] audioTab = new String[] {
            "mp3", "wav", "m4a"//, "webm"
        };
        String[] videoTab = new String[] {
            "mp4"//, "webm"
        };

        audioFormats = new HashMap<String, JRadioButton>();
        videoFormats = new HashMap<String, JRadioButton>();

        JLabel audioLbl = new JLabel("Choose Audio format :");
        audioLbl.setFont(audioLbl.getFont().deriveFont(24.0f));
        audioLbl.setForeground(GUI.textColor2);
        JPanel audioPan = new JPanel();
        audioPan.setOpaque(false);

        JLabel videoLbl = new JLabel("Choose Video format :");
        videoLbl.setForeground(GUI.textColor2);
        videoLbl.setFont(audioLbl.getFont().deriveFont(24.0f));
        JPanel videoPan = new JPanel();
        videoPan.setOpaque(false);

        setupAudioVideo(audioTab, audioFormats, audioGrp, audioPan, prefs, true);
        setupAudioVideo(videoTab, videoFormats, videoGrp, videoPan, prefs, false);

        audioFormats.get(prefs.getAudioFormat()).setSelected(true);
        videoFormats.get(prefs.getVideoFormat()).setSelected(true);

        JPanel centerPan = new JPanel();
        centerPan.setOpaque(false);
        centerPan.setLayout(new BoxLayout(centerPan, BoxLayout.Y_AXIS));
        centerPan.add(audioLbl);
        centerPan.add(audioPan);
        centerPan.add(videoLbl);
        centerPan.add(videoPan);

        IconPanel backArrow = new IconPanel("left_arrow", 64);

        backArrow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setSearchPanel();
            }
        });

        JPanel northPan = new JPanel();
        northPan.setOpaque(false);
        northPan.setLayout(new BorderLayout());
        northPan.add(backArrow, BorderLayout.WEST);

        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setBackground(GUI.darkColor1);
        this.setLayout(new BorderLayout());
        this.add(centerPan, BorderLayout.CENTER);
        this.add(northPan, BorderLayout.NORTH);
    }

    public static void setupAudioVideo(String[] tab, HashMap<String, JRadioButton> hash,
                                       ButtonGroup grp, JPanel panel, Preferences prefs, boolean music) {
        for(String str : tab) {
            JRadioButton radio = new JRadioButton(str);
            grp.add(radio);
            panel.add(radio);
            radio.addChangeListener(getAudioVideoListener(prefs, music));
            radio.setFont(radio.getFont().deriveFont(24.0f));
            hash.put(str, radio);
            radio.setForeground(GUI.textColor1);
            radio.setOpaque(false);
        }
    }

    public static ChangeListener getAudioVideoListener(Preferences prefs, boolean audio) {
        return new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent changEvent) {
                JRadioButton aButton = (JRadioButton)changEvent.getSource();
                ButtonModel aModel = aButton.getModel();
                boolean selected = aModel.isSelected();
                if(selected) {
                    if(audio)
                        prefs.setAudioFormat(aButton.getText());
                    else
                        prefs.setVideoFormat(aButton.getText());
                }
            }
            
        };
    }

}
