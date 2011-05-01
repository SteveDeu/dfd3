package com.bpn.diplom.video;

/* Use JavaCV to grab images from the local webcam and display them.
 */

import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_ANY;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.bpn.diplom.processing.ProcessingImage;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VideoStreamCV {
	private static volatile boolean isRunning = true;
	
	private ProcessingImage processing;

	private JFrame window;
	private CanvasFrame canvas;
	
	public VideoStreamCV(ProcessingImage p){
		this.processing = p;
		canvas = new CanvasFrame("Camera Capture");
		window = (JFrame)canvas;
		canvas.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isRunning = false;
			}
		});
	}
	
	
	
	
	
	public void showVideoProcessing(){
		
		System.out.println("Starting OpenCV...");
		try {
			System.out.println("Starting frame grabber...");
			OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(CV_CAP_ANY);
			grabber.start();

			IplImage frame;
			int i = 0;
			while (isRunning) {
				i++;
				if ((frame = grabber.grab()) == null)
					break;
				frame = processing.processingImage(frame);
				canvas.showImage(frame);
			}
			grabber.stop();
			canvas.dispose();
		} catch (Exception e) {
			System.out.println(e);
		}
	} // end of main()
	
	
	
	public JFrame getWindow() {
		return window;
	}

	
	
	public void setWindow(JFrame window) {
		this.window = window;
	}

} // end of CameraCapture class