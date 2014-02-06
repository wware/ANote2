package pt.uminho.generic.csvloaders.tabTable.table.headertable;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pt.uminho.generic.csvloaders.tabTable.table.cellrender.HeaderRenderer;
import pt.uminho.generic.csvloaders.tabTable.table.headereditable.EditableHeader;

public class JHeaderTable extends JTable implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TableCellRenderer dhcr ;
	public TableCellEditor dhce = new EditableHeader();
	protected Map<String, TableCellRenderer> cellRenders = null;
	protected Map<String, TableCellEditor> cellEditor = null;
	protected MultiHeaderModel tm;
	
	
	public JHeaderTable(MultiHeaderModel model){
		this(model, new HashMap<String, TableCellRenderer>(), new HashMap<String, TableCellEditor>());
	}
	
	public JHeaderTable(MultiHeaderModel model, Map<String, TableCellRenderer> cellRenders,
			Map<String, TableCellEditor> cellEditor){
		super(model);
		dhcr = new HeaderRenderer(this);
		tm=model;
		
		this.cellRenders = cellRenders;
		this.cellEditor = cellEditor;
		updateHeadersHeight(25);
		setBetterHeight();
		this.setSelectionModel(new NullSelectionModel());
		
	}
	
	public void setCellEditor(int row, int column, TableCellEditor tce){
		
		String key = row+""+column;
		cellEditor.put(key, tce);
		tm.setEditable(row, column, true);
	}
	
	public void setCellRenders(int row, int column, TableCellRenderer tce){
		
		String key = row+""+column;
		cellRenders.put(key, tce);
	}
	
//	public Dimension setPreferredScrollableViewportSize
	
//	@Override
//	 public Component prepareRenderer(
//             TableCellRenderer renderer, int row, int col) {
//         if (row < numOfHeaders) {
//             return this.getTableHeader().getDefaultRenderer()
//                 .getTableCellRendererComponent(this,
//                 this.getValueAt(row, col), false, false, row, col);
//         } else {
//             return super.prepareRenderer(renderer, row, col);
//         }
//     }
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {

		TableCellRenderer ret = cellRenders.get(row+""+column);
		if(ret == null)
			ret = dhcr;
		
		return ret;
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		TableCellEditor ret =  cellEditor.get(row+""+column);
		
		if(ret == null)
			ret = dhce;
		
		return ret;
	}

	
	public void updateHeadersHeight(int height){
		for(int i =0; i <tm.getRowCount(); i++)
			setRowHeight(i, height);
		
		setBetterHeight();
	}
	
	private void setBetterHeight() {
		Dimension d = getPreferredScrollableViewportSize();
		double height = 0.0;
		for(int i=0; i < getRowCount();i++){
			
			height += getRowHeight(i);
		}
		d.setSize(d.width, height);
	}

//	/* Sample code */
//	private static final Object[] colNames = new Object[] {
//	"A", "B", "C"
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
//	
////	public static void main(String[] args) {
////		JFrame f = new JFrame();
////
////		
////		
////		MultiHeaderTableModel model = new MultiHeaderTableModel();
////		model.addHeader(colNames);
////		model.addHeader(rowData[0]);
////		
////		model.addRow(rowData[1]);
////		model.addRow(rowData[2]);
////		model.addRow(rowData[3]);
////		model.addRow(rowData[4]);
////		
////		System.out.println(model.getColumnCount());
////		
////		final JHeaderTable table = new JHeaderTable(model);
//////		table.setRowMargin(2);
////		
////		
////		TableCellEditor ed = new SpinnerEditor();
//////		table.setCellEditor(1, 0, ed);
////
//////		table.setCellEditor(1, 0, new ComboEditor(colNames));
//////		table.setCellEditor(1, 1, new ComboEditor(colNames));
////		
////		
////		DefaultTableModel model2 = new DefaultTableModel(rowData, colNames);
////		JTable rows = new JTable(model2);
////		
////		final JScrollPane sp = new JScrollPane(rows);
//////		table.setPreferredScrollableViewportSize(new Dimension(table.getWidth(), 60));
////	
////        SwingUtilities.invokeLater( new Runnable() {
////            public void run() {
////            	sp.setColumnHeaderView(table);
////            }
////        });
////		
////        
//////        JScrollPane pane = new JScrollPane();
//////		sp.setCorner(JScrollPane.UPPER_LEADING_CORNER, rows);
//////		sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, rows.getTableHeader()); 
//////		Table
////		
//////		sp.setColumnHeaderView(null);
////////		sp.setColumnHeader(jvp);
////		
////		f.add(sp, BorderLayout.CENTER);
////
////		f.pack();
////		f.setLocationRelativeTo(null);
////		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////		f.setVisible(true);
////		}
	
	
}
