package com.bpn.diplom.gui.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIUtils {

	
	public static File openImageFileChooser(Component parent){
		
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, GIF, PNG, JPEG Images", "jpg", "gif", "png", "jpeg");
		fileChooser.setFileFilter(filter);
		File file = null;
		int returnVal = fileChooser.showOpenDialog(parent);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        file = fileChooser.getSelectedFile();
	    }
		
		return file;
	}
	
}
