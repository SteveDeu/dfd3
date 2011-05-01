package com.bpn.diplom.gui.utils;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class DataModel extends AbstractTableModel {
	
	ArrayList<Object[]> listRows = new ArrayList<Object[]>();
	private final String[] header;
	private final int columnCount;
	private int rowCount = 0;
	private Class<?>[] columnClass;
	
	/**
	 * 
	 * @param header - массив строк - заглавие столбцов
	 * @param columnCount - количество столбцов
	 */
	public DataModel(String[] header, Class<?>[] columnClass){
		super();
		this.header=header;
		this.columnCount=header.length;
		this.columnClass = columnClass;
	}
	
	public String getColumnName(int column) {
		return header[column];
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public Object getValueAt(int row, int col) {
		return listRows.get(row)[col];
	}

	public void setValueAt(Object val, int row, int col) {
		listRows.get(row)[col] = (val==null ? "":val.toString());
//		fireTableDataChanged(); // обновление таблицы после изменений
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void addRow(Object[] values) {
		if(values.length != columnCount){
			return;
		}
		listRows.add(new String[columnCount]);
		for(int i=0; i<columnCount; i++){
			setValueAt(values[i], 	rowCount, i);
		}
		rowCount++;
		fireTableDataChanged(); // обновление таблицы после изменений
	}
	
	public Class<?> getColumnClass(int columnIndex){
		return columnClass[columnIndex];
    }
	
	public boolean removeRow(int indexRow){
		if(indexRow<0)
			return false;
		listRows.remove(indexRow);
		rowCount -= 1;
		return true;
	}
	
	public boolean removeAllRows(){
		listRows.clear();
		rowCount = 0;
		return true;
	}
}