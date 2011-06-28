package com.bpn.diplom.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JButton;

import com.bpn.diplom.gui.utils.Resource;

public class ShowImageAuth extends AbstractShowImage{

	
	JButton btnNext;
	JButton btnBack;
	
	Object[] keys;
	TreeMap<String, Image> mapShow;
	 
	int current = -1;
	
	
	public ShowImageAuth() {
		super();
	}
	
	public ShowImageAuth(String title, BufferedImage image, TreeMap<String, Image> map) {
		super(title, image);
		mapShow = map;
		keys = map.navigableKeySet().toArray();
	}

	public ShowImageAuth(Image image) {
		super(image);
	}

	@Override
	protected void init() {
		btnNext = new JButton("Далі");
		btnBack = new JButton("Назад");
		buttons = new JButton[]{btnNext, btnBack};
	}

	@Override
	protected void settingEvents() {
		btnNext.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}});
		
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				back();
			}});

	}


	private void next(){
		change(1);
	}
	
	private void back(){
		change(-1);
	}
	
	private void change(int delta){
		
		if(current + delta >= 0 && current + delta < keys.length){
			current += delta;
			setTitle(((String)(keys[current])).substring(1));
			setImage(mapShow.get((String)(keys[current])));
		}
	}
	
}
