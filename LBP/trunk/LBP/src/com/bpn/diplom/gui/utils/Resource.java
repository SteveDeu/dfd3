package com.bpn.diplom.gui.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class Resource{
	
	private static final String ISO8859_1 = "ISO8859-1"; 
	private static final String UTF8 = "UTF8";
	private static final String BUNDLE = "strings";
	private static final String CONFIGURATION_BUNDLE = "config";
	private static final String MESSAGE_BUNDLE = "message";

	private static ResourceBundle resourceString; 
	private static ResourceBundle resourceConfiguration; 
	private static ResourceBundle resourceMessage; 

	private static Locale locale = Locale.getDefault();
	
	static{
		resourceString = ResourceBundle.getBundle(BUNDLE, locale);
		resourceConfiguration = ResourceBundle.getBundle(CONFIGURATION_BUNDLE, locale);
		resourceMessage = ResourceBundle.getBundle(MESSAGE_BUNDLE, locale);
	}

	
	/**
	 * возвращает строку по ключу из bundle
	 * @param key ключ строки
	 * @param bundle откуда будем брать строку
	 * @return
	 */
	public static String getPackageString(String key, ResourceBundle bundle){
		String resultString="";
		try {
			String value = bundle.getString(key);
			resultString = null;
			resultString = new String(value.getBytes(ISO8859_1),UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Logger.getRootLogger().error(ISO8859_1+"\n"+UTF8, e);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			Logger.getRootLogger().error("in Resuorce not found key: "+key+" string", e);
			resultString = "";
		}
		 
		return resultString;
	}	
	
	/**
	 * возвращает строку по ключу из resourceString
	 * @param key ключ строки
	 * @return
	 */
	public static String getString(String key){
		String resultString = getPackageString(key, resourceString);
		return resultString;
	}
	
	/**
	 * возвращает строку по ключу из resourceConfiguration
	 * @param key ключ строки
	 * @return
	 */
	public static String getConfiguration(String key){
		String resultString = getPackageString(key, resourceConfiguration);
		return resultString;
	}
	
	/**
	 * возвращает строку по ключу из resourceMessage
	 * @param key ключ строки
	 * @return
	 */
	public static String getMessage(String key){
		String resultString=getPackageString(key, resourceMessage);
		return resultString;
	}
	
	public static void setLocale(Locale newLocale){
		locale = newLocale;
		resourceString = ResourceBundle.getBundle(BUNDLE, locale);
		resourceConfiguration = ResourceBundle.getBundle(CONFIGURATION_BUNDLE, locale);
		resourceMessage = ResourceBundle.getBundle(MESSAGE_BUNDLE, locale);
	}
	
	public static Locale getLocale(){
	return locale;
	}
	
	
//	public static void main(String[] args) throws IOException{
//		ResourceString bundle = ResourceString.getBundle("MyResources", Locale.getDefault());
//	    System.out.println("HelpKey: " + bundle.getString("HelpKey"));	
//	    bundle = ResourceString.getBundle("MyResources", Locale.ENGLISH);
//	    System.out.println("HelpKey: " + bundle.getString("HelpKey"));	
//	}


}
