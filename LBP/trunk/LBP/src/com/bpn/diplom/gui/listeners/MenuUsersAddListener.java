package com.bpn.diplom.gui.listeners;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.bpn.diplom.dao.DAOLBPImpl;
import com.bpn.diplom.dao.EntityLBPUser;
import com.bpn.diplom.gui.ShowImageAddUser;
import com.bpn.diplom.gui.ShowImageShowDetection;
import com.bpn.diplom.gui.ShowImageSimple;
import com.bpn.diplom.gui.VirtualDesktop;
import com.bpn.diplom.gui.utils.GUIUtils;
import com.bpn.diplom.lbp.*;
import com.bpn.diplom.processing.ProcessingImageLBP;

public class MenuUsersAddListener extends AbstractAction {

	String magicWord = "d";
	
	public void actionPerformed(ActionEvent e) {

		new Thread(new Runnable() {	
			public void run() {
//				try {
//					File file;
//					file = GUIUtils.openImageFileChooser(VirtualDesktop.getInstance());
////					file = new File("g:\\p.jpg");
//					if(file != null){
//						Image src = ImageIO.read(file);
//						new ShowImageAddUser(src);
//					}
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
				
				
				try {
					File file;
					file = GUIUtils.openImageFileChooser(VirtualDesktop.getInstance());
//					file = new File("g:\\p.jpg");
//					file = new File("g:\\group.jpg");
//					file = new File("g:\\ps17.jpg");
					if(file == null)
						return;
					if(file.exists()){
						Image src = ImageIO.read(file);
						new ShowImageAddUser(src);
					} else {
						if(file.getName().equalsIgnoreCase(magicWord)){
							long start = System.currentTimeMillis();
							EntityLBPUser user = VectorsAverage.getAverageFromUsers(processingAllImgInDir(file));
							JOptionPane.showMessageDialog(VirtualDesktop.getInstance(), 
									"Закінчено за "+ (System.currentTimeMillis() - start)/1000 + "c " + (System.currentTimeMillis() - start)%1000 + "mc");
							
							if(user != null){
								ShowImageSimple userFace= new ShowImageSimple("Заведення нового обличчя до БД", (BufferedImage) user.getImageFaceArea());
								String name = JOptionPane.showInputDialog(userFace, "Введіть імя користувача");
								user.setName(name);
								new DAOLBPImpl().addUser(user);
							} else {
								JOptionPane.showMessageDialog(null, "error code 13546541");
							}
						} else	
							JOptionPane.showMessageDialog(VirtualDesktop.getInstance(), 
								"Файл " + file.getAbsolutePath()	 + " не найден !", "Ошибка!", JOptionPane.ERROR_MESSAGE);
						
						
					}
					
					
					
//					JOptionPane.showInputDialog(VirtualDesktop.getInstance(), "Введіть шлях до папки з фотографіями", "Вибір папки",  )
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
				
				
			}
		}).start();
	}
	
	
	
	
	private List<EntityLBPUser> processingAllImgInDir(File dir){
		List<EntityLBPUser> result = new ArrayList<EntityLBPUser>();
		File workDir = new File(dir.getAbsolutePath().substring(0, dir.getAbsolutePath().length() - magicWord.length()));
		if(workDir.isDirectory()){
			
			ProcessingImageLBP processing = new ProcessingImageLBP();
			File[] files = workDir.listFiles();
			for(File file : files){
				try{
					if(file.isFile() && GUIUtils.isAvalibleFormatImage(file)){
						Image img = ImageIO.read(file);
						List<EntityLBPUser> users = processing.processingImage(
								(BufferedImage) img, new HashMap<String, Image>());
						if(users.size() > 0)
							result.add(users.get(0));
//						result.addAll(
//								processing.processingImage(
//										(BufferedImage) img, new HashMap<String, Image>()));

					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return result;
	}

	
	
}