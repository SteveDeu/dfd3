package com.bpn.diplom.gui;

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

public class ShowImageAddUser extends AbstractShowImage{

	DAOLBP userManager = new DAOLBPImpl();

	JButton btnAddUser;
	
	
	public ShowImageAddUser(Image image) {
		super(image);
	}

	@Override
	protected void init() {
		btnAddUser = new JButton(Resource.getString("buttons.add"));
		buttons = new JButton[]{btnAddUser};
	}

	@Override
	protected void settingEvents() {
		btnAddUser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(ShowImageAddUser.this, "¬вед≥ть ≥м€ користувача");
//				String name = "";
				addUser(name);
			}});
		
	}


	private void addUser(String name){
		if(name == null)
			return;
		ProcessingImageLBP processing = new ProcessingImageLBP(); 
		Object[] result = processing.processingImage((BufferedImage) this.getImage());
		Image imageFace = (Image) result[0];
		this.setImage(imageFace);
		
		EntityLBPUser user = (EntityLBPUser) result[1];
		user.setName(name);
		userManager.addUser(user);
	}
}
