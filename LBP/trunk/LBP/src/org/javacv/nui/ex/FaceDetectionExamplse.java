package org.javacv.nui.ex;

//FaceDetection.java
//Andrew Davison, March 2011, ad@fivedots.psu.ac.th

/* Use JavaCV to detect faces in an image, and save a marked-faces
 version of the image to OUT_FILE.

 Usage:
 run FaceDetection group.jpg
 */

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacpp.Loader;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class FaceDetectionExamplse {
	private static final int SCALE = 2;
	// scaling factor to reduce size of input image

	// cascade definition for face detection
	private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";
	// instantiate a classifier cascade for face detection
	private static final CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
	// create temp storage, used during object detection
	private static final CvMemStorage storage = CvMemStorage.create();

	
	private static final String OUT_FILE = "markedFaces.jpg";

	
	public static void  drawFaceRectangle(Image origImg){
		
		BufferedImage bImg = new BufferedImage(origImg.getWidth(null), origImg.getHeight(null), BufferedImage.TYPE_INT_BGR);
		drawFaceRectangle(IplImage.createFrom(bImg));
	}
	
	public static void  drawFaceRectangle(IplImage origImg){
		long time = System.currentTimeMillis(); 
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
		CvSeq faces = cvHaarDetectObjects(equImg, cascade, storage, 1.1, 3,	CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);
		// iterate over the faces and draw yellow rectangles around them
		int total = faces.total();
		System.out.println("Found " + total + " face(s)");
		
		for (int i = 0; i < total; i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			cvRectangle(origImg,
					cvPoint(r.x() * SCALE, r.y() * SCALE), // undo the scaling
					cvPoint((r.x() + r.width()) * SCALE, (r.y() + r.height())
							* SCALE), CvScalar.YELLOW, 6, CV_AA, 0);
		}	
		System.out.println(System.currentTimeMillis() - start + " ms\t   all time");time = System.currentTimeMillis();
	}
	
	
	
	public Rectangle getFaceCoordinates(IplImage origImg){
		// convert to grayscale
		IplImage grayImg = IplImage.create(origImg.width(), origImg.height(), IPL_DEPTH_8U, 1);
		cvCvtColor(origImg, grayImg, CV_BGR2GRAY);

		// scale the grayscale (to speed up face detection)
		IplImage smallImg = IplImage.create(grayImg.width() / SCALE, grayImg.height() / SCALE, IPL_DEPTH_8U, 1);
		cvResize(grayImg, smallImg, CV_INTER_LINEAR);

		// equalize the small grayscale
		IplImage equImg = IplImage.create(smallImg.width(), smallImg.height(), IPL_DEPTH_8U, 1);
		cvEqualizeHist(smallImg, equImg);

		// create temp storage, used during object detection
		CvMemStorage storage = CvMemStorage.create();

		// instantiate a classifier cascade for face detection
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
		System.out.println("Detecting faces...");
		CvSeq faces = cvHaarDetectObjects(equImg, cascade, storage, 1.1, 3,	CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);

		Rectangle face = new Rectangle();
		
		// iterate over the faces and draw yellow rectangles around them
		int total = faces.total();
		System.out.println("Found " + total + " face(s)");
		for (int i = 0; i < total; i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			cvRectangle(origImg,
					cvPoint(r.x() * SCALE, r.y() * SCALE), // undo the scaling
					cvPoint((r.x() + r.width()) * SCALE, (r.y() + r.height())
							* SCALE), CvScalar.YELLOW, 6, CV_AA, 0);
		}	
		
		return face;
	}
} 