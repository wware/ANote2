package pt.uminho.anote2.relation.settings.pane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.RERelationDefaultSettings;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;

public class RERelationChangeSettingsPanel extends JPanel{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Map<String, Object> initial_props;
	protected JScrollPane jScrollPaneoPTIONS;
	protected JPanel jPaneloPTIONS;
	protected JPanel jPanelBiomedicalVerbs;
	private JCheckBox jCheckBoxRelationAdvancedWithout;
	protected JCheckBox jCheckBoxOnlyUseEntityInNearestVerb;
	protected JLabel jLabelMaxDistance;
	private TableSearchPanel jtableSearchRelationTypes;
	private JButton jButtonSelectAllOrNot;
	private JPanel jPanelRelationTypesMainPAnel;
	private JScrollPane jScrollPaneRelationTypes;
	protected JCheckBox jCheckBoxOnlyUseNearestEntitiesToVerb;
	protected JSpinner jSpinnerMaxDistance;
	protected JCheckBox jCheckBoxUsingMaxDistanceVerbEntities;
	private JPanel jPanelRelationContrains;
	private JPanel jPanelAdvenacedOptionPane;
	private JScrollPane jScrollPane1;
	protected JComboBox jComboBoxVerbAddition;
	protected JCheckBox jCheckBoxVerbAddition;
	protected JPanel jPanelVerbAddition;
	protected JComboBox jComboBoxVerbFilter;
	protected JCheckBox jCheckBoxVerbFilter;
	protected JPanel jPanelVerbFilter;
	protected JComboBox jComboBoxBiomedicalVerbs;
	protected JComboBox jComboBoxRelationModel;
	protected JPanel jPanelModel;
	protected JComboBox jComboBoxPOSTagger;
	protected JPanel jPanePosTagger;
	protected List<ILexicalWords> lexicalWords;
	private JPanel defaultSettings;
	private JTabbedPane jtabbedPaneOptions;
	private boolean jButtonAllSelected = true;

	private ButtonGroup buttonGroupSelectino;



	public RERelationChangeSettingsPanel(Map<String, Object> initial_props, Map<String, Object> defaultProps) throws SQLException, DatabaseLoadDriverException {
		
		super();
		if(initial_props.isEmpty()) 
			this.initial_props = defaultProps;
		else
			this.initial_props = initial_props;
		if(GlobalOptions.database!=null)
		{
			getLexicalWordsInDatabse();
			initGUI();
			fillSettings();
		}
	}

