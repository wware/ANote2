package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.anote2.process.IProcess;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationWizard1 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JRadioButton jRadioButton;
	private JRadioButton jRadioButtonNewNerAnote;
	private ButtonGroup buttonGroup1;
	private JTextField jTextFieldImportConfiguration;
	private JTextField jTextFieldNewNER;

	private List<IProcess> processes;
	private Corpus corpus;


	public NERAnoteOperationWizard1() {
		super(new ArrayList<Object>());	
		initGUI();	
		this.setModal(true);	
	}
	
	public NERAnoteOperationWizard1(boolean not) throws SQLException, DatabaseLoadDriverException {
		super(new ArrayList<Object>());	
		initGUI();	
		this.setModal(true);
		checkProcessInCorpus();	
		this.setTitle("NER @Note - Select Configuration");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}

	private void checkProcessInCorpus() throws SQLException, DatabaseLoadDriverException {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		corpus = (Corpus) obj;
		processes = corpus.getCorpora().getListProcess();
		int available = 0;
		for(IProcess process:processes)
		{			
			if(((IEProcess) process).getName().equals(GlobalNames.nerAnote))
			{
				available ++;
			}
		}
		if(available==0)
		{
			jRadioButton.setEnabled(false);
		}

	}

	private void initGUI() {
		setEnableDoneButton(false);
		setEnableBackButton(false);
	}


	public void done() {}

	public void goBack() {
	}

	public void goNext() {
		if(jRadioButtonNewNerAnote.isSelected())
		{
			closeView();
			new NERAnoteOperationWizard2(new ArrayList<Object>());
		}
		else
		{			
			List<Object> param = new ArrayList<Object>();
			param.add(processes);
			param.add(corpus);
			closeView();
			try {
				new NERAnoteOperationOtherCorpusWizard2(param);
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		}

	}
	
	private ButtonGroup getButtonGroup1() {
		if(buttonGroup1 == null) {
			buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(jRadioButtonNewNerAnote);
			buttonGroup1.add(jRadioButton);
		}
		return buttonGroup1;
	}

	public JPanel getMainComponent() {
		if(jPanelUpperPanel == null)
		{
			jPanelUpperPanel = new JPanel();
			jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder(null, "NER Configuration Options", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.1,0.025, 0.0, 0.1,0.025, 0.0,0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7,7,7 ,7, 7,7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.01, 0.1, 0.001};
			jPanelUpperPanelLayout.columnWidths = new int[] {7, 7, 7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jRadioButtonNewNerAnote = new JRadioButton();
				jPanelUpperPanel.add(jRadioButtonNewNerAnote, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButtonNewNerAnote.setText("New Configuration");
				jRadioButtonNewNerAnote.setSelected(true);
			}
			{
				jRadioButton = new JRadioButton();
				jPanelUpperPanel.add(jRadioButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButton.setText("Load Configuration");
			}
			{
				jTextFieldNewNER = new JTextField();
				jPanelUpperPanel.add(jTextFieldNewNER, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldNewNER.setText(GlobalTextInfo.nerSelectNewNER);
				jTextFieldNewNER.setEditable(false);
			}
			{
				jTextFieldImportConfiguration = new JTextField();
				jPanelUpperPanel.add(jTextFieldImportConfiguration, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldImportConfiguration.setText(GlobalTextInfo.nerSelectAlreadyDoneNer);
				jTextFieldImportConfiguration.setEditable(false);
			}
			getButtonGroup1();
		}
		return jPanelUpperPanel;
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Corpus selected on clipboard");
			dispose();
		}
		else
		{
			try {
				checkProcessInCorpus();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}	
			this.setTitle("NER @Note - Select Configuration");
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}
	}

	@Override
	public void onValidationError(Throwable arg0) {
		
	}
	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Create_Annotation_Schema_By_NER_Lexical_Resources#New_Configuration_or_Load_Configuration";
	}
}
