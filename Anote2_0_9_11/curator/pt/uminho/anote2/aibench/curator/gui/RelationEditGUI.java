package pt.uminho.anote2.aibench.curator.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.gui.help.RelationDetaillsPanel;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.gui.help.SentenceAnnotationPane;
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


public class RelationEditGUI extends DialogGenericView{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelRelationProperties;
	
	
	private IEventAnnotation eventToEdit;
	private JPanel jPanelPolarity;
	private JComboBox jComboBoxPolarity;
	private JPanel jPanelEditRelationPanel;
	private JTabbedPane jTabbedPaneMainPanel;
	private JPanel jPanelDirectionally;
	private JComboBox jComboBoxDirectionally;
	private CuratorDocument curatorDocument;
	private AnnotationPosition eventRange;
	private AnnotationPosition sentencesRange;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JPanel jPanelRelationsInRange;
	private JPanel jPanelOriginalRelation;
	private List<IEventAnnotation> relations;
	private TableSearchPanel tableSearchrelationInRange;
	private JPanel jPanelRelationEntities;
	private JPanel simpleentities;
	private JPanel jPanelDoubleEntitiesSide;
	private JScrollPane jScrollPaneEntitiesAtLeft;
	private JScrollPane jScrollPaneentitiesAtRight;
	private JTable jTableEntitiesAtLeft;
	private JTable jTableEntitiesAtRight;
	private JScrollPane jScrollPaneSimpleEntities;
	private JTable jTablesimpleEntities;
	private AnnotationPositions entities;

	
	public RelationEditGUI(CuratorDocument curatorDocument,IEventAnnotation eventToEdit)
	{
		super("Edit Relation");
		this.eventToEdit = eventToEdit;
		this.curatorDocument = curatorDocument;
		this.eventRange = new AnnotationPosition((int)eventToEdit.getStartOffset(), (int)eventToEdit.getEndOffset());
		try {
			sentencesRange = calculateSentenceRange(eventRange);
			entities = calculateEntitiesInRange(sentencesRange);
			relations = new ArrayList<IEventAnnotation>(curatorDocument.getEventFromSubSet(sentencesRange));
			initGUI();
			fillRelationFields();
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
	
	private void fillRelationFields() {
		jComboBoxPolarity.setSelectedItem(this.eventToEdit.getEventProperties().getPolarity());	
		jComboBoxDirectionally.setSelectedItem(this.eventToEdit.getEventProperties().getDirectionally());
		AnnotationPositions annotations = new AnnotationPositions();
		for(IEntityAnnotation annotEnt:this.eventToEdit.getEntitiesAtLeft())
		{
			annotations.addAnnotationWhitConflicts(new AnnotationPosition((int)annotEnt.getStartOffset(), (int)annotEnt.getEndOffset()), annotEnt);
		}		
		for(IEntityAnnotation annotEnt:this.eventToEdit.getEntitiesAtRight())
		{
			annotations.addAnnotationWhitConflicts(new AnnotationPosition((int)annotEnt.getStartOffset(), (int)annotEnt.getEndOffset()), annotEnt);
		}
		if(eventToEdit.getStartOffset()<eventToEdit.getEndOffset())
		{
			for(int i=0;i<jTableEntitiesAtLeft.getRowCount();i++)
			{
				IEntityAnnotation entity = (IEntityAnnotation) jTableEntitiesAtLeft.getValueAt(i, 0);
				if(annotations.containsKey(new AnnotationPosition((int)entity.getStartOffset(), (int)entity.getEndOffset())))
				{
					jTableEntitiesAtLeft.setValueAt(true, i, 1);
				}
			}
			for(int i=0;i<jTableEntitiesAtRight.getRowCount();i++)
			{
				IEntityAnnotation entity = (IEntityAnnotation) jTableEntitiesAtRight.getValueAt(i, 0);
				if(annotations.containsKey(new AnnotationPosition((int)entity.getStartOffset(), (int)entity.getEndOffset())))
				{
					jTableEntitiesAtRight.setValueAt(true, i, 1);
				}
			}
		}
		else
		{
			for(int i=0;i<jTablesimpleEntities.getRowCount();i++)
			{
				IEntityAnnotation entity = (IEntityAnnotation) jTablesimpleEntities.getValueAt(i, 0);
				if(annotations.containsKey(new AnnotationPosition((int)entity.getStartOffset(), (int)entity.getEndOffset())))
				{
					jTablesimpleEntities.setValueAt(true, i, 1);
				}
			}
		}

	}

	private AnnotationPositions calculateEntitiesInRange(AnnotationPosition sentencesRange) throws SQLException, DatabaseLoadDriverException {
		return ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityAnnotationPositions().getAnnotationPositionsSubSet(sentencesRange.getStart(), sentencesRange.getEnd());
	}

	
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException, RelationDelimiterExeption {
		GridBagLayout general = new GridBagLayout();
		general.rowWeights = new double[] {0.1, 0.0};
		general.rowHeights = new int[] {7, 20};
		general.columnWeights = new double[] {0.1};
		general.columnWidths = new int[] {7};
		getContentPane().setLayout(general);
		getContentPane().add(getJTabbedPaneMainPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}




	@Override
	protected void okButtonAction() {
		try {
			IEventAnnotation eventupdated = validateEditRelation();
			curatorDocument.editEventAnnotation(this.eventToEdit,eventupdated);
			finish();
		} catch (ManualCurationException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}		
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Curator_View#Edit_Relation_View";
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
			if(eventToEdit.getStartOffset()<eventToEdit.getEndOffset())
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
			tableModel.addRow(data);
		}
		return tableModel;
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
	
	private JTable getJTableEntitiesAtRight() {
		if(jTableEntitiesAtRight == null) {
			jTableEntitiesAtRight = new JTable();
			TableModel jTableEntitiesAtRightModel = getEntitiesAtRight();
			jTableEntitiesAtRight.setModel(jTableEntitiesAtRightModel);
			completeTable(jTableEntitiesAtRight);
		}
		return jTableEntitiesAtRight;
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
		Collection<IAnnotation> annotations = this.entities.getAnnotationsSubSet(this.eventToEdit.getEndOffset(),sentencesRange.getEnd());
		for(IAnnotation annot:annotations)
		{
			data[0] = (IEntityAnnotation) annot;
			data[1] = false;
			tableModel.addRow(data);
		}
		return tableModel;
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

	private void completeTable(JTable jtable) {
		JCheckBox box = new JCheckBox();
		jtable.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(box));
		jtable.getColumn("Entity(Class)").setCellRenderer(new ButtonTableEntityRender());
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
		Collection<IAnnotation> annotations = this.entities.getAnnotationsSubSet(sentencesRange.getStart(),this.eventToEdit.getStartOffset());
		Object[] data = new Object[2];
		for(IAnnotation annot:annotations)
		{
			data[0] = (IEntityAnnotation) annot;
			data[1] = false;
			tableModel.addRow(data);
		}
		return tableModel;
	}
	
	private JComboBox getJComboBoxPolarity() {
		if(jComboBoxPolarity == null) {
			ComboBoxModel jComboBoxPolarityModel = 
					new DefaultComboBoxModel(PolarityEnum.values());
			jComboBoxPolarity = new JComboBox();
			jComboBoxPolarity.setModel(jComboBoxPolarityModel);
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
		}
		return jComboBoxDirectionally;
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
	
	private JTabbedPane getJTabbedPaneMainPanel() throws SQLException, DatabaseLoadDriverException, RelationDelimiterExeption {
		if(jTabbedPaneMainPanel == null) {
			jTabbedPaneMainPanel = new JTabbedPane();
			jTabbedPaneMainPanel.addTab("Edit", null, getJPanelEditRelationPanel(), null);
			jTabbedPaneMainPanel.addTab("Original", null, getJPanelOriginalRelation(), null);
			jTabbedPaneMainPanel.addTab("Relations in Range", null, getRelationInRange(), null);
		}
		return jTabbedPaneMainPanel;
	}
	
	private JPanel getJPanelEditRelationPanel() throws SQLException, DatabaseLoadDriverException {
		if(jPanelEditRelationPanel == null) {
			jPanelEditRelationPanel = new JPanel();
			GridBagLayout jPanelEditRelationPanelLayout = new GridBagLayout();
			jPanelEditRelationPanelLayout.rowWeights = new double[] {0.3, 0.5, 0.0};
			jPanelEditRelationPanelLayout.rowHeights = new int[] {7, 7, 7};
			jPanelEditRelationPanelLayout.columnWeights = new double[] {0.1};
			jPanelEditRelationPanelLayout.columnWidths = new int[] {7};
			jPanelEditRelationPanel.setLayout(jPanelEditRelationPanelLayout);
			jPanelEditRelationPanel.add(getRelationProperties(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelEditRelationPanel.add(getJPanelSentence(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelEditRelationPanel.add(getRelationEntities(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelEditRelationPanel;
	}
	
	private JPanel getJPanelOriginalRelation() throws SQLException, DatabaseLoadDriverException, RelationDelimiterExeption {
		if(jPanelOriginalRelation == null) {
			jPanelOriginalRelation = new RelationDetaillsPanel(eventToEdit,(IIEProcess) curatorDocument.getAnnotatedDocument().getProcess());
		}
		return jPanelOriginalRelation;
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
	
	private JPanel getJPanelSentence() throws SQLException, DatabaseLoadDriverException {
		if(jPanel1 == null) {
			jPanel1 = new SentenceAnnotationPane(curatorDocument, sentencesRange, new AnnotationPosition((int) eventRange.getStart(), (int) eventRange.getEnd()));
		}
		return jPanel1;
	}
	
	private IEventAnnotation validateEditRelation() throws ManualCurationException, DatabaseLoadDriverException, SQLException {
		// With Clue
		if(eventToEdit.getStartOffset()<eventToEdit.getEndOffset())
		{
			List<IEntityAnnotation> entitiesAtLeft = getEntitiesFromTable(jTableEntitiesAtLeft);
			List<IEntityAnnotation> entitiesAtRight = getEntitiesFromTable(jTableEntitiesAtRight);
			if(entitiesAtLeft.size()+entitiesAtRight.size()<2)
				throw new ManualCurationException("Note: Relation must have (at least) two entities");
			EventProperties eventProperties = new EventProperties();
			eventProperties.setDirectionally((DirectionallyEnum)jComboBoxDirectionally.getSelectedItem());
			eventProperties.setLemma(eventToEdit.getEventClue());
			eventProperties.setPolarity(((PolarityEnum)jComboBoxPolarity.getSelectedItem()));
			IEventAnnotation cadidateAnnotation = new EventAnnotation(eventToEdit.getID() , eventToEdit.getStartOffset(), eventToEdit.getEndOffset(), GlobalNames.re, 
					entitiesAtLeft, entitiesAtRight, eventToEdit.getEventClue(), -1, "", eventProperties);
			testIfEventAnnoatationAlreadyExist(cadidateAnnotation,eventToEdit,relations);
			return cadidateAnnotation;
		}
		// Without Clue
		else
		{

			List<IEntityAnnotation> entities = getEntitiesFromTable(jTablesimpleEntities);
			if(entities.size()==2)
			{
				EventProperties eventProperties = new EventProperties();
				eventProperties.setDirectionally((DirectionallyEnum)jComboBoxDirectionally.getSelectedItem());
				eventProperties.setPolarity(((PolarityEnum)jComboBoxPolarity.getSelectedItem()));
				if(entities.get(0).getStartOffset() < entities.get(1).getStartOffset())
				{
					IEventAnnotation cadidateAnnotation =  new EventAnnotation(eventToEdit.getID(), eventToEdit.getStartOffset(),eventToEdit.getStartOffset(), GlobalNames.re, 
							entities.get(0), entities.get(1), "", -1, "", eventProperties);
					testIfEventAnnoatationAlreadyExist(cadidateAnnotation,eventToEdit,relations);
					return cadidateAnnotation;
				}
				else
				{
					IEventAnnotation cadidateAnnotation =  new EventAnnotation(eventToEdit.getID() , eventToEdit.getStartOffset(), eventToEdit.getStartOffset(), GlobalNames.re, 
							entities.get(1), entities.get(0), "", -1, "", eventProperties);
					testIfEventAnnoatationAlreadyExist(cadidateAnnotation,eventToEdit,relations);
					return cadidateAnnotation;
				}		
			}
			else
			{
				throw new ManualCurationException("Note: Relation must have two entities");
			}
		}
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
	
	private void testIfEventAnnoatationAlreadyExist(IEventAnnotation cadidateAnnotation, IEventAnnotation original,List<IEventAnnotation> relations) throws ManualCurationException {
		if(sameRelation(cadidateAnnotation,original))
		{
			if(sameProperties(cadidateAnnotation,original))
			{
				throw new ManualCurationException("Note: Relation Edit No Changes Found.");		
			}	
		}
		else
		{
			List<IEventAnnotation> relationWithouOriginal = relations;
			relationWithouOriginal.remove(relations);
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
							relationWithouOriginal.add(original);
							throw new ManualCurationException("Note: Relation Already Exist.");		
						}
					}

				}
			}
			relationWithouOriginal.add(original);
		}
	}

	private boolean sameProperties(IEventAnnotation cadidateAnnotation,
			IEventAnnotation original) {
		if(cadidateAnnotation.getEventProperties().getPolarity().equals(original.getEventProperties().getPolarity()))
		{
			if(cadidateAnnotation.getEventProperties().getDirectionally().equals(original.getEventProperties().getDirectionally()))
				return true;
		}
		return false;
	}

	private boolean sameRelation(IEventAnnotation cadidateAnnotation, IEventAnnotation original)
			throws ManualCurationException {
		if(original.getStartOffset() == cadidateAnnotation.getStartOffset() &&
				original.getEndOffset() == original.getEndOffset())
		{
			List<IEntityAnnotation> entitiesAtLeftCandidate = cadidateAnnotation.getEntitiesAtLeft();
			List<IEntityAnnotation> entitiesAtLeft = original.getEntitiesAtLeft();
			if(sameEntities(entitiesAtLeftCandidate,entitiesAtLeft))
			{
				List<IEntityAnnotation> entitiesAtRightCandidate = cadidateAnnotation.getEntitiesAtRight();
				List<IEntityAnnotation> entitiesAtRight = original.getEntitiesAtRight();
				if(sameEntities(entitiesAtRightCandidate,entitiesAtRight))
				{
					return true;	
				}
			}
		}
		return false;
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
	

}
