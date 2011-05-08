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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import com.googlecode.javacpp.Loader;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class EyeDetectionExamplse {
	private static final int SCALE = 1;
	// scaling factor to reduce size of input image

	// cascade definition for face detection
//	private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";
//	private static final String CASCADE_FILE = "haar/haarcascade_frontalface_alt_tree.xml";
//	private static final String CASCADE_FILE = "haar/lbpcascade_frontalface.xml";

//	private static final String CASCADE_FILE = "haar/haarcascade_eye_tree_eyeglasses.xml";
	private static final String CASCADE_FILE = "haar/haarcascade_eye.xml";
//	private static final String CASCADE_FILE = "haar/haarcascade_lefteye_2splits.xml";
//	private static final String CASCADE_FILE = "haar/haarcascade_mcs_eyepair_big.xml";
//	private static final String CASCADE_FILE = "haar/haarcascade_mcs_eyepair_small.xml";

	
	private static final String OUT_FILE = "markedFaces.jpg";

	public static void main(String[] args) {
		AffineTransform d;
		
//		if (args.length != 1) {
//			System.out.println("Usage: run FaceDetection <input-file>");
//			return;
//		}
		long start = System.currentTimeMillis(); 
		args = new String[]{"ggroup.jpg"};

		System.out.println("Starting OpenCV...");

		// preload the opencv_objdetect module to work around a known bug
		Loader.load(opencv_objdetect.class);

		// load an image
		System.out.println("Loading image from " + args[0]);
		IplImage origImg = cvLoadImage(args[0]);

		// convert to grayscale
		IplImage grayImg = IplImage.create(origImg.width(), origImg.height(),
				IPL_DEPTH_8U, 1);
		cvCvtColor(origImg, grayImg, CV_BGR2GRAY);

		// scale the grayscale (to speed up face detection)
		IplImage smallImg = IplImage.create(grayImg.width() / SCALE,
				grayImg.height() / SCALE, IPL_DEPTH_8U, 1);
		cvResize(grayImg, smallImg, CV_INTER_LINEAR);

		// equalize the small grayscale
		IplImage equImg = IplImage.create(smallImg.width(), smallImg.height(),
				IPL_DEPTH_8U, 1);
		cvEqualizeHist(smallImg, equImg);

		// create temp storage, used during object detection
		CvMemStorage storage = CvMemStorage.create();

		// instantiate a classifier cascade for face detection
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
				cvLoad(CASCADE_FILE));
		System.out.println("Detecting faces...");
		CvSeq faces = cvHaarDetectObjects(equImg, cascade, storage, 1.1, 3,
				CV_HAAR_DO_CANNY_PRUNING);
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

		if (total > 0) {
			System.out.println("Saving marked-faces version of " + args[0]
					+ " in " + OUT_FILE);
			cvSaveImage(OUT_FILE, origImg);
		}
		
		System.out.println(System.currentTimeMillis() - start );
	} // end of main()

} 