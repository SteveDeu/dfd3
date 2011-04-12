/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bpn.diplom.test.lbp;

import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pavlyha
 */
public class Main {
    public static final String jeka = "id128174932";
    public static final String yula = "id10664661";
    public static final String pedan = "id75567015";
    public static final String bilan = "id8254758";
    public static final String maksimenko = "id10889993";
    public static final String kornejchuk = "andrey_korneychuk";
    public static final String sokolov = "id1444942";
    public static final String myzuka = "id9182900";

    public static final String COMAND_IMG_1 = "-img1";
    public static final String COMAND_IMG_2 = "-img2";
    public static final String COMAND_POINT_COUNT = "-pcount";
    public static final String COMAND_HELP = "-h";
    public static final String COMAND_BLOCK_COUNT = "-bcount";
    public static final String COMAND_ENCODING = "-charset";
//    public static final String COMAND_ = "";

    /**
     * @param args the command line arguments
     * @throws UnsupportedEncodingException 
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
    	System.out.println(">>> START LDP:");
//        List<String> listId = new ArrayList<String>();
        String msg ="Input arguments:\n" +
        		"\t"+COMAND_IMG_1+"\t path to comparing image\n" +
        		"\t"+COMAND_IMG_2+"\t path to comparing image\n" +
        		"\t"+COMAND_POINT_COUNT+"\t 8 or 12 or 16 point in LDP\n" +
        		"\t"+COMAND_BLOCK_COUNT+"\t count of block for divide imgage (must be 7)\n" +
        		"\t"+COMAND_ENCODING+"\t encoding for output (default = \"cp866\")\n" +
        		"\t"+COMAND_HELP+"\t help";
        String img1Path = null;
        String img2Path = null;
        String encoding = null;
        Integer countPoint = null;
        Integer countBlock = null;
        int i = 0;
        while( i < args.length){
            if(args[i].equals(COMAND_HELP)){
                try {
                    System.out.println(new String(msg.getBytes(), "windows-1251"));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                return;
            }else
            if(args[i].equals(COMAND_IMG_1)){
                i++;
                img1Path = args[i];
                i++;
                continue;
            }else
            if(args[i].equals(COMAND_IMG_2)){
                i++;
                img2Path = args[i];
                i++;
                continue;
            }else
        	if(args[i].equals(COMAND_BLOCK_COUNT)){
                i++;
                countBlock = Integer.valueOf(args[i]);
                i++;
                continue;
            }else            	
        	if(args[i].equals(COMAND_POINT_COUNT)){
                i++;
                countPoint = Integer.valueOf(args[i]);
                i++;
                continue;
            }else
        	if(args[i].equals(COMAND_ENCODING)){
                i++;
                encoding = args[i];
                i++;
                continue;
            }else{
                System.out.println("Unknown argument: "+args[i]);
                return;
            }
        }
        
        if(countPoint == null){
        	System.out.println("Not set argument: " + COMAND_POINT_COUNT + "\n" + msg);
        	return ;
        }
        if(countPoint != 8 && countPoint != 12){
        	System.out.println("Wrong value argument: " + COMAND_POINT_COUNT + "\n" + msg);
	    	return ;
        }
        if(img1Path == null){
        	System.out.println("Not set argument: " + COMAND_IMG_1 + "\n" + msg);
        	return ;
        }
        if(img2Path == null){
        	System.out.println("Not set argument: " + COMAND_IMG_2 + "\n" + msg);
        	return ;
        }
        if(countBlock == null)
        	countBlock = 7;
        if(encoding == null)
        	encoding = "CP866";
		System.setOut(new ConsoleWriter(System.out));
        
        Point[] points = null;
        if (countPoint == 8)
        	points = LBPImage.AROUND_8_POINTS;
        if (countPoint == 12)
        	points = LBPImage.AROUND_12_POINTS;
        	
		LBPImage img1 = new LBPImage(img1Path, points, countBlock);
		LBPImage img2 = new LBPImage(img2Path, points, 7);
		System.out.println("\nDistanceX2 = "+MatcherPirsonX2.getDistanceX2(img1, img2));

    }

}



