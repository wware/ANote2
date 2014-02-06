package pt.uminho.anote2.aibench.resources.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResourceElement;
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
public class RulesView extends JPanel implements Observer{


	private static final long serialVersionUID = 3739180981313329259L;
	private JPanel jPanelDicInfo;
	private JButton jButtonHelp;
	private JTable jTableDicContent;
	private JScrollPane jScrollPaneContent;
	private JButton jButtonEditNote;
	private JButton jButtonEditName;
	private JButton jButton;
	private JTextPane jTextPaneNote;
	private JTextPane jTextPaneName;
	private JLabel jLabelInfo;
	private JLabel jLabelDicName;
	private JPanel jPanelOperations;
	private JPanel jPanelContent;

	private RulesAibench rules;
	private List<IResourceElement> elems;
	private Map<Integer,String> classIDclass;
	
	public RulesView(RulesAibench rules)
	{
		this.rules=rules;
		this.rules.addObserver(this);
		elems = new ArrayList<IResourceElement>();
		findRules();
		initGUI();
	}
	
	private void findRules() {
		elems = rules.getRules();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 244, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};	
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelDicInfo = new JPanel();
				GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
				jPanelDicInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "LookupTable Information", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelDicInfo, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDicInfoLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				jPanelDicInfoLayout.rowHeights = new int[] {15, 7, 7, 7};
				jPanelDicInfoLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.1};
				jPanelDicInfoLayout.columnWidths = new int[] {80, 339, 7, 7};
				jPanelDicInfo.setLayout(jPanelDicInfoLayout);
				{
					jLabelDicName = new JLabel();
					jPanelDicInfo.add(jLabelDicName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelDicName.setText("Name");
				}
				{
					jLabelInfo = new JLabel();
					jPanelDicInfo.add(jLabelInfo, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelInfo.setText("Notes");
				}
				{
					jTextPaneName = new JTextPane();
					jTextPaneName.setText(rules.getName());
					jPanelDicInfo.add(jTextPaneName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextPaneNote = new JTextPane();
					jPanelDicInfo.add(jTextPaneNote, new GridBagConstraints(1, 2, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jTextPaneNote.setText(rules.getInfo());
				}
				{
					jButtonEditName = new JButton();
					jPanelDicInfo.add(jButtonEditName, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonEditName.setText("Edit");
				}
				{
					jButtonEditNote = new JButton();
					jPanelDicInfo.add(jButtonEditNote, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonEditNote.setText("Edit");
				}
			}
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				jPanelContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "LookupTable Elements Information", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelContent, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelContent.setLayout(jPanelContentLayout);
				{
					jScrollPaneContent = new JScrollPane();
					jPanelContent.add(jScrollPaneContent, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jScrollPaneContent.setViewportView(getJTableDicContent());
				}
			}
			{
				jPanelOperations = new JPanel();
				GridBagLayout jPanelOperationsLayout = new GridBagLayout();
				jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "LookupTable Operations", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelOperations, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelOperationsLayout.rowHeights = new int[] {7, 7, 7};
				jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelOperations.setLayout(jPanelOperationsLayout);
				{
					jButton = new JButton();
					jPanelOperations.add(jButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelOperations.add(getJButtonHelp(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButton.setText("Add Rule");
					jButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh22.png")));
					jButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
								if (def.getID().equals("operations.createrule")){			
									Workbench.getInstance().executeOperation(def);
									return;
								}
							}
						}
					});
				}
			}
			this.setSize(900, 600);
			constructTable();
		}

	}
	
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Term","Class"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		try {
			classIDclass = ResourcesHelp.getClassIDClassOnDatabase(rules.getDb());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		data = new Object[elems.size()][2];
		int i=0;
		for(IResourceElement elem:elems)
		{
			data[i][0] = elem.getTerm();
			data[i][1] = classIDclass.get(elem.getTermClassID());				
			tableModel.addRow(data[i]);
		}	
		return tableModel;		
	}
	
	private void constructTable()
	{
		jTableDicContent.getColumnModel().getColumn(1).setMaxWidth(150);
		jTableDicContent.getColumnModel().getColumn(1).setMinWidth(150);
		jTableDicContent.getColumnModel().getColumn(1).setPreferredWidth(150);
		jTableDicContent.setRowHeight(20);		
		jTableDicContent.setRowHeight(25);
		TableColumn viewColumn2 = new TableColumn();
		viewColumn2.setHeaderValue("UP");
		viewColumn2.setMinWidth(50);
		viewColumn2.setMaxWidth(50);
		viewColumn2.setPreferredWidth(50);
		viewColumn2.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/high.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						changePrioretyUP();
					}
				});
				return viewButton;
			}			
		});
		
		viewColumn2.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/high.png")));
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						changePrioretyUP();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return true;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return false;
			}

			public boolean stopCellEditing() {
				return true;
			}
				
		});
		
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Down");
		viewColumn.setMinWidth(50);
		viewColumn.setMaxWidth(50);
		viewColumn.setPreferredWidth(50);
		viewColumn.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/low.png")));
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						changePrioretyDown();
					}
				});
				return viewButton;
			}			
		});
		
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/low.png")));
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						changePrioretyDown();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return true;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return true;
			}

			public boolean stopCellEditing() {
				return true;
			}
				
		});
		jTableDicContent.addColumn(viewColumn2);
		jTableDicContent.addColumn(viewColumn);	
	}
	
	private void changePrioretyUP() {
		int selectLine = jTableDicContent.getSelectedRow();
		if(selectLine==0)
		{
			Workbench.getInstance().warn("This rules have a highest priorety");
		}
		else if(selectLine!=-1&&jTableDicContent.getModel().getRowCount()>1)
		{
			IResourceElement elemSelect = elems.get(selectLine);
			IResourceElement changeElem = elems.get(selectLine-1);
			rules.changePriorety(elemSelect, changeElem);
			rules.notifyViewObservers();
		}
	}
	
	private void changePrioretyDown() {
		int selectLine = jTableDicContent.getSelectedRow();	
		if(selectLine==jTableDicContent.getRowCount()-1)
		{
			Workbench.getInstance().warn("This rules have a lowest priorety");
		}
		else if(jTableDicContent.getRowCount()>1)
		{
			IResourceElement elemSelect = elems.get(selectLine);
			IResourceElement changeElem = elems.get(selectLine+1);
			rules.changePriorety(changeElem, elemSelect);
			rules.notifyViewObservers();
		}
	}
	
	public void update(Observable arg0, Object arg1) 
	{
		findRules();
		TableModel jTableDicContentModel = fillModelPubTable();
		jTableDicContent.setModel(jTableDicContentModel );
		constructTable();
		jTableDicContent.updateUI();
	}
	
	private JTable getJTableDicContent() {
		if(jTableDicContent==null)
		{
			jTableDicContent = new JTable();
			TableModel jTableDicContentModel = fillModelPubTable();
			jTableDicContent.setModel(jTableDicContentModel );
		}
		return jTableDicContent;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"RulesSet_Add_Rule");
				}
			});
		}
		return jButtonHelp;
	}

}

