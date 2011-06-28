package com.bpn.diplom.gui.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.bpn.diplom.gui.VirtualDesktop;

public class Resource{
	
	public static final String JDBC_DRIVER = "JDBC_DRIVER";
	public static final String DB_NAME = "DB_NAME";
	public static final String PROTOCOL = "PROTOCOL";
	public static final String IP_ADRESS = "IP_ADRESS";
	public static final String PORT = "PORT";
	public static final String USER = "USER";
	public static final String PASS = "PASS";
	
	
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
	
	
	
	
	public static void setConfiguration(String key, String val){
		TreeMap<String, String> configContent = new TreeMap<String, String>();
		
		Enumeration<String> keys = resourceConfiguration.getKeys(); 
		while(keys.hasMoreElements()){
			String k = keys.nextElement();
			configContent.put(k, resourceConfiguration.getString(k));
		}
		
		configContent.put(key, val);
		
		try {
			File file = new File(CONFIGURATION_BUNDLE+".properties");
			if(!file.isFile() || !file.exists())
				throw new IOException();
			file.setWritable(true);
			FileWriter fw = new FileWriter(file);
			for(Entry<String, String> row : configContent.entrySet())
				fw.write(row.getKey()+"="+row.getValue()+"\n");
			fw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(VirtualDesktop.getInstance(), "Error while save settings.");
			e.printStackTrace();
		}
		return;
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
	
	
	public static void main(String[] args){
//		ResourceString bundle = ResourceString.getBundle("MyResources", Locale.getDefault());
//	    System.out.println("HelpKey: " + bundle.getString("HelpKey"));	
//	    bundle = ResourceString.getBundle("MyResources", Locale.ENGLISH);
//	    System.out.println("HelpKey: " + bundle.getString("HelpKey"));
		
		System.out.println("Start ");
		setConfiguration("pass","val1");
	}

}

//JDBCDriver=com.mysql.jdbc.Driver
//DBName=lbp
//protocol=jdbc:mysql
//IPAdress=localhost
//port=3306
//user=root
//pass=


