package pt.uminho.anote2.aibench.resources.views;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LookupTables;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class LookupTablesView extends JPanel implements Observer{


	private static final long serialVersionUID = 1075013594569256121L;
	
	private LookupTables lookupTAble;
	private JTable lookTable;
	private JScrollPane dicsScrollPane;
	private Connection connection;
	private JButton jButtonHelp;
	private JPanel jPanelLookupTAbles;
	private JPanel jPanelLookupTableOperations;
	private JButton jButtonAddDic;

	public LookupTablesView(LookupTables lookupTAble) throws SQLException{
		initGUI();
		this.lookupTAble = lookupTAble;
		this.lookupTAble.addObserver(this);
		populateGUI();
	}
	
	private void initGUI() {
		{
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.columnWidths = new int[] {7, 7, 7, 7};
				thisLayout.rowWeights = new double[] {0.1, 0.0};
				thisLayout.rowHeights = new int[] {7, 7};
				thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				this.setLayout(thisLayout);
				{
					this.add(getJPanelLookupTAbles(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					this.add(getJPanelLookupTableOperations(), new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}

	}
	
	private TableModel getTableModel() throws SQLException{
		
		String[] columns = new String[] {"Id", "Name","Notes"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		ResultSet res = this.lookupTAble.getResources().getResourceFielsByType("lookuptable");
		
		ArrayList<Values> values = new ArrayList<Values>();
		
		while(res.next())
			values.add(new Values(res.getInt(1), res.getString(2),res.getString(3)));
		
		data = new Object[values.size()][3];

		for(Values val : values)
		{
			Object[] objs = val.getObjects();
			
			data[i][0] = objs[0];
			data[i][1] = objs[1];
			data[i][2] = objs[2];
						
			tableModel.addRow(data[i++]);
		}
		
		return tableModel;
	}
	
	private void populateGUI() throws SQLException{
		
		lookTable.setModel(getTableModel());	
		completeTable();
	}
	
	class Values{
		private Object[] objs;
		
		public Values(Integer a, String b, String c) {
			this.objs = new Object[4];
			this.objs[0] = a;
			this.objs[1] = b;
			if(c==null)
			{
				this.objs[2] = "";
			}
			else
			{
				this.objs[2] = c;
			}
		}
		public Object[] getObjects() {
			return this.objs;
		}
	}

	public void update(Observable arg0, Object arg1) {
		try {
			this.populateGUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private void completeTable()
	{
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Load");
		viewColumn.setMinWidth(45);
		viewColumn.setMaxWidth(45);
		viewColumn.setPreferredWidth(45);
		viewColumn.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						addRules();
					}
				});
				return viewButton;
			}
				
		});
		
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						addRules();
					}



				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {}

			public void cancelCellEditing() {}

			public Object getCellEditorValue() { return null;}

			public boolean isCellEditable(EventObject arg0) { return true; }

			public void removeCellEditorListener(CellEditorListener arg0) {}

			public boolean shouldSelectCell(EventObject arg0) {return true;}

			public boolean stopCellEditing() { return true;}
		});
		
		lookTable.addColumn(viewColumn);
		
		lookTable.getColumnModel().getColumn(0).setMaxWidth(50);
		lookTable.getColumnModel().getColumn(0).setMinWidth(30);
		lookTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		lookTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		lookTable.getColumnModel().getColumn(3).setMaxWidth(45);
		lookTable.getColumnModel().getColumn(3).setMinWidth(45);
		lookTable.getColumnModel().getColumn(3).setPreferredWidth(45);
		lookTable.setRowHeight(20);
	}
	
	private void addRules() {		
		int id = (Integer) lookTable.getValueAt(lookTable.getSelectedRow(), 0);
		String note = (String) lookTable.getValueAt(lookTable.getSelectedRow(), 1);
		String info = (String) lookTable.getValueAt(lookTable.getSelectedRow(), 2);
		this.lookupTAble.addLookupTable(new LookupTableAibench(this.lookupTAble.getResources().getDb(),id,note,info));
	}

	
	private JButton getJButtonAddDic() {
		if(jButtonAddDic == null) {
			jButtonAddDic = new JButton();
			jButtonAddDic.setText("Add Lookup Table");
			jButtonAddDic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));
			jButtonAddDic.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.createlookuptable")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
		}
		return jButtonAddDic;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
	
	private JPanel getJPanelLookupTableOperations() {
		if(jPanelLookupTableOperations == null) {
			jPanelLookupTableOperations = new JPanel();
			jPanelLookupTableOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Lookup Table Operations", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelLookupTableOperationsLayout = new GridBagLayout();
			jPanelLookupTableOperationsLayout.rowWeights = new double[] {0.1};
			jPanelLookupTableOperationsLayout.rowHeights = new int[] {7};
			jPanelLookupTableOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelLookupTableOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelLookupTableOperations.setLayout(jPanelLookupTableOperationsLayout);
			jPanelLookupTableOperations.add(getJButtonAddDic(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLookupTableOperations.add(getJButtonHelp(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLookupTableOperations;
	}
	
	private JPanel getJPanelLookupTAbles() {
		if(jPanelLookupTAbles == null) {
			jPanelLookupTAbles = new JPanel();
			GridBagLayout jPanelLookupTAblesLayout = new GridBagLayout();
			jPanelLookupTAblesLayout.rowWeights = new double[] {0.1};
			jPanelLookupTAblesLayout.rowHeights = new int[] {7};
			jPanelLookupTAblesLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelLookupTAblesLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelLookupTAbles.setLayout(jPanelLookupTAblesLayout);
			jPanelLookupTAbles.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Load Lookup Tables for Clipboard", TitledBorder.LEADING, TitledBorder.TOP));

			{
				dicsScrollPane = new JScrollPane();
				jPanelLookupTAbles.add(dicsScrollPane, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				{
					TableModel dicsTableModel = 
						new DefaultTableModel();
					lookTable = new JTable(){
						
						private static final long serialVersionUID = -4090662450740771673L;
						
						@Override
						public boolean isCellEditable(int x,int y){
							if(y==3)
								return true;
							return false;
						}
					};				
					dicsScrollPane.setViewportView(lookTable);
					lookTable.setModel(dicsTableModel);		
				}
			}
		}
		return jPanelLookupTAbles;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"LookupTable_Create");
				}
			});
			
		}
		return jButtonHelp;
	}

}
