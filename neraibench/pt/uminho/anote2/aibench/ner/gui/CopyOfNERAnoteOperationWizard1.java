package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.process.IProcess;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CopyOfNERAnoteOperationWizard1 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JRadioButton jRadioButton;
	private JRadioButton jRadioButtonNewNerAnote;
	
	private List<IProcess> processes;
	private Corpus corpus;


	public CopyOfNERAnoteOperationWizard1() {
		super(600,400,new ArrayList<Object>());
		confirmDatatype();
		initGUI();
		checkProcessInCorpus();
		this.setTitle("NER @note Step 1/4");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);
		
	}

	private void checkProcessInCorpus() {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		corpus = (Corpus) obj;
		processes = corpus.getCorpora().getListProcess();
		if(processes.size()==0)
		{
			jRadioButton.setEnabled(false);
		}
	}

	private void confirmDatatype() {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("You must select Corpus Datatype in Clipboard");
			return;
		}
	}

	private void initGUI() {
		setEnableDoneButton(false);
		setEnableBackButton(false);
		{
			jPanelUpperPanel = new JPanel();
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jRadioButtonNewNerAnote = new JRadioButton();
				jPanelUpperPanel.add(jRadioButtonNewNerAnote, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButtonNewNerAnote.setText("New NER Anote");
				jRadioButtonNewNerAnote.setSelected(true);
				jRadioButtonNewNerAnote.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						selectNewNER();	
					}
				
				});	
			}
			{
				jRadioButton = new JRadioButton();
				jPanelUpperPanel.add(jRadioButton, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButton.setText("Existing Process Apply");
				jRadioButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						selectOlderNer();	
					}
				
				});	
			}
			getJScrollPaneUpPanel().setViewportView(jPanelUpperPanel);
		}	
	}

	
	protected void selectOlderNer() {
		if(jRadioButton.isSelected())
		{
			jRadioButtonNewNerAnote.setSelected(false);
		}
		else
		{
			jRadioButtonNewNerAnote.setSelected(true);
		}
	}

	protected void selectNewNER() {
		if(jRadioButtonNewNerAnote.isSelected())
		{
			jRadioButton.setSelected(false);
		}
		else
		{
			jRadioButton.setSelected(true);
		}
	}

	public void done() {}

	public void goBack() {
	}

	public void goNext() {
		if(jRadioButtonNewNerAnote.isSelected())
		{
			this.setVisible(false);
			new NERAnoteOperationWizard2(600,400,new ArrayList<Object>());
		}
		else
		{
			this.setVisible(false);
			List<Object> param = new ArrayList<Object>();
			param.add(processes);
			param.add(corpus);
			new NERAnoteOperationOtherCorpusWizard2(600,400,param);		
		}
	}
	
	public void finish() {
		this.setVisible(false);
		this.dispose();
		Workbench.getInstance().warn("Operation Ner Anote Cancel");
		return;
	}
}
