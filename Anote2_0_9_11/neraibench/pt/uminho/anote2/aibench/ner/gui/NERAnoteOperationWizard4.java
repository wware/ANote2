package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.ner.configuration.INERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.ner.gui.help.POSTagsHelpGUI;
import pt.uminho.anote2.aibench.ner.settings.NERLexicalResourcesDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.nlptools.PartOfSpeechLabels;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationWizard4 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelStopWords;
	private JPanel jPanelSelectNone;
	private JPanel jPanelPOSTagging;
	private JTabbedPane jPanelPOSTaggingAndStopWords;
	private JPanel jPanelUpper;
	private JList jListLexicalWords;
	private JScrollPane jScrollPaneLExicalWords;
	private JLabel jLabelStopWords;
	private JRadioButton jRadioButtonStopWords;
	private JRadioButton jRadioButtonPOSTaggingAndStopWords;
	private JRadioButton jRadioButtonNoProProcessing;
	private JRadioButton jRadioButtonPosTags;
	private ButtonGroup buttonGroup1;
	private JPanel jPanelSelectStopWords;
	private List<LexicalWords> lexicalWords;
	private JScrollPane jScrollPanePOSTags;
	private JTable jtablePOSTags;
	private JPanel jPanelSelectStopWords2;
	private JScrollPane jScrollPaneLExicalWords2;
	private JList jListLexicalWords2;
	private JPanel jPanelPOSTagging2;
	private JScrollPane jScrollPanePOSTags2;
	private JTable jtablePOSTags2;

	public NERAnoteOperationWizard4(List<Object> param) throws SQLException, DatabaseLoadDriverException {
		super(param);
		getLexicalWords();
		initGUI();
		fill();
		if(param.size()==3)
		{
			fillWithPreviousSettings(param.get(2));
			param.remove(2);
		}
		else
		{
			fillDefaultSetting();
		}
		this.setTitle("NER @Note - Pre-Processing Settings");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	private void fillWithPreviousSettings(Object object) {
		INERLexicalResourcesConfiguration configuration = (INERLexicalResourcesConfiguration) object;
		NERLexicalResourcesPreProssecingEnum preprocessing = configuration.getPreProcessingOption();
		int stopWordsID =  -1;
		if(configuration.getStopWords()!=null)
		{
			stopWordsID = configuration.getStopWords().getID();
		}
		if(preprocessing == NERLexicalResourcesPreProssecingEnum.StopWords)
		{
			if(lexicalWords != null && lexicalWords.size()>0)
			{
				if(stopWordsID > 0)
				{
					selectStopWords(jListLexicalWords,stopWordsID);
					changeToStopWords();
					jRadioButtonStopWords.setSelected(true);
				}
				else
				{
					jRadioButtonNoProProcessing.setSelected(true);
					changeNoPreprocessing();
				}
			}
			else
			{
				jRadioButtonNoProProcessing.setSelected(true);
				changeNoPreprocessing();
			}
		}
		else if(preprocessing == NERLexicalResourcesPreProssecingEnum.POSTagging)
		{
			jRadioButtonPosTags.setSelected(true);
			changePosTagging();
		}
		else if(preprocessing == NERLexicalResourcesPreProssecingEnum.Hybrid)
		{
			if(lexicalWords != null && lexicalWords.size()>0)
			{
				if(stopWordsID > 0)
				{
					jRadioButtonPOSTaggingAndStopWords.setSelected(true);
					selectStopWords(jListLexicalWords2,stopWordsID);
					changeToPOSTAggingAndStopWords();
				}
				else
				{
					jRadioButtonPosTags.setSelected(true);
					changePosTagging();
				}
			}
			else
			{
				jRadioButtonNoProProcessing.setSelected(true);
				changeNoPreprocessing();
			}
		}
		else
		{
			jRadioButtonNoProProcessing.setSelected(true);
			changeNoPreprocessing();
		}		
	}


	private void fillDefaultSetting() {
		Object posTagger = PropertiesManager.getPManager().getProperty(NERLexicalResourcesDefaultSettings.PRE_PROCESSING);
		NERLexicalResourcesPreProssecingEnum preprocessing = NERLexicalResourcesPreProssecingEnum.convertStringToNERLexicalResourcesPreProssecingEnum(posTagger.toString());
		int stopWordsID =  Integer.valueOf(PropertiesManager.getPManager().getProperty(NERLexicalResourcesDefaultSettings.LEXICAL_RESOURCE_STOPWORDS_ID).toString());
		if(preprocessing == NERLexicalResourcesPreProssecingEnum.StopWords)
		{
			if(lexicalWords != null && lexicalWords.size()>0)
			{
				if(stopWordsID > 0)
				{
					selectStopWords(jListLexicalWords,stopWordsID);
					changeToStopWords();
					jRadioButtonStopWords.setSelected(true);
				}
				else
				{
					jRadioButtonNoProProcessing.setSelected(true);
					changeNoPreprocessing();
				}
			}
			else
			{
				jRadioButtonNoProProcessing.setSelected(true);
				changeNoPreprocessing();
			}
		}
		else if(preprocessing == NERLexicalResourcesPreProssecingEnum.POSTagging)
		{
			jRadioButtonPosTags.setSelected(true);
			changePosTagging();
		}
		else if(preprocessing == NERLexicalResourcesPreProssecingEnum.Hybrid)
		{
			if(lexicalWords != null && lexicalWords.size()>0)
			{
				if(stopWordsID > 0)
				{
					jRadioButtonPOSTaggingAndStopWords.setSelected(true);
					selectStopWords(jListLexicalWords2,stopWordsID);
					changeToPOSTAggingAndStopWords();
				}
				jRadioButtonPosTags.setSelected(true);
				changePosTagging();
			}
			else
			{
				jRadioButtonNoProProcessing.setSelected(true);
				changeNoPreprocessing();
			}
		}
		else
		{
			jRadioButtonNoProProcessing.setSelected(true);
			changeNoPreprocessing();
		}
		
	}


	private void selectStopWords(JList jListLexicalWords, int stopWordsID) {
		ILexicalWords lexi = new LexicalWords(stopWordsID, "", "");
		jListLexicalWords.setSelectedValue(lexi, true);	
	}


	private void fill() {
		if(lexicalWords == null && lexicalWords.size()<1)
		{
			jRadioButtonStopWords.setEnabled(false);
			jRadioButtonPOSTaggingAndStopWords.setEnabled(false);
		}
		else
		{
			jRadioButtonStopWords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changeToStopWords();
				}
			});
			jRadioButtonPOSTaggingAndStopWords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changeToStopWords();
				}
			});
		}
		ListModel jListLexicalWordsModel = getModel();
		ListModel jListLexicalWordsModel2 = getModel();
		jListLexicalWords.setModel(jListLexicalWordsModel);
		jListLexicalWords2.setModel(jListLexicalWordsModel2);
	}



	private void initGUI() {
		setEnableNextButton(true);
		setEnableDoneButton(false);
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

	private JRadioButton getJRadioButtonNoPreprocessing() {
		if(jRadioButtonNoProProcessing == null) {
			jRadioButtonNoProProcessing = new JRadioButton();
			jRadioButtonNoProProcessing.setText("No");
			jRadioButtonNoProProcessing.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changeNoPreprocessing();
				}
			});
			
		}
		return jRadioButtonNoProProcessing;
	}
	
	protected void changeNoPreprocessing() {
		jPanelPOSTagging.setVisible(false);
		jPanelSelectNone.setVisible(true);	
		jPanelSelectStopWords.setVisible(false);
		jPanelPOSTaggingAndStopWords.setVisible(false);
		jPanelUpper.updateUI();
	}



	private JRadioButton getJRadioButtonStopWordsYes() {
		if(jRadioButtonStopWords == null) {
			jRadioButtonStopWords = new JRadioButton();
			jRadioButtonStopWords.setText("Stop Words");
			jRadioButtonStopWords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changeToStopWords();
				}
			});
		}
		return jRadioButtonStopWords;
	}
	
	protected void changeToStopWords() {
		jPanelPOSTagging.setVisible(false);
		jPanelSelectNone.setVisible(false);	
		jPanelSelectStopWords.setVisible(true);	
		jPanelPOSTaggingAndStopWords.setVisible(false);
		jPanelUpper.updateUI();

	}
	
	private JRadioButton getJRadioButtonPosTags() {
		if(jRadioButtonPosTags == null) {
			jRadioButtonPosTags = new JRadioButton();
			jRadioButtonPosTags.setText("POS-Tagging");
			jRadioButtonPosTags.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changePosTagging();
				}
			});
		}
		return jRadioButtonPosTags;
	}


	protected void changePosTagging() {
		jPanelPOSTagging.setVisible(true);
		jPanelSelectNone.setVisible(false);	
		jPanelSelectStopWords.setVisible(false);
		jPanelPOSTaggingAndStopWords.setVisible(false);
		jPanelUpper.updateUI();
	}
	
	private JRadioButton getJRadioButtonPOSTaggingAndStopWords() {
		if(jRadioButtonPOSTaggingAndStopWords == null) {
			jRadioButtonPOSTaggingAndStopWords = new JRadioButton();
			jRadioButtonPOSTaggingAndStopWords.setText("Hybrid (Stop Words + POS Tagging)");
			jRadioButtonPOSTaggingAndStopWords.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					changeToPOSTAggingAndStopWords();
				}
			});
		}
		return jRadioButtonPOSTaggingAndStopWords;
	}
	
	private void changeToPOSTAggingAndStopWords() {
		jPanelPOSTagging.setVisible(false);
		jPanelSelectNone.setVisible(false);	
		jPanelSelectStopWords.setVisible(false);
		jPanelPOSTaggingAndStopWords.setVisible(true);
		jPanelUpper.updateUI();
	}



	private JLabel getJLabelStopWords() {
		if(jLabelStopWords == null) {
			jLabelStopWords = new JLabel();
			jLabelStopWords.setText("Pre-Processing :");
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
	
	private JScrollPane getJScrollPaneLExicalWords2() {
		if(jScrollPaneLExicalWords2 == null) {
			jScrollPaneLExicalWords2 = new JScrollPane();
			jScrollPaneLExicalWords2.setViewportView(getJListLexicalWords2());
		}
		return jScrollPaneLExicalWords2;
	}
	
	private JList getJListLexicalWords2() {
		if(jListLexicalWords2 == null) {
			jListLexicalWords2 = new JList();
		}
		return jListLexicalWords2;
	}



	private JList getJListLexicalWords() {
		if(jListLexicalWords == null) {
			jListLexicalWords = new JList();
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

	}

	public void goBack() {
		List<Object> param = getParam();
		ResourcesToNerAnote resourceToNER = (ResourcesToNerAnote) getParam().get(1);
		param.add(resourceToNER.isUseOtherResourceInformationInRules());
		closeView();
		try {
			new NERAnoteOperationWizard3(param);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	public void goNext() {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		Corpus corpus = (Corpus) obj;
		ResourcesToNerAnote resourceToNER = (ResourcesToNerAnote) getParam().get(1);
		if(jRadioButtonNoProProcessing.isSelected())
		{
			
			List<Object> param = getParam();
			INERLexicalResourcesConfiguration configuration = new NERLexicalResourcesConfiguration(corpus, NERLexicalResourcesPreProssecingEnum.No, resourceToNER, null, null, resourceToNER.isCaseSensitive(),false,resourceToNER.isUseOtherResourceInformationInRules());
			param.add(configuration);
			closeView();
			new NERAnoteOperationWizard5(param);
		}
		else if(jRadioButtonStopWords.isSelected())
		{
			if(jListLexicalWords.getSelectedValue() == null)
			{
				Workbench.getInstance().warn("Please Select one Lexical Words Resource");
			}
			else
			{
				LexicalWords lex = lexicalWords.get(jListLexicalWords.getSelectedIndices()[0]);
				List<Object> param = getParam();
				INERLexicalResourcesConfiguration configuration = new NERLexicalResourcesConfiguration(corpus, NERLexicalResourcesPreProssecingEnum.StopWords, resourceToNER, null, lex, resourceToNER.isCaseSensitive(),false,resourceToNER.isUseOtherResourceInformationInRules());
				param.add(configuration);
				closeView();
				new NERAnoteOperationWizard5(param);
			}
			
		}
		else if(jRadioButtonPosTags.isSelected())
		{
			Set<String> postags = getPOSTagges();
			if(postags.size()<1)
			{
				Workbench.getInstance().warn("Please Select at least one Pos-Tag label");
			}
			else
			{
				List<Object> param = getParam();
				INERLexicalResourcesConfiguration configuration = new NERLexicalResourcesConfiguration(corpus, NERLexicalResourcesPreProssecingEnum.POSTagging, resourceToNER, postags, null, resourceToNER.isCaseSensitive(),false,resourceToNER.isUseOtherResourceInformationInRules());
				param.add(configuration);
				closeView();
				new NERAnoteOperationWizard5(param);
			}

		}
		else
		{
			Set<String> postags = getPOSTagges2();
			if(postags.size()<1)
			{
				Workbench.getInstance().warn("Please Select at least one Pos-Tag label");
				jPanelPOSTaggingAndStopWords.setSelectedComponent(jPanelPOSTagging2);
			}
			else if(jListLexicalWords2.getSelectedValue() == null)
			{
				Workbench.getInstance().warn("Please Select one Lexical Words Resource");
				jPanelPOSTaggingAndStopWords.setSelectedComponent(jPanelSelectStopWords2);
			}
			else
			{
				List<Object> param = getParam();
				LexicalWords lex = lexicalWords.get(jListLexicalWords2.getSelectedIndices()[0]);
				INERLexicalResourcesConfiguration configuration = new NERLexicalResourcesConfiguration(corpus, NERLexicalResourcesPreProssecingEnum.Hybrid, resourceToNER, postags, lex, resourceToNER.isCaseSensitive(),false,resourceToNER.isUseOtherResourceInformationInRules());
				param.add(configuration);
				closeView();
				new NERAnoteOperationWizard5(param);
			}
		}
	}
	
	private Set<String> getPOSTagges() {
		Set<String> posTAgsSelected = new HashSet<String>();
		int rowSize = jtablePOSTags.getModel().getRowCount();
		for(int i=0;i<rowSize;i++)
		{
			boolean sel = (Boolean) jtablePOSTags.getValueAt(i,1);
			if(sel)
			{
				posTAgsSelected.add(((PartOfSpeechLabels) jtablePOSTags.getValueAt(i, 0)).value());
			}
		}
		return posTAgsSelected;
	}
	
	private Set<String> getPOSTagges2() {
		Set<String> posTAgsSelected = new HashSet<String>();
		int rowSize = jtablePOSTags2.getModel().getRowCount();
		for(int i=0;i<rowSize;i++)
		{
			boolean sel = (Boolean) jtablePOSTags2.getValueAt(i,1);
			if(sel)
			{
				posTAgsSelected.add(((PartOfSpeechLabels) jtablePOSTags2.getValueAt(i, 0)).value());
			}
		}
		return posTAgsSelected;
	}

	public JComponent getMainComponent() {
		if(jPanelUpper == null)
		{
			jPanelUpper = new JPanel();
			buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(getJRadioButtonNoPreprocessing());
			buttonGroup1.add(getJRadioButtonStopWordsYes());
			buttonGroup1.add(getJRadioButtonPosTags());
			buttonGroup1.add(getJRadioButtonPOSTaggingAndStopWords());
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelUpper.setLayout(thisLayout);
			{
				jPanelStopWords = new JPanel();
				jPanelStopWords.setBorder(BorderFactory.createTitledBorder("Pre-Processing Options"));
				GridBagLayout jPanelEnableStopWordsLayout = new GridBagLayout();
				jPanelUpper.add(jPanelStopWords, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelEnableStopWordsLayout.rowHeights = new int[] {7, 7, 7};
				jPanelEnableStopWordsLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1};
				jPanelEnableStopWordsLayout.columnWidths = new int[] {7, 7, 7, 20, 20};
				jPanelStopWords.setLayout(jPanelEnableStopWordsLayout);
				jPanelStopWords.add(getJLabelStopWords(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelStopWords.add(getJRadioButtonNoPreprocessing(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelStopWords.add(getJRadioButtonStopWordsYes(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelStopWords.add(getJRadioButtonPosTags(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelStopWords.add(getJRadioButtonPOSTaggingAndStopWords(), new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{	
				jPanelUpper.add(getStopWordsPanel(), new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelUpper.add(getNonePAnel(), new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelUpper.add(getPostaggingPAnel(), new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
				jPanelUpper.add(getPostaggingAnsStopWordsAnel(), new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
			}
		}
		return jPanelUpper;
	}
	
	
	
	private JTabbedPane getPostaggingAnsStopWordsAnel() {
		if(jPanelPOSTaggingAndStopWords == null)
		{
			GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
			jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPOSTaggingAndStopWords = new JTabbedPane();
			jPanelPOSTaggingAndStopWords.setVisible(false);
			jPanelPOSTaggingAndStopWords.addTab("Select POS-Tags", null, getPostaggingPAnel2(), null);
			jPanelPOSTaggingAndStopWords.addTab("Available Lexical Words", null, getStopWordsPanel2(), null);
		}
		return jPanelPOSTaggingAndStopWords;
	}



	private JPanel getStopWordsPanel2() {
		if(jPanelSelectStopWords2 == null)
		{
			GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
			jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSelectStopWords2 = new JPanel();
			jPanelSelectStopWords2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Available Lexical Words", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelSelectStopWords2.setLayout(jPanelSelectStopWordsLayout);
			jPanelSelectStopWords2.add(getJScrollPaneLExicalWords2(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSelectStopWords2;
	}

	private JPanel getPostaggingPAnel2() {
		if(jPanelPOSTagging2 == null)
		{
			GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
			jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPOSTagging2 = new JPanel();
			jPanelPOSTagging2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Select POS-Tags", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelPOSTagging2.setLayout(jPanelSelectStopWordsLayout);
			jPanelPOSTagging2.add(getJScrollPanePOSTags2(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}
		return jPanelPOSTagging2;
	}



	private JPanel getStopWordsPanel() {
		if(jPanelSelectStopWords == null)
		{
			GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
			jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSelectStopWords = new JPanel();
			jPanelSelectStopWords.setVisible(false);
			jPanelSelectStopWords.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Available Lexical Words", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelSelectStopWords.setLayout(jPanelSelectStopWordsLayout);
			jPanelSelectStopWords.add(getJScrollPaneLExicalWords(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSelectStopWords;
	}



	private JPanel getPostaggingPAnel() {
		if(jPanelPOSTagging == null)
		{
			GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
			jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPOSTagging = new JPanel();
			jPanelPOSTagging.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Select POS-Tags", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelPOSTagging.setVisible(false);
			jPanelPOSTagging.setLayout(jPanelSelectStopWordsLayout);
			jPanelPOSTagging.add(getJScrollPanePOSTags(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}
		return jPanelPOSTagging;
	}



	private JScrollPane getJScrollPanePOSTags() {
		if(jScrollPanePOSTags == null) {
			jScrollPanePOSTags = new JScrollPane();
			jScrollPanePOSTags.setViewportView(getJtablePOSTags());
		}
		return jScrollPanePOSTags;
	}
	
	private JScrollPane getJScrollPanePOSTags2() {
		if(jScrollPanePOSTags2 == null) {
			jScrollPanePOSTags2 = new JScrollPane();
			jScrollPanePOSTags2.setViewportView(getJtablePOSTags2());
		}
		return jScrollPanePOSTags2;
	}
	private JTable getJtablePOSTags2() {
		if(jtablePOSTags2 == null) {
			jtablePOSTags2 = POSTagsHelpGUI.getJtablePOSTags();

		}
		return jtablePOSTags2;
	}


	private JTable getJtablePOSTags() {
		if(jtablePOSTags == null) {
			jtablePOSTags = POSTagsHelpGUI.getJtablePOSTags();
		}
		return jtablePOSTags;
	}
	
	private JPanel getNonePAnel() {
		if(jPanelSelectNone == null)
		{
			jPanelSelectNone = new JPanel();
		}
		return jPanelSelectNone;
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Create_Annotation_Schema_By_NER_Lexical_Resources#Pre-Processing";
	}	
	
}
