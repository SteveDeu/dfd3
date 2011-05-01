package com.bpn.diplom.processing;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Util {

	
	public static void imgToGray(WritableRaster wr, Rectangle area){
		imgToGray(wr, new Point(area.x, area.y), area.width, area.height);
	}
	
	public static void imgToGray(WritableRaster wr, Point from, int width, int height){
		for(int y = (int)from.getY(); y < (int)(height + from.getY()); y++){
			for(int x = (int)from.getX(); x < (int)(width + from.getX()); x++){
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

	
	
	
	public static void imgToGray(WritableRaster wr){
		System.out.println("start");
		for(int y = 0; y < wr.getHeight(); y++){
//			System.out.println("y: "+y);
			for(int x = 0; x < wr.getWidth(); x++){
//				System.out.println("x: "+x);
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
	
	
	public static void sleep(int milis){
		try { Thread.sleep(milis);} catch (InterruptedException e) {e.printStackTrace();}	
	}
		
	
	public static Raster extractArea(BufferedImage bi, Rectangle area){
		return bi.getData().createChild(
				(int)area.getMinX(), (int)area.getMinY(), 
				(int)area.getWidth(), (int)area.getHeight(), 0, 0, null);
	}
	
	
	public static BufferedImage resizeImage(BufferedImage bi, Dimension size){
		int width = 640;
		size = new Dimension(width, (int)(width/1.2020));
		IplImage src = IplImage.createFrom(bi);
		IplImage grayImg = IplImage.create(src.width(), src.height(), IPL_DEPTH_8U, 1);
		cvCvtColor(src, grayImg, CV_BGR2GRAY);
		IplImage newImg = IplImage.create((int) size.getWidth(), (int)size.getHeight(), IPL_DEPTH_8U, 1);
		cvResize(grayImg, newImg, CV_INTER_LINEAR);
		return newImg.getBufferedImage();
	}
}
