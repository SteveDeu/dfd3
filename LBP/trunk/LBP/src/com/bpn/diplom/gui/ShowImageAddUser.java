package com.bpn.diplom.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.lbp.MatcherPirsonX2;
import com.bpn.diplom.processing.*;
import com.bpn.diplom.dao.*;

public class ShowImageAddUser extends AbstractShowImage{

	DAOLBP userManager = new DAOLBPImpl();

	JButton btnAddUser;
	
	Map<String, Image> showProcesingImages = new TreeMap<String, Image>();
	
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
				addUser();
			}});
		
	}


	private void addUser(){	
		
		ProcessingImageLBP processing = new ProcessingImageLBP(); 
		List<EntityLBPUser> users = processing.processingImage((BufferedImage) this.getImage(), showProcesingImages);
		
//		for(Map.Entry<String, Image> entry : showProcesingImages.entrySet()){
//			new ShowImageSimple(entry.getKey(), (BufferedImage) entry.getValue());
//		}		
		
		for(EntityLBPUser user : users){
			ShowImageSimple userFace= new ShowImageSimple("Заведення нового обличчя до БД", (BufferedImage) user.getImageFaceArea());
			String name = JOptionPane.showInputDialog(userFace, "Введіть імя користувача");
			user.setName(name);
			userManager.addUser(user);
		}
}
}
