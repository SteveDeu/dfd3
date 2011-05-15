package com.bpn.diplom.gui.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIUtils {

	public static String [] avalibleFormat = {"jpg", "gif", "png", "jpeg", "bmp"};
	
	public static File openImageFileChooser(Component parent){
		
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, JPEG, GIF, PNG, BMP", avalibleFormat);
		fileChooser.setFileFilter(filter);
		File file = null;
		fileChooser.setSelectedFile(new File("d:\\db1\\"));
		int returnVal = fileChooser.showOpenDialog(parent);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        file = fileChooser.getSelectedFile();
	    }
		
		return file;
	}
	
}
