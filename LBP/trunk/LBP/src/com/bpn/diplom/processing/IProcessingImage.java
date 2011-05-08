package com.bpn.diplom.processing;

import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import com.bpn.diplom.dao.*;

public interface IProcessingImage {
	
	
	public EntityLBPUser processingImage(BufferedImage image);
	
	public IplImage processingImage(IplImage image);

}
