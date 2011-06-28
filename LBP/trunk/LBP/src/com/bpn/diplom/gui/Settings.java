package com.bpn.diplom.gui;

//import java.awt.*;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.bpn.diplom.dao.Connect;
import com.bpn.diplom.gui.listeners.CloseActionListener;
import com.bpn.diplom.gui.listeners.EscapeExitKeyListener;
import com.bpn.diplom.gui.listeners.WindowAdapterClose;
import com.bpn.diplom.gui.utils.BoxLayoutUtils;
import com.bpn.diplom.gui.utils.GUITools;
import com.bpn.diplom.gui.utils.Resource;

/**
 * Окно для входа в систему
 * 
 * @author pavel
 *
 */
public class Settings extends JInternalFrame {
	
	private static final String TITLE 			= "DBManager.title";
	private static final String BUTTON_SAVE_EXIT= "buttons.saveExit";
	private static final String BUTTON_CANCEL 	= "buttons.cancel";

//	
//	private static final int SCROLL_W = 400;
//	private static final int SCROLL_H = 300;
	
	
	private JButton btnSaveExit;
	private JButton btnCancel;
	
	private JTextField dbLogin;
	private JTextField dbPass;
	private JTextField dbDriver;
	private JTextField dbProtocol;
	private JTextField dbIp;
	private JTextField dbName;
	private JTextField dbPort;

	
	
	public Settings() {
		super(Resource.getString(TITLE));
		VirtualDesktop.getInstance().getDesktop().add(this);
		JPanel content = createGUI(); 
		this.add(content);
		this.settingEvents();
		this.setDefault();
		this.pack();
		GUITools.centeringWindow(this, this.getDesktopPane());
		this.setIconifiable(true);
		this.setVisible(true);
		this.setResizable(true);
	}
	
	//JDBCDriver=com.mysql.jdbc.Driver
	//DBName=lbp
	//protocol=jdbc:mysql
	//IPAdress=localhost
	//port=3306
	//user=root
	//pass=
	
