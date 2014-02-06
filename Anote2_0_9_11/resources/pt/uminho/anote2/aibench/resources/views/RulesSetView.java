package pt.uminho.anote2.aibench.resources.views;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
import pt.uminho.anote2.aibench.resources.gui.RemoveResourceGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;


public class RulesSetView extends JPanel implements Observer{
	
	private static final long serialVersionUID = 1075013594569256121L;
	
	private RulesSet rules;
	private JButton jButtonHelp;
	private JPanel jPanelRulesOperations;
	private JButton jButtonAddDic;
	private TableSearchPanel jTableSearch;


	public RulesSetView(RulesSet rules){
		initGUI();
		this.rules = rules;
		this.rules.addObserver(this);
		try {
			populateGUI();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {		
			TreatExceptionForAIbench.treatExcepion(e);
		}
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
	}
	
	private TableModel getTableModel() throws SQLException, DatabaseLoadDriverException{
		
		String[] columns = new String[] {"ID", "Name","Information","DB","Load"};
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 0 )?(Integer.class):String.class;
			}
			
		};
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		ResultSet res = this.rules.getResources().getResourceFielsByType(GlobalOptions.resourcesRuleSetName);
		
		ArrayList<Values> values = new ArrayList<Values>();
				
			while(res.next())
				values.add(new Values(res.getInt(1), res.getString(2),res.getString(3)));

		
		data = new Object[values.size()][5];

		for(Values val : values)
		{
			Object[] objs = val.getObjects();	
			data[i][0] = objs[0];
			data[i][1] = objs[1];
			data[i][2] = objs[2];
			data[i][3] = "";
			data[i][4] = "";
			tableModel.addRow(data[i++]);
		}
		return tableModel;
	}
	
	
	private void populateGUI() throws SQLException, DatabaseLoadDriverException{		
		jTableSearch.setModel(getTableModel());	
		completeTable(jTableSearch.getMainTable());
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
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void completeTable(JTable jtable)
	{
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(200);
		jtable.getColumnModel().getColumn(2).setMaxWidth(750);
		jtable.getColumnModel().getColumn(2).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(300);
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.loadfieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.loadfieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.loadfieldpreferredWith);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png"));
		jtable.getColumn("Load").setCellRenderer(new ButtonLoadResourceRenderer(icon2));
		jtable.getColumn("Load").setCellEditor(new ButtonLoadResourceCellEditor());

	}

	private void removeFromDB() {
		if(this.jTableSearch.getMainTable().getSelectedRows()[0]==-1)
		{
			
		}
		else
		{
			int id = (Integer) jTableSearch.getValueAt(this.jTableSearch.getMainTable().getSelectedRows()[0], 0);
			String note = (String) jTableSearch.getValueAt(this.jTableSearch.getMainTable().getSelectedRows()[0], 1);
			String info = (String) jTableSearch.getValueAt(this.jTableSearch.getMainTable().getSelectedRows()[0], 2);
			RulesAibench lex = new RulesAibench(id,note,info);
			new RemoveResourceGUI(lex);
		}
	}
	
	private void addRules() {		
		int id = (Integer) jTableSearch.getValueAt(this.jTableSearch.getMainTable().getSelectedRows()[0], 0);
		String note = (String) jTableSearch.getValueAt(this.jTableSearch.getMainTable().getSelectedRows()[0], 1);
		String info = (String) jTableSearch.getValueAt(this.jTableSearch.getMainTable().getSelectedRows()[0], 2);
		this.rules.addRules(new RulesAibench(id,note,info));
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

	/**
	 * @return
	 */
	private JPanel getJPanelRulesOperations() {
		if(jPanelRulesOperations == null) {
			jPanelRulesOperations = new JPanel();
			jPanelRulesOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
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
		if(jTableSearch == null) {
			jTableSearch = new TableSearchPanel();
			jTableSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Rule Sets", TitledBorder.LEADING, TitledBorder.TOP));	
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jTableSearch;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"RulesSet_Create");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() {
			removeFromDB();
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			removeFromDB();	
		}
		
	}
	
	class ButtonLoadResourceRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadResourceRenderer(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() {
			addRules();
		}

	}

	class ButtonLoadResourceCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			addRules();
		}

	}
	

}
