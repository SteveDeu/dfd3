package com.bpn.diplom.processing;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface IEyeDetector {

	public Rectangle[] getEyeCoordinates(BufferedImage origImg);
	
}

