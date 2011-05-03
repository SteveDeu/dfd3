package com.bpn.diplom.gui.listeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import com.bpn.diplom.gui.VirtualDesktop;
import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

/**Закрыть окно
 * 
 * @author pavel
 *
 */
public class CloseActionListener extends AbstractAction{
	
	public void actionPerformed(ActionEvent e) {
		try {
			Component comp = (Component) e.getSource();
			while(comp != null){
				if(comp instanceof JInternalFrame){
					JInternalFrame frame = (JInternalFrame)comp;
					frame.dispose();
					VirtualDesktop.getInstance().getDesktop().remove(comp);
					break;
				} else{
					comp = comp.getParent();
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
}
