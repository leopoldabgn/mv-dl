package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import model.Command;
import model.Preferences;
import model.Video;

public class SearchPanel extends JPanel {

    private GUI gui;
    private Preferences prefs;

    private SearchBar searchBar;
    private DownloadFolderPanel folderPan;
    private IconPanel settings;
    private ListPanel listPan;

    public SearchPanel(GUI gui, Preferences prefs) {
        this.gui = gui;
        this.prefs = prefs;
        this.searchBar = new SearchBar();
        this.folderPan = new DownloadFolderPanel();
        this.listPan = new ListPanel();
        
        JPanel northPan = new JPanel();
        northPan.setBackground(GUI.darkColor1);
        northPan.setLayout(new BorderLayout());
        int border = 10;
        northPan.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        JPanel tmp = new JPanel();
        tmp.setOpaque(false);
        tmp.add(searchBar);

        northPan.add(tmp, BorderLayout.WEST);

        tmp = new JPanel();
        tmp.setOpaque(false);
        tmp.add(folderPan);

        settings = new IconPanel("settings-icon", 32);
        settings.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setSettingsPanel();
            }
        });
        tmp.add(settings);

        northPan.add(tmp, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(listPan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                          JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.setLayout(new BorderLayout());
        this.setBackground(GUI.darkColor2);
        this.add(northPan, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshVideos(String search) {
        
    }

    public class SearchBar extends JPanel {
        
        public static final int HEIGHT = 40;
    
        private JTextField searchField;
        private IconPanel searchButton;
    
        public SearchBar() {
            this.setOpaque(false);
            this.searchField = new JTextField();
            searchField.setToolTipText("Search...");
            this.searchButton = new IconPanel("search_icon", 40);
    
            searchButton.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    ///////// OLD METHOD //////////
                    // String HTMLresult = Command.getHtmlFromSearch(searchField.getText());
                    // List<Video> videos = Command.getVideos(HTMLresult);
                    ///////////////////////////////
                    searchButton.setEnabled(false);
                    startSearch();
                }

                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    searchButton.setEnabled(true);
                }

            });
    
            this.setPreferredSize(new Dimension(350, HEIGHT));
            this.setLayout(new BorderLayout());
            this.add(searchField, BorderLayout.CENTER);
            this.add(searchButton, BorderLayout.EAST);
        }

        public void startSearch() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Video> videos = Command.getVideosByJSON(Command.cutHTML(Command.getYtbHTMLCode(searchField.getText())));
                    listPan.refresh(videos);
                }
            }).start();
        }
    
    }

    private class ListPanel extends JPanel {
        
        private List<VideoPanel> videoPnls;
        
        private ListPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(GUI.darkColor2);
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
            actualFolder.setForeground(GUI.textColor1);
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

    public void setSearchButtonEnabled(boolean enabled) {
        searchBar.searchButton.setEnabled(enabled);
    }

    public void startSearch() {
        searchBar.startSearch();
    }

}