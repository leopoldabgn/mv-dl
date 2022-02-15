package com.mvdl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.mvdl.model.Command;
import com.mvdl.model.Preferences;
import com.mvdl.model.Video;
import com.mvdl.model.Video.VideoInfos;

public class VideoPanel extends JPanel {

    private Command command;

    private Video video;
    private BufferedImage thumbnail;
    private DescriptionPanel desc;
    private DownloadPanel downloadPan;

    public VideoPanel(Preferences prefs, Video video) {
        this.command = new Command(prefs);
        this.video = video;
        try {
            URL url = new URL(video.getThumbnailURL());
            this.thumbnail = ImageIO.read(url);
        } catch(IOException e) {this.thumbnail = null;}
        this.desc = new DescriptionPanel();
        this.downloadPan = new DownloadPanel();

        int border = 10;
        setBorder(BorderFactory.createEmptyBorder(border, border, 0, border));
        setOpaque(false);
        this.setLayout(new BorderLayout());
        if(thumbnail != null) {
            //////// POUR FAIRE DES TESTS
            // JLabel lblImg = new JLabel(new ImageIcon(thumbnail));
            // lblImg.setPreferredSize(new Dimension(200, 100));
            ////////
            IconPanel icon = new IconPanel(thumbnail, 270, 152);
            this.add(icon, BorderLayout.WEST);
        }
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

        private JButton dlVideo, dlMusic;

        private DownloadPanel() {
            setOpaque(false);
            final ProgressBar progressBar = new ProgressBar();
            this.dlMusic = new JButton("Download music"); // new IconPanel("download_icon", 32);
            dlMusic.addMouseListener( new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        dlMusic.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Command.downloadsInProgress++;
                                command.downloadMusic(video, progressBar);
                                dlMusic.setEnabled(true);
                                Command.downloadsInProgress--;
                            }
                        }).start();
                    }
                }
            );
        
            dlVideo = new JButton("Download Video");
            dlVideo.addActionListener(e -> {
                new QualityFrame();
            });

            JPanel pan = new JPanel();
            pan.setOpaque(false);
            pan.add(progressBar);
            setLayout(new GridLayout(3, 1));    
            add(dlVideo);
            add(dlMusic);
            add(pan);
        }

        public class QualityFrame extends JFrame {
            
            private List<QualityPanel> qualityPanels;
            private QualityPanel selectedQuality;
            private JLabel errorMsg;

            public QualityFrame() {
                setSize(400, 400);
                qualityPanels = new ArrayList<QualityPanel>();
                JPanel northPan = new JPanel();
                JPanel tmp = new JPanel();
                tmp.setLayout(new BoxLayout(tmp, BoxLayout.PAGE_AXIS));
                tmp.add(new JLabel("Choose a video quality :"));
                northPan.add(tmp);
                JPanel centerPan = new JPanel();
                List<VideoInfos> qualities = Command.getVideoQualities(video);
                for(VideoInfos v : qualities) {
                    QualityPanel pan = new QualityPanel(v);
                    pan.addMouseListener(getListener(pan));
                    qualityPanels.add(pan);
                    centerPan.add(pan);
                }
                JPanel bottomPan = new JPanel();
                JButton cancel = new JButton("Cancel");
                JButton download = new JButton("Download");
                bottomPan.add(cancel);
                bottomPan.add(download);

                cancel.addActionListener(e -> {
                    dispose();
                });

                download.addActionListener(e -> {
                    if(selectedQuality == null) {
                        if(errorMsg == null) {
                            errorMsg = new JLabel("You need to select a quality !");
                            errorMsg.setForeground(Color.RED);
                            ((JPanel)northPan.getComponent(0)).add(errorMsg, 0);
                            revalidate();
                            repaint();
                        }
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Command.downloadsInProgress++;
                            dlVideo.setEnabled(false);
                            command.downloadVideo(video, selectedQuality.getInfos());
                            dlVideo.setEnabled(true);
                            Command.downloadsInProgress--;
                        }
                    }).start();
                    dispose();
                });

                setLayout(new BorderLayout());
                getContentPane().add(northPan, BorderLayout.NORTH);
                getContentPane().add(centerPan, BorderLayout.CENTER);
                getContentPane().add(bottomPan, BorderLayout.SOUTH);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setVisible(true);
            }

            public MouseListener getListener(QualityPanel quality) {
                return new MouseAdapter() {
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(selectedQuality == quality)
                            return;
                        quality.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(2,  2,  2,  2, Color.BLACK),
                                BorderFactory.createMatteBorder(3,  3,  3,  3, Color.WHITE)));
                                quality.setSelected(true);
                        if(selectedQuality != null) {
                            selectedQuality.setSelected(false);
                            selectedQuality.setBorder(BorderFactory.createLoweredBevelBorder());
                        }
                        selectedQuality = quality;
                    }
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(!quality.isSelected())
                            quality.setBorder(BorderFactory.createRaisedBevelBorder());
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(!quality.isSelected())
                            quality.setBorder(BorderFactory.createLoweredBevelBorder());
                    }
                    
                };
            }

        }

        public class QualityPanel extends JPanel {
            
            private VideoInfos infos;
            private boolean selected;

            public QualityPanel(VideoInfos infos) {
                this.infos = infos;
                String qp = infos.getResolution();
                try {
                    qp = (qp.split("x"))[1]+"p";
                } catch(Exception e) {}
                add(new JLabel(qp+" ("+infos.getSize()+")"));
                setBorder(BorderFactory.createLoweredBevelBorder());
            }
    
            public VideoInfos getInfos() {
                return infos;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }
    
        }

    }

}
