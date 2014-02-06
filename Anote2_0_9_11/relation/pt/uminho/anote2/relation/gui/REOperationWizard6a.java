package pt.uminho.anote2.relation.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
import pt.uminho.anote2.relation.configuration.RERelationConfiguration;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.RERelationDefaultSettings;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REOperationWizard6a extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JList jListLexicalWords;
	private JScrollPane jScrollPaneLExicalWords;
	private JLabel jLabelStopWords;
	private JPanel jPanelSelectStopWords;
	private List<LexicalWords> lexicalWords;

	public REOperationWizard6a(List<Object> param) {
		super(param);
		initGUI();
		defaultSettings();
		this.setTitle("Relation Extraction - Select Verbs (Lexical Words Resource)");
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
			jListLexicalWords.setSelectedValue(new LexicalWords(Integer.valueOf(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.BIOMEDICAL_VERB_MODEL).toString()),"",""), true);
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
		if(jListLexicalWords.isSelectionEmpty())
		{
			Workbench.getInstance().warn("Please Select Lexical Words Resource that contains Verb");
		}
		else 
		{

			LexicalWords lex2 = (LexicalWords) jListLexicalWords.getSelectedValue();
			Object obj = HelpAibench.getSelectedItem(Corpus.class);
			Corpus corpus = (Corpus) obj;		
			IEProcess entityProcess = (IEProcess) getParam().get(0);
			PosTaggerEnem enumValue = (PosTaggerEnem) getParam().get(1);
			RelationsModelEnem model = (RelationsModelEnem) getParam().get(2);
			IRERelationAdvancedConfiguration reAdvancedConfiguration = (IRERelationAdvancedConfiguration) getParam().get(3);
			IRERelationConfiguration relationConfiguration = new RERelationConfiguration(corpus, entityProcess, enumValue, model, null, lex2,reAdvancedConfiguration);
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
		new REOperationWizard4(getParam());
	}

	public void goNext() {
		
	}


	public JComponent getMainComponent() {
		if(jPanelSelectStopWords == null)
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
			jPanelSelectStopWords = new JPanel();
			jPanelSelectStopWords.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Available Lexical Words", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
			jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSelectStopWords.setLayout(jPanelSelectStopWordsLayout);
			jPanelSelectStopWords.add(getJScrollPaneLExicalWords(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSelectStopWords;
	}


	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Relation_Extraction_With_Biomedical_Verbs#Biomedical_Verb_List";
	}
	
	
}
