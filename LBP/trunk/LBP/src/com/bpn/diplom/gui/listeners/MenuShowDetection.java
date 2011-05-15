package com.bpn.diplom.gui.listeners;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.bpn.diplom.gui.ShowImageShowDetection;
import com.bpn.diplom.gui.VirtualDesktop;
import com.bpn.diplom.gui.utils.GUIUtils;

public class MenuShowDetection extends AbstractAction {

	String magicWord = "d";
	
	public void actionPerformed(ActionEvent e) {

		new Thread(new Runnable() {	
			public void run() {
				try {
					File file;
					file = GUIUtils.openImageFileChooser(VirtualDesktop.getInstance());
//					file = new File("g:\\p.jpg");
//					file = new File("g:\\group.jpg");
//					file = new File("g:\\ps17.jpg");
					
					if(file.exists()){
						Image src = ImageIO.read(file);
						new ShowImageShowDetection(file.getAbsolutePath(), src);
					} else {
						if(file.getName().equalsIgnoreCase(magicWord)){
							long start = System.currentTimeMillis();
							detectAllImgInDir(file);
							JOptionPane.showMessageDialog(VirtualDesktop.getInstance(), 
									"Закінчено за "+ (System.currentTimeMillis() - start)/1000 + "c " + (System.currentTimeMillis() - start)%1000 + "mc");
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
	
	
	private void detectAllImgInDir(File dir){
		File workDir = new File(dir.getAbsolutePath().substring(0, dir.getAbsolutePath().length() - magicWord.length()));
		if(workDir.isDirectory()){
			File[] files = workDir.listFiles();
			for(File file : files){

				try{
					if(file.isFile() && isAvalibleFormatImage(file)){
						Image src = ImageIO.read(file);
						new ShowImageShowDetection(file.getName(), src).doClickDetect();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private boolean isAvalibleFormatImage(File file){
		String[] parts = file.getName().split("[.]");
		if(parts.length == 0)
			return false;
		Set<String> avalibleFormat = new HashSet<String>();
		Collections.addAll(avalibleFormat, GUIUtils.avalibleFormat);
		return avalibleFormat.contains(parts[parts.length - 1]);
	}
}