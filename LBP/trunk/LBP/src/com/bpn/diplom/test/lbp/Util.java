package com.bpn.diplom.test.lbp;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

public class Util {

	/**
	 * @param args
	 */
	public static void main(String [] args){

//		System.out.println(intInBin(15461));
//		System.out.println(binToInt("11110001100101"));
		System.out.println(Math.pow(3,2));
		
	}

	
	
	/** Возвращает двоичное представление числа в виде стринг
	 * @param num
	 * @return
	 */
	public static String intInBin(int num){
		if(num < 0)
			return "-number";

		String numSt = "";
		while(num > 0){
			numSt = (num % 2) + numSt;
			num = num >> 1;
		}

		return numSt.toString();
	}
	
	
	
	public static int binToInt(String binSt){
		if(binSt.length() < 0)
			return -1;

		int num = 0;
		for(int i = 0; i < binSt.length(); i++){
			num = num << 1;
			if(binSt.charAt(i) == '1')
				num += 1; 
		}
		
		return num;
	}
	
	
	
	public static boolean isLDPCode(String code){
		int conversionCount = 0;
		char ch = code.charAt(0); 
		for(int i=0; i < code.length(); i++){
			if(code.charAt(i) != ch){
				conversionCount++;
				ch = code.charAt(i);
			}
		}
		return conversionCount < 3;
	}
}
