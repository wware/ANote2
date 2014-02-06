package pt.uminho.anote2.nergate.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.anote2.nergate.abner.ABNERTrainingModel;
import pt.uminho.generic.genericpanel.database.DataBaseDelete;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class AbnerGUI extends DataBaseDelete implements InputGUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ParamsReceiver paramsRec = null;
	private JComboBox models;
	private JPanel jPanelNormalizedtext;
	private JCheckBox jCheckBoxNormalizedText;
	private JPanel jPanelUpperPanel;

	public AbnerGUI() {
		super(GlobalTextInfo.abnerInfo);
		this.setTitle("Abner - Entity Recognition");
		this.setModal(true);	
	}

	@Override
	public JPanel getDetailPanel() {
		jPanelUpperPanel = new JPanel();
		jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Training Model Selection", TitledBorder.LEADING, TitledBorder.TOP));
		GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
		jPanelUpperPanelLayout.rowWeights = new double[] {0.1, 0.0, 0.1, 0.0};
		jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 20, 7};
		jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
		jPanelUpperPanelLayout.columnWidths = new int[] {7};
		jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
		jPanelUpperPanel.setPreferredSize(new java.awt.Dimension(431, 370));
		{
			ComboBoxModel jComboBoxRelationModelsModel = new DefaultComboBoxModel(ABNERTrainingModel.values());
			models = new JComboBox();
			models.setModel(jComboBoxRelationModelsModel);
			jPanelUpperPanel.add(models, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanel.add(getJPanelNormalizedtext(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelUpperPanel;
	}

	@Override
	protected void okButtonAction() {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		Corpus corpus = (Corpus) obj;		
		ABNERTrainingModel model = (ABNERTrainingModel) models.getSelectedItem();
		paramsRec.paramsIntroduced( new ParamSpec[]{
				new ParamSpec("Corpus", Corpus.class,corpus, null),
				new ParamSpec("normalized", Boolean.class,jCheckBoxNormalizedText.isSelected(), null),
				new ParamSpec("model", ABNERTrainingModel.class,model, null)
		});	
	}

	protected void cancelButtonAction() {
		finish();
	}

	public void init(ParamsReceiver receiver, OperationDefinition<?> operation) {
		
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Corpus selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = receiver;
			this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}
	}

	public void onValidationError(Throwable t) {
		Workbench.getInstance().error(t);				
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink + "Corpus_Create_Annotation_Schema_By_Abner";
	}
	
	private JPanel getJPanelNormalizedtext() {
		if(jPanelNormalizedtext == null) {
			jPanelNormalizedtext = new JPanel();
			GridBagLayout jPanelNormalizedtextLayout = new GridBagLayout();
			jPanelNormalizedtextLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelNormalizedtextLayout.rowHeights = new int[] {7, 7, 7};
			jPanelNormalizedtextLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			jPanelNormalizedtextLayout.columnWidths = new int[] {7, 7, 7};
			jPanelNormalizedtext.setLayout(jPanelNormalizedtextLayout);
			jPanelNormalizedtext.setBorder(BorderFactory.createTitledBorder("Normalize Text"));
			jPanelNormalizedtext.add(getJCheckBoxNormalizedText(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
		}
		return jPanelNormalizedtext;
	}
	
	private JCheckBox getJCheckBoxNormalizedText() {
		if(jCheckBoxNormalizedText == null) {
			jCheckBoxNormalizedText = new JCheckBox();
			jCheckBoxNormalizedText.setText("Normalize Text");
		}
		return jCheckBoxNormalizedText;
	}

}
