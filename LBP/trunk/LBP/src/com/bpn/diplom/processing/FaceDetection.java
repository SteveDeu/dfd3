package com.bpn.diplom.processing;

//FaceDetection.java
//Andrew Davison, March 2011, ad@fivedots.psu.ac.th

/* Use JavaCV to detect faces in an image, and save a marked-faces
 version of the image to OUT_FILE.

 Usage:
 run FaceDetection group.jpg
 */

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

import java.awt.Rectangle;


public class FaceDetection {
	

	// scaling factor to reduce size of input image
	private static final int SCALE = 6;


	// cascade definition for face detection
	private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";
	// create temp storage, used during object detection
	private static final CvMemStorage storage = CvMemStorage.create();

	// instantiate a classifier cascade for face detection
	private static final CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
	
	
	
	
	
	
	public Rectangle[] getFaceCoordinates(IplImage origImg){
		long start = System.currentTimeMillis();
		// convert to grayscale
		IplImage grayImg = IplImage.create(origImg.width(), origImg.height(), IPL_DEPTH_8U, 1);
		cvCvtColor(origImg, grayImg, CV_BGR2GRAY);

		// scale the grayscale (to speed up face detection)
		IplImage smallImg = IplImage.create(grayImg.width() / SCALE, grayImg.height() / SCALE, IPL_DEPTH_8U, 1);
		cvResize(grayImg, smallImg, CV_INTER_LINEAR);

		// equalize the small grayscale
		IplImage equImg = IplImage.create(smallImg.width(), smallImg.height(), IPL_DEPTH_8U, 1);
		cvEqualizeHist(smallImg, equImg);

		System.out.println("Detecting faces...");
		CvSeq facesSeq = cvHaarDetectObjects(equImg, cascade, storage, 1.1, 3,	CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);

		Rectangle[] faces = new Rectangle[facesSeq.total()];
		for (int i = 0; i < faces.length; i++) {
			CvRect r = new CvRect(cvGetSeqElem(facesSeq, i));
			int borderCut = 10;
			int difX = (r.width()*SCALE)/borderCut;
			int difY = (r.height()*SCALE)/borderCut;
			faces[i] = new Rectangle(r.x()*SCALE + difX, 	   r.y()*SCALE + difY, 
									 r.width()*SCALE - 2*difX, r.height()*SCALE - difY);
//			faces[i] = new Rectangle(r.x()*SCALE, r.y()*SCALE, r.width()*SCALE, r.height()*SCALE);
		}
		System.out.println("Found " + faces.length + " face(s)");
		System.out.println(System.currentTimeMillis() - start + " ms\t   all time");
		return faces;
	}
	
	
	
} 