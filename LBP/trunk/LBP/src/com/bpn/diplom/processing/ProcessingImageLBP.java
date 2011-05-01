package com.bpn.diplom.processing;

import com.bpn.diplom.lbp.*;
import com.bpn.diplom.video.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import com.googlecode.javacv.cpp.opencv_core.*;


public class ProcessingImageLBP implements ProcessingImage{
	
//	Dimension videoSize = new Dimension(352, 288);
//	Dimension focusSize = new Dimension(80, 100);
//	Point focus = new Point((int)(videoSize.getWidth() - focusSize.getWidth())/2, 
//								   (int)(videoSize.getHeight() - focusSize.getHeight())/2);
	
	int countBlock = 7; 
	final LBPImage faceVectorBuilder = new LBPImage(LBPImage.AROUND_16_POINTS, countBlock);
	int[] storedFaceVector;
	final VideoStreamCV wcamCV = new VideoStreamCV(this);
	final FaceDetection faceDetector = new FaceDetection();

	int[] faceVector = null;
	BufferedImage frame = null;
	Rectangle face = null;

	int stepSize = 3;
	int step;
	long minDistance;
	long maxDistance;
	long currentDistance;

	
	
	public static void main(String [] args){
		
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				ProcessingImageLBP instanse = new ProcessingImageLBP();
				instanse.init();
				
			}
			
		}).start();
		
		
	}
	
	
	private void init(){
		JFrame window = wcamCV.getWindow();
		window.setBounds(20, 80, 300, 300);
		
		Container c = window.getContentPane();
		Box row = Box.createHorizontalBox();
		JButton btn = new JButton("Remember face");
		JButton btnClear = new JButton("Clear");
//		btn.setFont(new Font("Tahoma", 1, 12));
		row.add(btn);
		row.add(btnClear);
		c.add(row, BorderLayout.SOUTH);
		
//		frame.setSize(1000,1000);
		
		
		
		
		btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				minDistance = Long.MAX_VALUE;
				maxDistance = 0;
				if(face != null)
					storedFaceVector = preprocessingCalculation();
			}
		});
		
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				minDistance = Long.MAX_VALUE;
				maxDistance = 0;
			}
		});
		
		wcamCV.showVideoProcessing();
	}

	
	@Override
	public BufferedImage processingImage(BufferedImage image){
		return processingImage(IplImage.createFrom(image)).getBufferedImage();
	}	
	
	@Override
	public IplImage processingImage(IplImage image) {
		IplImage result = null;
		try{
			Rectangle[] faces = faceDetector.getFaceCoordinates(image);
			
			frame = image.getBufferedImage();
			Graphics2D g = (Graphics2D) frame.getGraphics();
			drawPanel(g);
			drawFacesGrid(faces, g);
	
			
			if(faces.length > 0){
				face = faces[0];
				
				faceVector = preprocessingCalculation(); 
				preprocessingViews(); 
				
				if(storedFaceVector != null)
					currentDistance = MatcherPirsonX2.getDistanceX2(storedFaceVector, faceVector, faceVectorBuilder);		
				if(currentDistance < minDistance)
					minDistance = currentDistance;
				if(currentDistance > maxDistance)
					maxDistance = currentDistance;
			} else{
				System.out.println(" not find");
			}
			
			
			result = IplImage.createFrom(frame);
			
		}catch(Throwable c){
			c.printStackTrace();
		}
		return result;
	}
	
	
	private void drawFacesGrid(Rectangle[] faces, Graphics2D g){
		for(Rectangle face : faces){
			int difX = face.width / countBlock;
			int difY = face.height / countBlock;
			for(int i = 0; i < countBlock; i++){
				g.drawLine(face.x, face.y + difY * i, face.x + face.width, face.y + difY * i);
				g.drawLine(face.x + difX * i, face.y, face.x + difX * i, face.y + face.height);
			}
			g.draw(face);	
		}
	}
	
	
	private void drawPriority(Graphics2D g){
		int difX = face.width / countBlock;
		int difY = face.height / countBlock;
		Color old = g.getColor();
		g.setColor(Color.RED);
		for(int i = 0; i < countBlock*countBlock; i++){
			g.drawString(String.valueOf(LBPImage.PRIORITY_7_BLOCKS[i]), face.x + difX/2 + difX*(i%countBlock), face.y + difY/2 + difY*(i/countBlock));
		}
		g.setColor(old);
	}
	
	
	private void drawPanel(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawString("    min: " + minDistance, 0, 20);
		g.drawString("    max: " + maxDistance, 0, 40);
		g.drawString("current: " + currentDistance, 0, 80);
		if(storedFaceVector != null)
			g.drawString("lngth: " + storedFaceVector.length, 0, 100);
//		g.drawRect((int)focus.getX(), (int)focus.getY(), (int)focusSize.getWidth(), (int)focusSize.getHeight());
	}


	/**
	 * возвращает вектор признаков лица
	 * @return
	 */
	private synchronized int[] preprocessingCalculation(){
		BufferedImage b = new BufferedImage(face.width,face.height, 5);
//		BufferedImage b = new BufferedImage(640,532, 5);
		b.setData(frame.getData(face));
		frame.getSubimage(face.x, face.y, face.width, face.height).copyData(b.getRaster());
		BufferedImage faceRaster = Util.resizeImage(b, null);
//		b.setData(faceRaster.getData());
//		faceRaster.copyData(b.getRaster());
		
//		Graphics2D g = (Graphics2D) frame.getGraphics();
//		g.drawImage(faceRaster, null, 0, 150);
		return faceVectorBuilder.getFaceVectorByRaster(faceRaster.getData());
	} 
	
	
	
	private void preprocessingViews(){
		Graphics2D g = (Graphics2D) frame.getGraphics();
		Util.imgToGray(frame.getRaster(), face);
		drawPriority(g);
	}

	
	
}
