package com.mvdl.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressBar extends JPanel {

    private JLabel progressLbl = new JLabel("0%");

    private Mode mode = Mode.PROGRESS;
    private double progress = 0;

    public ProgressBar() {
        this.setPreferredSize(new Dimension(200, 30));
        this.add(progressLbl);
        this.progressLbl.setAlignmentX(JLabel.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(mode.getColor());
        g.fillRect(0, 0, getProgressPos(), getHeight());
    }

    public String toStr(double nb) {
        String str = nb+"";
        String[] split = str.split("\\.");
        if(split[1].charAt(0) == '0')
            return split[0];
        return str;
    }

    public void setProgress(double progress) {
        this.progressLbl.setText(toStr(progress)+"%");
        this.progress = progress;
        revalidate();
        repaint();
    }

    private int getProgressPos() {
        return (int)((progress / 100) * this.getWidth());
    }

    public void switchToProgress() {
        mode = Mode.PROGRESS;
        setProgress(0);
    }

    public void switchToConversion() {
        mode = Mode.CONVERSION;
        this.progressLbl.setText("Conversion...");
        if(progress != 100)
            progress = 100;
        repaint();
    }

    public void switchToDone() {
        mode = Mode.DONE;
        this.progressLbl.setText("Done");
        if(progress != 100)
            progress = 100;
        repaint();
    }

    public Mode getMode() {
        return mode;
    }

    public static enum Mode {
        PROGRESS(Color.GREEN), CONVERSION(Color.ORANGE), DONE(Color.GREEN);

        private Color color;

        private Mode(Color c) {
            this.color = c;
        }

        public Color getColor() {
            return color;
        }

    }

}
