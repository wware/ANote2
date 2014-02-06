package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.wizart.WizartStandard;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationOtherCorpusWizard2 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JList jListProcesses;
	private JLabel jLabel1;

	public NERAnoteOperationOtherCorpusWizard2(int sizeH,int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		initGUI();
		this.setTitle("NER @Note - Lexical Resources");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);
		
	}


	private void initGUI() {
		setEnableDoneButton(false);
		{
			jPanelUpperPanel = new JPanel();
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.05};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.025, 0.1, 0.1, 0.025};
			jPanelUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jLabel1 = new JLabel();
				jPanelUpperPanel.add(jLabel1, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setText("Select Process");
			}
			{
				ListModel jListProcessesModel = getProcessesModel();
				jListProcesses = new JList();
				jPanelUpperPanel.add(jListProcesses, new GridBagConstraints(1, 1, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jListProcesses.setModel(jListProcessesModel);
			}
			getJScrollPaneUpPanel().setViewportView(jPanelUpperPanel);
		}	
	}
	
	@SuppressWarnings("unchecked")
	private DefaultComboBoxModel getProcessesModel()
	{
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		List<IProcess> lis = (List<IProcess>) getParam().get(0);
		Corpus corpus = (Corpus) getParam().get(1);
		Set<IProcess> processes = corpus.getIProcesses("");
		for(IProcess pro:lis)
		{
			IIEProcess process = (IIEProcess) pro;
			if(process.getDescription().equals("Anote NER")&&!processes.contains(pro))
			{
				model.addElement(pro);
			}
		}	
		return model;
	}

	

	public void done() {}

	public void goBack() {
		this.setVisible(false);
		new NERAnoteOperationWizard1();
	}

	public void goNext() {
		
		if(jListProcesses.getSelectedIndices().length==1)
		{
			IProcess process = (IProcess) jListProcesses.getSelectedValue();
			List<Object> param = getParam();
			param.add(process);
			this.setVisible(false);
			new NERAnoteOperationOtherCorpusWizard3(600,400,param);
		}
		else
		{
			Workbench.getInstance().warn("Select one Process");
		}
		
	}
	
	public void finish() {
		this.setVisible(false);
		this.dispose();
		Workbench.getInstance().warn("Operation Ner Anote Cancel");
		return;
	}

}
