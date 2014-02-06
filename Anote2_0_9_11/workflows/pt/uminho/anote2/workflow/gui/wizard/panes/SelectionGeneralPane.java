package pt.uminho.anote2.workflow.gui.wizard.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;

import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.AWorkflowSelectionPane;

public class SelectionGeneralPane extends AWorkflowSelectionPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JCheckBox jRadioButtonPubmedSearch;
	private JCheckBox jRadioButtonNER;
	private JCheckBox jRadioButtonRE;
	private JCheckBox jRadioButtonCreateCorpus;

	public SelectionGeneralPane()
	{
		initGUI();
	}


	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.3, 0.1, 0.1, 0.1, 0.1, 0.3};
			thisLayout.rowHeights = new int[] {7, 20, 20, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.15, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7};
			this.setLayout(thisLayout);
			{
				jRadioButtonPubmedSearch = new JCheckBox();
				this.add(jRadioButtonPubmedSearch, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				jRadioButtonPubmedSearch.setText("Pubmed Search (IR)");
				jRadioButtonPubmedSearch.setSelected(true);
				jRadioButtonPubmedSearch.setEnabled(false);
			}
			{
				jRadioButtonCreateCorpus = new JCheckBox();
				this.add(jRadioButtonCreateCorpus, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				jRadioButtonCreateCorpus.setText("Create Corpus");
				jRadioButtonCreateCorpus.setSelected(true);
				jRadioButtonCreateCorpus.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!jRadioButtonCreateCorpus.isSelected())
						{
							jRadioButtonNER.setSelected(false);
							jRadioButtonRE.setSelected(false);
						}
					}
				});
			}
			{
				jRadioButtonNER = new JCheckBox();
				this.add(jRadioButtonNER, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				jRadioButtonNER.setText("Named Entity Recognition (NER)");
				jRadioButtonNER.setSelected(true);
				jRadioButtonNER.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(jRadioButtonNER.isSelected())
						{
							jRadioButtonCreateCorpus.setSelected(true);
						}
						else
						{
							jRadioButtonRE.setSelected(false);
						}
					}
				});
			}
			{
				jRadioButtonRE = new JCheckBox();
				this.add(jRadioButtonRE, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				jRadioButtonRE.setText("Relation Extraction (RE)");
				jRadioButtonRE.setSelected(true);
				jRadioButtonRE.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(jRadioButtonRE.isSelected())
						{
							jRadioButtonCreateCorpus.setSelected(true);
							jRadioButtonNER.setSelected(true);
						}
					}
				});
			}
		}
	}


	@Override
	public Set<WorkflowStep> getWorkFlowSteps() {
		Set<WorkflowStep> result = new HashSet<WorkflowStep>();
		if(jRadioButtonPubmedSearch.isSelected())
			result.add(WorkflowStep.PubmedSearch);
		if(jRadioButtonCreateCorpus.isSelected())
			result.add(WorkflowStep.CreateCorpus);
		if(jRadioButtonNER.isSelected())
			result.add(WorkflowStep.NER);
		if(jRadioButtonRE.isSelected())
			result.add(WorkflowStep.RE);
		return result;
	}

	
	@Override
	public void setWorkFlowSteps(Set<WorkflowStep> steps) {
		if(steps.contains(WorkflowStep.PubmedSearch))
		{
			jRadioButtonPubmedSearch.setSelected(true);
		}
		else
		{
			jRadioButtonPubmedSearch.setSelected(false);
		}
		
		if(steps.contains(WorkflowStep.CreateCorpus))
		{
			jRadioButtonCreateCorpus.setSelected(true);
		}
		else
		{
			jRadioButtonCreateCorpus.setSelected(false);
		}
		if(steps.contains(WorkflowStep.NER))
		{
			jRadioButtonNER.setSelected(true);
		}
		else
		{
			jRadioButtonNER.setSelected(false);
		}
		if(steps.contains(WorkflowStep.RE))
		{
			jRadioButtonRE.setSelected(true);
		}
		else
		{
			jRadioButtonRE.setSelected(false);
		}
	}
}
