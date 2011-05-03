package com.bpn.diplom.gui.listeners;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;

import com.bpn.diplom.gui.VirtualDesktop;

public class EscapeExitKeyListener implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("beforef");
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.println("esc");
			try {
				Component comp = (Component) e.getSource();
				while (comp != null) {
					if (comp instanceof JInternalFrame) {
						JInternalFrame frame = (JInternalFrame) comp;
						frame.dispose();
						VirtualDesktop.getInstance().getDesktop().remove(comp);
						break;
					} else {
						comp = comp.getParent();
					}
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
