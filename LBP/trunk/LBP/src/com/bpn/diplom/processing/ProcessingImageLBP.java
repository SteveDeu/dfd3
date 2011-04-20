package com.bpn.diplom.processing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.bpn.diplom.lbp.LBPImage;
import com.bpn.diplom.lbp.MatcherPirsonX2;
import com.bpn.diplom.video.DisplayWebcamVideo;


public class ProcessingImageLBP implements ProcessingImage{
	
	static int stepSize = 3;
	static int step;
	
	static int[] storedFaceVector;
	static long minDistance;
	static long maxDistance;
	static long currentDistance;
	
	static Dimension videoSize = new Dimension(320, 240);
	static Dimension focusSize = new Dimension(80, 100);
	static Point focus = new Point((int)(videoSize.getWidth() - focusSize.getWidth())/2, 
								   (int)(videoSize.getHeight() - focusSize.getHeight())/2);
	
	static final LBPImage faceVectorBuilder = new LBPImage(LBPImage.AROUND_16_POINTS, 7);
	static ProcessingImage processing = new ProcessingImageLBP();
	static final DisplayWebcamVideo wcam = new DisplayWebcamVideo(0, videoSize, processing);
	
	public static void main(String [] args){
		
		JFrame frame = wcam.getmScreen();
		frame.setBounds(20, 80, 300, 300);
		
		Container c = frame.getContentPane();
//		c.setLayout(new BorderLayout());
		Box row = Box.createHorizontalBox();
		JButton btn = new JButton("Зафиксировать");
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
				storedFaceVector = faceVectorBuilder.getFaceVectorByRaster(
						wcam.getCurrentImage().getData().createChild((int)focus.getX(), (int)focus.getY(), 
																	 (int)focusSize.getWidth(), (int)focusSize.getHeight(),0, 0, null));
			}
		});
		
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				minDistance = Long.MAX_VALUE;
				maxDistance = 0;
			}
		});
		
		wcam.showVideoProcessing();
	}

	
	
	@Override
	public void processingImage(BufferedImage image) {
		WritableRaster r =wcam.getCurrentImage().getRaster(); 
		imgToGray2(r);

		if(step == stepSize){
			int [] faceVector = faceVectorBuilder.getFaceVectorByRaster(wcam.getCurrentImage().getData().createChild((int)focus.getX(), (int)focus.getY(), (int)focusSize.getWidth(), (int)focusSize.getHeight(),0, 0, null));
			if(storedFaceVector != null)
				currentDistance = MatcherPirsonX2.getDistanceX2(storedFaceVector, faceVector, faceVectorBuilder);		
			if(currentDistance < minDistance)
				minDistance = currentDistance;
			if(currentDistance > maxDistance)
				maxDistance = currentDistance;
			step = 0;
		} else
			step++;
		
		drawPanel((Graphics2D) image.getGraphics());
	}
	
	
	
	private void drawPanel(Graphics2D g) {
//		BufferedImageOp
//		AffineTransformOp, ConvolveOp, ColorConvertOp, RescaleOp, LookupOp;
		g.setColor(Color.RED);
		g.drawString("    min: " + minDistance, 0, 20);
		g.drawString("    max: " + maxDistance, 0, 40);
		g.drawString("current: " + currentDistance, 0, 80);
		if(storedFaceVector != null)
			g.drawString("lngth: " + storedFaceVector.length, 0, 100);
		g.drawRect((int)focus.getX(), (int)focus.getY(), (int)focusSize.getWidth(), (int)focusSize.getHeight());
		

//		g.drawImage(r, null, 0, 0);
	}

	private void imgToGray2(WritableRaster wr){
		for(int y = (int)focus.getY(); y < (int)(focusSize.getHeight() + focus.getY()); y++){
			for(int x = (int)focus.getX(); x < (int)(focusSize.getWidth()+focus.getX()); x++){
				int r = wr.getSample(x, y, 0);
				int g = wr.getSample(x, y, 1);
				int b = wr.getSample(x, y, 2);
				
				int n = (r+g+b)/3;
				wr.setSample(x, y, 0, n);
				wr.setSample(x, y, 1, n);
				wr.setSample(x, y, 2, n);
			}
		}
		
	}
	
	private void imgToGray(WritableRaster wr){
		System.out.println("start");
		for(int y = 0; y < wr.getHeight(); y++){
			System.out.println("y: "+y);
			for(int x = 0; x < wr.getWidth(); x++){
				System.out.println("x: "+x);
				int r = wr.getSample(x, y, 0);
				int g = wr.getSample(x, y, 1);
				int b = wr.getSample(x, y, 2);
				
				int n = Math.min(Math.min(r,g),b);
				wr.setSample(x, y, 0, n);
				wr.setSample(x, y, 1, n);
				wr.setSample(x, y, 2, n);
			}
		}
		
	}
	
}
