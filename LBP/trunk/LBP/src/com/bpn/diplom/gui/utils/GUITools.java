package com.bpn.diplom.gui.utils;


import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

/**Набор инструментов для окончательной
* шлифовки и придания блеска интерфейсу
*/
public class GUITools {
	
	public static final int MARGIN_BUTTON = 12;
	
	
	public static void createRecommendedMargin(JButton button) {
		Insets margin = button.getMargin();
		margin.left = MARGIN_BUTTON;
		margin.right = MARGIN_BUTTON;
		button.setMargin(margin);
	}
	
	public static void createRecommendedMargin(JButton[] buttons) {
		for (int i=0; i < buttons.length; i++) {
			Insets margin = buttons[i].getMargin();
			margin.left = MARGIN_BUTTON;
			margin.right = MARGIN_BUTTON;
			buttons[i].setMargin(margin);
		}
	}  
	
	/**
	 * инструмент для придания группе компонентов
	 * одинаковых размеров (минимальных,
	 * предпочтительных и максимальных).
	 * Компоненты принимают размер самого
	 * большого (по ширине) компонента в группе
	 * @param components группа компонентов 
	 */
	public static void makeSameSize(JComponent[] components) {
		// получение ширины компонентов
		int[] sizes = new int[components.length];
		for (int i=0; i<sizes.length; i++) {
			sizes[i] = components[i].getPreferredSize().width;      
		}
		// определение максимального размера
		int maxSizePos = maxElementPosition(sizes);
		Dimension maxSize =	components[maxSizePos].getPreferredSize();
		//придание одинаковых размеров
		for (int i=0; i<components.length; i++)	{
			components[i].setPreferredSize(maxSize);
			components[i].setMinimumSize(maxSize);
			components[i].setMaximumSize(maxSize);
		}
	}  
	
	/** позволяет исправить оплошность в
	* размерах текстового поля JTextField
	*/
	public static void fixTextFieldSize(JTextField field) {
		Dimension size = field.getPreferredSize();
		size.width = field.getMaximumSize().width;
		field.setMaximumSize(size);
	}
	
	/** позволяет исправить оплошность в
	* размерах массива текстовых полей JTextField
	*/
	public static void fixTextFieldSize(JTextField[] fields) {
		for(JTextField field: fields){
			fixTextFieldSize(field);
		}
	}
	
	/**
	 * Центрирование окна относительно экрана
	 * @param window ссылка на окно для центрирования
	 */
	public static void centeringWindow(Component window){
		if(window==null){
			Logger.getRootLogger().error("null in centeringWindow", new NullPointerException());
			return;
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = window.getSize();
		window.setLocation(
				((Number)((screen.getWidth()-size.getWidth())/2)).intValue(), 
				((Number)((screen.getHeight()-size.getHeight())/2)).intValue()
		);
		return;
	}
	
	/**
	 * Центрирование окна относительно родительского окна
	 * @param window - окно для центрирования
	 * @param parent - родительское окно, относительно которого будем центрировать, 
	 * если null то центрирование относительно экрана.
	 */
	public static void centeringWindow(Component window, Component parent){
		if(window==null){
			Logger.getRootLogger().error("null in centeringWindow", new NullPointerException());
			return;
		}
		if(parent==null){
			Logger.getRootLogger().error("null in centeringWindow parent=null", new NullPointerException());
			centeringWindow(window);
			return;
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();		
		Point locParent = parent.getLocation();
		window.setLocation(
				((Number)(screen.getWidth()-(screen.getWidth()-locParent.getX())+(parent.getWidth()-window.getWidth())/2)).intValue(), 
				((Number)(screen.getHeight()-(screen.getHeight()-locParent.getX())+(parent.getHeight()-window.getHeight())/2)).intValue()
				);
	}
	
	
	
	/**
     * вспомогательный метод для определения позиции
     * максимального элемента массива
     * 
     * @return   позиция максимального элемента массива 
     *           <code>int</code>
     */
	private static int maxElementPosition(int[] array) {
		int maxPos = 0;
		for (int i=1; i < array.length; i++) {
			if (array[i] > array [maxPos]) maxPos = i;
		}
		return maxPos;
	}
	
	/**
	 * Придание жестких размеров компоненту
	 * @param component
	 * @param width
	 * @param height
	 */
	public static void setComponentSize(Component component, int width, int height){
		Dimension tableSize = new Dimension(width, height);
		component.setMaximumSize(tableSize);
		component.setPreferredSize(tableSize);
		component.setMinimumSize(tableSize);
	}
	
	/**
	 * Устанавливает ширину колонок в соответствие с принятым массивом.
	 * Колво элементов в массиве должно равнятся колву столбцов в таблице иначе выход
	 * @param table
	 * @param width
	 */
	public static void setSizeOfColumns(JTable table, int[] width){
		if(table.getColumnCount() != width.length){
			return;
		}
		int variation = 20;
		for(int i=0; i<table.getColumnCount(); i++ ){
			table.getColumnModel().getColumn(i).setMinWidth(width[i]-variation);
			table.getColumnModel().getColumn(i).setMaxWidth(width[i]+variation);
			table.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
		}
	}
	
	/**
	 * Устанавливает ширину колонок в соответствие с принятым массивом В ПРОЦЕНТАХ.
	 * Колво элементов в массиве должно равнятся колву столбцов в таблице иначе выход
	 * @param table
	 * @param width
	 */
	public static void setSizeOfColumnsInPercent(JTable table, int[] widthArray, int tableWidth){
		if(table.getColumnCount() != widthArray.length)
			return;
		int sum = 0;
		for(int n: widthArray)
			sum+=n;
		if(sum != 100)
			return;
		int variation = 20;
		for(int i=0; i<table.getColumnCount(); i++ ){
			table.getColumnModel().getColumn(i).setMinWidth((int)(tableWidth*((float)widthArray[i]/100))-variation);
			table.getColumnModel().getColumn(i).setMaxWidth((int)(tableWidth*((float)widthArray[i]/100))+variation);
			table.getColumnModel().getColumn(i).setPreferredWidth((int)(tableWidth*((float)widthArray[i]/100)));
		}
	}
	
	
	
	
	public static BufferedImage getGrayImage(BufferedImage image){
		return (BufferedImage)getGrayImage((Image)image);
	}
	
	public static Image getGrayImage(Image image){
		BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		((Graphics2D)(result.getGraphics())).drawImage((BufferedImage) image, null, 0, 0);
		return result;
	}
	
	public static BufferedImage rotateImageCenter(BufferedImage img, double angleInRadians){
		return rotateImage(img, angleInRadians, img.getWidth()/2, img.getHeight()/2);
	}
	
	public static BufferedImage rotateImage(BufferedImage img, double angleInRadians, double anchorx, double anchory){
		AffineTransform at = AffineTransform.getRotateInstance(angleInRadians, anchorx, anchory);
		AffineTransformOp aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		BufferedImage result = new BufferedImage(img.getWidth(null), img.getHeight(null), img.getType());
		((Graphics2D)(result.getGraphics())).drawImage(img, aop, 0, 0);
		return result;
	}
	
	
}