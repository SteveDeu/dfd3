package com.bpn.diplom.gui;

//import java.awt.*;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.bpn.diplom.gui.listeners.*;
import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.dao.*;

/**
 * Окно для входа в систему
 * 
 * @author pavel
 *
 */
public class DBManager extends JInternalFrame {
	private static final String TITLE 			= "DBManager.title";
	private static final String BUTTON_UPDATE 	= "buttons.update";
	private static final String BUTTON_DELETE 	= "buttons.delete";
	private static final String BUTTON_CANCEL 	= "buttons.cancel";

	private static final String DB_TABLE_H0 	= "db.table.h0";
	private static final String DB_TABLE_H1 	= "db.table.h1";
	private static final String DB_TABLE_H2 	= "db.table.h2";
	private static final String DB_TABLE_H3 	= "db.table.h3";
	
	
	private static final int SCROLL_W = 400;
	private static final int SCROLL_H = 300;
	
	
	
	JTable table;
	TableDataModel model;
//	JDesktopPane desktop;
	DAOLBP userManager = new DAOLBPImpl();
	
	private JScrollPane scrollTable;
	private JPanel panelTable;	
	private JButton btnUpdate;
	private JButton btnRemove;
	private JButton btnCancel;

	public DBManager() {
		super(Resource.getString(TITLE));
		VirtualDesktop.getInstance().getDesktop().add(this);
		JPanel content = createGUI(); 
		this.add(content);
		this.settingEvents();
		this.pack();
		GUITools.centeringWindow(this, this.getDesktopPane());
		this.fillTable();
		this.setVisible(true);
		this.setResizable(true);
	}
	
	/** возвращает главную панель с
	* созданным расположением
	*/
	private JPanel createGUI() {
		JPanel panelMain = BoxLayoutUtils.createVerticalPanel();
		JPanel panelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel panelButton = new JPanel(new GridLayout(1, 2, 5, 0));
		
		panelTable = new JPanel();
		panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.X_AXIS));
		panelTable.add(Box.createHorizontalStrut(12));
		scrollTable = new JScrollPane();
		
		panelMain.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		
		model = new TableDataModel(new String[]{Resource.getString(DB_TABLE_H0), Resource.getString(DB_TABLE_H1), Resource.getString(DB_TABLE_H2)}, 
								   new Class[] {String.class,                    String.class,                    String.class,                  });
		table = new JTable(model);
//		table.gett
		scrollTable.setViewportView(table);
		GUITools.setComponentSize(scrollTable, SCROLL_W, SCROLL_H);
		
		btnUpdate = new JButton(Resource.getString(BUTTON_UPDATE));
		btnRemove = new JButton(Resource.getString(BUTTON_DELETE));
		btnCancel = new JButton(Resource.getString(BUTTON_CANCEL));
		panelButton.add(btnUpdate);
		panelButton.add(btnRemove);
		panelButton.add(btnCancel);
		panelFlow.add(panelButton);

		BoxLayoutUtils.setGroupAlignmentX(new JComponent[] { scrollTable, panelMain, panelFlow }, Component.LEFT_ALIGNMENT);
		GUITools.createRecommendedMargin(new JButton[] { btnUpdate, btnRemove, btnCancel });

		panelMain.add(scrollTable);
		panelMain.add(Box.createVerticalStrut(12));
		panelMain.add(panelFlow);

		return panelMain;
	}

	
	private void settingEvents(){
		
		btnUpdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fillTable();
//				List l = (List) userManager.getUsers("Lt");
//				System.out.println("size: "+l.size());
//				System.out.println(l.get(0));
//				System.out.println(l.get(1));
			}
			
		});
		
		btnRemove.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
//				if(table.getSelectedRow() < 0)
//					return;
//				int count = userManager.removeUser((Integer) table.getValueAt(table.getSelectedRow(), 0));
//				if(count > 0)
//					fillTable();
				
				
				
//				class SBufferedImage extends BufferedImage implements Serializable{
//
//					public SBufferedImage(int width, 
//	                         int height, 
//	                         int imageType){
//						super(width, height, imageType);
//					}
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//					
//				}
				
				try {
					userManager.addUser(new EntityLBPUser(
							"new Super", new int[]{1,8}, new int[]{1,2, 33, 12}, new int[]{1,18}, ImageIO.read(new File("g:\\group.jpg"))));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
			
		});

		
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() < 2)
					return;
				ShowImageSimple show = new ShowImageSimple((Image)model.getValueAt(table.getSelectedRow(), 3));
				show.setTitle(Resource.getString("SimpleShowImage.user") + " " + model.getValueAt(table.getSelectedRow(), 1));
			}
		});
		
		
		this.btnCancel.addActionListener(new CloseActionListener());
		this.addKeyListener(new EscapeExitKeyListener());
		this.addInternalFrameListener(new WindowAdapterClose());
		
	}
	
	
	private void fillTable(){
		model.removeAllRows();
		for(final EntityLBPUser user : userManager.getAllUsers()){
//			JButton btnRender = new JButton("face"){
//				{
//					this.addActionListener(new ActionListener(){
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							new ShowImage(user.getImageFace());
//						}});
//				}
//			};
			model.addRow(
					new Object[]{
							user.getId(), user.getName(), user.getDateTimeAdd(), user.getImageFace() 
			});
		}
	}

	
	public static void main(String[] args){
		try {
			VirtualDesktop.main(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}



