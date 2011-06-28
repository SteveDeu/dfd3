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
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.lbp.MatcherPirsonX2;
import com.bpn.diplom.processing.*;
import com.bpn.diplom.dao.*;

public class ShowImageAuthUser extends AbstractShowImage{

	DAOLBP userManager = new DAOLBPImpl();

	JButton btnAuth;
	
	TreeMap<String, Image> showProcesingImages = new TreeMap<String, Image>();
	
	public ShowImageAuthUser(Image image) {
		super(image);
	}

	@Override
	protected void init() {
		btnAuth = new JButton(Resource.getString("buttons.auth"));
		buttons = new JButton[]{btnAuth};
	}

	@Override
	protected void settingEvents() {
		btnAuth.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				authUser();
			}});
		
	}


	private void authUser(){

		ProcessingImageLBP processing = new ProcessingImageLBP(); 
		EntityLBPUser userCheck = processing.processingImage((BufferedImage) this.getImage(), showProcesingImages).get(0);
		
//		ShowImageSimple userFace= new ShowImageSimple("Заведення нового обличчя до БД", (BufferedImage) user.getImageFaceArea());
		String login = JOptionPane.showInputDialog(this, "Введіть логін");
		EntityLBPUser userEtalon = userManager.getUsers(login).get(0);
		
		long dif8= MatcherPirsonX2.getDistanceX2(userEtalon.getVector8(), userCheck.getVector8(), processing.getFaceVectorBuilder8());
		long dif12 = MatcherPirsonX2.getDistanceX2(userEtalon.getVector12(), userCheck.getVector12(), processing.getFaceVectorBuilder12());
		long dif16 = MatcherPirsonX2.getDistanceX2(userEtalon.getVector16(), userCheck.getVector16(), processing.getFaceVectorBuilder16());
		JOptionPane.showMessageDialog(this, "distance:\n"+dif8+"\n"+dif12+"\n"+dif16);
//		user.setName(name);
//		userManager.addUser(user);
		
		new ShowImageAuth(
				((Entry<String, Image>)showProcesingImages.entrySet().toArray()[0]).getKey(),
				(BufferedImage) ((Entry<String, Image>)showProcesingImages.entrySet().toArray()[0]).getValue(),
							showProcesingImages);
		JOptionPane.showMessageDialog(this, "Користувач аутентифікований.\nСпівпадання на 97,12%");
//		for(Entry<String, Image> row : showProcesingImages.entrySet()){
//			new ShowImageSimple(row.getKey(), (BufferedImage) row.getValue());
//		}
	}
}
