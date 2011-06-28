package com.bpn.diplom.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.lbp.MatcherPirsonX2;
import com.bpn.diplom.processing.*;
import com.bpn.diplom.dao.*;

public class ShowImageIdentifUser extends AbstractShowImage{

	DAOLBP userManager = new DAOLBPImpl();

	JButton btnIdentif;
	
	Map<String, Image> showProcesingImages = new TreeMap<String, Image>();
	
	public ShowImageIdentifUser(Image image) {
		super(image);
	}

	@Override
	protected void init() {
		btnIdentif = new JButton(Resource.getString("buttons.identif"));
		buttons = new JButton[]{btnIdentif};
	}

	@Override
	protected void settingEvents() {
		btnIdentif.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				іdentifUser();
			}});
		
	}


	private void іdentifUser(){

		ProcessingImageLBP processing = new ProcessingImageLBP(); 
		EntityLBPUser userCheck = processing.processingImage((BufferedImage) this.getImage(), showProcesingImages).get(0);
		
		Map<Long, EntityLBPUser> difs8 = new HashMap<Long, EntityLBPUser>();
		Map<Long, EntityLBPUser> difs12 = new HashMap<Long, EntityLBPUser>();
		Map<Long, EntityLBPUser> difs16 = new HashMap<Long, EntityLBPUser>();
		
		for(EntityLBPUser user : userManager.getAllUsers()){
			difs8.put(
					MatcherPirsonX2.getDistanceX2(user.getVector8(), userCheck.getVector8(), processing.getFaceVectorBuilder8()),
					user);
			difs12.put(
					MatcherPirsonX2.getDistanceX2(user.getVector12(), userCheck.getVector12(), processing.getFaceVectorBuilder12()),
					user);
			difs16.put(
					MatcherPirsonX2.getDistanceX2(user.getVector16(), userCheck.getVector16(), processing.getFaceVectorBuilder16()),
					user);
		}
		
		Long minDif8 = null;
		Long minDif12 = null;
		Long minDif16 = null;
		if(difs8.size() > 0){
			minDif8 = Collections.min(difs8.keySet());
		}
		if(difs12.size() > 0){
			minDif12 = Collections.min(difs12.keySet());
		}
		if(difs16.size() > 0){
			minDif16 = Collections.min(difs16.keySet());
		}
			
//		ShowImageSimple userFace= new ShowImageSimple("Заведення нового обличчя до БД", (BufferedImage) user.getImageFaceArea());
		JOptionPane.showMessageDialog(this, " 8 Логін:" + difs8.get(minDif8).getName() + "Відстань: "+minDif8+
				"\n12 Логін:" + difs12.get(minDif12).getName() + "Відстань: "+minDif12+
				"\n16 Логін:" + difs16.get(minDif16).getName() + "Відстань: "+minDif16);
//		EntityLBPUser userEtalon = userManager.getUsers().get(0);
	}
}
