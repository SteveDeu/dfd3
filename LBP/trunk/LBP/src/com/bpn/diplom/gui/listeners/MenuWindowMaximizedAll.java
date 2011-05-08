package com.bpn.diplom.gui.listeners;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import com.bpn.diplom.gui.VirtualDesktop;

public class MenuWindowMaximizedAll  extends AbstractAction {

	public void actionPerformed(ActionEvent e) {

		for(JInternalFrame iframe : VirtualDesktop.getInstance().getDesktop().getAllFrames()){
			try {
				iframe.setIcon(false);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
}