package com.bpn.diplom.test.lbp;

import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;

public class Block {
	
	/** Номер блока на изображении по горизонтали */
	private int numX;
	/** Номер блока на изображении по вертикали */
	private int numY;
	
	/** Левая верхняя точка блока	 */
	private Point fromPoint;
	/** Правая нижняя точка блока	 */
	private Point toPoint;
	
	private TreeMap<Integer, Integer> gistogram; 
	
	
	
	public Block(){
	}
	
	
	public Block(int x, int y, Point from, Point to){
		numX = x;
		numY = y;
		fromPoint = from;
		toPoint = to;
//		gistogram  = new TreeMap<Integer, Integer>();
	}
	
	
	public Block(int x, int y, int fromX, int fromY, int toX, int toY){
		this(x, y, new Point(fromX, fromY), new Point(toX, toY));
	}


	public Map<Integer, Integer> getGistogram() {
		return gistogram;
	}


	public void setGistogram(TreeMap<Integer, Integer> gistogram) {
		this.gistogram = gistogram;
	}


	public Point getToPoint() {
		return toPoint;
	}


	public void setToPoint(Point toPoint) {
		this.toPoint = toPoint;
	}


	public Point getFromPoint() {
		return fromPoint;
	}


	public void setFromPoint(Point fromPoint) {
		this.fromPoint = fromPoint;
	}
	
	public int getNumX() {
		return numX;
	}


	public void setNumX(int numX) {
		this.numX = numX;
	}


	public int getNumY() {
		return numY;
	}


	public void setNumY(int numY) {
		this.numY = numY;
	}


	@Override
	public String toString(){
		return "block("+numX+","+numY+"){from("+fromPoint.x+","+fromPoint.y+") to("+toPoint.x+","+toPoint.y+"), gistogram"+gistogram.toString()+"}";
	}
}
