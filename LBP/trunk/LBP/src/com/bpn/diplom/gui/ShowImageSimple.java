package com.bpn.diplom.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class ShowImageSimple extends AbstractShowImage{

	
	public ShowImageSimple() {
		super();
	}
	
	public ShowImageSimple(String title, BufferedImage image) {
		super(title, image);
	}

	public ShowImageSimple(Image image) {
		super(image);
	}

	@Override
	protected void init() {
	}

	@Override
	protected void settingEvents() {
	}


}
