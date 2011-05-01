package com.bpn.diplom.gui.listeners;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import org.apache.log4j.Logger;


/**Выход из программы
 * 
 * @author pavel
 *
 */
public class SystemExitActionListener extends AbstractAction{
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() instanceof java.awt.Window){
			((java.awt.Window)e.getSource()).dispose();
		}
		
		Logger.getRootLogger().info("! ! ! ! ! До свидания ! ! ! ! !");
		System.exit(0);
		
	}
	
}
