package com.bpn.diplom.gui.listeners;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import com.bpn.diplom.gui.VirtualDesktop;

public class MenuWindowMinimizeAll  extends AbstractAction {

	public void actionPerformed(ActionEvent e) {

		for(JInternalFrame iframe : VirtualDesktop.getInstance().getDesktop().getAllFrames()){
			try {
				iframe.setIcon(true);
			} catch (PropertyVetoException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
}