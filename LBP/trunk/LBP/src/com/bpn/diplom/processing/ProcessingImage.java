package com.bpn.diplom.processing;

import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public interface ProcessingImage {
	
	
	public void processingImage(BufferedImage image);
	
	public IplImage processingImage(IplImage image);

}
