package com.bpn.diplom.gui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.spi.LoggingEvent;

public class DateLogLayout extends org.apache.log4j.helpers.DateLayout {

	static SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd.MM.yyyy:  E.HH:mm --> ");

	@Override
	public String format(LoggingEvent event) {
		
		String exception="";
		if(event.getThrowableInformation() != null){
			exception = "\t" + event.getThrowableInformation().getThrowable().toString() + "\n";
		}
		return formatter.format(new Date()) + event.getMessage() + "\n" + exception;
	}
	
	

	@Override
	public boolean ignoresThrowable() {
		return false;
	}

}