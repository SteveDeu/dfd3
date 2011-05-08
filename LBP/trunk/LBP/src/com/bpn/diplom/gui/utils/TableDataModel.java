package com.bpn.diplom.gui.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class TableDataModel extends AbstractTableModel {

	private Class[] types;
	private String[] header;
//	private int cols = 2;
	private List<Object[]> content = new ArrayList<Object[]>();
	private boolean editable = false; 
	
	public TableDataModel(String[] header, Class[] types){
		this.header = header;
		this.types = types;
//		this.cols = header.length; 
	}
	
	@Override
	public String getColumnName(int col) {
		return header[col];
	}
	
	@Override
	public int getRowCount() {
		return content.size();
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if(row >= 0 && content.size() >= row)
			return content.get(row)[col];
		else 
			throw new IllegalArgumentException();
	}

	@Override
	public void setValueAt(Object val, int row, int col) {
		if(content.size() >= row)
			content.get(row)[col] = val;
		else 
			throw new IllegalArgumentException();
		fireTableDataChanged(); // обновление таблицы после изменений
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return isEditable();
	}

	public void addRow(Object[] row) {
		if(row.length < header.length)
			throw new IllegalArgumentException();
		content.add(row);
		fireTableDataChanged(); // обновление таблицы после изменений
	}
	
	public Object[] removeRow(int row) {
		if(content.size() >= row)
			throw new IllegalArgumentException();
		Object[] removed = content.remove(row);
		fireTableDataChanged(); // обновление таблицы после изменений
		return removed;
	}
	
	public void removeAllRows() {
		content.clear();
		fireTableDataChanged(); // обновление таблицы после изменений
	}

	@Override
	public Class getColumnClass(int col) {
		return types[col];
	}
	
	
	
	public synchronized boolean isEditable() {
		return editable;
	}

	public synchronized void setEditable(boolean editable) {
		this.editable = editable;
	}


}
