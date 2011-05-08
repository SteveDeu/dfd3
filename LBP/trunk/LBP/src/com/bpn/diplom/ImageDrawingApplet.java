package com.bpn.diplom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class ImageDrawingApplet extends JApplet {

	static String imageFileName = "g:\\group.jpg";
	private URL imageSrc;

	public ImageDrawingApplet() {
	}

	public ImageDrawingApplet(URL imageSrc) {
		this.imageSrc = imageSrc;
	}

	public void init() {
		try {
			imageSrc = new URL(getCodeBase(), imageFileName);
		} catch (MalformedURLException e) {
		}
		buildUI();
	}

	public void buildUI() {
		final ImageDrawingComponent id = new ImageDrawingComponent(imageSrc);
		add("Center", id);
		JComboBox choices = new JComboBox(id.getDescriptions());
		choices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				id.setOpIndex(cb.getSelectedIndex());
				id.repaint();
			};
		});
		add("South", choices);
	}

	public static void main(String s[]) {
		JFrame f = new JFrame("ImageDrawing");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		URL imageSrc = null;
		try {
			imageSrc = ((new File(imageFileName)).toURI()).toURL();
		} catch (MalformedURLException e) {
		}
		ImageDrawingApplet id = new ImageDrawingApplet(imageSrc);
		id.buildUI();
		f.add("Center", id);
		f.pack();
		f.setVisible(true);
	}
}