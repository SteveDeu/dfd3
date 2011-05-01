package org.javacv.nui.ex;

//CameraCapture.java
//Andrew Davison, March 2011, ad@fivedots.coe.psu.ac.th

/* Use JavaCV to grab images from the local webcam and display them.
 */

import java.io.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.*;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class CameraCapture {
	private static volatile boolean isRunning = true;

	public static void main(String[] args) {
		System.out.println("Starting OpenCV...");
		try {
			CanvasFrame canvas = new CanvasFrame("Camera Capture");
			canvas.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					isRunning = false;
				}
			});

			System.out.println("Starting frame grabber...");
			OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(CV_CAP_ANY);
			grabber.start();

			IplImage frame;
			int i = 0;
			long time = System.currentTimeMillis();
			while (isRunning) {
				i++;
				if ((frame = grabber.grab()) == null)
					break;
//				int v = 0;
//				int x = 10;
//				int y = 10;
//				
//				if(i%4==0){
//				
//				FaceDetectionExamplse.drawFaceRectangle(frame);
				System.out.println(System.currentTimeMillis() - time + "\t= drawFaceRectangle");
				time = System.currentTimeMillis();
//				}
				
				canvas.showImage(frame);
			}
			grabber.stop();
			canvas.dispose();
		} catch (Exception e) {
			System.out.println(e);
		}
	} // end of main()

} // end of CameraCapture class