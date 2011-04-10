package com.bpn.diplom.test.lbp;

public class MatcherPirsonX2 {

	
	public static Long getDistanceX2(LBPImage trueFace, LBPImage someFace ){
		return getDistanceX2(trueFace.getFaceVector(), someFace.getFaceVector());
	}
	
	public static Long getDistanceX2(int[] trueFace, int[] someFace ){
		if(trueFace == null || someFace == null)
			return null;
		if(trueFace.length != someFace.length)
			return null;
		long x2 = 0;
		
		System.out.println("HELLO trueFace");
		for(int i = 0; i < trueFace.length; i++ ){
			System.out.print(trueFace[i]+" "); 
		}
		
		System.out.println("\nHELLO someFace");
		for(int i = 0; i < someFace.length; i++ ){
			System.out.print(someFace[i]+" "); 
		}
		
		for(int i = 0; i < trueFace.length; i++ ){
			x2 += ((someFace[i] - trueFace[i])*(someFace[i] - trueFace[i]))/(someFace[i] + trueFace[i]); 
		}
		
		return x2;
	}
	
	

	public static void main(String [] args){
		System.out.println("HELLO\n\n");
//		new LBPImage("img/whiteSquare.bmp", LBPImage.AROUND_8_POINTS, 7);
	}
	
	
}
