package com.bpn.diplom.processing;

import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public interface ProcessingImage {
	
	
	public BufferedImage processingImage(BufferedImage image);
	
	public IplImage processingImage(IplImage image);

}
