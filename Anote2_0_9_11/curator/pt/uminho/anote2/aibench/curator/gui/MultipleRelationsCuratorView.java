package pt.uminho.anote2.aibench.curator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.gui.help.SentenceAnnotationPane;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class MultipleRelationsCuratorView  extends DialogGenericViewOkButtonOnly implements Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CuratorDocument curatorDocument;
	private JScrollPane jScrollPaneAnnotations;
	private JPanel jPanelAnnotationDocInfo;
	private JPanel jPanelAnnotations;
	private TableSearchPanel environmentalConditionTable;
	private List<IEventAnnotation> relations;
	private SentenceAnnotationPane sentenceAnnoationPane;
	private AnnotationPosition sentenceRange;
	private AnnotationPosition selectedRange;
	private JPanel jPanelSentenceAnnotationAnAddOption;
	private JButton jButtonAddRelation;
	
	public MultipleRelationsCuratorView(CuratorDocument curatorDocument,AnnotationPosition selectedRange,Set<IEventAnnotation> relations) throws RelationDelimiterExeption, SQLException, DatabaseLoadDriverException
	{
		super();
		this.curatorDocument=curatorDocument;
		this.selectedRange= selectedRange;
		sentenceRange = calculateSentenceRange(selectedRange);
		this.relations = new ArrayList<IEventAnnotation>(relations);
		initGUI();
		fillView();
		this.curatorDocument.getAnnotatedDocument().addObserver(this);
		this.setSize(GlobalOptions.superWidth, GlobalOptions.superHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private AnnotationPosition calculateSentenceRange(AnnotationPosition selectedRange) throws RelationDelimiterExeption, SQLException, DatabaseLoadDriverException {
		List<ISentence> sentences = curatorDocument.getAnnotatedDocument().getSentencesText();
		ISentence sentenceInit = findSentence(sentences,selectedRange.getStart());	
		ISentence sentenceEnd = findSentence(sentences,selectedRange.getEnd());
		if(sentenceInit == null || sentenceEnd == null || sentenceInit.getStartOffset()>=sentenceEnd.getEndOffset())
		{
			throw new RelationDelimiterExeption();
		}
		return new AnnotationPosition((int)sentenceInit.getStartOffset(),(int)sentenceEnd.getEndOffset());
	}
	
	private ISentence findSentence(List<ISentence> sentences, int offset) {
		for(ISentence set:sentences)
		{
			if(set.getStartOffset() <= offset && offset <= set.getEndOffset())
			{
				return set;
			}
		}
		return null;
	}


	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1};			
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			jPanelAnnotations = new JPanel();
			GridBagLayout jPanelAnnotationsLayout = new GridBagLayout();
			getContentPane().add(jPanelAnnotations, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAnnotationsLayout.rowWeights = new double[] {0.75, 0.1};
			jPanelAnnotationsLayout.rowHeights = new int[] {7, 20};
			jPanelAnnotationsLayout.columnWeights = new double[] {0.1};
			jPanelAnnotationsLayout.columnWidths = new int[] {7};
			jPanelAnnotations.setLayout(jPanelAnnotationsLayout);
			{
				jScrollPaneAnnotations = new JScrollPane();
				jPanelAnnotations.add(jScrollPaneAnnotations, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelAnnotations.add(getJPanelSentenceAnnotationAnAddOption(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					environmentalConditionTable = new TableSearchPanel(true);
					jScrollPaneAnnotations.setViewportView(environmentalConditionTable);
				}
			}
		}
		{
			jPanelAnnotationDocInfo = new JPanel();
			getContentPane().add(jPanelAnnotationDocInfo, new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
	
	private TableModel getModelAnnots() {
		String[] columns = new String[] {"DB","Clue(Verb)", "Lemma(Clue)", "Entities At Left", "Entities At Right","Polarity","Directionally","ID","Edit","Details"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;		
		int relatiosNumber = relations.size();
		data = new Object[relatiosNumber][10];
		for(IEventAnnotation relationID:relations)
		{
			String verb = new String();
			String lemma = new String();
			if(relationID.getEventClue()!=null)
				verb = relationID.getEventClue();
			if(relationID.getEventProperties().getLemma()!=null)
				lemma = relationID.getEventProperties().getLemma();
			data[i][0] = "";
			data[i][1] = verb;
			data[i][2] = lemma;			
			data[i][3] = convertEntitiesToString(relationID.getEntitiesAtLeft());
			data[i][4] = convertEntitiesToString(relationID.getEntitiesAtRight());
			data[i][5] = relationID.getEventProperties().getPolarity().toString();
			data[i][6] = relationID.getEventProperties().getDirectionally();
			data[i][7] = relationID.getID();
			data[i][8] = "";
			data[i][9] = "";
			tableModel.addRow(data[i]);
			i++;
		}		
		return tableModel;	
	}
	
	private void fillView() {		
		TableModel tableModel = getModelAnnots();
		environmentalConditionTable.setModel(tableModel);
		complementTable(environmentalConditionTable.getMainTable());
	}
	
	private void complementTable(JTable jtable){
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);	
		jtable.getColumnModel().getColumn(1).setMinWidth(75);
		jtable.getColumnModel().getColumn(2).setMinWidth(100);
		jtable.getColumnModel().getColumn(3).setMinWidth(120);
		jtable.getColumnModel().getColumn(4).setMinWidth(120);
		jtable.getColumnModel().getColumn(5).setMinWidth(75);
		jtable.getColumnModel().getColumn(6).setMinWidth(85);
		jtable.getColumnModel().getColumn(7).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(7).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(7).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(8).setMaxWidth(PreferencesSizeComponents.moreinfofieldmaxWith);
		jtable.getColumnModel().getColumn(8).setMinWidth(PreferencesSizeComponents.moreinfofieldminWith);
		jtable.getColumnModel().getColumn(8).setPreferredWidth(PreferencesSizeComponents.moreinfofieldpreferredWith);
		jtable.getColumnModel().getColumn(9).setMaxWidth(PreferencesSizeComponents.moreinfofieldmaxWith);
		jtable.getColumnModel().getColumn(9).setMinWidth(PreferencesSizeComponents.moreinfofieldminWith);
		jtable.getColumnModel().getColumn(9).setPreferredWidth(PreferencesSizeComponents.moreinfofieldpreferredWith);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/find.png"));
		jtable.getColumn("Details").setCellRenderer(new ButtonTableFindRenderer(icon));
		jtable.getColumn("Details").setCellEditor(new ButtonTableFindCellEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon2));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		ImageIcon icon3 = new ImageIcon(getClass().getClassLoader().getResource("icons/rule26.png"));
		jtable.getColumn("Edit").setCellRenderer(new ButtonTableRelationEditRenderer(icon3));
		jtable.getColumn("Edit").setCellEditor(new ButtonTableRelationEditCellEditor());
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();
		for (int j = 1; j < 8; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
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
			IEventAnnotation event = relations.get(environmentalConditionTable.getSelectedRowsInOriginalModel()[0]);
			new CuratorRelationDetailsGUI(event,(IIEProcess)curatorDocument.getAnnotatedDocument().getProcess(),false);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}	
	}

	@Override
	protected void okButtonAction() {
		finish();
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Curator_View#Multi_Relation_View";
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
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 1L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			try {
				removeRelation();
			} catch (ManualCurationException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			try {
				removeRelation();
			} catch (ManualCurationException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}
		
	}
	
	class ButtonTableRelationEditRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 1L;

		public ButtonTableRelationEditRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			editRelation();	
		}

	}
	
	class ButtonTableRelationEditCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			editRelation();	
		}
		
	}

	public void removeRelation() throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		int line = environmentalConditionTable.getSelectedRowsInOriginalModel()[0];
		IEventAnnotation event = relations.get(line);
		relations.remove(event);
		environmentalConditionTable.setModel(getModelAnnots());
		complementTable(environmentalConditionTable.getMainTable());
		this.curatorDocument.removeEventAnnotation(event);
	}
	
	public void editRelation() {
		int line = environmentalConditionTable.getSelectedRowsInOriginalModel()[0];
		IEventAnnotation event = relations.get(line);
		new RelationEditGUI(curatorDocument, event);	
	}

	private SentenceAnnotationPane getSentenceAnnotationPane() throws SQLException, DatabaseLoadDriverException {
		if(sentenceAnnoationPane == null) {
			sentenceAnnoationPane = new SentenceAnnotationPane(curatorDocument, sentenceRange, selectedRange);
		}
		return sentenceAnnoationPane;
	}
	
	private JPanel getJPanelSentenceAnnotationAnAddOption() throws SQLException, DatabaseLoadDriverException {
		if(jPanelSentenceAnnotationAnAddOption == null) {
			jPanelSentenceAnnotationAnAddOption = new JPanel();
			GridBagLayout jPanelSentenceAnnotationAnAddOptionLayout = new GridBagLayout();
			jPanelSentenceAnnotationAnAddOptionLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelSentenceAnnotationAnAddOptionLayout.rowHeights = new int[] {7, 20};
			jPanelSentenceAnnotationAnAddOptionLayout.columnWeights = new double[] {0.1, 0.0};
			jPanelSentenceAnnotationAnAddOptionLayout.columnWidths = new int[] {7, 7};
			jPanelSentenceAnnotationAnAddOption.setLayout(jPanelSentenceAnnotationAnAddOptionLayout);
			jPanelSentenceAnnotationAnAddOption.add(getJButtonAddRelation(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
			jPanelSentenceAnnotationAnAddOption.add(getSentenceAnnotationPane(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 10, 5, 5), 0, 0));
		}
		return jPanelSentenceAnnotationAnAddOption;
	}
	
	private JButton getJButtonAddRelation() {
		if(jButtonAddRelation == null) {
			jButtonAddRelation = new JButton();
			jButtonAddRelation.setText("Add Relation (In Range)");
			jButtonAddRelation.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boolean isAClue = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getVerbRelations().containsKey(selectedRange);
					new RelationAddGUI(curatorDocument, selectedRange,isAClue);
				}
			});
		}
		return jButtonAddRelation;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			sentenceRange = calculateSentenceRange(selectedRange);
			relations = new ArrayList<IEventAnnotation>(curatorDocument.getEventFromSubSet(selectedRange));
			fillView();
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

}
