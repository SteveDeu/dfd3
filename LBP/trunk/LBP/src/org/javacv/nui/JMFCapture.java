package org.javacv.nui;

//JMFCapture.java
//Andrew Davison, March 2011, ad@fivedots.coe.psu.ac.th

/* The specified capture device is assigned a JMF player.

 For the assignment to work, the device should already be
 registered with JMF via its JMF Registry application.

 The video format with the largest frame size is selected.

 The user takes a snap by calling getImage(), which
 returns the image as a BufferedImage object.
 */

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.format.*;
import javax.media.util.*;

public class JMFCapture implements ControllerListener {
	// I obtained this information from JMF Registry, under its capture devices
	// tab
	private static final String CAP_DEVICE = "vfw:Microsoft WDM Image Capture (Win32):0";
	// common name in WinXP
	// private static final String CAP_DEVICE =
	// "vfw:Logitech USB Video Camera:0";

	private static final String CAP_LOCATOR = "vfw://0";

	// used while waiting for the BufferToImage object to be initialized
	private static final int MAX_TRIES = 7;
	private static final int TRY_PERIOD = 1000; // ms

	private VideoFormat largestVf = null; // video format with largest frame
											// size
	private Dimension frameSize = null;

	private Player p = null;
	private FrameGrabbingControl fg;
	private BufferToImage bufferToImage = null;
	private boolean closedDevice;

	// used for waiting until the player has started
	private Object waitSync = new Object();
	private boolean stateTransitionOK = true;

	public JMFCapture() {
		closedDevice = true; // since device is not available yet

		// link player to capture device
		try {
			MediaLocator ml = findMedia(CAP_DEVICE);
			p = Manager.createRealizedPlayer(ml);
			System.out.println("Created player");
		} catch (Exception e) {
			System.out.println("Failed to create player");
			System.exit(0);
		}

		setToLargestVideoFrame(p);
		p.addControllerListener(this);

		// create the frame grabber
		fg = (FrameGrabbingControl) p
				.getControl("javax.media.control.FrameGrabbingControl");
		if (fg == null) {
			System.out.println("Frame grabber could not be created");
			System.exit(0);
		}

		// wait until the player has started
		System.out.println("Starting the player...");
		p.start();
		if (!waitForStart()) {
			System.err.println("Failed to start the player.");
			System.exit(0);
		}

		waitForBufferToImage();
	} // end of JMFCapture()

	private MediaLocator findMedia(String requireDeviceName)
	// return a media locator for the specified capture device
	{
		Vector devices = CaptureDeviceManager.getDeviceList(null);
		if (devices == null) {
			System.out.println("Devices list is null");
			System.exit(0);
		}
		if (devices.size() == 0) {
			System.out.println("No devices found");
			System.exit(0);
		}

		CaptureDeviceInfo devInfo = null;
		int idx;
		for (idx = 0; idx < devices.size(); idx++) {
			devInfo = (CaptureDeviceInfo) devices.elementAt(idx);
			// System.out.println("  " + idx + ". " + devInfo );
			String devName = devInfo.getName();
			if (devName.equals(requireDeviceName)) // found device
				break;
		}

		MediaLocator ml = null;
		if (idx == devices.size()) { // no device found with that name
			System.out.println("Device " + requireDeviceName + " not found");
			System.out.println("Using default media locator: " + CAP_LOCATOR);
			ml = new MediaLocator(CAP_LOCATOR);
		} else { // found a suitable device
			System.out.println("Found device: " + requireDeviceName);
			storeLargestVf(devInfo);
			ml = devInfo.getLocator(); // this method may not work
		}
		return ml;
	} // end of findMedia()

	private void storeLargestVf(CaptureDeviceInfo devInfo)
	// store largest frame size video format for this device in largestVf
	{
		Format[] forms = devInfo.getFormats();

		largestVf = null;
		double maxSize = -1;
		for (int i = 0; i < forms.length; i++) {
			// System.out.println("  " + i + ". " + forms[i]);
			if (forms[i] instanceof VideoFormat) {
				VideoFormat vf = (VideoFormat) forms[i];
				Dimension dim = vf.getSize();
				// System.out.println("    frame size: " + dim + "\n");
				double size = dim.getWidth() * dim.getHeight();
				if (size > maxSize) {
					largestVf = vf;
					maxSize = size;
				}
			}
		}

		if (largestVf == null)
			System.out.println("No video format found");
		// else
		// System.out.println("Largest format: " + largestVf);
	} // end of storeLargestVf()

