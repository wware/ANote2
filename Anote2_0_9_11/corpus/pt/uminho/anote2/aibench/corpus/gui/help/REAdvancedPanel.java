package pt.uminho.anote2.aibench.corpus.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.RelationDetailsGUI;
import pt.uminho.anote2.aibench.corpus.gui.wizard.reexport.REToXGMMLWizardStep1;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;

public class REAdvancedPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelInfo;
	private JTextField jTextFieldDuration;
	private JTextField jTextFieldEntities;
	private JTextField jTextFieldRelations;
	private JLabel jLabelDuration;
	private JLabel jLabelEntities;
	private JLabel jLabelRelations;
	private JPanel jPanelOperations;
	private IREProcessReport report;
	private RESchema reProcess;
	private JButton jButtonExportToXGMML;
	private JButton jButtonExportToCSV;
	private TableSearchPanel relations;

	public REAdvancedPanel(IREProcessReport report) throws SQLException, DatabaseLoadDriverException
	{
		this.report = report;
		this.reProcess = new RESchema(report.getREProcess().getID(),report.getREProcess().getCorpus(),report.getREProcess().getName(),
				report.getREProcess().getType(),report.getREProcess().getProperties());
		initGUI();
		completeGUI();
	}
	
	
	private void completeGUI() throws SQLException, DatabaseLoadDriverException {
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextFieldEntities.setText(String.valueOf(report.getNumberOFEntities()));
		jTextFieldRelations.setText(String.valueOf(report.getNumberOfRelations()));	
		completeTable();
	}


	private void completeTable() throws SQLException, DatabaseLoadDriverException {
		TableModel tableModel = fillModelPubTable();
		relations.setModel(tableModel);
		complementTableButtons(relations.getMainTable());		
	}


	private void complementTableButtons(JTable jtable) {
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

	private void initGUI() {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				this.setPreferredSize(new java.awt.Dimension(702, 586));
				thisLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
				thisLayout.rowHeights = new int[] {7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1};
				thisLayout.columnWidths = new int[] {7};
				this.setLayout(thisLayout);
				{
					jPanelInfo = new JPanel();
					GridBagLayout jPanelInfoLayout = new GridBagLayout();
					this.add(jPanelInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelInfoLayout.rowWeights = new double[] {0.0};
					jPanelInfoLayout.rowHeights = new int[] {7};
					jPanelInfoLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.1, 0.1};
					jPanelInfoLayout.columnWidths = new int[] {7, 20, 20, 7, 7, 7};
					jPanelInfo.setLayout(jPanelInfoLayout);
					jPanelInfo.setBorder(BorderFactory.createTitledBorder("General Information"));
					{
						jLabelRelations = new JLabel();
						jPanelInfo.add(jLabelRelations, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
						jLabelRelations.setText("Relations :");
					}
					{
						jLabelEntities = new JLabel();
						jPanelInfo.add(jLabelEntities, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
						jLabelEntities.setText("Entities :");
					}
					{
						jLabelDuration = new JLabel();
						jPanelInfo.add(jLabelDuration, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jLabelDuration.setText("Processing Time :");
					}
					{
						jTextFieldRelations = new JTextField();
						jTextFieldRelations.setEditable(false);
						jPanelInfo.add(jTextFieldRelations, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
					}
					{
						jTextFieldEntities = new JTextField();
						jPanelInfo.add(jTextFieldEntities, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
						jTextFieldEntities.setEditable(false);
					}
					{
						jTextFieldDuration = new JTextField();
						jTextFieldDuration.setEditable(false);
						jPanelInfo.add(jTextFieldDuration, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 10), 0, 0));
					}
				}
				{
					relations = new TableSearchPanel(false);
					this.add(relations, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					relations.setBorder(BorderFactory.createTitledBorder("Relations"));
				}
				{
					jPanelOperations = new JPanel();
					GridBagLayout jPanelOperationsLayout = new GridBagLayout();
					this.add(jPanelOperations, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelOperationsLayout.rowWeights = new double[] {0.1};
					jPanelOperationsLayout.rowHeights = new int[] {7};
					jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelOperations.setLayout(jPanelOperationsLayout);
					jPanelOperations.setBorder(BorderFactory.createTitledBorder("Export Options"));
					{
						jButtonExportToCSV = new JButton();
						jPanelOperations.add(jButtonExportToCSV, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jButtonExportToCSV.setText("Export to TSV");
						ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/saveAll.png"));
						jButtonExportToCSV.setIcon(icon);
						jButtonExportToCSV.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								try {
									if(reProcess.getAllEvents().size()>0)
									{
										new REAdvanceExportCSVGUI(reProcess);
									}
									else
									{
										Workbench.getInstance().warn("No Relations to Export");
									}
								} catch (SQLException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								} catch (DatabaseLoadDriverException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								}
							}
						});

					}
					{
						jButtonExportToXGMML = new JButton();
						jPanelOperations.add(jButtonExportToXGMML, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jButtonExportToXGMML.setText("Export to Cytoscape (XGMML)");
						ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/exportretoxgmml.png"));
						jButtonExportToXGMML.setIcon(icon2);
						jButtonExportToXGMML.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									if(reProcess.getAllEvents().size()>0)
									{
										new REToXGMMLWizardStep1(reProcess);
									}
									else
									{
										Workbench.getInstance().warn("No Relations to Export");
									}
								} catch (SQLException e1) {
									TreatExceptionForAIbench.treatExcepion(e1);
								} catch (DatabaseLoadDriverException e1) {
									TreatExceptionForAIbench.treatExcepion(e1);
								}
							}
						});
						
					}
				}
			}
	}
	
	private TableModel fillModelPubTable() throws SQLException, DatabaseLoadDriverException{
		String[] columns = new String[] {"Verb (Clue)", "Lemma (Clue)", "Left Entities", "Right Entities","Polarity","Directionally","ID","Details"};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 6)?(Integer.class):String.class;
			}
			
		};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;		
		int relatiosNumber = reProcess.getEventsAnnotations().size();
		data = new Object[relatiosNumber][8];
		for(int relationID:reProcess.getEventsAnnotations().keySet())
		{
			String verb = new String();
			String lemma = new String();
			String polarity = "unknown";
			String directionally = "unknown";
			IEventAnnotation annot = reProcess.getEventsAnnotations().get(relationID);
			if(annot.getEventClue()!=null)
				verb = annot.getEventClue();
			if(annot.getEventProperties().getLemma()!=null)
				lemma = annot.getEventProperties().getLemma();
			data[i][0] = verb;
			data[i][1] = lemma;			
			data[i][2] = convertEntitiesToString(annot.getEntitiesAtLeft());
			data[i][3] = convertEntitiesToString(annot.getEntitiesAtRight());
			data[i][4] = annot.getEventProperties().getPolarity().toString();
			data[i][5] =  annot.getEventProperties().getDirectionally().toString();
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

	private void initViewGUI() throws SQLException, DatabaseLoadDriverException 
	{	
		int relationID = (Integer) relations.getMainTable().getValueAt(relations.getMainTable().getSelectedRow(), 6);
		IEventAnnotation event = reProcess.getEventsAnnotations().get(relationID);
		try {
			new RelationDetailsGUI(event,reProcess);
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}	
	}
	
	class ButtonTableFindRenderer extends ButtonTableRenderer
	{
		private static final long serialVersionUID = 1L;
		
		public ButtonTableFindRenderer(ImageIcon icon)
		{
			super(icon);
		}

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			initViewGUI();
		}
		
	}
	
	class ButtonTableFindCellEditor extends ButtonTableCellEditor
	{

		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			initViewGUI();
		}
		
	}

}
