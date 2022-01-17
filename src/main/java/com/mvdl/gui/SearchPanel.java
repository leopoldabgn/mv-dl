package com.mvdl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.mvdl.model.Command;
import com.mvdl.model.Preferences;
import com.mvdl.model.Video;

public class SearchPanel extends JPanel {

    private Preferences prefs;

    private SearchBar searchBar;
    private DownloadFolderPanel folderPan;
    private ListPanel listPan;

    public SearchPanel(Preferences prefs) {
        this.prefs = prefs;
        this.searchBar = new SearchBar();
        this.folderPan = new DownloadFolderPanel();
        this.listPan = new ListPanel();
        
        JPanel northPan = new JPanel();
        northPan.setBackground(new Color(70, 70, 70));
        northPan.setLayout(new BorderLayout());
        int border = 10;
        northPan.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        JPanel tmp = new JPanel();
        tmp.setOpaque(false);
        tmp.add(searchBar);

        northPan.add(tmp, BorderLayout.WEST);
        northPan.add(folderPan, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(listPan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                          JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(37, 37, 38));
        this.add(northPan, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshVideos(String search) {
        
    }

    public class SearchBar extends JPanel {
        
        public static final int HEIGHT = 40;
    
        private JTextField searchField;
        private JButton searchButton;
    
        public SearchBar() {
            this.searchField = new JTextField();
            searchField.setToolTipText("Search...");
            this.searchButton = new JButton("Search");
    
            searchButton.addActionListener(e -> {
                ///////// OLD METHOD //////////
                // String HTMLresult = Command.getHtmlFromSearch(searchField.getText());
                // List<Video> videos = Command.getVideos(HTMLresult);
                ///////////////////////////////
                List<Video> videos = Command.getVideosByJSON(Command.cutHTML(Command.getYtbHTMLCode(searchField.getText())));
                listPan.refresh(videos);
            });
    
            this.setPreferredSize(new Dimension(350, HEIGHT));
            this.setLayout(new BorderLayout());
            this.add(searchField, BorderLayout.CENTER);
            this.add(searchButton, BorderLayout.EAST);
        }
    
    }

    private class ListPanel extends JPanel {
        
        private List<VideoPanel> videoPnls;
        
        private ListPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(37, 37, 38));
        }

        private ListPanel(List<Video> videos) {
            this();
            refresh(videos);
        }

        public void refresh(List<Video> videos) {
            this.removeAll();            
            videoPnls = new ArrayList<>();

            if(videos == null || videos.isEmpty()) {
                revalidate();
                repaint();
                return;
            }

            for(Video video : videos) {
                videoPnls.add(new VideoPanel(prefs, video));
                this.add(videoPnls.get(videoPnls.size()-1));
            }
            revalidate();
            repaint();
        }

    }

    private class DownloadFolderPanel extends JPanel {

        private JLabel actualFolder;
        private IconPanel chooseFolder;

        private DownloadFolderPanel() {
            this.actualFolder = new JLabel(prefs.getDownloadFolder().getAbsolutePath());
            actualFolder.setForeground(new Color(200, 210, 220));
            this.chooseFolder = new IconPanel("folder", 32);
            chooseFolder.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    changeSourceDirectory();
                }
            });

            this.setOpaque(false);
            this.add(actualFolder);
            this.add(chooseFolder);
        }

        public void changeSourceDirectory() {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose a directory to upload: ");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    
            int returnValue = jfc.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (jfc.getSelectedFile().isDirectory()) {
                    setFolder(jfc.getSelectedFile().getAbsolutePath());
                }
                else {
                    //openWarningDialog("Sorry, but this is not a folder.");
                }
            }
        }

        public void setFolder(String path) {
            File f = new File(path);
            if(!f.exists())
                return;
            prefs.setDownloadFolder(f);
            actualFolder.setText(prefs.getDownloadFolder().getAbsolutePath());
        }

    }

}