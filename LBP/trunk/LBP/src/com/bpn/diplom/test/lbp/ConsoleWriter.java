package com.bpn.diplom.test.lbp;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

public class ConsoleWriter extends PrintStream {

	String encoding = "cp866";	
	

	public ConsoleWriter(PrintStream out) {
		super(out, true);
	}
	
	public ConsoleWriter(PrintStream out, String encoding) {
		super(out, true);
		this.encoding = encoding;
	}
	
	
	@Override
    public void print(String s) {
		try {
			write(s.getBytes(encoding));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
    public void println(String s) {
		try {
			write(s.getBytes(encoding));
			println();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}



	
}
