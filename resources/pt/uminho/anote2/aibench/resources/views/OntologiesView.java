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

import pt.uminho.anote2.aibench.resources.datatypes.Ontologies;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
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
public class OntologiesView extends JPanel implements Observer{


	private static final long serialVersionUID = 1075013594569256121L;
	
	private Ontologies ontologies;
	private JTable ontologyTable;
	private Connection connection;
	private JScrollPane jScrollPaneOntologies;
	private JPanel jPanelOntologies;
	private JPanel jPanelOperations;
	private JButton jButtonHelp;
	private JButton jButtonAddDic;

	public OntologiesView(Ontologies ontologies) throws SQLException{
		initGUI();
		this.ontologies = ontologies;
		this.ontologies.addObserver(this);
		populateGUI();
	}
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(560, 267));
			thisLayout.rowWeights = new double[] {0.1, 0.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {7, 85, 102, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				this.add(getJPanelOperations(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getJPanelOntologies(), new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel dicsTableModel = 
						new DefaultTableModel();
					ontologyTable = new JTable(){

						private static final long serialVersionUID = -4090662450740771673L;

						@Override
						public boolean isCellEditable(int x,int y){
							if(y==3)
								return true;
							return false;
						}
					};
					
					jScrollPaneOntologies.setViewportView(ontologyTable);
					ontologyTable.setModel(dicsTableModel);
					
				}
			}
		}

	}
	
	private TableModel getTableModel() throws SQLException{
		
		String[] columns = new String[] {"Id", "Name", "Notes"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		ResultSet res = this.ontologies.getResources().getResourceFielsByType("ontology");
		
		ArrayList<Values> values = new ArrayList<Values>();
		
		while(res.next())
			values.add(new Values(res.getInt(1), res.getString(2),res.getString(3)));
		
		data = new Object[values.size()][4];

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
	
	private void populateGUI() throws SQLException{

		ontologyTable.setModel(getTableModel());	
		ontologyTable.getColumnModel().getColumn(0).setMaxWidth(50);
		ontologyTable.getColumnModel().getColumn(0).setMinWidth(30);
		ontologyTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		completeTable();
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
		
		ontologyTable.addColumn(viewColumn);
	}
	
	private void addRules() {		
		int id = (Integer) ontologyTable.getValueAt(ontologyTable.getSelectedRow(), 0);
		String note = (String) ontologyTable.getValueAt(ontologyTable.getSelectedRow(), 1);
		String info = (String) ontologyTable.getValueAt(ontologyTable.getSelectedRow(), 2);
		this.ontologies.addOntology(new OntologyAibench(this.ontologies.getResources().getDb(),id,note,info));
	}

	
	private JButton getJButtonAddDic() {
		if(jButtonAddDic == null) {
			jButtonAddDic = new JButton();
			jButtonAddDic.setText("Add Ontology");
			jButtonAddDic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));;
			jButtonAddDic.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.createontology")){			
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
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Ontology_Create");
				}
			});
			
		}
		return jButtonHelp;
	}
	
	private JPanel getJPanelOperations() {
		if(jPanelOperations == null) {
			jPanelOperations = new JPanel();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Ontologies Operations", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7, 7, 7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			jPanelOperations.add(getJButtonAddDic(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOperations.add(getJButtonHelp(), new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelOperations;
	}
	
	private JPanel getJPanelOntologies() {
		if(jPanelOntologies == null) {
			jPanelOntologies = new JPanel();
			jPanelOntologies.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Ontologies for Clipboard", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelOntologiesLayout = new GridBagLayout();
			jPanelOntologiesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOntologiesLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelOntologiesLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOntologiesLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOntologies.setLayout(jPanelOntologiesLayout);
			jPanelOntologies.add(getJScrollPaneOntologies(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelOntologies;
	}
	
	private JScrollPane getJScrollPaneOntologies() {
		if(jScrollPaneOntologies == null) {
			jScrollPaneOntologies = new JScrollPane();
		}
		return jScrollPaneOntologies;
	}

}
