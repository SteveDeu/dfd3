package com.bpn.diplom.processing;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public interface IFaceDetector {

	public Rectangle[] getFaceCoordinates(BufferedImage origImg);
	
}
