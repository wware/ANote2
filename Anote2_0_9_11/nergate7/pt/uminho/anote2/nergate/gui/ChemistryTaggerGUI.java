package pt.uminho.anote2.nergate.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.generic.genericpanel.database.DataBaseDelete;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class ChemistryTaggerGUI extends DataBaseDelete implements InputGUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ParamsReceiver paramsRec = null;
	private JPanel jPanelUpperPanel;
	private JRadioButton chemistryElements;
	private JCheckBox jCheckBoxNormalizeText;
	private JPanel jPanelNormalized;
	private JRadioButton chemistryCompounds;
	private JRadioButton chemistryIon;

	public ChemistryTaggerGUI() {
		super(GlobalTextInfo.chemistryTaggerInfo);
		this.setTitle("Chemistry Tagger - Entity Recognition");
		this.setModal(true);	
	}

	@Override
	public JPanel getDetailPanel() {
		jPanelUpperPanel = new JPanel();
		jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Select Chemistry Tagger Source(s)", TitledBorder.LEADING, TitledBorder.TOP));
		GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
		jPanelUpperPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7, 20};
		jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
		jPanelUpperPanelLayout.columnWidths = new int[] {7};
		jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
		{
			jPanelUpperPanel.add(getChemicalIonButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanel.add(getChemicalElementsButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanel.add(getChemicalCompoudsButton(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanel.add(getJPanelNormalized(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}
		return jPanelUpperPanel;
	}
	
	private JRadioButton getChemicalCompoudsButton() {
		if(chemistryCompounds==null)
		{
			chemistryCompounds = new JRadioButton();
			chemistryCompounds.setText("Compounds");
			chemistryCompounds.setSelected(true);
		}
		return chemistryCompounds;
	}

	private JRadioButton getChemicalElementsButton() {
		if(chemistryElements==null)
		{
			chemistryElements = new JRadioButton();
			chemistryElements.setText("Elements");
			chemistryElements.setSelected(true);
		}
		return chemistryElements;
	}

	private JRadioButton getChemicalIonButton() {
		if(chemistryIon==null)
		{
			chemistryIon = new JRadioButton();
			chemistryIon.setText("Ion");
			chemistryIon.setSelected(true);
		}
		return chemistryIon;
	}

	@Override
	protected void okButtonAction() {

		if(chemistryElements.isSelected() || chemistryCompounds.isSelected() || chemistryIon.isSelected())
		{
			Object obj = HelpAibench.getSelectedItem(Corpus.class);
			Corpus corpus = (Corpus) obj;		
			paramsRec.paramsIntroduced( new ParamSpec[]{
					new ParamSpec("Corpus", Corpus	.class,corpus, null),
					new ParamSpec("normalized", Boolean.class,jCheckBoxNormalizeText.isSelected(), null),
					new ParamSpec("ChemistryElements", Boolean.class,chemistryElements.isSelected(), null),
					new ParamSpec("ChemistryCompounds", Boolean.class,chemistryCompounds.isSelected(), null),
					new ParamSpec("ChemistryIon", Boolean.class,chemistryIon.isSelected(), null)
			});	
		}
		else
		{
			Workbench.getInstance().warn("Select at least one option");
		}
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
		return GlobalOptions.wikiGeneralLink + "Corpus_Create_Annotation_Schema_By_Chemical_Tagger";
	}
	
	private JPanel getJPanelNormalized() {
		if(jPanelNormalized == null) {
			jPanelNormalized = new JPanel();
			GridBagLayout jPanelNormalizedLayout = new GridBagLayout();
			jPanelNormalizedLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelNormalizedLayout.rowHeights = new int[] {7, 7, 7};
			jPanelNormalizedLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			jPanelNormalizedLayout.columnWidths = new int[] {7, 7, 7};
			jPanelNormalized.setLayout(jPanelNormalizedLayout);
			jPanelNormalized.setBorder(BorderFactory.createTitledBorder("Normalized Text"));
			jPanelNormalized.add(getJCheckBoxNormalizeText(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
		}
		return jPanelNormalized;
	}
	
	private JCheckBox getJCheckBoxNormalizeText() {
		if(jCheckBoxNormalizeText == null) {
			jCheckBoxNormalizeText = new JCheckBox();
			jCheckBoxNormalizeText.setText("Normalize Text");
		}
		return jCheckBoxNormalizeText;
	}

}
