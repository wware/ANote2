package pt.uminho.generic.csvloaders.tabTable.table;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import pt.uminho.generic.csvloaders.tabTable.table.celleditor.ButtonEditor;
import pt.uminho.generic.csvloaders.tabTable.table.celleditor.SpinnerEditor;
import pt.uminho.generic.csvloaders.tabTable.table.cellrender.ButtonRenderer;
import pt.uminho.generic.csvloaders.tabTable.table.cellrender.SpinnerRenderer;

public class JTableUtils {
	
	
	static public void setColumnLayoutAsButton(JTable table, int columnIdx){
		
		ButtonEditor editor = new ButtonEditor();
		ButtonRenderer render = new ButtonRenderer();
		
		setCellEditor(table, columnIdx, editor);
		setCellRender(table, columnIdx, render);
		
	}
	
	static public void setColumnLayoutAsButton(JTable table, int columnIdx, String text){
		
		ButtonEditor editor = new ButtonEditor(text);
		ButtonRenderer render = new ButtonRenderer(text);
		
		setCellEditor(table, columnIdx, editor);
		setCellRender(table, columnIdx, render);
	}
	
	static public void setColumnLayoutAsButton(JTable table, int columnIdx, Icon icon){
		
		ButtonEditor editor = new ButtonEditor(icon);
		ButtonRenderer render = new ButtonRenderer(icon);
		
		setCellEditor(table, columnIdx, editor);
		setCellRender(table, columnIdx, render);
	}
	
	
	static public void setColumnLayoutAsSpinner(JTable table, int columnIdx){
		
		SpinnerEditor editor = new SpinnerEditor();
		SpinnerRenderer render = new SpinnerRenderer();
		
		setCellEditor(table, columnIdx, editor);
		setCellRender(table, columnIdx, render);
		table.setRowHeight(23);
	}
	
	
	
	static public void setCellRender(JTable table, int columnIdx, TableCellRenderer render){
		TableColumn o = table.getColumnModel().getColumn(columnIdx);
		o.setCellRenderer(render);
	}
	
	static public void setCellEditor(JTable table, int columnIdx, TableCellEditor editor){
		TableColumn o = table.getColumnModel().getColumn(columnIdx);
		o.setCellEditor(editor);
	}
	
	static public void setColumnWidth(JTable table, int columnIdx, int width ){
		TableColumn o = table.getColumnModel().getColumn(columnIdx);
		
		o.setWidth(width);
		o.setMaxWidth(width);
		o.setMinWidth(width);
	}

	public static void setRowHeight(JTable table, int row) {
		table.setRowHeight(row);		
	}

}
