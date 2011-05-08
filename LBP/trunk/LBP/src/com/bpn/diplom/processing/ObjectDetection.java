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
import java.awt.image.BufferedImage;


public class ObjectDetection implements IFaceDetector, IEyeDetector
{
	// cascade definition for face detection
	private static final String CASCADE_FACE_FILE_1 = "haar/haarcascade_frontalface_alt.xml";
	private static final String CASCADE_FACE_FILE_2 = "haar/haarcascade_frontalface_alt_tree.xml";
	
	private static final String CASCADE_EYEGLASSES_FILE = "haar/haarcascade_eye_tree_eyeglasses.xml";
	private static final String CASCADE_EYE_FILE = "haar/haarcascade_eye.xml";
	private static final String CASCADE_EYE_LEFT_FILE = "haar/haarcascade_lefteye_2splits.xml";
	private static final String CASCADE_EYE_PAIR_BIG_FILE = "haar/haarcascade_mcs_eyepair_big.xml";
	private static final String CASCADE_EYE_PAIR_SMALL_FILE = "haar/haarcascade_mcs_eyepair_small.xml";
	
	
	
	// scaling factor to reduce size of input image
	private int scaleFace = 6;
	private int scaleEye = 1;
	
	private String cascadeFaceFile;
	private String cascadeEyeFile;
	
	// create temp storage, used during object detection
	private CvMemStorage storage;

	// instantiate a classifier cascade for face detection
	private CvHaarClassifierCascade cascadeFace = new CvHaarClassifierCascade(cvLoad(CASCADE_FACE_FILE_1));
	private CvHaarClassifierCascade cascadeEye = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_FILE));
	
	
	public ObjectDetection(){
		super();
		cascadeFaceFile = CASCADE_FACE_FILE_1;
		cascadeEyeFile = CASCADE_EYE_FILE;
		storage = CvMemStorage.create();
		cascadeFace = new CvHaarClassifierCascade(cvLoad(cascadeFaceFile));
		cascadeEye = new CvHaarClassifierCascade(cvLoad(cascadeEyeFile));
	}
	
	public ObjectDetection(int scaleForFace, int scaleForEye){
		this();
		this.scaleFace = scaleForFace;
		this.scaleEye = scaleForEye;
	}
	
	
	
	public Rectangle[] getObjectCoordinates(IplImage origImg, CvHaarClassifierCascade cascade, int scale, int borderCut){
		long start = System.currentTimeMillis();
		// convert to grayscale
		IplImage grayImg = IplImage.create(origImg.width(), origImg.height(), IPL_DEPTH_8U, 1);
		cvCvtColor(origImg, grayImg, CV_BGR2GRAY);

		// scale the grayscale (to speed up face detection)
		IplImage smallImg = IplImage.create(grayImg.width() / scale, grayImg.height() / scale, IPL_DEPTH_8U, 1);
		cvResize(grayImg, smallImg, CV_INTER_LINEAR);

		// equalize the small grayscale
		IplImage equImg = IplImage.create(smallImg.width(), smallImg.height(), IPL_DEPTH_8U, 1);
		cvEqualizeHist(smallImg, equImg);

		System.out.println("Detecting faces...");
		CvSeq objectSeq = cvHaarDetectObjects(equImg, cascade, storage, 1.1, 3,	CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);

		Rectangle[] objects = new Rectangle[objectSeq.total()];
		for (int i = 0; i < objects.length; i++) {
			CvRect r = new CvRect(cvGetSeqElem(objectSeq, i));
//			int borderCut = 10;
			int difX = (r.width()*scale)/borderCut;
			int difY = (r.height()*scale)/borderCut;
			objects[i] = new Rectangle(r.x()*scale + difX, 	   r.y()*scale + difY, 
									 r.width()*scale - 2*difX, r.height()*scale - difY);
//			objects[i] = new Rectangle(r.x()*scale, r.y()*scale, r.width()*scale, r.height()*scale);
		}
		System.out.println("Found " + objects.length + " object(s)");
		System.out.println(System.currentTimeMillis() - start + " ms\t   all time");
		return objects;
	}
	

	
	@Override
	public Rectangle[] getFaceCoordinates(BufferedImage origImage){
		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeFace, scaleFace, 10);
	}

	@Override
	public Rectangle[] getEyeCoordinates(BufferedImage origImage) {
		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEye, scaleEye, 1);
	}
	
//	IFaceDetector, IEyeDetector
//	public IFaceDetector getFaceDetector(){
//		return new IFaceDetector(){
//			@Override
//			public Rectangle[] getFaceCoordinates(BufferedImage origImg) {
//				// TODO Auto-generated method stub
//				return null;
//			}}; 
//	}
	
	
	
} 