	/** возвращает главную панель с
	* созданным расположением
	*/
	private JPanel createGUI() {
		JPanel panelMain = BoxLayoutUtils.createVerticalPanel();
		JPanel panelLogin = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelPassword = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelProtocol = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelDriver = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelIp = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelPort = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelDBName = BoxLayoutUtils.createHorizontalPanel();
		JPanel panelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel panelButton = new JPanel(new GridLayout(1, 2, 5, 0));

		panelMain.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		
		dbLogin 	= new JTextField(20);
		dbPass 		= new JTextField(20);
		dbDriver 	= new JTextField(20);
		dbProtocol 	= new JTextField(20);
		dbIp 		= new JTextField(20);
		dbPort 		= new JTextField(20);
		dbName 		= new JTextField(20);
		
		
		JLabel labelLogin = new JLabel("db login: ");
		JLabel labelPass = new JLabel("db password: ");
		JLabel labelDriver = new JLabel("db driver: ");
		JLabel labelProtocol = new JLabel("db protocol: ");
		JLabel labelIp = new JLabel("db IP: ");
		JLabel labelPort = new JLabel("db port: ");
		JLabel labelDBName = new JLabel("db name: ");
		
		panelLogin.add(labelLogin);
		panelLogin.add(Box.createHorizontalStrut(12));
		panelLogin.add(dbLogin);

		panelPassword.add(labelPass);
		panelPassword.add(Box.createHorizontalStrut(12));
		panelPassword.add(dbPass);

		panelDriver.add(labelDriver);
		panelDriver.add(Box.createHorizontalStrut(12));
		panelDriver.add(dbDriver);

		panelProtocol.add(labelProtocol);
		panelProtocol.add(Box.createHorizontalStrut(12));
		panelProtocol.add(dbProtocol);

		panelIp.add(labelIp);
		panelIp.add(Box.createHorizontalStrut(12));
		panelIp.add(dbIp);

		panelPort.add(labelPort);
		panelPort.add(Box.createHorizontalStrut(12));
		panelPort.add(dbPort);

		panelDBName.add(labelDBName);
		panelDBName.add(Box.createHorizontalStrut(12));
		panelDBName.add(dbName);

		
		btnSaveExit = new JButton(Resource.getString(BUTTON_SAVE_EXIT));
		btnCancel = new JButton(Resource.getString(BUTTON_CANCEL));
		panelButton.add(btnSaveExit);
		panelButton.add(btnCancel);
		panelFlow.add(panelButton);

		BoxLayoutUtils.setGroupAlignmentX(new JComponent[] { panelLogin, panelPassword, panelDriver, panelProtocol, panelIp, panelDBName, panelPort, panelMain, panelFlow }, Component.LEFT_ALIGNMENT);
//		ArrayList<JTextField> textFields = new ArrayList<JTextField>();
//		ArrayList<JLabel> textLabels = new ArrayList<JLabel>();
//		for(Field field : Settings.class.getFields()){
//			if(field.getType() == JTextField.class)
//				textFields.add(field.get)
//		}
		BoxLayoutUtils.setGroupAlignmentY(new JComponent[] { dbLogin, dbPass, dbProtocol, dbDriver, dbIp, dbName, dbPort, 
															 labelLogin, labelPass, labelProtocol, labelDriver, labelIp, labelDBName, labelPort }, 
										  Component.CENTER_ALIGNMENT);
		GUITools.makeSameSize(new JComponent[] { labelLogin, labelPass , labelProtocol, labelDriver, labelIp, labelDBName, labelPort});
		GUITools.createRecommendedMargin(new JButton[] { btnSaveExit, btnCancel});
		GUITools.fixTextFieldSize(new JTextField[]{dbLogin, dbPass, dbProtocol, dbDriver, dbIp, dbName, dbPort});
		panelMain.add(panelLogin);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelPassword);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelDriver);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelProtocol);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelIp);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelDBName);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelPort);
		panelMain.add(Box.createVerticalStrut(17));
		panelMain.add(panelFlow);

		return panelMain;
	}

	
	private void settingEvents(){
		
		btnSaveExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.this.saveSettings();
				Settings.this.dispose();
				VirtualDesktop.getInstance().getDesktop().remove(Settings.this);
			}
			
		});
		
		this.btnCancel.addActionListener(new CloseActionListener());
		this.addKeyListener(new EscapeExitKeyListener());
		this.addInternalFrameListener(new WindowAdapterClose());
		
	}
	
	
	private void saveSettings(){
		Connect.user 		= dbLogin.getText();
		Connect.pass 		= dbPass.getText();
		Connect.DBName 		= dbName.getText();
		Connect.JDBCDriver 	= dbDriver.getText();
		Connect.protocol 	= dbProtocol.getText();
		Connect.IPAdress 	= dbIp.getText();
		Connect.port	 	= dbPort.getText();
		
		Resource.setConfiguration(Resource.JDBC_DRIVER, Connect.JDBCDriver);
		Resource.setConfiguration(Resource.PROTOCOL, Connect.protocol);
		Resource.setConfiguration(Resource.USER, Connect.user);
		Resource.setConfiguration(Resource.PASS, Connect.pass);
		Resource.setConfiguration(Resource.IP_ADRESS, Connect.IPAdress);
		Resource.setConfiguration(Resource.PORT, Connect.port);
		Resource.setConfiguration(Resource.DB_NAME, Connect.DBName);
	}
	
	private void setDefault(){
		dbLogin.setText(Connect.user);
		dbPass.setText(Connect.pass);
		dbProtocol.setText(Connect.protocol);
		dbDriver.setText(Connect.JDBCDriver);
		dbIp.setText(Connect.IPAdress);
		dbName.setText(Connect.DBName);
		dbPort.setText(Connect.port);
	}
	
}



