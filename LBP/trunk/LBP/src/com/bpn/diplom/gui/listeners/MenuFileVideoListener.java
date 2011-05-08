package com.bpn.diplom.gui.listeners;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import com.bpn.diplom.processing.ProcessingVideoLBP;

public class MenuFileVideoListener extends AbstractAction{
	
	public void actionPerformed(ActionEvent e) {
		
		ProcessingVideoLBP.main(null);
		
	}
	
}