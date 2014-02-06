package pt.uminho.anote2.aibench.corpus.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.RelationDetailsGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;



public class RERelationsViewer extends JPanel{

	private static final long serialVersionUID = 1L;
	private TableSearchPanel environmentalConditionTable;
	private JButton jButtonHelp;
	private JPanel jPanelHeader;
	private RESchema reprocess;
	
	public RERelationsViewer(RESchema reprocess)
	{
		this.reprocess = reprocess;
		initGUI();
		try {
			fillView();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}


	private void initGUI() 
	{
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(524, 289));
			thisLayout.rowWeights = new double[] {0.05, 0.95};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelHeader = new JPanel();
				GridBagLayout jPanelHeaderLayout = new GridBagLayout();
				this.add(jPanelHeader, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelHeaderLayout.rowWeights = new double[] {0.1};
				jPanelHeaderLayout.rowHeights = new int[] {7};
				jPanelHeaderLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelHeaderLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelHeader.setLayout(jPanelHeaderLayout);
			}
			{
				environmentalConditionTable = new TableSearchPanel(true);
				this.add(environmentalConditionTable, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jButtonHelp = getJButtonHelp();
					environmentalConditionTable.add(jButtonHelp, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
					jButtonHelp.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try {
								Help.internetAccess(GlobalOptions.wikiGeneralLink+"Process_Relations_view");
							} catch (IOException e1) {
								TreatExceptionForAIbench.treatExcepion(e1);
							}
						}
					});
				}
			}
		}

	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Process_Relations_view");
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}
	
	private void fillView() throws SQLException, DatabaseLoadDriverException {		
		TableModel tableModel = fillModelPubTable();
		environmentalConditionTable.setModel(tableModel);
		complementTable(environmentalConditionTable.getMainTable());
	}
	
	private TableModel fillModelPubTable() throws SQLException, DatabaseLoadDriverException{
		String[] columns = new String[] {"Clue(Verb)", "Lemma(Clue)", "Entities At Left", "Entities At Right","Polarity","Directionally","ID","Details"};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 6)?(Integer.class):String.class;
			}
			
		};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;		
		int relatiosNumber = reprocess.getEventsAnnotations().size();
		data = new Object[relatiosNumber][8];
		for(int relationID:reprocess.getEventsAnnotations().keySet())
		{
			String verb = new String();
			String lemma = new String();
			IEventAnnotation annot = reprocess.getEventsAnnotations().get(relationID);
			if(annot.getEventClue()!=null)
				verb = annot.getEventClue();
			if(annot.getEventProperties().getLemma()!=null)
				lemma = annot.getEventProperties().getLemma();
			data[i][0] = verb;
			data[i][1] = lemma;			
			data[i][2] = convertEntitiesToString(annot.getEntitiesAtLeft());
			data[i][3] = convertEntitiesToString(annot.getEntitiesAtRight());
			data[i][4] = annot.getEventProperties().getPolarity().toString();
			data[i][5] = annot.getEventProperties().getDirectionally().toString();
			data[i][6] = relationID;
			data[i][7] = "";
			tableModel.addRow(data[i]);
			i++;
		}		
		return tableModel;		
	}
	
	private String convertEntitiesToString(List<IEntityAnnotation> entities) {
		String entityToString = new String();
		for(IEntityAnnotation ent:entities)
		{
			entityToString = entityToString + ent.getAnnotationValue() + " (" + ClassProperties.getClassIDClass().get(ent.getClassAnnotationID()) + ") \n";
		}
		return entityToString + "\n";
	}

	private void initViewGUI() 
	{	
		int relationID = (Integer) environmentalConditionTable.getMainTable().getValueAt(environmentalConditionTable.getMainTable().getSelectedRow(), 6);
		IEventAnnotation event;
		try {
			event = reprocess.getEventsAnnotations().get(relationID);
			new RelationDetailsGUI(event,reprocess);	
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void complementTable(JTable jtable){	
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMinWidth(75);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setMinWidth(120);
		jtable.getColumnModel().getColumn(3).setMinWidth(120);
		jtable.getColumnModel().getColumn(4).setMinWidth(75);
		jtable.getColumnModel().getColumn(5).setMinWidth(85);
		jtable.getColumnModel().getColumn(6).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(6).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(6).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);	
		jtable.getColumnModel().getColumn(7).setMaxWidth(PreferencesSizeComponents.moreinfofieldmaxWith);
		jtable.getColumnModel().getColumn(7).setMinWidth(PreferencesSizeComponents.moreinfofieldminWith);
		jtable.getColumnModel().getColumn(7).setPreferredWidth(PreferencesSizeComponents.moreinfofieldpreferredWith);	
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/find.png"));
		jtable.getColumn("Details").setCellRenderer(new ButtonTableFindRenderer(icon));
		jtable.getColumn("Details").setCellEditor(new ButtonTableFindCellEditor());
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();
		for (int j = 0; j < 7; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
		}		
		for (int k = 0; k<jtable.getRowCount(); k++){
			for (int j = 0; j<jtable.getColumnCount(); j++){
				jtable.getCellRenderer(k, j).getTableCellRendererComponent(jtable, jtable.getValueAt(k, j), false, false, k,j);
			}
		}	
	}
	
	class ButtonTableFindRenderer extends ButtonTableRenderer
	{
		private static final long serialVersionUID = 1L;
		
		public ButtonTableFindRenderer(ImageIcon icon)
		{
			super(icon);
		}

		public void whenClick() {
			initViewGUI();
		}
		
	}
	
	class ButtonTableFindCellEditor extends ButtonTableCellEditor
	{

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			initViewGUI();
		}
		
	}
}
