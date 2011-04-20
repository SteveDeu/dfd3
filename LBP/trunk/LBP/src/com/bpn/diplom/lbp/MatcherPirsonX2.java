package com.bpn.diplom.lbp;

public class MatcherPirsonX2 {

	
	public static Long getDistanceX2(LBPImage trueFace, LBPImage someFace){
		return getDistanceX2(trueFace.getFaceVector(), someFace.getFaceVector(), trueFace);
	}
	
	public static Long getDistanceX2(final int[] trueFace, final int[] someFace, LBPImage source ){
		if(trueFace == null || someFace == null)
			return null;
		if(trueFace.length != someFace.length)
			return null;
		long x2 = 0;
		
//		System.out.println("HELLO trueFace");
//		for(int i = 0; i < trueFace.length; i++ ){
//			System.out.print(trueFace[i]+" "); 
//		}
//		
//		System.out.println("\nHELLO someFace size: "+someFace.length);
//		for(int i = 0; i < someFace.length; i++ ){
//			System.out.print(someFace[i]+" "); 
//		}
		
		for(int i = 0; i < trueFace.length; i++ ){
			int weigth = LBPImage.PRIORITY_7_BLOCKS[ i / (source.getSetUniformPattern().size()+1) ];
			x2 += weigth * (((someFace[i] - trueFace[i])*(someFace[i] - trueFace[i]))/(someFace[i] + trueFace[i])); 
		}
		
		return x2;
	}
	
	

	public static void main(String [] args){
		System.out.println("HELLO\n\n");
//		new LBPImage("img/whiteSquare.bmp", LBPImage.AROUND_8_POINTS, 7);
	}
	
	
}
