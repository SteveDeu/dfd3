package com.bpn.diplom.gui.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableCellRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setHorizontalAlignment(CENTER);
		setText(value.toString());
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);

		if (isSelected) {
			setBackground(Color.LIGHT_GRAY);
			setForeground(Color.BLACK);
		}
		return this;

	}
}
