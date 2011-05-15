package com.bpn.diplom.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.processing.FacesDetection;
import com.bpn.diplom.processing.ObjectDetection;

public class ShowImageShowDetection extends AbstractShowImage{


	JButton btnDetection;
	
	public ShowImageShowDetection(Image image) {
		super(image);
	}

	public ShowImageShowDetection(String name, Image src) {
		super(name, src);	
	}

	@Override
	protected void init() {
		btnDetection = new JButton(Resource.getString("buttons.detect"));
		buttons = new JButton[]{btnDetection};
	}

	@Override
	protected void settingEvents() {
		btnDetection.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doClickDetect();
			}});
	}



	public void doClickDetect(){
//		setImage(image);
		final FacesDetection faceDetector = new FacesDetection();
		List<Image> faces = faceDetector.detect(image, false, new TreeMap<String, Image>());
		if(faces.size() == 0){
			JOptionPane.showMessageDialog(VirtualDesktop.getInstance(), "Ќе знайдено жодного оболичч€");
			return;
		}
		
		List<AbstractShowImage> facesWindow = new ArrayList<AbstractShowImage>();
		for(Image face : faces){
			AbstractShowImage window = new  ShowImageSimple("¬ид≥лене обличч€", (BufferedImage) face);
			if(facesWindow.size() == 0)
				window.setLocation(0, 0);
			else{
				AbstractShowImage last = facesWindow.get(facesWindow.size() - 1);
				window.setLocation(last.getX() + last.getWidth(), last.getY());
				if((window.getLocation().getX() + window.getWidth()) > window.getParent().getWidth())
					window.setLocation(0, last.getY()+200);
			}
			facesWindow.add(window);	
		}
	}
	
}
