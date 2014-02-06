package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.ner.configuration.INERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.settings.NERLexicalResourcesDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationWizard5 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelEnableNormalization;
	private JPanel jPanelUpper;
	private JLabel jLabelStopWords;
	private JRadioButton jRadioButtonNormalizatioYes;
	private JRadioButton jRadioButtonNormalizatioNo;
	private ButtonGroup buttonGroup1;
	private JPanel jPanelNormalizatioExample;
	private JLabel jLabelImage;

	public NERAnoteOperationWizard5(List<Object> param) {
		super(param);
		initGUI();
		fillDefautSettings();
		this.setTitle("NER @Note - Normalization Option");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillDefautSettings() {
		boolean normalization = (Boolean) PropertiesManager.getPManager().getProperty(NERLexicalResourcesDefaultSettings.NORMALIZATION);
		if(normalization)
		{
			jRadioButtonNormalizatioYes.setSelected(true);
		}
		else
		{
			jRadioButtonNormalizatioNo.setSelected(true);
		}
	}

	private void initGUI() {
		setEnableNextButton(false);
		setEnableDoneButton(true);
	}

	private JRadioButton getJRadioButtonStopWordsYNo() {
		if(jRadioButtonNormalizatioNo == null) {
			jRadioButtonNormalizatioNo = new JRadioButton();
			jRadioButtonNormalizatioNo.setText("No");
		}
		return jRadioButtonNormalizatioNo;
	}


	private JRadioButton getJRadioButtonStopWordsYes() {
		if(jRadioButtonNormalizatioYes == null) {
			jRadioButtonNormalizatioYes = new JRadioButton();
			jRadioButtonNormalizatioYes.setText("Yes");	
		}
		return jRadioButtonNormalizatioYes;
	}
	
	private JLabel getJLabelStopWords() {
		if(jLabelStopWords == null) {
			jLabelStopWords = new JLabel();
			jLabelStopWords.setText("Normalization :");
		}
		return jLabelStopWords;
	}

	public void done() {
		
		INERLexicalResourcesConfiguration configuration = (INERLexicalResourcesConfiguration) getParam().get(2);
		configuration.setNormalized(jRadioButtonNormalizatioYes.isSelected());
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("Corpus", Corpus.class,((Corpus)configuration.getCorpus()), null),
				new ParamSpec("configuration", INERLexicalResourcesConfiguration.class,configuration, null)
		};

		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.neranote")){	
				closeView();
				Workbench.getInstance().executeOperation(def, paramsSpec);
			}
		}
	}

	public void goBack() {
		List<Object> param = getParam();
		closeView();
		try {
			new NERAnoteOperationWizard4(param);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	public void goNext() {

	}

	public JComponent getMainComponent() {
		if(jPanelUpper == null)
		{
			{
				jPanelUpper = new JPanel();
				getContentPane().add(jPanelUpper);
				buttonGroup1 = new ButtonGroup();
				buttonGroup1.add(getJRadioButtonStopWordsYNo());
				buttonGroup1.add(getJRadioButtonStopWordsYes());
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelUpper.setLayout(thisLayout);
				{
					jPanelEnableNormalization = new JPanel();
					jPanelEnableNormalization.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Normalization Option", TitledBorder.LEADING, TitledBorder.TOP));
					GridBagLayout jPanelEnableStopWordsLayout = new GridBagLayout();
					jPanelUpper.add(jPanelEnableNormalization, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelEnableStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
					jPanelEnableStopWordsLayout.rowHeights = new int[] {7, 7, 7};
					jPanelEnableStopWordsLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
					jPanelEnableStopWordsLayout.columnWidths = new int[] {7, 7, 7};
					jPanelEnableNormalization.setLayout(jPanelEnableStopWordsLayout);
					jPanelEnableNormalization.add(getJLabelStopWords(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jPanelEnableNormalization.add(getJRadioButtonStopWordsYNo(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelEnableNormalization.add(getJRadioButtonStopWordsYes(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jPanelNormalizatioExample = new JPanel();
					jPanelNormalizatioExample.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
					GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
					jPanelUpper.add(jPanelNormalizatioExample, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1};
					jPanelSelectStopWordsLayout.rowHeights = new int[] {7};
					jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1};
					jPanelSelectStopWordsLayout.columnWidths = new int[] {7};
					jPanelNormalizatioExample.setLayout(jPanelSelectStopWordsLayout);
					jPanelNormalizatioExample.add(getJLabelImage(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
		return jPanelUpper;
	}
	
	private JLabel getJLabelImage() {
		if(jLabelImage == null) {
			jLabelImage = new JLabel();
			jLabelImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/normalization.png")));

		}
		return jLabelImage;
	}

	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Create_Annotation_Schema_By_NER_Lexical_Resources#Normalization";
	}

}
