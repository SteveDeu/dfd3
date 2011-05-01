package com.bpn.diplom.gui.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

public class EscapeExitKeyListener implements KeyListener{
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			if(e.getSource() instanceof java.awt.Window){
				((java.awt.Window)e.getSource()).dispose();
			}
			Logger.getRootLogger().info("! ! ! ! ! До свидания ! ! ! ! !");
			System.exit(0);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
