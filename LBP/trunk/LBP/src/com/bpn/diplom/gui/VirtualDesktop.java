package com.bpn.diplom.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
//import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

//import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.bpn.diplom.gui.utils.BoxLayoutUtils;
import com.bpn.diplom.gui.utils.GUITools;
//import com.bpn.diplom.gui.utils.Reference;
import com.bpn.diplom.gui.utils.Resource;
import com.bpn.diplom.gui.utils.XMLMenuLoader;


/**
* <code>VirtualDesktop</code> - класс главного окна. 
* Содержит в себе меню и виртуальный рабочий стол JPaneDesktop
* 
* @author  Bilan Pavel
* @version 1.0, 24/11/10
* 
* @since   JDK1.6
*/
public class VirtualDesktop extends JFrame{
	
	private final static String TITLE ="VirtualDesktop.title";
	private final static String FILE_MENU_XML="menu.xml";
	
	private static VirtualDesktop instance;
	
	
	private final Logger log = Logger.getRootLogger();
	private final JDesktopPane desktop = new JDesktopPane();

	private JSplitPane splitPane;

	private VirtualDesktop(){
		super(Resource.getString(TITLE));
		log.debug("make main window: VirtualDesktop");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSizeAsScreen(false);
		this.setJMenuBar(createMenuXML());
		JPanel content = createGUI(); 
		this.add(content);
		this.settingEvents();
		if(this.getExtendedState() != JFrame.MAXIMIZED_BOTH)
			GUITools.centeringWindow(this);
		this.setVisible(true);
	}
	

	
	public static VirtualDesktop getInstance(){
		if(instance == null){
			instance = new VirtualDesktop();
		}	
		return VirtualDesktop.instance;
	}

	private void setSizeAsScreen(boolean fullScreen){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); 
		int x = dimension.width; 
		int y = dimension.height; 
		this.setSize(x-x/10, y - y/10) ; 
		this.setLocation(0,0);
		if(fullScreen)
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	};
	
	/** возвращает главную панель с
	* созданным расположением
	*/
	private JPanel createGUI() {
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel panelMain = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelDesktop = BoxLayoutUtils.createVerticalPanel();
		JPanel panelMore = BoxLayoutUtils.createVerticalPanel();

		int b = 3;
		panelMain.setBorder(BorderFactory.createEmptyBorder(b,b,b,b));
		panelDesktop.add(desktop);
		splitPane.setLeftComponent(panelDesktop);
		splitPane.setRightComponent(panelMore);
		splitPane.setDividerSize(10);
		splitPane.setDividerLocation(this.getWidth());
		panelMain.add(splitPane);
		return panelMain;
	}

	
	
	private JMenuBar createMenuXML(){
		XMLMenuLoader loader=null;
		try {
			loader = new XMLMenuLoader(VirtualDesktop.class.getResourceAsStream(FILE_MENU_XML));
			loader.parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("не найден файл с содержимим меню\n",e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ошибка при парсинге содержимого меню\n",e);
		}
		return loader.getMenuBar(XMLMenuLoader.XMLParser.ELEMENT_MENU_BAR);
	}
	
	private void settingEvents(){
		
//		this.addPropertyChangeListener(new PropertyChangeListener(){
//			@Override
//			public void propertyChange(PropertyChangeEvent ev) {
//				System.out.println(ev.getPropertyName()+" oldValue:" + ev.getOldValue() + " newVal:"+ev.getNewValue());
//			}
//		});
//		
//		this.addWindowStateListener(new WindowStateListener(){
//			@Override
//			public void windowStateChanged(WindowEvent arg0) {
//				if( VirtualDesktop.this.getExtendedState() == JFrame.MAXIMIZED_BOTH
//					|| VirtualDesktop.this.getExtendedState() == JFrame.MAXIMIZED_HORIZ)
//				{
//					splitPane.setDividerLocation(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
//				} else {
//					splitPane.setDividerLocation(VirtualDesktop.this.getWidth());
//				}	
//			}
//		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
				System.exit(0);
			}
		});
		
		
	}
	
	public synchronized JDesktopPane getDesktop() {
		return desktop;
	}


//	public synchronized void setDesktop(JDesktopPane desktop) {
//		this.desktop = desktop;
//	}
//	
	
	public static void main(String[] args) throws IOException{
		getInstance();
		
	}
	
	
}