	public Dimension getFrameSize()
	// return the player's video frame size
	{
		if (p == null)
			return null;

		FormatControl formatControl = (FormatControl) p
				.getControl("javax.media.control.FormatControl");
		if (formatControl == null)
			return null;

		VideoFormat vf = (VideoFormat) formatControl.getFormat();
		if (vf == null)
			return null;

		return vf.getSize();
	} // end of getFrameSize()

	private void setToLargestVideoFrame(Player player)
	// change the player's video format to the one with the largest frame size
	{
		FormatControl formatControl = (FormatControl) player
				.getControl("javax.media.control.FormatControl");
		if (formatControl == null) {
			System.out.println("No format controller found");
			return;
		}

		Format format = formatControl.setFormat(largestVf);
		if (format == null) {
			System.out.println("Could not change video format");
			return;
		}
		System.out.println("Video format changed to largest frame size");
	} // end of setToLargestVideoFrame()

	private boolean waitForStart()
	// wait for the player to enter its Started state
	{
		synchronized (waitSync) {
			try {
				while (p.getState() != Controller.Started && stateTransitionOK)
					waitSync.wait();
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	} // end of waitForStart()

	public void controllerUpdate(ControllerEvent evt)
	// respond to events
	{
		if (evt instanceof StartEvent) { // the player has started
			synchronized (waitSync) {
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		} else if (evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) { // there was a problem getting a player
										// resource
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		}
	} // end of controllerUpdate()

	private void waitForBufferToImage()
	/*
	 * Wait for the BufferToImage object to be initialized. May take several
	 * seconds to initialize this object, so this method makes up to MAX_TRIES
	 * attempts.
	 */
	{
		int tryCount = MAX_TRIES;
		System.out.println("Initializing BufferToImage...");
		while (tryCount > 0) {
			if (hasBufferToImage()) // initialization succeeded
				break;
			try { // initialization failed so wait a while and try again
				System.out.println("Waiting...");
				Thread.sleep(TRY_PERIOD);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			tryCount--;
		}

		if (tryCount == 0) {
			System.out.println("Giving Up");
			System.exit(0);
		}

		closedDevice = false; // device now available
	} // end of waitForBufferToImage()

	private boolean hasBufferToImage()
	/*
	 * The BufferToImage object is initialized here, so that when getImage() is
	 * called later, the snap can be quickly changed to an image.
	 * 
	 * The object is initialized by taking a snap, which may be an actual
	 * picture or be 'empty'.
	 * 
	 * An 'empty' snap is a Buffer object with no video information, as detected
	 * by examining its component VideoFormat data.
	 * 
	 * An 'empty' snap is caused by the delay in the player, which although in
	 * its started state may still take several seconds to start capturing.
	 * 
	 * The dimensions of the snap are used to calculate the scale factor from
	 * the original image size to size*size.
	 */
	{
		Buffer buf = fg.grabFrame(); // take a snap
		if (buf == null) {
			System.out.println("No grabbed frame");
			return false;
		}

		// there is a buffer, but check if it's empty or not
		VideoFormat vf = (VideoFormat) buf.getFormat();
		if (vf == null) {
			System.out.println("No video format");
			return false;
		}

		System.out.println("Video format: " + vf);
		// initialize bufferToImage with the video format info.
		bufferToImage = new BufferToImage(vf);
		return true;
	} // end of hasBufferToImage()

	public int getFrameRate() {
		return 30;
	}

	synchronized public BufferedImage getImage()
	/*
	 * Capture an image/frame. The frame is converted from Buffer object to
	 * Image, and finally to BufferedImage.
	 */
	{
		if (closedDevice)
			return null;

		// grab the current frame as a buffer object
		Buffer buf = fg.grabFrame();
		if (buf == null) {
			System.out.println("No grabbed buffer");
			return null;
		}

		// convert buffer to image
		Image im = bufferToImage.createImage(buf);
		if (im == null) {
			System.out.println("No grabbed image");
			return null;
		}

		return (BufferedImage) im;
	} // end of getImage()

	synchronized public void close()
	/*
	 * close() and getImage() are synchronized so that it's not possible to
	 * close down the player while a frame is being snapped.
	 */
	{
		p.close();
		closedDevice = true;
	}

	public boolean isClosed() {
		return closedDevice;
	}

} // end of JMFCapture class 