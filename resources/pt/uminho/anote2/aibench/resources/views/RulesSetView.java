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

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
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
public class RulesSetView extends JPanel implements Observer{
	
	private static final long serialVersionUID = 1075013594569256121L;
	
	private RulesSet rules;
	private JTable rulesSetTable;
	private JScrollPane dicsScrollPane;
	private Connection connection;
	private JButton jButtonHelp;
	private JPanel jPanelRulesSet;
	private JPanel jPanelRulesOperations;
	private JButton jButtonAddDic;

	public RulesSetView(RulesSet rules) throws SQLException{
		initGUI();
		this.rules = rules;
		this.rules.addObserver(this);
		populateGUI();
	}
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			this.setLayout(thisLayout);
			{
				this.add(getJPanelRulesSet(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				this.add(getJPanelRulesOperations(), new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			//this.add(, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			//this.add(, new GridBagConstraints(0, 0, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}
	
	private TableModel getTableModel() throws SQLException{
		
		String[] columns = new String[] {"Id", "Name", "Notes"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		ResultSet res = this.rules.getResources().getResourceFielsByType("rules");
		
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
		rulesSetTable.setModel(getTableModel());	
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
		viewColumn.setMinWidth(35);
		viewColumn.setMaxWidth(35);
		viewColumn.setPreferredWidth(35);
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
		
		rulesSetTable.addColumn(viewColumn);
		rulesSetTable.getColumnModel().getColumn(0).setMaxWidth(50);
		rulesSetTable.getColumnModel().getColumn(0).setMinWidth(30);
		rulesSetTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		rulesSetTable.getColumnModel().getColumn(3).setMaxWidth(45);
		rulesSetTable.getColumnModel().getColumn(3).setMinWidth(45);
		rulesSetTable.getColumnModel().getColumn(3).setPreferredWidth(45);
		rulesSetTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		rulesSetTable.setRowHeight(20);
	}
	
	private void addRules() {		
		int id = (Integer) rulesSetTable.getValueAt(rulesSetTable.getSelectedRow(), 0);
		String note = (String) rulesSetTable.getValueAt(rulesSetTable.getSelectedRow(), 1);
		String info = (String) rulesSetTable.getValueAt(rulesSetTable.getSelectedRow(), 2);
		this.rules.addRules(new RulesAibench(this.rules.getResources().getDb(),id,note,info));
	}


	
	private JButton getJButtonAddDic() {
		if(jButtonAddDic == null) {
			jButtonAddDic = new JButton();
			jButtonAddDic.setText("Add RulesSet");
			jButtonAddDic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));
			jButtonAddDic.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.createruleset")){			
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
	/**
	 * @return
	 */
	private JPanel getJPanelRulesOperations() {
		if(jPanelRulesOperations == null) {
			jPanelRulesOperations = new JPanel();
			jPanelRulesOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "RuleSet Operations", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelRulesOperationsLayout = new GridBagLayout();
			jPanelRulesOperationsLayout.rowWeights = new double[] {0.1};
			jPanelRulesOperationsLayout.rowHeights = new int[] {7};
			jPanelRulesOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelRulesOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelRulesOperations.setLayout(jPanelRulesOperationsLayout);
			jPanelRulesOperations.add(getJButtonAddDic(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRulesOperations.add(getJButtonHelp(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRulesOperations;
	}
	
	private JPanel getJPanelRulesSet() {
		if(jPanelRulesSet == null) {
			jPanelRulesSet = new JPanel();
			GridBagLayout jPanelRulesSetLayout = new GridBagLayout();
			jPanelRulesSetLayout.rowWeights = new double[] {0.1, 0.0};
			jPanelRulesSetLayout.rowHeights = new int[] {7, 7};
			jPanelRulesSetLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelRulesSetLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelRulesSet.setLayout(jPanelRulesSetLayout);
			jPanelRulesSet.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Load RuleSet for Clipboard", TitledBorder.LEADING, TitledBorder.TOP));
			{
				dicsScrollPane = new JScrollPane();
				jPanelRulesSet.add(dicsScrollPane, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel dicsTableModel = 
						new DefaultTableModel();
					rulesSetTable = new JTable(){
						
						private static final long serialVersionUID = -4090662450740771673L;
						
						@Override
						public boolean isCellEditable(int x,int y){
							if(y==3)
								return true;
							return false;
						}
					};			
					dicsScrollPane.setViewportView(rulesSetTable);
					rulesSetTable.setModel(dicsTableModel);	
				}
			}
		}
		return jPanelRulesSet;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"RulesSet_Create");
				}
			});
		}
		return jButtonHelp;
	}

}