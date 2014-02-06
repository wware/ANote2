package pt.uminho.anote2.workflow.gui.wizard.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.datastructures.corpora.CorpusCreateConfiguration;
import pt.uminho.anote2.workflow.gui.ACorpusPanel;
import es.uvigo.ei.aibench.workbench.Workbench;


public abstract class ACorpusPaneWizard extends ACorpusPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean selectTextType;
	protected JTextField jTextFieldName;
	protected JRadioButton jRadioButtonAbstract;
	protected JCheckBox jCheckBoxRetrievalPDFs;
	protected JRadioButton jRadioButtonFullText;
	private JPanel jPanelAdvanceOption;
	private JLabel jLabelName;
	private JPanel jPanelSelectName;
	private ButtonGroup jButtonsGroupTextType;
	
	public ACorpusPaneWizard(boolean selectTextType)
	{
		this.selectTextType = selectTextType;
		initGUI();
		defaultSettings();
	}


	protected abstract void defaultSettings();


	private void initGUI() {
		{
			this.setPreferredSize(new java.awt.Dimension(593, 463));
			GridBagLayout thisLayout = new GridBagLayout();
			this.setBorder(BorderFactory.createTitledBorder("Select Corpus Options"));
			thisLayout.rowWeights = new double[] {0.7, 0.3};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelSelectName = new JPanel();
				GridBagLayout jPanelSelectNameLayout = new GridBagLayout();
				this.add(jPanelSelectName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSelectNameLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelSelectNameLayout.rowHeights = new int[] {7, 7, 7};
				jPanelSelectNameLayout.columnWeights = new double[] {0.05, 0.1, 0.05};
				jPanelSelectNameLayout.columnWidths = new int[] {7, 7, 7};
				jPanelSelectName.setLayout(jPanelSelectNameLayout);
				{
					jLabelName = new JLabel();
					jPanelSelectName.add(jLabelName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
					jLabelName.setText("Corpus Name :");
				}
				{
					jTextFieldName = new JTextField();
					jPanelSelectName.add(jTextFieldName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 4), 0, 0));
				}
			}
			{
				if(selectTextType)
				{
					jButtonsGroupTextType = new ButtonGroup();
					jPanelAdvanceOption = new JPanel();
					GridBagLayout jPanelAdvanceOptionLayout = new GridBagLayout();
					this.add(jPanelAdvanceOption, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelAdvanceOptionLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelAdvanceOptionLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelAdvanceOptionLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelAdvanceOptionLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelAdvanceOption.setLayout(jPanelAdvanceOptionLayout);
					jPanelAdvanceOption.setBorder(BorderFactory.createTitledBorder(null, "Select Text Type", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						jRadioButtonAbstract = new JRadioButton();
						jPanelAdvanceOption.add(jRadioButtonAbstract, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jRadioButtonAbstract.setText("Abstract");
						jButtonsGroupTextType.add(jRadioButtonAbstract);
						jRadioButtonAbstract.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeJournalRetrievalOption();
							}
						});
					}
					{
						jRadioButtonFullText = new JRadioButton();
						jPanelAdvanceOption.add(jRadioButtonFullText, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jRadioButtonFullText.setText("Full Text");
						jButtonsGroupTextType.add(jRadioButtonFullText);
						jRadioButtonFullText.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeJournalRetrievalOption();
							}
						});
					}
					{
						jCheckBoxRetrievalPDFs = new JCheckBox();
						jPanelAdvanceOption.add(jCheckBoxRetrievalPDFs, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jCheckBoxRetrievalPDFs.setText("Retrieve PDF files");
						jCheckBoxRetrievalPDFs.setVisible(false);
					}
				}
			}
		}

	}
	
	protected void changeJournalRetrievalOption() {
		jCheckBoxRetrievalPDFs.setVisible(jRadioButtonFullText.isSelected());
	}


	@Override
	public boolean validateOptions() {
		if(jTextFieldName.getText().equals(""))
		{
			Workbench.getInstance().warn("Please Select a Corpus Name");
			return false;
		}
		return true;
	}
	
	@Override
	public void updataCorpusSettings(ICorpusCreateConfiguration conf) {
		jTextFieldName.setText(conf.getCorpusName());
		CorpusTextType textType = conf.getCorpusTextType();
		if(textType.equals(CorpusTextType.Abstract))
		{
			jCheckBoxRetrievalPDFs.setVisible(false);
			jRadioButtonAbstract.setSelected(true);
			jCheckBoxRetrievalPDFs.setSelected(false);
		}
		else
		{
			jCheckBoxRetrievalPDFs.setVisible(true);
			jRadioButtonFullText.setSelected(true);
			if(conf.processJournalRetrievalBefore())
			{
				jCheckBoxRetrievalPDFs.setSelected(true);
			}
		}
		
	}

	@Override
	public ICorpusCreateConfiguration getConfiguration() {
		if(validateOptions())
		{
			CorpusTextType textType = CorpusTextType.Abstract;
			if(selectTextType)
			{
				if(jRadioButtonFullText.isSelected())
				{
					textType = CorpusTextType.FullText;
				}
			}
			boolean journalRetrievalBefore = false;
			if(selectTextType && jRadioButtonFullText.isSelected() && jCheckBoxRetrievalPDFs.isSelected())
				journalRetrievalBefore = true;
			String name = jTextFieldName.getText();
			return new CorpusCreateConfiguration(name,null,textType,journalRetrievalBefore);
		}
		return null;
	}

}
