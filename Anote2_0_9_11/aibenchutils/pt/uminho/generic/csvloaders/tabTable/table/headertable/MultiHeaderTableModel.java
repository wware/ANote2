package pt.uminho.generic.csvloaders.tabTable.table.headertable;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class MultiHeaderTableModel{


	protected MultiHeaderModel headersModel;
	protected DefaultTableModel datamodel;
	protected Map<String, TableCellRenderer> cellRenders = new HashMap<String, TableCellRenderer>();
	protected Map<String, TableCellEditor> cellEditor = new HashMap<String, TableCellEditor>();
	private int preferedColumnWidth = 150;
	
	
	public MultiHeaderTableModel() {
		super();
		headersModel = new MultiHeaderModel();
		datamodel = new DefaultTableModel();
//		headersModel.getValueAt(row, column)
	}
	
	public void addHeader(Object[] header){
		
		if(headersModel.getColumnCount()==0)
			headersModel.setColumnCount(header.length);
		headersModel.addRow(header);
	}
	
	public void addRow(Object[] row){
		
		if(datamodel.getColumnCount()==0){
			datamodel.setColumnCount(row.length);
		}
		datamodel.addRow(row);
	}

//	public void setEditable(int row, int column, boolean editable){
//		iscelleditable.put(key, editable);
//	}
	
	public Class<?> getColumnClass(int columnIndex) {
		return datamodel.getColumnClass(columnIndex);
	}

	public int getColumnCount() {
		return datamodel.getColumnCount();
	}

	public String getColumnName(int columnIndex) {
		return datamodel.getColumnName(columnIndex);
	}

	public int getRowCount() {
		return datamodel.getRowCount() + headersModel.getRowCount();
	}

	public int getNumberOfHeaders(){
		return headersModel.getRowCount();
	}

	public MultiHeaderModel getHeadersModel() {
		return headersModel;
	}
	
	public void setCellEditor(int row, int column, TableCellEditor tce){
		String key = row+""+column;
		cellEditor.put(key, tce);
		headersModel.setEditable(row, column, true);
	}
	
	public void setEditable(int row, int column, boolean editable){
		headersModel.setEditable(row, column, editable);
	}
	
	public void setCellRenders(int row, int column, TableCellRenderer tce){
		
		String key = row+""+column;
		cellRenders.put(key, tce);
	}

	public void removeTableModelListener(TableModelListener l) {
		datamodel.removeTableModelListener(l);
		
	}

	public void addTableModelListener(TableModelListener l) {
		headersModel.addTableModelListener(l);
		datamodel.addTableModelListener(l);
	}
	
	public JScrollPane getMultiHeaderTable(TableModelListener l){
		
		JTable rows = new JTable(datamodel);	
		JTable headers = new JHeaderTable(headersModel, cellRenders, cellEditor);
        headersModel.addTableModelListener(l);
		
        for(int i =0; i < rows.getColumnCount();i++){
			rows.getColumnModel().getColumn(i).setMinWidth(preferedColumnWidth);
			headers.getColumnModel().getColumn(i).setMinWidth(preferedColumnWidth);
        }
		JScrollPane sp = new JScrollPane(rows);
		
		CorrectStrangeBehaviourListener t = new CorrectStrangeBehaviourListener(rows, sp, headers);
		sp.addComponentListener(t);
		
		replaceColumnHeader(sp, headers);

        return sp;
		
	}
	
	private class CorrectStrangeBehaviourListener extends 
	ComponentAdapter {
		
		JTable mainTable = null;
		JTable headers = null;
		JScrollPane mainTableScrollPane=null;
		
		public CorrectStrangeBehaviourListener(JTable mainTable, JScrollPane mainTableScrollPane, JTable headers){
			super();
			this.mainTable = mainTable;
			this.mainTableScrollPane = mainTableScrollPane;
			this.headers = headers;
		}
		
		
	  public void componentResized(ComponentEvent e){
	    if (mainTable.getPreferredSize().width <=
	    mainTableScrollPane.getViewport().getExtentSize().width)
	    {
	      mainTable.setAutoResizeMode(
	        JTable.AUTO_RESIZE_ALL_COLUMNS);
	    } else {
	      mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	      headers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	    }
	  }
	}
	
	public static final void replaceColumnHeader(JScrollPane scrollPane, JComponent newHeader)
	{
		final class HeaderSwapper implements HierarchyListener
		{
			private final JComponent c;
			
			private HeaderSwapper(JComponent c)
			{
				this.c = c;
			}
			
			public void hierarchyChanged(HierarchyEvent e)
			{
				JScrollPane p = (JScrollPane) e.getSource();
				if (p.isShowing())
				{
					JViewport v = new JViewport();
					v.setView(this.c);
					p.setColumnHeader(v);
				}
			}
		}
		
		scrollPane.addHierarchyListener(new HeaderSwapper(newHeader));
	}
	
	
//	/* Sample code */
//	private static final Object[] colNames = new Object[] {
//	new Integer(1), "B", "C"
//	};
//	private static final Object[][] rowData = new Object[][] {
//	new Object[] { 1, "B", null },
//	new Object[] { 2, 20, "Foo" },
//	new Object[] { 3, 10, 10 },
//	new Object[] { 4, true, false },
//	new Object[] { 5, false, "Bar" },
//	new Object[] { 6, new Date(), true },
//	new Object[] { 7, 10, new Date() },
//	};
//	public static void main(String[] args) {
//		JFrame f = new JFrame();
//		
//		MultiHeaderTableModel model = new MultiHeaderTableModel();
//		model.addHeader(colNames);
//		model.addHeader(rowData[0]);
//		
//		model.addRow(rowData[1]);
//		model.addRow(rowData[2]);
//		model.addRow(rowData[3]);
//		model.addRow(rowData[4]);
//		
//		ComboRenderer cr = new ComboRenderer(colNames);
//		ComboEditor ce = new ComboEditor(colNames);
////		ItemListener listner = new ItemListener() {
////			
////			@Override
////			public void itemStateChanged(ItemEvent e) {				
////			}
////		};
//		model.setCellEditor(0, 0, new EditableHeader());
//		
//		for(int i =0; i < model.getColumnCount(); i++){
//			model.setCellRenders(1, i, cr);
//			model.setCellEditor(1, i, ce);
//		}
//		
//		System.out.println(model.getColumnCount());
//		System.out.println(model.getNumberOfHeaders());
//		
//		f.add(model.getMultiHeaderTable(), BorderLayout.CENTER);
//
//		f.pack();
//		f.setLocationRelativeTo(null);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setVisible(true);
//	}

}
