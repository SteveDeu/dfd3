package com.bpn.diplom.processing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.bpn.diplom.gui.AbstractShowImage;
import com.bpn.diplom.gui.ShowImageSimple;
import com.bpn.diplom.gui.VirtualDesktop;
import com.bpn.diplom.gui.utils.GUITools;

public class FacesDetection {

	ObjectDetection detector = new ObjectDetection(6, 1);
	
	
	public List<Image> detect(final Image image, boolean forRecognize, Map<String, Image> imagesForShowProcessing){
		
		System.out.println("detect");
		final List<Image> result = new ArrayList<Image>();		
		
		Rectangle[] faces = detector.getFaceCoordinates((BufferedImage) image);
		if(faces.length == 0){
			return result;
		}
		int i = -1;
		for(Rectangle faceArea : faces){
			i++;
			BufferedImage faceImage = GUITools.extractImage((BufferedImage) image, faceArea);
			Graphics2D gFace = (Graphics2D)faceImage.getGraphics();
			
			imagesForShowProcessing.put("ќбласть зображенн€ в котр≥й знаходитьс€ обличч€", 
					GUITools.extractImage((BufferedImage) image, faceArea));
			
			BufferedImage imgForSearchEye =  new BufferedImage(faceImage.getWidth(), faceImage.getHeight()/2, BufferedImage.TYPE_INT_RGB);
			((Graphics2D)imgForSearchEye.getGraphics()).drawImage(faceImage, 0, 0, faceImage.getWidth(), faceImage.getHeight()/2, 
					 0, 0, faceImage.getWidth(), faceImage.getHeight()/2, null);
			Rectangle[] eyes = getEyePair(imgForSearchEye);
			if(eyes != null)
				for(Rectangle eyeArea : eyes){
					gFace.draw(eyeArea);
					gFace.drawLine(eyeArea.x, eyeArea.y, eyeArea.x + (int)eyeArea.getWidth(), eyeArea.y + (int)eyeArea.getHeight());
					gFace.drawLine(eyeArea.x + (int)eyeArea.getWidth(), eyeArea.y, eyeArea.x, eyeArea.y + (int)eyeArea.getHeight());
				}
			
			if(eyes != null && eyes.length == 2){
				Point le;
				Point re;
				if(Math.min(eyes[0].x, eyes[1].x) == eyes[0].x){
					le = GUITools.getCenterPoint(eyes[0]);
					re = GUITools.getCenterPoint(eyes[1]);
				}else{
					le = GUITools.getCenterPoint(eyes[1]);
					re = GUITools.getCenterPoint(eyes[0]);
				}
				faceImage = calibrationFaceOnEye(faceImage, le, re);
				gFace = (Graphics2D)faceImage.getGraphics();
				imagesForShowProcessing.put("ѕоворот област≥ ≥з зображенн€м обличч€ ор≥Їнтуючись по зрачкам",
						GUITools.extractImage(faceImage, new Rectangle(0, 0, faceImage.getWidth(), faceImage.getHeight())));
			}
			
			gFace.setColor(Color.BLUE);
			Rectangle eyePair = null;
			for(Rectangle ePair : detector.getEyeCoordinates((BufferedImage) faceImage, ObjectDetection.TYPE_EYE_BIG_PAIR)){//ObjectDetection.TYPE_EYE_SMALL_PAIR
				gFace.draw(ePair);
				eyePair = ePair;
			}

			gFace.setColor(Color.CYAN);
			Rectangle nose = null;
			BufferedImage imgForSearchNose =  new BufferedImage(faceImage.getWidth(), faceImage.getHeight()/2, BufferedImage.TYPE_INT_RGB);
			((Graphics2D)imgForSearchNose.getGraphics()).drawImage(faceImage, 0, 0, faceImage.getWidth(), faceImage.getHeight()/2, 
					 0, (int)(faceImage.getHeight()*0.25), faceImage.getWidth(), (int)(faceImage.getHeight()*0.75), null);
			for(Rectangle ns :detector.getNoseCoordinates(imgForSearchNose)){
				nose = new Rectangle(ns.x, ns.y + (int)(faceImage.getHeight()*0.25), ns.width, ns.height);
				gFace.draw(nose);
			}
			
			
			gFace.setColor(Color.RED);
			Rectangle face = null;
			if(eyePair != null)
				if(nose != null && !nose.intersects(eyePair)){
					face = new Rectangle(eyePair.x, eyePair.y, (int)eyePair.getWidth(), (int)(nose.y - eyePair.y + nose.getHeight() + eyePair.getHeight()));
					gFace.draw(face);
				} else{
					face = new Rectangle(eyePair.x, eyePair.y, (int)eyePair.getWidth(), (int)(eyePair.getHeight()*3.5));
					gFace.draw(face);
				}
			else
				continue;
			
			if(forRecognize){
//				Rectangle resFace = new Rectangle(face.x + faceArea.x, face.y + faceArea.y, face.width, face.height);
//				Rectangle resFace = new Rectangle(face.x + faceArea.x, face.y + faceArea.y, face.width, face.height);
				BufferedImage resultFace = GUITools.extractImage(faceImage, face);
				result.add(resultFace);
				imagesForShowProcessing.put("¬ид≥лене ≥з вх≥дного зображенн€ обличч€",GUITools.extractImage(faceImage, face));
			} else {
				result.add(faceImage); 
			}
//			gFace.setColor(Color.YELLOW);
//			for(Rectangle mouth :detector.getMouthCoordinates((BufferedImage) faceImage)){
//				gFace.setColor(Color.YELLOW);
//				gFace.draw(mouth);
//				gFace.draw(new Rectangle(mouth.x, mouth.y, (int)mouth.getWidth(), (int)mouth.getWidth()));
////				setForDraw.add(mouth);
////				setForDraw.add(new Rectangle(mouth.x, mouth.y, (int)mouth.getWidth(), (int)mouth.getWidth()));
//			}
			
		}
		
		return result;
	}
	
