package pt.uminho.anote2.aibench.publicationmanager.gui.panes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.process.IR.IIRSearchConfiguration;

public class PubmedSearchSimplePane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel keywordsLabel;
	private JLabel organismLabel;
	private JTextField keywordsTextField;
	private JTextField organismTextField;
	
	public PubmedSearchSimplePane()
	{
		initPanel();
	}

	private void initPanel() {
		GridBagLayout jPanelQueryMainInformationLayout = new GridBagLayout();
		jPanelQueryMainInformationLayout.rowWeights = new double[] {0.1, 0.1};
		jPanelQueryMainInformationLayout.rowHeights = new int[] {7, 7};
		jPanelQueryMainInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1};
		jPanelQueryMainInformationLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query Main Information", TitledBorder.LEADING, TitledBorder.TOP));
		this.setLayout(jPanelQueryMainInformationLayout);
		{
			keywordsLabel = new JLabel();
			this.add(keywordsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			keywordsLabel.setText("Keywords:");
		}
		{
			organismLabel = new JLabel();
			this.add(organismLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			organismLabel.setText("Organism:");
		}
		{
			keywordsTextField = new JTextField();
			this.add(keywordsTextField, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			keywordsTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		}
		{
			organismTextField = new JTextField();
			this.add(organismTextField, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			organismTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		}
	}
	
	public String getOrganism()
	{
		return organismTextField.getText();
	}
	
	public String getKeyWords()
	{
		return keywordsTextField.getText();
	}

	public void updateFields(IIRSearchConfiguration pubmedSearchConfigutration) {
		keywordsTextField.setText(pubmedSearchConfigutration.getKeywords());
		organismTextField.setText(pubmedSearchConfigutration.getOrganism());
	}
	
	

}
