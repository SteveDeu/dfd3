package org.javacv;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.JavaCV;
import com.googlecode.javacv.OpenCVFrameGrabber;

//import cvaux.CvauxLibrary.IplImage;

public class WebCamGrabber {
	char c;
	public static void main(String[] args) throws Exception {
		CanvasFrame frame = new CanvasFrame("Avi grabber");
		frame.setDefaultCloseOperation(CanvasFrame.EXIT_ON_CLOSE);
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.start();
		while (true) {
			IplImage img = (IplImage) grabber.grab();
			if (img == null)
				return;

//			JavaCV cv = new JavaCV();
//			cv.loadImage("test.jpg", 300, 400);
//			cv.cascade("haarcascade_frontalface_default.xml");
//			Rectangle bounds[] = cv.detect();

			frame.showImage(img);
			KeyEvent key = frame.waitKey(27);
			if (key != null) {
				frame.dispose();
			}
		}
	}
}