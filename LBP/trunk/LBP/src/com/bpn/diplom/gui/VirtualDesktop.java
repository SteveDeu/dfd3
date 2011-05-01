package com.bpn.diplom.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
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
	
	
	private final Logger log = Logger.getRootLogger();
		
	private final int WIDTH = 800;
	private final int HEIGTH = 600;
	private static VirtualDesktop instance;

	JDesktopPane desktop = new JDesktopPane(); 
	

	private VirtualDesktop(){
		super(Resource.getString(TITLE));
		log.debug("make main window: VirtualDesktop");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGTH);
		setJMenuBar(createMenuXML());
		JPanel content = createGUI(); 
		add(content);
		settingEvents();
		GUITools.centeringWindow(this);
		setVisible(true);
	}
	
	
	public static VirtualDesktop getInstance(){
		if(instance == null){
			instance = new VirtualDesktop();
		}	
		return VirtualDesktop.instance;
	}

	
	/** возвращает главную панель с
	* созданным расположением
	*/
	private JPanel createGUI() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel panelMain = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelDesktop = BoxLayoutUtils.createVerticalPanel();
		JPanel panelMore = BoxLayoutUtils.createVerticalPanel();

		int b = 3;
		panelMain.setBorder(BorderFactory.createEmptyBorder(b,b,b,b));
		panelDesktop.add(desktop);
		
		splitPane.setLeftComponent(panelDesktop);
		splitPane.setRightComponent(panelMore);
		splitPane.setDividerSize(10);
		splitPane.setDividerLocation(((Number)((this.getSize().getWidth()-(this.getSize().getWidth()/5)))).intValue());
		
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
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
				System.exit(0);
			}
		});
		
		
	}
	
	public static void main(String[] args) throws IOException{
		getInstance();
		
	}
	
	
}



