package pt.uminho.anote2.relation.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.IRERelationConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationConfiguration;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.RERelationDefaultSettings;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REOperationWizard6 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jMainPanel;
	private JPanel jPanelEnableStopWords;
	private JList jListLexicalWords;
	private JScrollPane jScrollPaneLExicalWords;
	private JLabel jLabelStopWords;
	private JRadioButton jRadioButtonStopWordsYes;
	private JRadioButton jRadioButtonStopWordsYNo;
	private ButtonGroup buttonGroup1;
	private JPanel jPanelSelectStopWords;
	private List<LexicalWords> lexicalWords;

	public REOperationWizard6(List<Object> param) {
		super(param);
		initGUI();
		defaultSettings();
		this.setTitle("Relation Extraction - Addition Verbs (Lexical Words Resource)");	
		Utilities.centerOnOwner(this);
		this.setModal(true);	
		this.setVisible(true);	
	}

	private void initGUI() {
		setEnableNextButton(false);
		setEnableDoneButton(true);
	}
	
	private void defaultSettings() {
		if(lexicalWords != null && lexicalWords.size()>0)
		{	
			if(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.VERB_ADDITION).toString()))
			{
				changeYes();
				jListLexicalWords.setSelectedValue(new LexicalWords(Integer.valueOf(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.VERB_ADDITION_LEXICAL_WORDS_ID).toString()),"",""), true);
			}
			else
			{
				changeNo();
			}
		}
		else
		{
			changeNo();
		}
	}
	
	
	private void getLexicalWords() throws SQLException, DatabaseLoadDriverException{
		lexicalWords = new ArrayList<LexicalWords>();		
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
				LexicalWords lex = new LexicalWords(id,name,note);
				lexicalWords.add(lex);
			}
			rs.close();
			resourceType.close();

	}

	private JRadioButton getJRadioButtonStopWordsYNo() {
		if(jRadioButtonStopWordsYNo == null) {
			jRadioButtonStopWordsYNo = new JRadioButton();
			jRadioButtonStopWordsYNo.setText("No");
			jRadioButtonStopWordsYNo.setSelected(true);
			jRadioButtonStopWordsYNo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changeNo();
				}
			});
			
		}
		return jRadioButtonStopWordsYNo;
	}
	
	protected void changeNo() {
		jRadioButtonStopWordsYNo.setSelected(true);
		jListLexicalWords.setEnabled(false);	
	}



	private JRadioButton getJRadioButtonStopWordsYes() {
		if(jRadioButtonStopWordsYes == null) {
			jRadioButtonStopWordsYes = new JRadioButton();
			jRadioButtonStopWordsYes.setText("Yes");
			if(lexicalWords == null && lexicalWords.size()<1)
			{
				jRadioButtonStopWordsYes.setEnabled(false);
			}
			else
			{
				jRadioButtonStopWordsYes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						changeYes();
					}
				});
			}
		}
		return jRadioButtonStopWordsYes;
	}
	
	protected void changeYes() {
		jRadioButtonStopWordsYes.setSelected(true);
		jListLexicalWords.setEnabled(true);		
	}



	private JLabel getJLabelStopWords() {
		if(jLabelStopWords == null) {
			jLabelStopWords = new JLabel();
			jLabelStopWords.setText("Addition Verbs Words List:");
		}
		return jLabelStopWords;
	}
	
	private JScrollPane getJScrollPaneLExicalWords() {
		if(jScrollPaneLExicalWords == null) {
			jScrollPaneLExicalWords = new JScrollPane();
			jScrollPaneLExicalWords.setViewportView(getJListLexicalWords());
		}
		return jScrollPaneLExicalWords;
	}
	
	private JList getJListLexicalWords() {
		if(jListLexicalWords == null) {
			ListModel jListLexicalWordsModel = getModel();
			jListLexicalWords = new JList();
			jListLexicalWords.setModel(jListLexicalWordsModel);
			jListLexicalWords.setEnabled(false);
		}
		return jListLexicalWords;
	}



	private ListModel getModel() {
		DefaultComboBoxModel jListLexicalWordsModel = new DefaultComboBoxModel();
		for(LexicalWords lex:lexicalWords)
		{
			jListLexicalWordsModel.addElement(lex);
		}
		return jListLexicalWordsModel;
	}

	public void done() {
		if(jRadioButtonStopWordsYes.isSelected() && jListLexicalWords.isSelectionEmpty())
		{
			Workbench.getInstance().warn("Please Select Lexical Words Resource");
		}
		else 
		{
			LexicalWords lex2;
			if(!jRadioButtonStopWordsYes.isSelected())
			{
				lex2 = null;
			}
			else
			{
				lex2 = (LexicalWords) jListLexicalWords.getSelectedValue();
			}
			Object obj = HelpAibench.getSelectedItem(Corpus.class);
			Corpus corpus = (Corpus) obj;		
			IEProcess entityProcess = (IEProcess) getParam().get(0);
			PosTaggerEnem enumValue = (PosTaggerEnem) getParam().get(1);
			RelationsModelEnem model = (RelationsModelEnem) getParam().get(2);
			IRERelationAdvancedConfiguration advancedConfiguration = (IRERelationAdvancedConfiguration) getParam().get(3);
			LexicalWords lex1 = (LexicalWords) getParam().get(4);
			IRERelationConfiguration relationConfiguration = new RERelationConfiguration(corpus, entityProcess, enumValue, model, lex1, lex2,advancedConfiguration);
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("Corpus", Corpus.class,corpus, null),
					new ParamSpec("configuration", IRERelationConfiguration.class,relationConfiguration, null)
			};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.rerelation")){	
					Workbench.getInstance().executeOperation(def, paramsSpec);
					closeView();
					return;
				}
			}
		}
	}

	public void goBack() {
		closeView();
		new REOperationWizard5(getParam());
	}

	public void goNext() {
	}


	public JComponent getMainComponent() {
		if(jMainPanel == null)
		{
			try {
				getLexicalWords();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
			jMainPanel = new JPanel();
			buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(getJRadioButtonStopWordsYNo());
			buttonGroup1.add(getJRadioButtonStopWordsYes());
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			jMainPanel.setLayout(thisLayout);
			{
				jPanelEnableStopWords = new JPanel();
				jPanelEnableStopWords.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Addition Verb Option (Lexical Words)", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelEnableStopWordsLayout = new GridBagLayout();
				jMainPanel.add(jPanelEnableStopWords, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelEnableStopWordsLayout.rowHeights = new int[] {7, 7, 7};
				jPanelEnableStopWordsLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
				jPanelEnableStopWordsLayout.columnWidths = new int[] {7, 7, 7};
				jPanelEnableStopWords.setLayout(jPanelEnableStopWordsLayout);
				jPanelEnableStopWords.add(getJLabelStopWords(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableStopWords.add(getJRadioButtonStopWordsYNo(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableStopWords.add(getJRadioButtonStopWordsYes(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelSelectStopWords = new JPanel();
				jPanelSelectStopWords.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Available Lexical Words", TitledBorder.LEADING, TitledBorder.TOP));

				GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
				jMainPanel.add(jPanelSelectStopWords, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelSelectStopWords.setLayout(jPanelSelectStopWordsLayout);
				jPanelSelectStopWords.add(getJScrollPaneLExicalWords(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jMainPanel;
	}



	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Relation_Extraction#Verb_List_Added";
	}

}