	private BufferedImage calibrationFaceOnEye(BufferedImage face, Point eyeL, Point eyeR){
		double hCatet = eyeR.getX() - eyeL.getX();
		double vCatet = eyeL.getY() - eyeR.getY();
		double anguleForRotate = Math.atan(vCatet/hCatet);
		System.out.println("Rotate FORRRRRR : "+anguleForRotate);
		return GUITools.rotateImageCenter(face, anguleForRotate);
	}

	
	private Rectangle[] getEyePair(BufferedImage faceImage){
		System.out.println("<<< height in getEyePair: "+faceImage.getHeight());
		
		List<Rectangle> eyes = new ArrayList<Rectangle>();
		
		Rectangle[] eyeAreas = null;
		
		eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE);
		if(eyeAreas.length == 2 && !eyeAreas[0].intersects(eyeAreas[1])){
			System.out.println("======STANDART");
			return eyeAreas; 
		}
		
		eyeAreas = detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_GLASSES);
		if(eyeAreas.length == 2 && !eyeAreas[0].intersects(eyeAreas[1])){
			System.out.println("======GLASSES");
			return eyeAreas; 
		}		
		
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_LEFT)));
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_RIGHT)));
		if(eyes.size() == 2 && !eyes.get(0).intersects(eyes.get(1))){
			System.out.println("======L & R");
			return new Rectangle[]{ (Rectangle) eyes.toArray()[0], (Rectangle) eyes.toArray()[1]};
		}
		
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_MCS_LEFT)));
		eyes.addAll(Arrays.asList(detector.getEyeCoordinates(faceImage, ObjectDetection.TYPE_EYE_MCS_RIGHT)));
		if(eyes.size() == 2 && !eyes.get(0).intersects(eyes.get(1))){
			System.out.println("======MCS L & R");
			return new Rectangle[]{ (Rectangle) eyes.toArray()[0], (Rectangle) eyes.toArray()[1]};
		}
		return null;
	}
	
	
	
}
