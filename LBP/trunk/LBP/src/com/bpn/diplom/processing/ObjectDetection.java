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


public class ObjectDetection 
//implements IFaceDetector, IEyeDetector
{
	public static final int TYPE_EYE = 0;
	public static final int TYPE_EYE_GLASSES = 1;
	public static final int TYPE_EYE_LEFT = 2;
	public static final int TYPE_EYE_RIGHT = 3;
	public static final int TYPE_EYE_MCS_LEFT = 4;
	public static final int TYPE_EYE_MCS_RIGHT = 5;
	public static final int TYPE_EYE_BIG_PAIR = 6;
	public static final int TYPE_EYE_SMALL_PAIR = 7;
	
	
	
	
	// cascade definition for face detection
	private static final String CASCADE_FACE_FILE_1 = 			"haar/haarcascade_frontalface_alt.xml";
	private static final String CASCADE_FACE_FILE_2 = 			"haar/haarcascade_frontalface_alt_tree.xml";
	
	private static final String CASCADE_EYEGLASSES_FILE = 		"haar/haarcascade_eye_tree_eyeglasses.xml";
	private static final String CASCADE_EYE_FILE = 				"haar/haarcascade_eye.xml";
	private static final String CASCADE_EYE_LEFT_FILE = 		"haar/haarcascade_lefteye_2splits.xml";
	private static final String CASCADE_EYE_RIGHT_FILE = 		"haar/haarcascade_righteye_2splits.xml";
	private static final String CASCADE_EYE_MCS_LEFT_FILE = 	"haar/haarcascade_mcs_lefteye.xml";
	private static final String CASCADE_EYE_MCS_RIGHT_FILE = 	"haar/haarcascade_mcs_righteye.xml";
	
	
	
	
	private static final String CASCADE_EYE_PAIR_BIG_FILE = 	"haar/haarcascade_mcs_eyepair_big.xml";
	private static final String CASCADE_EYE_PAIR_SMALL_FILE = 	"haar/haarcascade_mcs_eyepair_small.xml";
	
	private static final String CASCADE_MOUTH_FILE = 			"haar/haarcascade_mcs_mouth.xml";
	
	private static final String CASCADE_NOSE_FILE = 			"haar/haarcascade_mcs_nose.xml";
	
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
	private CvHaarClassifierCascade cascadeEyeGlasses = new CvHaarClassifierCascade(cvLoad(CASCADE_EYEGLASSES_FILE));
	private CvHaarClassifierCascade cascadeEyeLeft = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_LEFT_FILE));
	private CvHaarClassifierCascade cascadeEyeRight = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_RIGHT_FILE));
	private CvHaarClassifierCascade cascadeEyeMcsLeft = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_MCS_LEFT_FILE));
	private CvHaarClassifierCascade cascadeEyeMcsRight = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_MCS_RIGHT_FILE));
	
	private CvHaarClassifierCascade cascadeEyeBigPair = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_PAIR_BIG_FILE));
	private CvHaarClassifierCascade cascadeEyeSmallPair = new CvHaarClassifierCascade(cvLoad(CASCADE_EYE_PAIR_SMALL_FILE));
	
	private CvHaarClassifierCascade cascadeNose = new CvHaarClassifierCascade(cvLoad(CASCADE_NOSE_FILE));
	private CvHaarClassifierCascade cascadeMouth = new CvHaarClassifierCascade(cvLoad(CASCADE_MOUTH_FILE));
	
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
	
	
	
	public Rectangle[] getObjectCoordinates(IplImage origImg, CvHaarClassifierCascade cascade, int scale, int borderCut, int haarType){
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
		CvSeq objectSeq = cvHaarDetectObjects(equImg, cascade, storage, 1.01, 3, haarType);
		cvClearMemStorage(storage);

//		com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
//		com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_ROUGH_SEARCH;
//		com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_FEATURE_MAX;
//		com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_FIND_BIGGEST_OBJECT;
//		com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_MAGIC_VAL;
//		com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_SCALE_IMAGE;
		
		Rectangle[] objects = new Rectangle[objectSeq.total()];
		for (int i = 0; i < objects.length; i++) {
			CvRect r = new CvRect(cvGetSeqElem(objectSeq, i));
//			
			int difX = 0;
			int difY = 0;
			if(borderCut != 0){
				difX = (r.width()*scale)/borderCut;
				difY = (r.height()*scale)/borderCut;
			} 
			objects[i] = new Rectangle(r.x()*scale + difX, 	   r.y()*scale + difY, 
									 r.width()*scale - 2*difX, r.height()*scale - difY);
//			objects[i] = new Rectangle(r.x()*scale, r.y()*scale, r.width()*scale, r.height()*scale);
		}
		System.out.println("Found " + objects.length + " object(s)");
		System.out.println(System.currentTimeMillis() - start + " ms\t   all time");
		return objects;
	}
	

	
//	@Override
	public Rectangle[] getFaceCoordinates(BufferedImage origImage){
		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeFace, scaleFace, 10, CV_HAAR_FEATURE_MAX);
	}

//	@Override
//	public Rectangle[] getEyeCoordinates(BufferedImage origImage) {
//		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEye, scaleEye, 0);
//	}
	
	
	public Rectangle[] getEyeCoordinates(BufferedImage origImage, int typeEyeCascade) {
		
		if(typeEyeCascade == TYPE_EYE_GLASSES)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeGlasses, scaleEye, 0, CV_HAAR_FEATURE_MAX);
		
		if(typeEyeCascade == TYPE_EYE_LEFT)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeLeft, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);
		
		if(typeEyeCascade == TYPE_EYE_RIGHT)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeRight, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);

		if(typeEyeCascade == TYPE_EYE_MCS_LEFT)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeMcsLeft, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);
		
		if(typeEyeCascade == TYPE_EYE_MCS_RIGHT)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeMcsRight, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);
		
		
		if(typeEyeCascade == TYPE_EYE_BIG_PAIR)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeBigPair, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);
		
		if(typeEyeCascade == TYPE_EYE_SMALL_PAIR)
			return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEyeSmallPair, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);

		
		
		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeEye, scaleEye, 0, CV_HAAR_FEATURE_MAX);
	}
	
	
	public Rectangle[] getNoseCoordinates(BufferedImage origImage){
		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeNose, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);
	}
	
	public Rectangle[] getMouthCoordinates(BufferedImage origImage){
		return getObjectCoordinates(IplImage.createFrom(origImage), cascadeMouth, scaleEye, 0, CV_HAAR_FIND_BIGGEST_OBJECT);
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