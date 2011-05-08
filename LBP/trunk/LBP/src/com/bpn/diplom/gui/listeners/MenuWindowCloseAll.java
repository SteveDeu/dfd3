package com.bpn.diplom.gui.listeners;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import com.bpn.diplom.gui.*;

public class MenuWindowCloseAll extends AbstractAction {

	public void actionPerformed(ActionEvent e) {

		for(JInternalFrame iframe : VirtualDesktop.getInstance().getDesktop().getAllFrames()){
			iframe.dispose();
			
		}
		
	}
}