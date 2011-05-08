package com.bpn.diplom.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.processing.*;
import com.bpn.diplom.dao.*;

public class ShowImageShowDetection extends AbstractShowImage{


	JButton btnDetection;
	
	
	public ShowImageShowDetection(Image image) {
		super(image);
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
				detect();
				setImage(image);
			}});
		
	}


	private void detect(){
		System.out.println("detect");
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setColor(Color.RED);
		
		
		
		
		g2.drawLine(10, 10, 50, 50);
		
	}
}
