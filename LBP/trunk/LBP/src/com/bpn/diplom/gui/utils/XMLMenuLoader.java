

package com.bpn.diplom.gui.utils;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import java.io.*;
import javax.xml.parsers.*;
import java.awt.event.*;
import java.util.*;



/**пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ пїЅпїЅ пїЅпїЅпїЅпїЅпїЅ XML*/
public class XMLMenuLoader {
	
	
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ	 */
	private JMenuBar currentMenuBar;
	/** пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
	private LinkedList menus = new LinkedList();
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
	private Map menuStorage = new HashMap();

	
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ XML*/
	private InputSource source;
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ XML*/
	private DefaultHandler documentHandler;
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ XML*/
	private SAXParser parser;

	

	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ, пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅ пїЅпїЅпїЅпїЅ*/
	public XMLMenuLoader(InputStream stream) {
		// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ XML
		try {
			Reader reader = new InputStreamReader(stream);
			source = new InputSource(reader);
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		// пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ XML
		documentHandler = new XMLParser();
	}

	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ XML пїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
	public void parse() throws Exception {
		parser.parse(source, documentHandler);
	}

	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
	public JMenuBar getMenuBar(String name) {
	return (JMenuBar) menuStorage.get(name);
	}
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
	public JMenu getMenu(String name) {
	return (JMenu) menuStorage.get(name);
	}
	/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
	public JMenuItem getMenuItem(String name) {
	return (JMenuItem) menuStorage.get(name);
	}

	/** пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ*/
	public void addActionListener(String name, ActionListener listener) {
		getMenuItem(name).addActionListener(listener);
	}

	/** 
	 * 
	 * пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ XML 
	 * 
	 * */
	public class XMLParser extends DefaultHandler {
		public static final String ELEMENT_MENU_BAR="menubar";
		public static final String ELEMENT_MENU="menu";
		public static final String ELEMENT_MENU_ITEM="menuitem";
		public static final String ATTRIBUTE_NAME="name";
		public static final String ATTRIBUTE_NAME_SEPARATOR="separator";
		public static final String ATTRIBUTE_TEXT="text";
		public static final String ATTRIBUTE_MNEMONIC="mnemonic";
		public static final String ATTRIBUTE_ACCELERATOR="accelerator";
		public static final String ATTRIBUTE_ENABLED="enabled";
		public static final String ATTRIBUTE_CLASS="class";
		
		/**  пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ XML*/
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			if (qName.equals(ELEMENT_MENU_BAR))
				parseMenuBar(attributes);
			else if (qName.equals(ELEMENT_MENU))
				parseMenu(attributes);
			else if (qName.equals(ELEMENT_MENU_ITEM))
				parseMenuItem(attributes);
		}

		/** пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ, пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
		public void endElement(String uri, String localName, String qName) {
			if (qName.equals(ELEMENT_MENU))
				menus.removeFirst();
		}

		/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
		protected void parseMenuBar(Attributes attrs) {
			JMenuBar menuBar = new JMenuBar();
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ
			String name = attrs.getValue(ATTRIBUTE_NAME);
			menuStorage.put(name, menuBar);
			currentMenuBar = menuBar;
		}

		/** пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
		protected void parseMenu(Attributes attrs) {
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			JMenu menu = new JMenu();
			String name = attrs.getValue(ATTRIBUTE_NAME);
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
			adjustProperties(menu, attrs);
			menuStorage.put(name, menu);
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ пїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
			// пїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			if (menus.size() != 0) {
				((JMenu) menus.getFirst()).add(menu);
			} else {
				currentMenuBar.add(menu);
			}
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			menus.addFirst(menu);
		}

		/** пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
		protected void parseMenuItem(Attributes attrs) {
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ, пїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅ пїЅпїЅпїЅ
			String name = attrs.getValue(ATTRIBUTE_NAME);
			if (name.equals(ATTRIBUTE_NAME_SEPARATOR)) {
				((JMenu) menus.getFirst()).addSeparator();
				return;
			}
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			JMenuItem menuItem = new JMenuItem();
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
			adjustProperties(menuItem, attrs);
			menuStorage.put(name, menuItem);
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			((JMenu) menus.getFirst()).add(menuItem);
		}
		
		/**  пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ*/
		private void adjustProperties(JMenuItem menuItem, Attributes attrs) {
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
			String text = attrs.getValue(ATTRIBUTE_TEXT);
			String mnemonic = attrs.getValue(ATTRIBUTE_MNEMONIC);
			String accelerator = attrs.getValue(ATTRIBUTE_ACCELERATOR);
			String enabled = attrs.getValue(ATTRIBUTE_ENABLED);
			String listener = attrs.getValue(ATTRIBUTE_CLASS);
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			menuItem.setText(text);
			if (mnemonic != null) {
				menuItem.setMnemonic(mnemonic.charAt(0));
			}
			if (accelerator != null) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator.charAt(0), KeyEvent.CTRL_MASK));
			}
			if (enabled != null) {
				boolean isEnabled = true;
				if (enabled.equals("false")){
					isEnabled = false;
				}else{
					if(listener != null){
						
						Class listenerClass;
						try {
							listenerClass = Class.forName(listener);
							menuItem.addActionListener((ActionListener) listenerClass.newInstance());
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							Logger.getRootLogger().error("пїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ", e);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							Logger.getRootLogger().error("пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅ class.newInstance()", e);
						} catch (InstantiationException e) {
							e.printStackTrace();
							Logger.getRootLogger().error("пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅ java.lang.InstantiationException", e);
						}

					}
				}
				menuItem.setEnabled(isEnabled);
			}
		}
	}
}
