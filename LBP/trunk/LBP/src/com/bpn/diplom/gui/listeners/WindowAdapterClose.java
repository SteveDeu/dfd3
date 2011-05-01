package com.bpn.diplom.gui.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.apache.log4j.Logger;

public class WindowAdapterClose extends WindowAdapter {
	public void windowClosing(WindowEvent we) {
			
			
			if(we.getSource() instanceof java.awt.Window){
				((java.awt.Window)we.getSource()).dispose();
			}
			
			Logger.getRootLogger().info("! ! ! ! ! До свидания ! ! ! ! !");
			System.exit(0);
	}
}
