package com.bpn.diplom.lbp;

/** Точка java.awt.Point имеющая цвевт(градация черного)
 * @author Pavlyha
 *
 */
public class CPoint extends java.awt.Point{
	
	int color = 0;
	int codeLBP = -1;

	public CPoint(){
		super();
	}
	
	public CPoint(int x, int y, int color){
		super();
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public CPoint(int x, int y, int color, int code){
		super();
		this.x = x;
		this.y = y;
		this.color = color;
		this.codeLBP = code;
	}
	
	public int getCodeLBP() {
		return codeLBP;
	}

	public void setCodeLBP(int codeLBP) {
		this.codeLBP = codeLBP;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
}
