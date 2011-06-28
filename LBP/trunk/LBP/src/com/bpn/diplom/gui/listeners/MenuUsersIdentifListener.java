package com.bpn.diplom.gui.listeners;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;

import com.bpn.diplom.gui.ShowImageIdentifUser;
import com.bpn.diplom.gui.VirtualDesktop;
import com.bpn.diplom.gui.utils.GUIUtils;

public class MenuUsersIdentifListener extends AbstractAction {

	public void actionPerformed(ActionEvent e) {

		new Thread(new Runnable() {	
			public void run() {
				try {
					File file;
					file = GUIUtils.openImageFileChooser(VirtualDesktop.getInstance());
//					file = new File("g:\\p.jpg");
					if(file != null){
						Image src = ImageIO.read(file);
						new ShowImageIdentifUser(src);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}
}