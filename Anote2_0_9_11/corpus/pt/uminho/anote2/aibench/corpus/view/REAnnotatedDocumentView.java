package pt.uminho.anote2.aibench.corpus.view;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.gui.RelationDetailsGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;


public class REAnnotatedDocumentView extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1229351096914888366L;	
	private REDocumentAnnotation reAnnotDoc;
	private JPanel jPanelOtherInformation;
	private JScrollPane jScrollPaneAnnotations;
	private JPanel jPanelAnnotationDocInfo;
	private JPanel jPanelAnnotations;
	private TableSearchPanel environmentalConditionTable;

	public REAnnotatedDocumentView(REDocumentAnnotation nerAnnotDoc)
	{
		this.reAnnotDoc=nerAnnotDoc;
		initGUI();
		this.reAnnotDoc.addObserver(this);
		try {
			fillView();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.05, 0.1, 0.1, 0.05};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};			
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelAnnotations = new JPanel();
				GridBagLayout jPanelAnnotationsLayout = new GridBagLayout();
				this.add(jPanelAnnotations, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelAnnotationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelAnnotationsLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelAnnotationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelAnnotationsLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelAnnotations.setLayout(jPanelAnnotationsLayout);
				{
					jScrollPaneAnnotations = new JScrollPane();
					jPanelAnnotations.add(jScrollPaneAnnotations, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						environmentalConditionTable = new TableSearchPanel(true);
						jScrollPaneAnnotations.setViewportView(environmentalConditionTable);
					}
				}
			}
			{
				jPanelAnnotationDocInfo = new JPanel();
				this.add(jPanelAnnotationDocInfo, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelOtherInformation = new JPanel();
				this.add(jPanelOtherInformation, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
	}

	private TableModel getModelAnnots() throws SQLException, DatabaseLoadDriverException {
		String[] columns = new String[] {"Clue(Verb)", "Lemma(Clue)", "Entities At Left", "Entities At Right","Polarity","Directionally","ID","Details"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;		
		int relatiosNumber = reAnnotDoc.getEventAnnotations().size();
		data = new Object[relatiosNumber][8];
		for(IEventAnnotation relationID:reAnnotDoc.getEventAnnotations())
		{
			String verb = new String();
			String lemma = new String();
			if(relationID.getEventClue()!=null)
				verb = relationID.getEventClue();
			if(relationID.getEventProperties().getLemma()!=null)
				lemma = relationID.getEventProperties().getLemma();
			data[i][0] = verb;
			data[i][1] = lemma;			
			data[i][2] = convertEntitiesToString(relationID.getEntitiesAtLeft());
			data[i][3] = convertEntitiesToString(relationID.getEntitiesAtRight());
			data[i][4] = relationID.getEventProperties().getPolarity().toString();
			data[i][5] = relationID.getEventProperties().getDirectionally().toString();
			data[i][6] = relationID.getID();
			data[i][7] = relationID.getID();
			tableModel.addRow(data[i]);
			i++;
		}		
		return tableModel;	
	}
	
	private void fillView() throws SQLException, DatabaseLoadDriverException {		
		TableModel tableModel = getModelAnnots();
		environmentalConditionTable.setModel(tableModel);
		complementTable(environmentalConditionTable.getMainTable());
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
		try {
			IEventAnnotation event = reAnnotDoc.getEventAnnotations().get(environmentalConditionTable.getSelectedRowsInOriginalModel()[0]);
			new RelationDetailsGUI(event,(IEProcess)reAnnotDoc.getProcess());
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}	
	}

	public void update(Observable arg0, Object arg1) {
		try {
			environmentalConditionTable.setModel(getModelAnnots());
			complementTable(environmentalConditionTable.getMainTable());
			environmentalConditionTable.updateUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
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
