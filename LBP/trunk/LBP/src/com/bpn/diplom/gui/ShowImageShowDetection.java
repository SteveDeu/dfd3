package com.bpn.diplom.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;

import com.bpn.diplom.gui.utils.*;
import com.bpn.diplom.processing.ObjectDetection;

public class ShowImageShowDetection extends AbstractShowImage{


	JButton btnDetection;
	
	ObjectDetection detector = new ObjectDetection(3,1);
	
	public ShowImageShowDetection(Image image) {
		super(image);
	}

	public ShowImageShowDetection(String name, Image src) {
		super(name, src);	
	}

	@Override
	protected void init() {
		btnDetection = new JButton(Resource.getString("buttons.detect"));
		buttons = new JButton[]{btnDetection};
	}

	@Override
	protected void settingEvents() {
		btnDetection.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				detect();
				setImage(image);
			}});
		
	}


	private void detect(){
		System.out.println("detect");
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setColor(Color.RED);
		
		List<AbstractShowImage> facesWindow = new ArrayList<AbstractShowImage>();
		for(Rectangle faceArea :detector.getFaceCoordinates((BufferedImage) image)){
			BufferedImage faceImage = new BufferedImage((int)faceArea.getWidth(), (int)faceArea.getHeight(), BufferedImage.TYPE_INT_RGB); 
			((Graphics2D)faceImage.getGraphics()).drawImage(image, 0, 0, (int)faceArea.getWidth(), (int)faceArea.getHeight(), 
					(int)faceArea.x, (int)faceArea.y, (int)(faceArea.x + faceArea.getWidth()), (int)(faceArea.y + faceArea.getHeight()), null);
			
			Set<Shape> setForDraw = new HashSet<Shape>();
			
//			BufferedImage faceImageView = new BufferedImage((int)faceArea.getWidth(), (int)faceArea.getHeight(), BufferedImage.TYPE_INT_RGB); 
//			((Graphics2D)faceImageView.getGraphics()).drawImage(image, 0, 0, (int)faceArea.getWidth(), (int)faceArea.getHeight(), 
//					(int)faceArea.x, (int)faceArea.y, (int)(faceArea.x + faceArea.getWidth()), (int)(faceArea.y + faceArea.getHeight()), null);
			
//			Rectangle[] eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE);
//			List<Rectangle> listEye = new ArrayList<Rectangle>();
//			listEye.addAll(Arrays.asList(eyeAreas));
//			if(eyeAreas.length < 2){
//				System.out.println("******TRY GLASSES");
//				eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_GLASSES);
//				listEye.addAll(Arrays.asList(eyeAreas));
//				if(eyeAreas.length < 2){
//					System.out.println("******TRY LEFT");
//					eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_LEFT);
//					listEye.addAll(Arrays.asList(eyeAreas));
//				}
//			}
//			
//			if(eyeAreas.length < 2 && listEye.size() > 1){
//				Rectangle firstEye = listEye.get(0);
//				System.out.println("TRY IN LIST for count eye: "+listEye.size());
//				for(int i = 1; i < listEye.size(); i++){
//					System.out.println("TRY IN LIST for eye#"+i);
//					if(!firstEye.intersects(listEye.get(i))){
//						System.out.println("TRY IN LIST succesfullll!!!!!!!!!!!!!! in eye: "+i);
//						eyeAreas = new Rectangle[]{firstEye, listEye.get(i)};
//						break;
//					}
//						
//				}
//			}
//				
//			for(Rectangle eyeArea : eyeAreas){
//				System.out.println("eye areas : "+eyeArea);
////				Graphics2D gFace = (Graphics2D)faceImage.getGraphics();
////				gFace.setColor(Color.RED);
////				gFace.draw(eyeArea);
////				setForDraw.add(eyeArea);
////				gFace.drawLine(eyeArea.x, eyeArea.y, eyeArea.x + (int)eyeArea.getWidth(), eyeArea.y + (int)eyeArea.getHeight());
////				gFace.drawLine(eyeArea.x + (int)eyeArea.getWidth(), eyeArea.y, eyeArea.x, eyeArea.y + (int)eyeArea.getHeight());
//			}
			
			if(eyeAreas.length == 2){
				Point le;
				Point re;
				if(Math.min(eyeAreas[0].x, eyeAreas[1].x) == eyeAreas[0].x){
					le = new Point(eyeAreas[0].x, eyeAreas[0].y);
					re = new Point(eyeAreas[1].x, eyeAreas[1].y);
				}else{
					le = new Point(eyeAreas[1].x, eyeAreas[1].y);
					re = new Point(eyeAreas[0].x, eyeAreas[0].y);
				}
				faceImage = calibrationFaceOnEye(faceImage, le, re);
			}
			
			
			Graphics2D gFace = (Graphics2D)faceImage.getGraphics();
			
			gFace.setColor(Color.BLUE);
			for(Rectangle eyePair :detector.getEyeCoordinates((BufferedImage) faceImage, ObjectDetection.TYPE_EYE_BIG_PAIR)){
//				gFace.draw(eyePair);
//				gFace.draw(new Rectangle(eyePair.x, eyePair.y, (int)eyePair.getWidth(), (int)eyePair.getWidth()));
				setForDraw.add(eyePair);
				setForDraw.add(new Rectangle(eyePair.x, eyePair.y, (int)eyePair.getWidth(), (int)eyePair.getWidth()));
			}
			gFace.setColor(Color.CYAN);
			for(Rectangle nose :detector.getNoseCoordinates((BufferedImage) faceImage)){
//				gFace.draw(nose);
//				gFace.draw(new Rectangle(nose.x, nose.y, (int)nose.getWidth(), (int)nose.getWidth()));
				setForDraw.add(nose);
				setForDraw.add(new Rectangle(nose.x, nose.y, (int)nose.getWidth(), (int)nose.getWidth()));
			}
//			gFace.setColor(Color.YELLOW);
//			for(Rectangle mouth :detector.getMouthCoordinates((BufferedImage) faceImage)){
////				gFace.draw(mouth);
////				gFace.draw(new Rectangle(mouth.x, mouth.y, (int)mouth.getWidth(), (int)mouth.getWidth()));
//				setForDraw.add(mouth);
//				setForDraw.add(new Rectangle(mouth.x, mouth.y, (int)mouth.getWidth(), (int)mouth.getWidth()));
//			}
			
			
			for(Shape shape : setForDraw){
				((Graphics2D)faceImage.getGraphics()).draw(shape);	
			}
			
			AbstractShowImage window = new  ShowImageSimple("Виділене обличчя", faceImage);
			if(facesWindow.size() == 0)
				window.setLocation(0, 0);
			else{
				AbstractShowImage last = facesWindow.get(facesWindow.size() - 1);
				window.setLocation(last.getX() + last.getWidth(), last.getY());
				if((window.getLocation().getX() + window.getWidth()) > window.getParent().getWidth())
					window.setLocation(0, last.getY()+200);
			}
			facesWindow.add(window);
		}
	}
	
	private BufferedImage calibrationFaceOnEye(BufferedImage face, Point eyeL, Point eyeR){
		double hCatet = eyeR.getX() - eyeL.getX();
		double vCatet = eyeL.getY() - eyeR.getY();
		double anguleForRotate = Math.atan(vCatet/hCatet);
		System.out.println("Rotate FORRRRRR : "+anguleForRotate);
		return GUITools.rotateImageCenter(face, anguleForRotate);
	}

	
	private Rectangle[] getEyePair(BufferedImage faceImage){
		List<Rectangle> eyes = new ArrayList<Rectangle>();
		
		Rectangle[] eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE);
		if(eyeAreas.length == 2)
			return eyeAreas; 

		eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_GLASSES);
		if(eyeAreas.length == 2)
			return eyeAreas; 
		
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_LEFT)));
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_RIGHT)));
		if(eyes.size() == 2)
			return (Rectangle[]) eyes.toArray();
		
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_MCS_LEFT)));
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_MCS_RIGHT)));
		if(eyes.size() == 2)
			return (Rectangle[]) eyes.toArray();
		
		return null;
	}
	
}
