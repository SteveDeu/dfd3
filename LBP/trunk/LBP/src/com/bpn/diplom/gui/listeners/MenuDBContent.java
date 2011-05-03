package com.bpn.diplom.gui.listeners;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;

import com.bpn.diplom.gui.*;

public class MenuDBContent extends AbstractAction {

	public void actionPerformed(ActionEvent e) {

		new Thread(new Runnable() {	
			public void run() {
//				try {
					new DBManager();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
		}).start();
	}
}