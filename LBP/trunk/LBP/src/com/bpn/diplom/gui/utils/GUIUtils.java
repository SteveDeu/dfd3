package com.bpn.diplom.gui.utils;

import java.awt.Component;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	
	
	public static boolean isAvalibleFormatImage(File file){
		String[] parts = file.getName().split("[.]");
		if(parts.length == 0)
			return false;
		Set<String> avalibleFormat = new HashSet<String>();
		Collections.addAll(avalibleFormat, GUIUtils.avalibleFormat);
		return avalibleFormat.contains(parts[parts.length - 1]);
	}
	
}