	protected void fillSettings() {
		jComboBoxPOSTagger.setSelectedItem(PosTaggerEnem.convertStringInPosTaggerEnem(initial_props.get(RERelationDefaultSettings.POSTAGGER).toString()));
		jComboBoxRelationModel.setSelectedItem(RelationsModelEnem.convertStringToRelationsModelEnem(initial_props.get(RERelationDefaultSettings.MODEL).toString()));
		jComboBoxBiomedicalVerbs.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(RERelationDefaultSettings.BIOMEDICAL_VERB_MODEL).toString()), "", ""));
		jCheckBoxVerbFilter.setSelected(Boolean.valueOf(initial_props.get(RERelationDefaultSettings.VERB_FILTER).toString()));
		jComboBoxVerbFilter.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(RERelationDefaultSettings.VERB_FILTER_LEXICAL_WORDS_ID).toString()), "", ""));
		jCheckBoxVerbAddition.setSelected(Boolean.parseBoolean(initial_props.get(RERelationDefaultSettings.VERB_ADDITION).toString()));
		jComboBoxVerbAddition.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(RERelationDefaultSettings.VERB_ADDITION_LEXICAL_WORDS_ID).toString()), "", ""));
		jCheckBoxOnlyUseNearestEntitiesToVerb.setSelected(Boolean.parseBoolean(initial_props.get(RERelationDefaultSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES).toString()));
		jCheckBoxOnlyUseEntityInNearestVerb.setSelected(Boolean.parseBoolean(initial_props.get(RERelationDefaultSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB).toString()));
		Integer value = Integer.valueOf((initial_props.get(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE).toString()));
		if(value!=0)
		{
			jCheckBoxUsingMaxDistanceVerbEntities.setSelected(true);
			jSpinnerMaxDistance.setValue(value);
		}
		SortedSet<IRelationsType> rt = ( SortedSet<IRelationsType>) initial_props.get(RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE);
		fillRelationTypeSettings(rt);
	}


	protected void fillRelationTypeSettings(SortedSet<IRelationsType> rt) {	
		if(!rt.isEmpty())
		{
			for(int i=0;i<jtableSearchRelationTypes.getMainTable().getRowCount();i++)
			{
				IRelationsType rowType = (IRelationsType) jtableSearchRelationTypes.getMainTable().getValueAt(i, 0);
				if(!rt.contains(rowType))
				{
					 jtableSearchRelationTypes.getMainTable().setValueAt(Boolean.FALSE, i, 1);
				}
			}
		}
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			jtabbedPaneOptions = new JTabbedPane();
			this.add(jtabbedPaneOptions, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jtabbedPaneOptions.addTab("Default",getJScrollPaneoPTIONS());
			jtabbedPaneOptions.addTab("Advanced", getJPanelAdvenacedOptionPane());
			jtabbedPaneOptions.addTab("Relation Types",getJPanelRelationTypesMainPAnel());
		}
	}


	private ComboBoxModel getLexicalWords() {
		ILexicalWords lw0 = new LexicalWords(0, "", "");
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(lw0);
		for(int i =0; i < lexicalWords.size(); i++)
		{
			model.addElement(lexicalWords.get(i));
		}
		return model;
	}


	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(RERelationDefaultSettings.POSTAGGER, jComboBoxPOSTagger.getSelectedItem());
		settings.put(RERelationDefaultSettings.MODEL, jComboBoxRelationModel.getSelectedItem());
		settings.put(RERelationDefaultSettings.BIOMEDICAL_VERB_MODEL, ((ILexicalWords)jComboBoxBiomedicalVerbs.getSelectedItem()).getID());
		settings.put(RERelationDefaultSettings.VERB_FILTER, jCheckBoxVerbFilter.isSelected());
		settings.put(RERelationDefaultSettings.VERB_FILTER_LEXICAL_WORDS_ID, ((ILexicalWords)jComboBoxVerbFilter.getSelectedItem()).getID());
		settings.put(RERelationDefaultSettings.VERB_ADDITION, jCheckBoxVerbAddition.isSelected());
		settings.put(RERelationDefaultSettings.VERB_ADDITION_LEXICAL_WORDS_ID, ((ILexicalWords)jComboBoxVerbAddition.getSelectedItem()).getID());
		settings.put(RERelationDefaultSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES, jCheckBoxOnlyUseNearestEntitiesToVerb.isSelected());
		if(jCheckBoxUsingMaxDistanceVerbEntities.isSelected())
		{
			settings.put(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, jSpinnerMaxDistance.getValue());
		}
		else
		{
			settings.put(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, 0);
		}
		settings.put(RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE, getRelationsTypes());
		settings.put(RERelationDefaultSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB, jCheckBoxOnlyUseEntityInNearestVerb.isSelected());
		return settings;
	}


	protected SortedSet<IRelationsType> getRelationsTypes() {
		int columnRows = jtableSearchRelationTypes.getMainTable().getRowCount();
		SortedSet<IRelationsType> rtselected = new TreeSet<IRelationsType>();
		for(int i=0;i<jtableSearchRelationTypes.getMainTable().getRowCount();i++)
		{
			Boolean selected = (Boolean) jtableSearchRelationTypes.getMainTable().getValueAt(i, 1);
			if(selected)
			{
				IRelationsType rowType = (IRelationsType) jtableSearchRelationTypes.getMainTable().getValueAt(i, 0);
				rtselected.add(rowType);
			}
		}
		if(rtselected.size()==0 || rtselected.size()==columnRows)
		{
			return new TreeSet<IRelationsType>() ;
		}
		else
		{
			return rtselected;
		}
	}

	public boolean haveChanged() {
		if(!initial_props.get(RERelationDefaultSettings.POSTAGGER).equals(jComboBoxPOSTagger.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.MODEL).equals(jComboBoxRelationModel.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.BIOMEDICAL_VERB_MODEL).equals(((ILexicalWords)jComboBoxBiomedicalVerbs.getSelectedItem()).getID()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.VERB_FILTER).equals(jCheckBoxVerbFilter.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.VERB_FILTER_LEXICAL_WORDS_ID).equals( ((ILexicalWords)jComboBoxVerbFilter.getSelectedItem()).getID()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.VERB_ADDITION).equals(jCheckBoxVerbAddition.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.VERB_ADDITION_LEXICAL_WORDS_ID).equals( ((ILexicalWords)jComboBoxVerbAddition.getSelectedItem()).getID()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES).equals(jCheckBoxOnlyUseNearestEntitiesToVerb.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(RERelationDefaultSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB).equals(jCheckBoxOnlyUseEntityInNearestVerb.isSelected()))
		{
			return true;
		}
		int value = 0;
		if(jCheckBoxUsingMaxDistanceVerbEntities.isSelected())
		{
			value = (Integer) jSpinnerMaxDistance.getValue();
		}
		if(!initial_props.get(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE).equals(value))
		{
			return true;
		}
		return false;
	}
	
	private JScrollPane getJScrollPaneoPTIONS() {
		if(jScrollPaneoPTIONS == null) {
			jScrollPaneoPTIONS = new JScrollPane();
			jScrollPaneoPTIONS.setViewportView(getJPaneloPTIONS());
		}
		return jScrollPaneoPTIONS;
	}
	
	private JPanel getJPaneloPTIONS() {
		if(jPaneloPTIONS == null) {
			jPaneloPTIONS = new JPanel();
			GridBagLayout jPaneloPTIONSLayout = new GridBagLayout();
			jPaneloPTIONSLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPaneloPTIONSLayout.rowHeights = new int[] {7, 7, 7, 7, 20};
			jPaneloPTIONSLayout.columnWeights = new double[] {0.1};
			jPaneloPTIONSLayout.columnWidths = new int[] {7};
			jPaneloPTIONS.setLayout(jPaneloPTIONSLayout);
			jPaneloPTIONS.add(getJPanelPOSTagger(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPaneloPTIONS.add(getJPanelModel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPaneloPTIONS.add(getJPanelBiomedicalVerbs(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPaneloPTIONS.add(getJPanelVerbFilter(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPaneloPTIONS.add(getJPanelVerbAddition(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPaneloPTIONS;
	}
	
	private JPanel getJPanelPOSTagger() {
		if(jPanePosTagger == null) {
			jPanePosTagger = new JPanel();
			GridBagLayout jPanelLayout = new GridBagLayout();
			jPanePosTagger.setBorder(BorderFactory.createTitledBorder("POS Tagger"));
			jPanelLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
			jPanelLayout.rowHeights = new int[] {7, 7, 7};
			jPanelLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelLayout.columnWidths = new int[] {7, 7, 7};
			jPanePosTagger.setLayout(jPanelLayout);
			jPanePosTagger.add(getJComboBoxPOSTagger(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanePosTagger;
	}
	
	private JComboBox getJComboBoxPOSTagger() {
		if(jComboBoxPOSTagger == null) {
			ComboBoxModel jComboBoxPOSTaggerModel = 
					new DefaultComboBoxModel(PosTaggerEnem.values());
			jComboBoxPOSTagger = new JComboBox();
			jComboBoxPOSTagger.setModel(jComboBoxPOSTaggerModel);
		}
		return jComboBoxPOSTagger;
	}
	
	private JPanel getJPanelModel() {
		if(jPanelModel == null) {
			jPanelModel = new JPanel();
			GridBagLayout jPanelModelLayout = new GridBagLayout();
			jPanelModel.setBorder(BorderFactory.createTitledBorder("Relation Model"));
			jPanelModelLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
			jPanelModelLayout.rowHeights = new int[] {7, 7, 7};
			jPanelModelLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelModelLayout.columnWidths = new int[] {7, 7, 7};
			jPanelModel.setLayout(jPanelModelLayout);
			jPanelModel.add(getJComboBoxRelationModel(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelModel;
	}
	
	private JComboBox getJComboBoxRelationModel() {
		if(jComboBoxRelationModel == null) {
			ComboBoxModel jComboBoxRelationModelModel = 
					new DefaultComboBoxModel(RelationsModelEnem.values());
			jComboBoxRelationModel = new JComboBox();
			jComboBoxRelationModel.setModel(jComboBoxRelationModelModel);
		}
		return jComboBoxRelationModel;
	}
	
	private JPanel getJPanelBiomedicalVerbs() {
		if(jPanelBiomedicalVerbs == null) {
			jPanelBiomedicalVerbs = new JPanel();
			GridBagLayout jPanelBiomedicalVerbsLayout = new GridBagLayout();
			jPanelBiomedicalVerbs.setBorder(BorderFactory.createTitledBorder("Biomedical Verbs (For Binary Selected Verb Model Only)"));
			jPanelBiomedicalVerbsLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
			jPanelBiomedicalVerbsLayout.rowHeights = new int[] {7, 7, 7};
			jPanelBiomedicalVerbsLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelBiomedicalVerbsLayout.columnWidths = new int[] {7, 7, 7};
			jPanelBiomedicalVerbs.setLayout(jPanelBiomedicalVerbsLayout);
			jPanelBiomedicalVerbs.add(getJComboBoxBiomedicalVerbs(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelBiomedicalVerbs;
	}
	
	private JComboBox getJComboBoxBiomedicalVerbs() {
		if(jComboBoxBiomedicalVerbs == null) {
			ComboBoxModel jComboBoxBiomedicalVerbsModel = getLexicalWords();
			jComboBoxBiomedicalVerbs = new JComboBox();
			jComboBoxBiomedicalVerbs.setModel(jComboBoxBiomedicalVerbsModel);
		}
		return jComboBoxBiomedicalVerbs;
	}
	
	private JPanel getJPanelVerbFilter() {
		if(jPanelVerbFilter == null) {
			jPanelVerbFilter = new JPanel();
			GridBagLayout jPanelVerbFilterLayout = new GridBagLayout();
			jPanelVerbFilterLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
			jPanelVerbFilterLayout.rowHeights = new int[] {7, 7, 7};
			jPanelVerbFilterLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelVerbFilterLayout.columnWidths = new int[] {7, 7, 7};
			jPanelVerbFilter.setLayout(jPanelVerbFilterLayout);
			jPanelVerbFilter.setBorder(BorderFactory.createTitledBorder("Verb Filter"));
			jPanelVerbFilter.add(getJCheckBoxVerbFilter(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVerbFilter.add(getJComboBoxVerbFilter(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelVerbFilter;
	}
	
	private JCheckBox getJCheckBoxVerbFilter() {
		if(jCheckBoxVerbFilter == null) {
			jCheckBoxVerbFilter = new JCheckBox();
			jCheckBoxVerbFilter.setText("Verb Filter");
		}
		return jCheckBoxVerbFilter;
	}
	
	private JComboBox getJComboBoxVerbFilter() {
		if(jComboBoxVerbFilter == null) {
			ComboBoxModel jComboBoxVerbFilterModel = getLexicalWords();
			jComboBoxVerbFilter = new JComboBox();
			jComboBoxVerbFilter.setModel(jComboBoxVerbFilterModel);
		}
		return jComboBoxVerbFilter;
	}
	
	private JPanel getJPanelVerbAddition() {
		if(jPanelVerbAddition == null) {
			jPanelVerbAddition = new JPanel();
			GridBagLayout jPanelVerbAdditionLayout = new GridBagLayout();
			jPanelVerbAddition.setBorder(BorderFactory.createTitledBorder("Verb Addition"));
			jPanelVerbAdditionLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
			jPanelVerbAdditionLayout.rowHeights = new int[] {7, 7, 7};
			jPanelVerbAdditionLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelVerbAdditionLayout.columnWidths = new int[] {7, 7, 7};
			jPanelVerbAddition.setLayout(jPanelVerbAdditionLayout);
			jPanelVerbAddition.add(getJCheckBoxVerbAddition(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVerbAddition.add(getJComboBoxVerbAddition(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelVerbAddition;
	}
	
	private JCheckBox getJCheckBoxVerbAddition() {
		if(jCheckBoxVerbAddition == null) {
			jCheckBoxVerbAddition = new JCheckBox();
			jCheckBoxVerbAddition.setText("Verb Addition");
		}
		return jCheckBoxVerbAddition;
	}
	
	private JComboBox getJComboBoxVerbAddition() {
		if(jComboBoxVerbAddition == null) {
			ComboBoxModel jComboBoxVerbAdditionModel = getLexicalWords();
			jComboBoxVerbAddition = new JComboBox();
			jComboBoxVerbAddition.setModel(jComboBoxVerbAdditionModel);
		}
		return jComboBoxVerbAddition;
	}
	
	private void getLexicalWordsInDatabse() throws SQLException, DatabaseLoadDriverException{
		lexicalWords = new ArrayList<ILexicalWords>();		
		PreparedStatement resourceType;
		resourceType = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceFilterByType);
		resourceType.setNString(1,GlobalOptions.resourcesLexicalWords);
		ResultSet rs = resourceType.executeQuery();
		int id;
		String name,note;
		while(rs.next())
		{
			id = rs.getInt(1);
			name = rs.getString(2);
			note = rs.getString(3);
			ILexicalWords lex = new LexicalWords(id,name,note);
			lexicalWords.add(lex);
		}
		rs.close();
		resourceType.close();
	}
	

	public void setInitial_props(Map<String, Object> initial_props) {
		this.initial_props = initial_props;
	}
	
	
	private JPanel getJPanelAdvenacedOptionPane() {
		if(jPanelAdvenacedOptionPane == null) {
			jPanelAdvenacedOptionPane = new JPanel();
			buttonGroupSelectino = new ButtonGroup();
			GridBagLayout jPanelAdvenacedOptionPaneLayout = new GridBagLayout();
			jPanelAdvenacedOptionPane.setPreferredSize(new java.awt.Dimension(219, 202));
			jPanelAdvenacedOptionPaneLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelAdvenacedOptionPaneLayout.rowHeights = new int[] {7, 7};
			jPanelAdvenacedOptionPaneLayout.columnWeights = new double[] {0.1};
			jPanelAdvenacedOptionPaneLayout.columnWidths = new int[] {7};
			jPanelAdvenacedOptionPane.setLayout(jPanelAdvenacedOptionPaneLayout);
			jPanelAdvenacedOptionPane.add(getJPanelRelationverbEntitiesDistance(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAdvenacedOptionPane;
	}
	
	private JPanel getJPanelRelationverbEntitiesDistance() {
		if(jPanelRelationContrains == null) {
			jPanelRelationContrains = new JPanel();
			GridBagLayout jPanelRelationverbEntitiesDistanceLayout = new GridBagLayout();
			jPanelRelationContrains.setBorder(BorderFactory.createTitledBorder("Relation Constrains"));
			jPanelRelationverbEntitiesDistanceLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelRelationverbEntitiesDistanceLayout.rowHeights = new int[] {7, 7, 20, 7, 7};
			jPanelRelationverbEntitiesDistanceLayout.columnWeights = new double[] {0.1, 0.1, 0.025};
			jPanelRelationverbEntitiesDistanceLayout.columnWidths = new int[] {7, 20, 7};
			jPanelRelationContrains.setLayout(jPanelRelationverbEntitiesDistanceLayout);
			jPanelRelationContrains.add(getJCheckBoxUsingMaxDistanceVerbEntities(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJSpinnerMaxDistance(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxOnlyUseMearestEntitiesToVerb(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJLabelMaxDistance(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxOnlyUseEntityInNearestVerb(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxRelationAdvancedWithout(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 9), 0, 0));
		}
		return jPanelRelationContrains;
	}
	
	private JCheckBox getJCheckBoxUsingMaxDistanceVerbEntities() {
		if(jCheckBoxUsingMaxDistanceVerbEntities == null) {
			jCheckBoxUsingMaxDistanceVerbEntities = new JCheckBox();
			buttonGroupSelectino.add(jCheckBoxUsingMaxDistanceVerbEntities);
			jCheckBoxUsingMaxDistanceVerbEntities.setText("Using Max Distance Verb-Entities (In Words)");
		}
		return jCheckBoxUsingMaxDistanceVerbEntities;
	}
	
	private JSpinner getJSpinnerMaxDistance() {
		if(jSpinnerMaxDistance == null) {
			SpinnerNumberModel jSpinnerMaxDistanceModel = new SpinnerNumberModel(100, 1, 500, 1);
			jSpinnerMaxDistance = new JSpinner();
			jSpinnerMaxDistance.setModel(jSpinnerMaxDistanceModel);
		}
		return jSpinnerMaxDistance;
	}
	
	private JCheckBox getJCheckBoxOnlyUseMearestEntitiesToVerb() {
		if(jCheckBoxOnlyUseNearestEntitiesToVerb == null) {
			jCheckBoxOnlyUseNearestEntitiesToVerb = new JCheckBox();
			buttonGroupSelectino.add(jCheckBoxOnlyUseNearestEntitiesToVerb);
			jCheckBoxOnlyUseNearestEntitiesToVerb.setText("Keep only relations where verbs are associated only with the nearest entities");
		}
		return jCheckBoxOnlyUseNearestEntitiesToVerb;
	}
	
	private JPanel getJPanelRelationTypesMainPAnel() {
		if(jPanelRelationTypesMainPAnel == null) {
			jPanelRelationTypesMainPAnel = new JPanel();
			GridBagLayout jPanelRelationTypesMainPAnelLayout = new GridBagLayout();
			jPanelRelationTypesMainPAnelLayout.rowWeights = new double[] {0.1, 0.0};
			jPanelRelationTypesMainPAnelLayout.rowHeights = new int[] {7, 7};
			jPanelRelationTypesMainPAnelLayout.columnWeights = new double[] {0.1};
			jPanelRelationTypesMainPAnelLayout.columnWidths = new int[] {7};
			jPanelRelationTypesMainPAnel.setLayout(jPanelRelationTypesMainPAnelLayout);
			jPanelRelationTypesMainPAnel.add(getJButtonSelectAllOrNot(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationTypesMainPAnel.add(getJPanel1(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRelationTypesMainPAnel;
	}
	
	private JButton getJButtonSelectAllOrNot() {
		if(jButtonSelectAllOrNot == null) {
			jButtonSelectAllOrNot = new JButton();
			jButtonSelectAllOrNot.setText("Unselect All");
			jButtonSelectAllOrNot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					changeOriginalTableModel();
				}
			});
			
		}
		return jButtonSelectAllOrNot;
	}
	
	protected void changeOriginalTableModel() {
		boolean selectedAll = !jButtonAllSelected  ;
		for(int i=0;i<this.jtableSearchRelationTypes.getOriginalTableModel().getRowCount();i++)
		{
			this.jtableSearchRelationTypes.getOriginalTableModel().setValueAt(selectedAll, i, 1);
		}			
		if(selectedAll)
			jButtonSelectAllOrNot.setText("Unselect");
		else
			jButtonSelectAllOrNot.setText("Select All");
		jButtonAllSelected= selectedAll;
		jPanelRelationTypesMainPAnel.updateUI();
	}
	
	private TableSearchPanel getJPanel1() {
		if(jtableSearchRelationTypes == null) {
			jtableSearchRelationTypes = new TableSearchPanel(getRelationTypeModal(),false);
			completeTable(jtableSearchRelationTypes.getMainTable());
		}
		return jtableSearchRelationTypes;
	}
	
	private void completeTable(JTable mainTable) {
		mainTable.setRowHeight(20);
		mainTable.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		mainTable.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		mainTable.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
	}
	
	
	private TableModel getRelationTypeModal() {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0)
					return IRelationsType.class;
				else			
					return Boolean.class;
			}
		};
		String[] columns = new String[] {"RelationType",""};
		tableModel.setColumnIdentifiers(columns);	
		Set<Integer> classIDS = ClassProperties.getClassIDClass().keySet();
		List<Integer> listClasses = new ArrayList<Integer>(classIDS);
		Object[] data = new Object[2];
		for(int i=0;i<listClasses.size();i++)
		{
			for(int j=i;j<listClasses.size();j++)
			{
				if(j==i)
				{
					data[0] = new RelationType(listClasses.get(i), listClasses.get(i));
					data[1] =  Boolean.TRUE;
					tableModel.addRow(data);
				}
				else
				{
					data[0] = new RelationType(listClasses.get(i), listClasses.get(j));
					data[1] =  Boolean.TRUE;
					tableModel.addRow(data);
					data[0] = new RelationType(listClasses.get(j), listClasses.get(i));
					data[1] =  Boolean.TRUE;
					tableModel.addRow(data);
				}
			}
		}
		return tableModel;
	}
	
	private JLabel getJLabelMaxDistance() {
		if(jLabelMaxDistance == null) {
			jLabelMaxDistance = new JLabel();
			jLabelMaxDistance.setText("Max Words Distance :");
		}
		return jLabelMaxDistance;
	}
	
	private JCheckBox getJCheckBoxOnlyUseEntityInNearestVerb() {
		if(jCheckBoxOnlyUseEntityInNearestVerb == null) {
			jCheckBoxOnlyUseEntityInNearestVerb = new JCheckBox();
			buttonGroupSelectino.add(jCheckBoxOnlyUseEntityInNearestVerb);
			jCheckBoxOnlyUseEntityInNearestVerb.setText("Keep only relations where the entities are only associated to the nearest verb.");
		}
		return jCheckBoxOnlyUseEntityInNearestVerb;
	}
	
	private JCheckBox getJCheckBoxRelationAdvancedWithout() {
		if(jCheckBoxRelationAdvancedWithout == null) {
			jCheckBoxRelationAdvancedWithout = new JCheckBox();
			jCheckBoxRelationAdvancedWithout.setText("Without");
			jCheckBoxRelationAdvancedWithout.setSelected(true);
			buttonGroupSelectino.add(jCheckBoxRelationAdvancedWithout);
		}
		return jCheckBoxRelationAdvancedWithout;
	}

}
