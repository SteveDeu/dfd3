package org.javacv.nui;

//FacePanel.java
//Andrew Davison, March 2011, ad@fivedots.psu.ac.th

/* This panel repeatedly uses JMFCapture to snap a picture and draw it onto
 the panel.  A face is highlighted with a yellow rectangle, which is updated 
 as the face moves. A "crosshairs" graphic is also drawn, positioned at the
 center of the rectangle.

 The highlighted part of the image can be saved.

 Face detection is done using a Haar face classifier in JavaCV. 
 It is executed inside its own thread since the processing can be lengthy,
 and I don't want the image grabbing speed to be affected.

 I borrowed some coding ideas from the JavaCV Android example in 
 samples\FacePreview.java
 */

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.text.DecimalFormat;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacpp.Loader;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class FacePanel extends JPanel implements Runnable {
	private static final Dimension PANEL_SIZE = new Dimension(200, 50);
	// dimensions of panel initially; later set to video's frame size

	private static final int DELAY = 30; // time (ms) between redraws of the
											// panel

	private static final int IM_SCALE = 4;
	private static final int SMALL_MOVE = 5;
	private static final int DETECT_DELAY = 1000;
	// time (ms) between each face detection
	private static final int MAX_TASKS = 4;
	// max no. of tasks that can be waiting to be executed

	// circle and cross-hairs dimensions (only used if crosshairs image cannot
	// be loaded)
	private static final int CIRCLE_SIZE = 40;
	private static final int LINES_LEN = 60;

	// cascade definition to be used for face detection
	private static final String FACE_CASCADE_FNM = "haarcascade_frontalface_alt.xml";
	// "haarcascade_frontalface_alt2.xml";
	/*
	 * others are in C:\OpenCV2.2\data\haarcascades\ and at
	 * http://alereimondo.no-ip.org/OpenCV/34
	 */

	private static final String CROSSHAIRS_FNM = "crosshairs.png";
	private static final String FACE_FNM = "savedFace.png";

	private JFrame top;
	private BufferedImage image = null; // current webcam snap
	private JMFCapture camera;
	private volatile boolean isRunning;

	// used for the average ms snap time information
	private int imageCount = 0;
	private long totalTime = 0;
	private DecimalFormat df;
	private Font msgFont;

	// JavaCV variables
	private CvHaarClassifierCascade classifier;
	private CvMemStorage storage;
	private CanvasFrame debugCanvas;

	// used for thread that executes the face detection
	private ExecutorService executor;
	private AtomicInteger numTasks;
	// used to record number of detection tasks
	private long detectStartTime = 0;

	private Rectangle faceRect; // holds the coordinates of the highlighted face
	private BufferedImage grayIm, crosshairs;

	private boolean saveFace = false;

	public FacePanel(JFrame top) {
		this.top = top;
		setBackground(Color.white);
		setPreferredSize(PANEL_SIZE);

		df = new DecimalFormat("0.#"); // 1 dp
		msgFont = new Font("SansSerif", Font.BOLD, 18);

		// load the crosshairs image (a transparent PNG)
		crosshairs = null;
		try {
			crosshairs = ImageIO.read(new File(CROSSHAIRS_FNM));
		} catch (IOException e) {
			System.out.println("Could not find " + CROSSHAIRS_FNM);
		}

		executor = Executors.newSingleThreadExecutor();
		/*
		 * this executor manages a single thread with an unbounded queue. Only
		 * one task can be executed at a time, the others wait.
		 */
		numTasks = new AtomicInteger(0);
		// used to limit the size of the executor queue

		initOpenCV();
		faceRect = new Rectangle();

		new Thread(this).start(); // start updating the panel's image
	} // end of FacePanel()

	private void initOpenCV() {
		System.out.print("Starting OpenCV...");

		// preload the opencv_objdetect module to work around a known bug
		Loader.load(opencv_objdetect.class);

		// instantiate a classifier cascade for face detection
		classifier = new CvHaarClassifierCascade(cvLoad(FACE_CASCADE_FNM));
		if (classifier.isNull()) {
			System.out.println("\nCould not load the classifier file: "
					+ FACE_CASCADE_FNM);
			System.exit(1);
		}

		storage = CvMemStorage.create(); // create storage used during object
											// detection

		// debugCanvas = new CanvasFrame("Debugging Canvas");
		// useful for showing JavaCV IplImage objects, to check on image
		// processing

		System.out.println("done");
	} // end of initOpenCV()

	public void run()
	/*
	 * display the current webcam image every DELAY ms The time statistics
	 * gathered here will NOT include the time taken to find a face, which are
	 * farmed out to a separate thread in trackFace().
	 * 
	 * Tracking is only started at least every DETECT_DELAY (1000) ms, and only
	 * if the number of tasks is < MAX_TASKS (one will be executing, the others
	 * waiting)
	 */
	{
		camera = new JMFCapture();

		// update panel and window sizes to fit video's frame size
		Dimension frameSize = camera.getFrameSize();
		if (frameSize != null) {
			setPreferredSize(frameSize);
			top.pack(); // resize and center JFrame
			top.setLocationRelativeTo(null);
		}

		long duration;
		BufferedImage im = null;
		isRunning = true;
		while (isRunning) {
			long startTime = System.currentTimeMillis();
			im = camera.getImage(); // take a snap
			if (im == null) {
				System.out.println("Problem loading image " + (imageCount + 1));
				duration = System.currentTimeMillis() - startTime;
			} else {
				if (((System.currentTimeMillis() - detectStartTime) > DETECT_DELAY)
						&& (numTasks.get() < MAX_TASKS))
					trackFace(im);

				image = im; // only update image if im contains something
				imageCount++;
				duration = System.currentTimeMillis() - startTime;
				totalTime += duration;
				repaint();
			}

			if (duration < DELAY) {
				try {
					Thread.sleep(DELAY - duration); // wait until DELAY time has
													// passed
				} catch (Exception ex) {
				}
			}
		}
		camera.close(); // close down the camera
	} // end of run()

	public void paintComponent(Graphics g)
	/*
	 * Draw the image, the rectangle (and crosshairs) around a detected face,
	 * and the average ms snap time at the bottom left of the panel. This time
	 * does NOT include the face detection task.
	 */
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (image != null)
			g2.drawImage(image, 0, 0, this);

		drawRect(g2);
		writeStats(g2);
	} // end of paintComponent()

	private void drawRect(Graphics2D g2)
	/*
	 * use the face rectangle to draw a yellow rectangle around the face, with
	 * crosshairs at its center. The drawing of faceRect is in a synchronized
	 * block since it may be being updated or used for image saving at the same
	 * time in other threads.
	 */
	{
		synchronized (faceRect) {
			if (faceRect.width == 0)
				return;

			// draw a thick yellow rectangle
			g2.setColor(Color.YELLOW);
			g2.setStroke(new BasicStroke(6));
			g2.drawRect(faceRect.x, faceRect.y, faceRect.width, faceRect.height);

			int xCenter = faceRect.x + faceRect.width / 2;
			int yCenter = faceRect.y + faceRect.height / 2;
			drawCrosshairs(g2, xCenter, yCenter);
		}
	} // end of drawRect()

	private void drawCrosshairs(Graphics2D g2, int xCenter, int yCenter)
	// draw crosshairs graphic or make one from lines and a circle
	{
		if (crosshairs != null)
			g2.drawImage(crosshairs, xCenter - crosshairs.getWidth() / 2,
					yCenter - crosshairs.getHeight() / 2, this);
		else {
			// draw thick red circle and cross-hairs in center
			g2.setColor(Color.RED);
			g2.drawOval(xCenter - CIRCLE_SIZE / 2, yCenter - CIRCLE_SIZE / 2,
					CIRCLE_SIZE, CIRCLE_SIZE);
			g2.drawLine(xCenter, yCenter - LINES_LEN / 2, xCenter, yCenter
					+ LINES_LEN / 2); // vertical line
			g2.drawLine(xCenter - LINES_LEN / 2, yCenter, xCenter + LINES_LEN
					/ 2, yCenter); // horizontal line
		}
	} // end of drawCrosshairs()

	private void writeStats(Graphics2D g2)
	/*
	 * write statistics in bottom-left corner, or "Loading" at start time
	 */
	{
		g2.setColor(Color.BLUE);
		g2.setFont(msgFont);
		int panelHeight = getHeight();
		if (imageCount > 0) {
			double avgGrabTime = (double) totalTime / imageCount;
			g2.drawString("Pic " + imageCount + "  " + df.format(avgGrabTime)
					+ " ms", 5, panelHeight - 10); // bottom left
		} else
			// no image yet
			g2.drawString("Loading...", 5, panelHeight - 10);
	} // end of writeStats()

	public void closeDown()
	/*
	 * Terminate run() and wait for the camera to be closed. This stops the
	 * application from exiting until everything has finished.
	 */
	{
		isRunning = false;
		while (!camera.isClosed()) {
			try {
				Thread.sleep(DELAY);
			} catch (Exception ex) {
			}
		}
	} // end of closeDown()

	// ------------------------- face tracking ----------------------------

	private void trackFace(final BufferedImage img)
	/*
	 * Create a separate thread for the time-consuming detection task: find a
	 * face in the current image, store its coordinates in faceRect, and save
	 * the face part of the image in a file if requested. Print the time taken
	 * for all of this to stdout.
	 */
	{
		grayIm = scaleGray(img);
		numTasks.getAndIncrement(); // increment no. of tasks before entering
									// queue
		executor.execute(new Runnable() {
			public void run() {
				detectStartTime = System.currentTimeMillis();
				CvRect rect = findFace(grayIm);
				if (rect != null) {
					setRectangle(rect);
					if (saveFace) {
						clipSaveFace(img, FACE_FNM);
						saveFace = false;
					}
				}
				long detectDuration = System.currentTimeMillis()
						- detectStartTime;
				System.out.println(" detection duration: " + detectDuration
						+ "ms");
				numTasks.getAndDecrement(); // decrement no. of tasks since
											// finished
			}
		});
	} // end of trackFace()

	private BufferedImage scaleGray(BufferedImage img)
	/*
	 * Scale the image and convert it to grayscale. Scaling makes the image
	 * smaller and so faster to process, and Haar detection requires a grayscale
	 * image as input
	 */
	{
		int nWidth = img.getWidth() / IM_SCALE;
		int nHeight = img.getHeight() / IM_SCALE;

		BufferedImage grayIm = new BufferedImage(nWidth, nHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2 = grayIm.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, nWidth, nHeight, 0, 0, img.getWidth(),
				img.getHeight(), null);
		g2.dispose();

		return grayIm;
	} // end of scaleGray()

	private CvRect findFace(BufferedImage grayIm)
	/*
	 * The Haar detector is a JavaCV function, so requires an IplImage object.
	 * Also, use JavaCV's grayscale equalizer to improve the image.
	 */
	{
		IplImage cvImg = IplImage.createFrom(grayIm); // BufferedImage -->
														// IplImage

		// equalize the grayscale using OpenCV
		IplImage equImg = IplImage.create(cvImg.width(), cvImg.height(),
				IPL_DEPTH_8U, 1);
		cvEqualizeHist(cvImg, equImg);
		/*
		 * // show the greyscale and qualized images, to check on image
		 * processing steps debugCanvas.showImage(cvImg);
		 * debugCanvas.waitKey(0);
		 * 
		 * debugCanvas.showImage(equImg); debugCanvas.waitKey(0);
		 */
		// System.out.println("Detecting largest face..."); // cvImage
		CvSeq faces = cvHaarDetectObjects(equImg, classifier, storage, 1.1, 1, // 3
				// CV_HAAR_SCALE_IMAGE |
				CV_HAAR_DO_ROUGH_SEARCH | CV_HAAR_FIND_BIGGEST_OBJECT);
		// speed things up by searching for only a single, largest face subimage

		int total = faces.total();
		if (total == 0) {
			System.out.println("No faces found");
			return null;
		} else if (total > 1) // this case should not happen, but included for
								// safety
			System.out.println("Multiple faces detected (" + total
					+ "); using the first");
		else
			System.out.println("Face detected");

		CvRect rect = new CvRect(cvGetSeqElem(faces, 0));

		cvClearMemStorage(storage);
		return rect;
	} // end of findface()

	private void setRectangle(CvRect r)
	/*
	 * Extract the (x, y, width, height) values of the highlighted image from
	 * the JavaCV rectangle data structure, and store them in a Java rectangle.
	 * In the process, undo the scaling which was applied to the image before
	 * face detection was carried out. Report any movement of the new rectangle
	 * compared to the previous one. The updating of faceRect is in a
	 * synchronized block since it may be used for drawing or image saving at
	 * the same time in other threads.
	 */
	{
		synchronized (faceRect) {
			int xNew = r.x() * IM_SCALE;
			int yNew = r.y() * IM_SCALE;
			int widthNew = r.width() * IM_SCALE;
			int heightNew = r.height() * IM_SCALE;

			// calculate movement of the new rectangle compared to the previous
			// one
			int xMove = (xNew + widthNew / 2)
					- (faceRect.x + faceRect.width / 2);
			int yMove = (yNew + heightNew / 2)
					- (faceRect.y + faceRect.height / 2);

			// report movement only if it is 'significant'
			if ((Math.abs(xMove) > SMALL_MOVE)
					|| (Math.abs(yMove) > SMALL_MOVE))
				System.out.println("Movement (x,y): (" + xMove + "," + yMove
						+ ")");

			faceRect.setRect(xNew, yNew, widthNew, heightNew);
		}
	} // end of setRectangle()

	// ---------------- face saving -------------------------

	public void saveFace() {
		saveFace = true;
	}

	private void clipSaveFace(BufferedImage img, String fnm)
	/*
	 * clip the image using the current face rectangle, and save it into fnm The
	 * use of faceRect is in a synchronized block since it may be being updated
	 * or used for drawing at the same time in other threads.
	 */
	{
		BufferedImage clipIm = null;
		synchronized (faceRect) {
			if (faceRect.width == 0) {
				System.out.println("No face selected");
				return;
			}
			clipIm = clipImage(img, faceRect);
		}
		if (clipIm != null)
			saveImage(clipIm, fnm);
	} // end of clipSaveFace()

	private BufferedImage clipImage(BufferedImage im, Rectangle rect) {
		BufferedImage clipIm = null;
		try {
			clipIm = im.getSubimage(rect.x, rect.y, rect.width, rect.height);
		} catch (RasterFormatException e) {
			System.out.println("Could not clip the image");
		}
		return clipIm;
	} // end of clipImage()

	private void saveImage(BufferedImage im, String fnm)
	// save image in fnm
	{
		try {
			ImageIO.write(im, "png", new File(fnm));
			System.out.println("Saved image to " + fnm);
		} catch (IOException e) {
			System.out.println("Could not save image to " + fnm);
		}
	} // end of saveImage()

} // end of FacePanel class

