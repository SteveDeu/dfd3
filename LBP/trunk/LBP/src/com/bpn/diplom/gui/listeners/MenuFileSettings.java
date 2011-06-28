package com.bpn.diplom.gui.listeners;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import com.bpn.diplom.gui.DBManager;
import com.bpn.diplom.gui.Settings;
import com.bpn.diplom.processing.ProcessingVideoLBP;

public class MenuFileSettings extends AbstractAction{
	
	public void actionPerformed(ActionEvent e) {

		new Thread(new Runnable() {	
			public void run() {
					new Settings();
			}
		}).start();
	}
	
}