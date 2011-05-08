package com.bpn.diplom.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.bpn.diplom.gui.listeners.CloseActionListener;
import com.bpn.diplom.gui.listeners.EscapeExitKeyListener;
import com.bpn.diplom.gui.listeners.WindowAdapterClose;
import com.bpn.diplom.gui.utils.BoxLayoutUtils;
import com.bpn.diplom.gui.utils.GUITools;
import com.bpn.diplom.gui.utils.GUIUtils;
import com.bpn.diplom.gui.utils.Resource;

/**
 * Окно для входа в систему
 * 
 * @author pavel
 *
 */
public abstract class AbstractShowImage extends JInternalFrame {
	protected static final String TITLE = "AbstractShowImage.title";
	protected static final String BUTTON_OK 	= "buttons.start";
	protected static final String BUTTON_OPEN 	= "buttons.open";
	protected static final String BUTTON_CANCEL = "buttons.cancel";

	private static final int SCROLL_W = 700;
	private static final int SCROLL_H = 500;
	
	Image image;
	JDesktopPane desktop;
	
	private JScrollPane scrollImage;
	private JPanel panelImage;	
	protected JButton[] buttons = new JButton[0];
	private JButton btnCancel;

	public AbstractShowImage(Image image) {
		super();
		this.image = image;
		this.desktop = VirtualDesktop.getInstance().getDesktop();
		this.desktop.add(this);
		init();
		JPanel content = createGUI(); 
		this.add(content);
		this.settingEventsPrivate();
		this.pack();
		this.setLocation(50, 50);
		this.setTitle(Resource.getString(TITLE));
		if(this.getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getHeight() ||
				this.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().getWidth())
			this.setLocation(10, 10);
		else
			GUITools.centeringWindow(this, this.getDesktopPane());
		this.setResizable(true);
		this.setIconifiable(true);
		this.setVisible(true);
	}
	
	/** возвращает главную панель с
	* созданным расположением
	*/
	private JPanel createGUI() {
		JPanel panelMain = BoxLayoutUtils.createVerticalPanel();
		JPanel panelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel panelButton = new JPanel(new GridLayout(1, 2, 5, 0));
		panelMain.setLayout(new BorderLayout());
		
		panelImage = new JPanel(){
			@Override
			 public void paintComponent(Graphics g){
				 super.paintComponent(g);
				 g.drawImage(image, 0, 0, this);
			 }
		};
		panelImage.setLayout(new BoxLayout(panelImage, BoxLayout.X_AXIS));
		scrollImage = new JScrollPane();
		setImage(image);
		
		for(JButton btn : buttons){
			if(btn != null)
				panelButton.add(btn);
		}
		btnCancel = new JButton(Resource.getString(BUTTON_CANCEL));
		panelButton.add(btnCancel);
		panelFlow.add(panelButton);
		
		BoxLayoutUtils.setGroupAlignmentX(new JComponent[] { scrollImage, panelMain, panelFlow }, Component.LEFT_ALIGNMENT);
		GUITools.createRecommendedMargin(buttons);
		GUITools.createRecommendedMargin(btnCancel);
//		GUITools.setComponentSize(panelFlow, w, h);
		
		int bw = 12;
		panelMain.setBorder(BorderFactory.createEmptyBorder(bw, bw, bw, bw));
		panelFlow.setBorder(BorderFactory.createEmptyBorder(bw, bw, bw, bw));
		
		panelMain.add(scrollImage, BorderLayout.NORTH);
		panelMain.add(panelFlow);

		return panelMain;
	}

	public void setImage(Image image){
		if(image == null)
			return;
		this.image = image;

		GUITools.setComponentSize(panelImage, image.getWidth(null), image.getHeight(null)-25);
		
		int w = Math.min(SCROLL_W, image.getWidth(null) + 3);
		int h = Math.min(SCROLL_H, image.getHeight(null) - 9);
		GUITools.setComponentSize(scrollImage, w, h);
		
		scrollImage.setViewportView(panelImage);
		this.pack();
		this.repaint();
	}
	
	
	
	public Image getImage() {
		return image;
	}
	
	private void settingEventsPrivate(){
		this.btnCancel.addActionListener(new CloseActionListener());
		this.addKeyListener(new EscapeExitKeyListener());
		this.addInternalFrameListener(new WindowAdapterClose());
		
		this.addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentShown(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent ev) {
				GUITools.setComponentSize(scrollImage, 
						AbstractShowImage.this.getWidth(), 
						AbstractShowImage.this.getHeight() - 95
				);
			}

		});
		
		settingEvents();
	}
	 
	
	protected abstract void init();
	protected abstract void settingEvents();
	
	
//	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException {
//		try {
//			VirtualDesktop.main(null);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}