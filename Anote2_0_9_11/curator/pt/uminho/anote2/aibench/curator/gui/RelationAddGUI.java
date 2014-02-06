package pt.uminho.anote2.aibench.curator.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.gui.help.SentenceAnnotationPane;
import pt.uminho.anote2.aibench.curator.gui.help.SentenceAnnotationPaneExtention;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.re.EventAnnotation;
import pt.uminho.anote2.datastructures.annotation.re.EventProperties;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class RelationAddGUI extends DialogGenericView implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelRelationAddPanel;
	private JPanel jPanelRelationsInRange;
	private TableSearchPanel tableSearchrelationInRange;
	private JPanel jPanelRelationProperties;
	private JPanel jPanelRelationEntities;
	private JPanel jPanelRelationClue;
	private JTabbedPane jTabbedPaneMainPanel;
	private JComboBox jComboBoxDirectionally;
	private JPanel jPanelDirectionally;
	private JComboBox jComboBoxPolarity;
	private JPanel jPanelPolarity;
	private SentenceAnnotationPaneExtention jPanelSelectRelationClue;
	private JRadioButton jRadioButtonUsingClueNo;
	private JScrollPane jScrollPaneSimpleEntities;
	private JRadioButton jRadioButtonUsingClueYes;
	private JLabel jLabelUsingClue;
	private JPanel jPanelUsingClue;
	private ButtonGroup buttonGroupCluoptional;
	private JTable jTableEntitiesAtRight;
	private JTable jTableEntitiesAtLeft;
	private JScrollPane jScrollPaneentitiesAtRight;
	private JScrollPane jScrollPaneEntitiesAtLeft;
	private JPanel jPanelDoubleEntitiesSide;
	private JPopupMenu menu;

	
	private final boolean isAClueRange;
	private CuratorDocument curatorDocument;
	private AnnotationPosition selectedRange;
	private AnnotationPosition sentencesRange;
	private AnnotationPositions entities;
	private List<IEventAnnotation> relations;
	private JPanel simpleentities;
	private JTable jTablesimpleEntities;
	private JPanel jpanalDifineClue;
	private JLabel jLabelNeedSelectClue;


	public RelationAddGUI(CuratorDocument curatorDocument,AnnotationPosition selectedRange,boolean isAClueRange)
	{
		super("Add Relation");
		this.curatorDocument = curatorDocument;
		this.selectedRange = selectedRange;
		this.isAClueRange = isAClueRange;
		try {
			sentencesRange = calculateSentenceRange(selectedRange);
			entities = calculateEntitiesInRange(sentencesRange);
			relations = new ArrayList<IEventAnnotation>(curatorDocument.getEventFromSubSet(sentencesRange));
			initGUI();
			this.setSize(GlobalOptions.superWidth, GlobalOptions.superHeight);
			Utilities.centerOnOwner(this);
			this.setModal(true);
			this.setVisible(true);
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
			finish();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			finish();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			finish();
		}
	}

	private AnnotationPositions calculateEntitiesInRange(AnnotationPosition sentencesRange) throws SQLException, DatabaseLoadDriverException {
		return ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityAnnotationPositions().getAnnotationPositionsSubSet(sentencesRange.getStart(), sentencesRange.getEnd());
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
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(609, 521));
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jTabbedPaneMainPanel = new JTabbedPane();
				this.add(jTabbedPaneMainPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelRelationAddPanel = new JPanel();
					GridBagLayout jPanelRelationAddPanelLayout = new GridBagLayout();
					jTabbedPaneMainPanel.addTab("Relation Add", jPanelRelationAddPanel);
					jPanelRelationAddPanelLayout.rowWeights = new double[] {0.3, 0.5, 0.0};
					jPanelRelationAddPanelLayout.rowHeights = new int[] {7, 7, 7};
					jPanelRelationAddPanelLayout.columnWeights = new double[] {0.1};
					jPanelRelationAddPanelLayout.columnWidths = new int[] {7};
					jPanelRelationAddPanel.setLayout(jPanelRelationAddPanelLayout);
					{
						jPanelRelationAddPanel.add(getRelationCluePanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jPanelRelationAddPanel.add(getRelationEntities(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{

						jPanelRelationAddPanel.add(getRelationProperties(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jTabbedPaneMainPanel.addTab("Relation In Selected Range", getRelationInRange());	
				}
			}
		}
	}

	private Component getRelationInRange() {
		if(jPanelRelationsInRange==null)
		{
			jPanelRelationsInRange = new JPanel();
			GridBagLayout jPanelRelationsInRangeLayout = new GridBagLayout();
			jPanelRelationsInRangeLayout.rowWeights = new double[] {0.1};
			jPanelRelationsInRangeLayout.rowHeights = new int[] {7};
			jPanelRelationsInRangeLayout.columnWeights = new double[] {0.1};
			jPanelRelationsInRangeLayout.columnWidths = new int[] {7};
			jPanelRelationsInRange.setLayout(jPanelRelationsInRangeLayout);
			{
				tableSearchrelationInRange = new TableSearchPanel(getTableRelationInRangeModel(),true);
				jPanelRelationsInRange.add(tableSearchrelationInRange, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				complementTable(tableSearchrelationInRange.getMainTable());
			}
		}
		return jPanelRelationsInRange;
	}

	private TableModel getTableRelationInRangeModel() {
		String[] columns = new String[] {"Clue(Verb)", "Lemma(Clue)", "Entities At Left", "Entities At Right","Polarity","Directionally","ID","Details"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;		
		int relatiosNumber = relations.size();
		data = new Object[relatiosNumber][8];
		for(IEventAnnotation relationID:relations)
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
	
	private void initViewGUI() 
	{	
		try {		
			IEventAnnotation event = relations.get(tableSearchrelationInRange.getSelectedRowsInOriginalModel()[0]);
			new CuratorRelationDetailsGUI(event,(IIEProcess)curatorDocument.getAnnotatedDocument().getProcess(),false);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (RelationDelimiterExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}	
	}

	private Component getRelationEntities() {
		if(jPanelRelationEntities==null)
		{
			jPanelRelationEntities = new JPanel();			
			GridBagLayout jPanelRelationEntitiesLayout = new GridBagLayout();
			jPanelRelationEntities.setBorder(BorderFactory.createTitledBorder("Relation Entities"));
			jPanelRelationEntitiesLayout.rowWeights = new double[] {0.1};
			jPanelRelationEntitiesLayout.rowHeights = new int[] {7};
			jPanelRelationEntitiesLayout.columnWeights = new double[] {0.1};
			jPanelRelationEntitiesLayout.columnWidths = new int[] {7};
			jPanelRelationEntities.setLayout(jPanelRelationEntitiesLayout);
			if(isAClueRange)
			{
				jPanelRelationEntities.add(getJPanelDoubleEntities(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			else
			{
				jPanelRelationEntities.add(getJPaneSimpleEntity(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			
		}
		return jPanelRelationEntities;
	}

	private JPanel getJPaneSimpleEntity() {
		if(simpleentities==null)
		{
			simpleentities = new JPanel();
			GridBagLayout simpleentitiesLayout = new GridBagLayout();
			simpleentitiesLayout.rowWeights = new double[] {0.1};
			simpleentitiesLayout.rowHeights = new int[] {7};
			simpleentitiesLayout.columnWeights = new double[] {0.1};
			simpleentitiesLayout.columnWidths = new int[] {7};
			simpleentities.setLayout(simpleentitiesLayout);
			simpleentities.add(getJScrollPaneSimpleEntities(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return simpleentities;
	}

	private Component getRelationProperties() {
		if(jPanelRelationProperties==null)
		{
			jPanelRelationProperties = new JPanel();
			jPanelRelationProperties.setBorder(BorderFactory.createTitledBorder("Relation Properties"));
			GridBagLayout jPanelRelationPropertiesLayout = new GridBagLayout();
			jPanelRelationPropertiesLayout.rowWeights = new double[] {0.1};
			jPanelRelationPropertiesLayout.rowHeights = new int[] {7};
			jPanelRelationPropertiesLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelRelationPropertiesLayout.columnWidths = new int[] {7, 7};
			jPanelRelationProperties.setLayout(jPanelRelationPropertiesLayout);
			jPanelRelationProperties.add(getJPanelPolarity(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
			jPanelRelationProperties.add(getJPanelDirectionally(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRelationProperties;
	}

	private Component getRelationCluePanel() throws SQLException, DatabaseLoadDriverException {
		if(jPanelRelationClue == null)
		{
			jPanelRelationClue = new JPanel();
			GridBagLayout jPanelRelationClueLayout = new GridBagLayout();
			jPanelRelationClue.setBorder(BorderFactory.createTitledBorder("Select Relation Clue (Optional)"));
			jPanelRelationClueLayout.rowWeights = new double[] {0.1};
			jPanelRelationClueLayout.rowHeights = new int[] {7};
			jPanelRelationClueLayout.columnWeights = new double[] {0.0, 0.1};
			jPanelRelationClueLayout.columnWidths = new int[] {7, 7};
			jPanelRelationClue.setLayout(jPanelRelationClueLayout);
			jPanelRelationClue.add(getJPanelUsingClue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationClue.add(getJPanelSelectRelationClue(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			getButtonGroupCluoptional();
		}
		return jPanelRelationClue;
	}
	
	private JPanel getJPanelUsingClue() {
		if(jPanelUsingClue == null) {
			jPanelUsingClue = new JPanel();
			GridBagLayout jPanelUsingClueLayout = new GridBagLayout();
			jPanelUsingClueLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelUsingClueLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelUsingClueLayout.columnWeights = new double[] {0.1};
			jPanelUsingClueLayout.columnWidths = new int[] {7};
			jPanelUsingClue.setLayout(jPanelUsingClueLayout);
			jPanelUsingClue.add(getJLabelUsingClue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 15), 0, 0));
			jPanelUsingClue.add(getJRadioButtonUsingClueYes(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
			jPanelUsingClue.add(getJRadioButtonNo(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
		}
		return jPanelUsingClue;
	}
	
	private JLabel getJLabelUsingClue() {
		if(jLabelUsingClue == null) {
			jLabelUsingClue = new JLabel();
			jLabelUsingClue.setText("Using Clue (Verb)");
		}
		return jLabelUsingClue;
	}
	
	private JRadioButton getJRadioButtonUsingClueYes() {
		if(jRadioButtonUsingClueYes == null) {
			jRadioButtonUsingClueYes = new JRadioButton();
			jRadioButtonUsingClueYes.setText("Yes");
			if(this.isAClueRange)
			{
				jRadioButtonUsingClueYes.setSelected(true);
				jRadioButtonUsingClueYes.setEnabled(false);
			}
			jRadioButtonUsingClueYes.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					changeRelationClueOrnot();
				}
			});
		}
		return jRadioButtonUsingClueYes;
	}
	
	protected void changeRelationClueOrnot() {
		jPanelRelationEntities.remove(getJPanelDoubleEntities());
		jPanelRelationEntities.remove(getJPaneSimpleEntity());
		jPanelRelationEntities.remove(getJPanelNeedDifineClue());
		if(jRadioButtonUsingClueYes.isSelected())
		{

			this.jPanelSelectRelationClue.addMenuToJeditor(getClueMenu());
			if(!jPanelSelectRelationClue.isselectedClue())
			{
				jPanelRelationEntities.add(getJPanelNeedDifineClue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			else
			{
				jPanelRelationEntities.add(getJPanelDoubleEntities(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				TableModel jTableEntitiesAtLeftModel = getEntitiesAtLeft();
				jTableEntitiesAtLeft.setModel(jTableEntitiesAtLeftModel);
				completeTable(jTableEntitiesAtLeft);
				TableModel jTableEntitiesAtRightModel = getEntitiesAtRight();
				jTableEntitiesAtRight.setModel(jTableEntitiesAtRightModel);
				completeTable(jTableEntitiesAtRight);
			}
		}
		else
		{
			this.jPanelSelectRelationClue.removeMenuFromEditorPane();
			jPanelRelationEntities.add(getJPaneSimpleEntity(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			
		}
		jPanelRelationEntities.updateUI();
	}

	private JPopupMenu getClueMenu() {
		if(menu==null)
		{
			menu = new JPopupMenu();
			JMenuItem item = new JMenuItem("Select as Clue");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						jPanelSelectRelationClue.validateSelection();
						jPanelSelectRelationClue.changeHtml();
						changeRelationClueOrnot();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (ManualCurationException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			menu.add(item);
		}
		return menu;
	}

	private Component getJPanelNeedDifineClue() {
		if(jpanalDifineClue==null)
		{
			jpanalDifineClue = new JPanel();
			GridBagLayout simpleentitiesLayout = new GridBagLayout();
			simpleentitiesLayout.rowWeights = new double[] {0.1};
			simpleentitiesLayout.rowHeights = new int[] {7};
			simpleentitiesLayout.columnWeights = new double[] {0.1};
			simpleentitiesLayout.columnWidths = new int[] {7};
			jpanalDifineClue.setLayout(simpleentitiesLayout);
			jpanalDifineClue.add(getJLabelNeedSelectClue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
		
		}
		return jpanalDifineClue;
	}

	private JLabel getJLabelNeedSelectClue() {
		if(jLabelNeedSelectClue==null)
		{
			jLabelNeedSelectClue = new JLabel("Need Select Clue");
		}
		return jLabelNeedSelectClue;
	}

	private JRadioButton getJRadioButtonNo() {
		if(jRadioButtonUsingClueNo == null) {
			jRadioButtonUsingClueNo = new JRadioButton();
			jRadioButtonUsingClueNo.setText("No");
			if(!this.isAClueRange)
			{
				jRadioButtonUsingClueNo.setSelected(true);
			}
			else
			{
				jRadioButtonUsingClueNo.setEnabled(false);
			}
			jRadioButtonUsingClueNo.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					changeRelationClueOrnot();
				}
			});
		}
		return jRadioButtonUsingClueNo;
	}
	
	private SentenceAnnotationPane getJPanelSelectRelationClue() throws SQLException, DatabaseLoadDriverException {
		if(jPanelSelectRelationClue == null) {
			jPanelSelectRelationClue = new SentenceAnnotationPaneExtention(curatorDocument,sentencesRange,selectedRange);
		}
		return jPanelSelectRelationClue;
	}
	
	

	
	
	private JPanel getJPanelPolarity() {
		if(jPanelPolarity == null) {
			jPanelPolarity = new JPanel();
			GridBagLayout jPanelPolarityLayout = new GridBagLayout();
			jPanelPolarity.setBorder(BorderFactory.createTitledBorder("Polarity"));
			jPanelPolarityLayout.rowWeights = new double[] {0.1};
			jPanelPolarityLayout.rowHeights = new int[] {7};
			jPanelPolarityLayout.columnWeights = new double[] {0.1};
			jPanelPolarityLayout.columnWidths = new int[] {7};
			jPanelPolarity.setLayout(jPanelPolarityLayout);
			jPanelPolarity.add(getJComboBoxPolarity(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
		}
		return jPanelPolarity;
	}
	
	private JComboBox getJComboBoxPolarity() {
		if(jComboBoxPolarity == null) {
			ComboBoxModel jComboBoxPolarityModel = 
					new DefaultComboBoxModel(PolarityEnum.values());
			jComboBoxPolarity = new JComboBox();
			jComboBoxPolarity.setModel(jComboBoxPolarityModel);
			jComboBoxPolarity.setSelectedItem(PolarityEnum.Unknown);
		}
		return jComboBoxPolarity;
	}
	
	private JPanel getJPanelDirectionally() {
		if(jPanelDirectionally == null) {
			jPanelDirectionally = new JPanel();
			GridBagLayout jPanelDirectionallyLayout = new GridBagLayout();
			jPanelDirectionally.setBorder(BorderFactory.createTitledBorder("Directionally"));
			jPanelDirectionallyLayout.rowWeights = new double[] {0.1};
			jPanelDirectionallyLayout.rowHeights = new int[] {7};
			jPanelDirectionallyLayout.columnWeights = new double[] {0.1};
			jPanelDirectionallyLayout.columnWidths = new int[] {7};
			jPanelDirectionally.setLayout(jPanelDirectionallyLayout);
			jPanelDirectionally.add(getJComboBoxDirectionally(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
		}
		return jPanelDirectionally;
	}
	
	private JComboBox getJComboBoxDirectionally() {
		if(jComboBoxDirectionally == null) {
			ComboBoxModel jComboBoxDirectionallyModel = 
					new DefaultComboBoxModel(DirectionallyEnum.values());
			jComboBoxDirectionally = new JComboBox();
			jComboBoxDirectionally.setModel(jComboBoxDirectionallyModel);
			jComboBoxDirectionally.setSelectedItem(DirectionallyEnum.Unknown);
		}
		return jComboBoxDirectionally;
	}
	
	

	@Override
	protected void okButtonAction() {
		try {
			IEventAnnotation event = validateNewRelation();
			curatorDocument.addEventAnnotation(event);
			finish();
		} catch (ManualCurationException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private IEventAnnotation validateNewRelation() throws ManualCurationException, DatabaseLoadDriverException, SQLException {
		if(isAClueRange)
		{
			if(this.selectedRange!=null && this.selectedRange.getStart()<this.selectedRange.getEnd())
			{
				List<IEntityAnnotation> entitiesAtLeft = getEntitiesFromTable(jTableEntitiesAtLeft);
				List<IEntityAnnotation> entitiesAtRight = getEntitiesFromTable(jTableEntitiesAtRight);
				if(entitiesAtLeft.size()+entitiesAtRight.size()<2)
					throw new ManualCurationException("Note: Relation must have (at least) two entities");
				int id = HelpDatabase.getNextInsertTableID(DatabaseTablesName.annotation);
				String clue = this.curatorDocument.getAnnotatedDocument().getDocumetAnnotationText().substring(selectedRange.getStart(),selectedRange.getEnd());
				EventProperties eventProperties = new EventProperties();
				eventProperties.setDirectionally((DirectionallyEnum)jComboBoxDirectionally.getSelectedItem());
				eventProperties.setLemma(clue);
				eventProperties.setPolarity(((PolarityEnum)jComboBoxPolarity.getSelectedItem()));
				IEventAnnotation cadidateAnnotation = new EventAnnotation(id , selectedRange.getStart(), selectedRange.getEnd(), GlobalNames.re, 
						entitiesAtLeft, entitiesAtRight, clue, -1, "", eventProperties);
				testIfEventAnnoatationAlreadyExist(cadidateAnnotation,relations);
				return cadidateAnnotation;
			}
			else
				throw new ManualCurationException("Note: You must select a clue.");		
		}
		else
		{
			if(jRadioButtonUsingClueNo.isSelected())
			{
				List<IEntityAnnotation> entities = getEntitiesFromTable(jTablesimpleEntities);
				if(entities.size()==2)
				{
					int id = HelpDatabase.getNextInsertTableID(DatabaseTablesName.annotation);
					EventProperties eventProperties = new EventProperties();
					eventProperties.setDirectionally((DirectionallyEnum)jComboBoxDirectionally.getSelectedItem());
					eventProperties.setPolarity(((PolarityEnum)jComboBoxPolarity.getSelectedItem()));
					if(entities.get(0).getStartOffset() < entities.get(1).getStartOffset())
					{
						IEventAnnotation cadidateAnnotation =  new EventAnnotation(id , selectedRange.getStart(),selectedRange.getStart(), GlobalNames.re, 
								entities.get(0), entities.get(1), "", -1, "", eventProperties);
						testIfEventAnnoatationAlreadyExist(cadidateAnnotation,relations);
						return cadidateAnnotation;
					}
					else
					{
						IEventAnnotation cadidateAnnotation =  new EventAnnotation(id , selectedRange.getStart(), selectedRange.getStart(), GlobalNames.re, 
								entities.get(1), entities.get(0), "", -1, "", eventProperties);
						testIfEventAnnoatationAlreadyExist(cadidateAnnotation,relations);
						return cadidateAnnotation;
					}		
				}
				else
				{
					throw new ManualCurationException("Note: Relation Without clue can only have two entities.");
				}
			}
			else
			{
				if(jPanelSelectRelationClue.isselectedClue() && jPanelSelectRelationClue.getSelectClueAnnotation().diference()>0)
				{
					List<IEntityAnnotation> entitiesAtLeft = getEntitiesFromTable(jTableEntitiesAtLeft);
					List<IEntityAnnotation> entitiesAtRight = getEntitiesFromTable(jTableEntitiesAtRight);
					if(entitiesAtLeft.size()+entitiesAtRight.size()<2)
						throw new ManualCurationException("Note: Relation must have (at least) two entities");
					int id = HelpDatabase.getNextInsertTableID(DatabaseTablesName.annotation);
					String clue = this.curatorDocument.getAnnotatedDocument().getDocumetAnnotationText().substring(jPanelSelectRelationClue.getSelectClueAnnotation().getStart(),jPanelSelectRelationClue.getSelectClueAnnotation().getEnd());
					EventProperties eventProperties = new EventProperties();
					eventProperties.setDirectionally((DirectionallyEnum)jComboBoxDirectionally.getSelectedItem());
					eventProperties.setLemma(clue);
					eventProperties.setPolarity(((PolarityEnum)jComboBoxPolarity.getSelectedItem()));
					IEventAnnotation cadidateAnnotation = new EventAnnotation(id , jPanelSelectRelationClue.getSelectClueAnnotation().getStart(), jPanelSelectRelationClue.getSelectClueAnnotation().getEnd(), GlobalNames.re, 
							entitiesAtLeft, entitiesAtRight, clue, -1, "", eventProperties);
					testIfEventAnnoatationAlreadyExist(cadidateAnnotation,relations);
					return cadidateAnnotation;
				}
				else 
				{
					throw new ManualCurationException("Note: Relation Clue not defined.");
				}
			}
		}
	}

	private void testIfEventAnnoatationAlreadyExist(IEventAnnotation cadidateAnnotation, List<IEventAnnotation> relations) throws ManualCurationException {
		for(IEventAnnotation relation : relations)
		{
			if(relation.getStartOffset() == cadidateAnnotation.getStartOffset() &&
					relation.getEndOffset() == relation.getEndOffset())
			{
				List<IEntityAnnotation> entitiesAtLeftCandidate = cadidateAnnotation.getEntitiesAtLeft();
				List<IEntityAnnotation> entitiesAtLeft = relation.getEntitiesAtLeft();
				if(sameEntities(entitiesAtLeftCandidate,entitiesAtLeft))
				{
					List<IEntityAnnotation> entitiesAtRightCandidate = cadidateAnnotation.getEntitiesAtRight();
					List<IEntityAnnotation> entitiesAtRight = relation.getEntitiesAtRight();
					if(sameEntities(entitiesAtRightCandidate,entitiesAtRight))
					{
						throw new ManualCurationException("Note: Relation Already Exist.");		
					}
				}

			}
		}
	}

	private boolean sameEntities(List<IEntityAnnotation> entitiesAtLeftCandidate,List<IEntityAnnotation> entities) {
		Set<Integer> candidateIDs = new HashSet<Integer>();
		for(IEntityAnnotation ent:entitiesAtLeftCandidate)
		{
			candidateIDs.add(ent.getID());
		}
		Set<Integer> entitiesIds = new HashSet<Integer>();
		for(IEntityAnnotation entIds:entities)
		{
			entitiesIds.add(entIds.getID());
		}
		if(candidateIDs.size()==0 && entitiesIds.size()==0)
		{
			return true;
		}
		else if(candidateIDs.size()==0 || entitiesIds.size()==0)
		{
			return false;
		}
		if(entitiesIds.containsAll(candidateIDs) && candidateIDs.containsAll(entitiesIds))
		{
			return true;
		}
		return false;
	}

	private List<IEntityAnnotation> getEntitiesFromTable(JTable jTable) {
		List<IEntityAnnotation> entities = new ArrayList<IEntityAnnotation>();
		for(int i=0;i<jTable.getRowCount();i++)
		{
			if((Boolean)jTable.getValueAt(i, 1))
			{
				entities.add((IEntityAnnotation) jTable.getValueAt(i, 0));
			}
		}
		return entities;
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Curator_View#Add_Relation_View";
	}
	
	private JPanel getJPanelDoubleEntities() {
		if(jPanelDoubleEntitiesSide == null) {
			jPanelDoubleEntitiesSide = new JPanel();
			GridBagLayout jPanelEntitiesLayout = new GridBagLayout();
			jPanelEntitiesLayout.rowWeights = new double[] {0.1};
			jPanelEntitiesLayout.rowHeights = new int[] {7};
			jPanelEntitiesLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelEntitiesLayout.columnWidths = new int[] {7, 7};
			jPanelDoubleEntitiesSide.setLayout(jPanelEntitiesLayout);
			jPanelDoubleEntitiesSide.add(getJScrollPaneEntitiesAtLeft(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDoubleEntitiesSide.add(getJScrollPaneentitiesAtRight(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelDoubleEntitiesSide;
	}
	
	private JScrollPane getJScrollPaneEntitiesAtLeft() {
		if(jScrollPaneEntitiesAtLeft == null) {
			jScrollPaneEntitiesAtLeft = new JScrollPane();
			jScrollPaneEntitiesAtLeft.setBorder(BorderFactory.createTitledBorder("Entities At Left"));
			jScrollPaneEntitiesAtLeft.setViewportView(getJTableEntitiesAtLeft());
		}
		return jScrollPaneEntitiesAtLeft;
	}
	
	private JScrollPane getJScrollPaneentitiesAtRight() {
		if(jScrollPaneentitiesAtRight == null) {
			jScrollPaneentitiesAtRight = new JScrollPane();
			jScrollPaneentitiesAtRight.setBorder(BorderFactory.createTitledBorder("Entities At Rght"));
			jScrollPaneentitiesAtRight.setViewportView(getJTableEntitiesAtRight());

		}
		return jScrollPaneentitiesAtRight;
	}
	
	private JTable getJTableEntitiesAtLeft() {
		if(jTableEntitiesAtLeft == null) {
			jTableEntitiesAtLeft = new JTable();
			TableModel jTableEntitiesAtLeftModel = getEntitiesAtLeft();
			jTableEntitiesAtLeft.setModel(jTableEntitiesAtLeftModel);
			completeTable(jTableEntitiesAtLeft);
		}
		return jTableEntitiesAtLeft;
	}
	
	private TableModel getEntitiesAtLeft() {
		String[] columns = new String[] {"Entity(Class)",""};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public boolean isCellEditable(int x,int y){
				if(y==1)
					return true;
				return false;
			}
			
			public Class<?> getColumnClass(int column){
				if(column == 1)
					return Boolean.class;
				return IEntityAnnotation.class;
			}
		};
		tableModel.setColumnIdentifiers(columns);
		Collection<IAnnotation> annotations = this.entities.getAnnotationsSubSet(sentencesRange.getStart(),jPanelSelectRelationClue.getSelectClueAnnotation().getStart());
		Object[] data = new Object[2];
		for(IAnnotation annot:annotations)
		{
			data[0] = (IEntityAnnotation) annot;
			data[1] = false;
			tableModel.addRow(data);
		}
		return tableModel;
	}

	private JTable getJTableEntitiesAtRight() {
		if(jTableEntitiesAtRight == null) {
			jTableEntitiesAtRight = new JTable();
			TableModel jTableEntitiesAtRightModel = getEntitiesAtRight();
			jTableEntitiesAtRight.setModel(jTableEntitiesAtRightModel);
			completeTable(jTableEntitiesAtRight);
			
		}
		return jTableEntitiesAtRight;
	}
	
	private void completeTable(JTable jtable) {
		JCheckBox box = new JCheckBox();
		jtable.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(box));
		jtable.getColumn("Entity(Class)").setCellRenderer(new ButtonTableEntityRender());
	}
	
	class ButtonTableEntityRender  implements TableCellRenderer{
		
		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel();
			IEntityAnnotation entity = (IEntityAnnotation) table.getValueAt(row, column);
			String termAndClass = entity.getAnnotationValue() + " ("+ClassProperties.getClassIDClass().get(entity.getClassAnnotationID())+")";	
			String color = "";
			try {
				color = CorporaProperties.getCorporaClassColor(entity.getClassAnnotationID()).getColor();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DatabaseLoadDriverException e) {
				e.printStackTrace();
			}
			label.setText("<html><font color=\""+color+"\">\t"+termAndClass+"</font></html>");
			return label;
		}
	}

	private TableModel getEntitiesAtRight() {
		String[] columns = new String[] {"Entity(Class)",""};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public boolean isCellEditable(int x,int y){
				if(y==1)
					return true;
				return false;
			}
			
			public Class<?> getColumnClass(int column){
				if(column == 1)
					return Boolean.class;
				return IEntityAnnotation.class;
			}
		};
		tableModel.setColumnIdentifiers(columns);
		Object[] data = new Object[2];
		Collection<IAnnotation> annotations = this.entities.getAnnotationsSubSet(jPanelSelectRelationClue.getSelectClueAnnotation().getEnd(),sentencesRange.getEnd());
		for(IAnnotation annot:annotations)
		{
			data[0] = (IEntityAnnotation) annot;
			data[1] = false;
			tableModel.addRow(data);
		}
		return tableModel;
	}

	private ButtonGroup getButtonGroupCluoptional() {
		if(buttonGroupCluoptional == null) {
			buttonGroupCluoptional = new ButtonGroup();
			buttonGroupCluoptional.add(jRadioButtonUsingClueNo);
			buttonGroupCluoptional.add(jRadioButtonUsingClueYes);

		}
		return buttonGroupCluoptional;
	}
	
	private JScrollPane getJScrollPaneSimpleEntities() {
		if(jScrollPaneSimpleEntities == null) {
			jScrollPaneSimpleEntities = new JScrollPane();
			jScrollPaneSimpleEntities.setViewportView(getTableSearchSimpleEntities());
		}
		return jScrollPaneSimpleEntities;
	}

	private JTable getTableSearchSimpleEntities() {
		if(jTablesimpleEntities == null)
		{
			jTablesimpleEntities = new JTable();
			jTablesimpleEntities.setModel(getTableSimpleModelEntities());
			completeTable(jTablesimpleEntities);
		}
		return jTablesimpleEntities;
	}

	private TableModel getTableSimpleModelEntities() {
		String[] columns = new String[] {"Entity(Class)",""};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public boolean isCellEditable(int x,int y){
				if(y==1)
					return true;
				return false;
			}
			
			public Class<?> getColumnClass(int column){
				if(column == 1)
					return Boolean.class;
				return IEntityAnnotation.class;
			}
		};
		tableModel.setColumnIdentifiers(columns);
		Object[] data = new Object[2];
		Collection<IAnnotation> annotations = this.entities.getAnnotationsSubSet(sentencesRange.getStart(),sentencesRange.getEnd());
		for(IAnnotation annot:annotations)
		{
			data[0] = (IEntityAnnotation) annot;
			data[1] = false;
			tableModel.addRow(data);
		}
		return tableModel;
	}

	@Override
	public void update(Observable o, Object arg) {
		changeRelationClueOrnot();		
	}


}
