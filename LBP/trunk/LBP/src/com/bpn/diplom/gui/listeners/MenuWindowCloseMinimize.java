package com.bpn.diplom.gui.listeners;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import com.bpn.diplom.gui.VirtualDesktop;

public class MenuWindowCloseMinimize extends AbstractAction {

	public void actionPerformed(ActionEvent e) {

		for(JInternalFrame iframe : VirtualDesktop.getInstance().getDesktop().getAllFrames()){
			if(iframe.isIcon())
				iframe.dispose();
		}
		
	}
}