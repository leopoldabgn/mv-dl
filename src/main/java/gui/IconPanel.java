package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class IconPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage icon, defaultIcon, grayIcon;
	
	public IconPanel(BufferedImage icon, int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		this.icon = icon;
		this.defaultIcon = icon;
	}

	public IconPanel(String path, int size) {
		this.setPreferredSize(new Dimension(size, size));
		setOpaque(false);
		String name = path;
		path += ".png";// convertPath(path);

		try {
			icon = (BufferedImage)getImage(path);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(path);
		}
		
		defaultIcon = icon;
		path =  "gray_"+name+".png";
		try {
			grayIcon = (BufferedImage)getImage(path);
		}
		catch(Exception e) {}
	}
	
	public String convertPath(String path) {
		path = path.charAt(0)+path.substring(1).toLowerCase();
		path = "res/"+path+".png";
		return path;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled)
			icon = defaultIcon;
		else if(grayIcon != null)
			icon = grayIcon;
		
		revalidate();
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(icon != null) {
			g.drawImage(icon, 0, 0, getWidth(), getHeight(), null);	
		}
	}

	public static Image getImage(final String pathAndFileName) throws Exception {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
		return ImageIO.read(url);
	}

}