package pt.uminho.anote2.aibench.ner.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

public class Build extends JDialog{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelEnableDisambiguation;
	private JPanel jPanelUpper;
	private JLabel jLabelStopWords;
	private JRadioButton jRadioButtonDisambiguationYes;
	private JRadioButton jRadioButtonDisambiguationNo;
	private ButtonGroup buttonGroup1;
	private JPanel jPanelDisambiguationExample;
	private JTextPane jTextPaneInfo;
	private JLabel jLabelImage;

	public Build()
	{
		this.setSize(800, 600);
		initGUI();
	}

	private void initGUI() {
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
				jPanelEnableDisambiguation = new JPanel();
				jPanelEnableDisambiguation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Disambiguation Option", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelEnableStopWordsLayout = new GridBagLayout();
				jPanelUpper.add(jPanelEnableDisambiguation, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelEnableStopWordsLayout.rowHeights = new int[] {7, 7, 7};
				jPanelEnableStopWordsLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
				jPanelEnableStopWordsLayout.columnWidths = new int[] {7, 7, 7};
				jPanelEnableDisambiguation.setLayout(jPanelEnableStopWordsLayout);
				jPanelEnableDisambiguation.add(getJLabelStopWords(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableDisambiguation.add(getJRadioButtonStopWordsYNo(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelEnableDisambiguation.add(getJRadioButtonStopWordsYes(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelDisambiguationExample = new JPanel();
				jPanelDisambiguationExample.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelSelectStopWordsLayout = new GridBagLayout();
				jPanelUpper.add(jPanelDisambiguationExample, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSelectStopWordsLayout.rowWeights = new double[] {0.1};
				jPanelSelectStopWordsLayout.rowHeights = new int[] {7};
				jPanelSelectStopWordsLayout.columnWeights = new double[] {0.1, 0.1};
				jPanelSelectStopWordsLayout.columnWidths = new int[] {7, 7};
				jPanelDisambiguationExample.setLayout(jPanelSelectStopWordsLayout);
				jPanelDisambiguationExample.add(getJLabelImage(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDisambiguationExample.add(getJTextPaneInfo(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}
	
	private JRadioButton getJRadioButtonStopWordsYNo() {
		if(jRadioButtonDisambiguationNo == null) {
			jRadioButtonDisambiguationNo = new JRadioButton();
			jRadioButtonDisambiguationNo.setText("No");
			jRadioButtonDisambiguationNo.setSelected(true);
		}
		return jRadioButtonDisambiguationNo;
	}


	private JRadioButton getJRadioButtonStopWordsYes() {
		if(jRadioButtonDisambiguationYes == null) {
			jRadioButtonDisambiguationYes = new JRadioButton();
			jRadioButtonDisambiguationYes.setText("Yes");		
		}
		return jRadioButtonDisambiguationYes;
	}
	
	private JLabel getJLabelStopWords() {
		if(jLabelStopWords == null) {
			jLabelStopWords = new JLabel();
			jLabelStopWords.setText("Disambiguation  :");
		}
		return jLabelStopWords;
	}
	
	private JLabel getJLabelImage() {
		if(jLabelImage == null) {
			jLabelImage = new JLabel();
		}
		return jLabelImage;
	}
	
	private JTextPane getJTextPaneInfo() {
		if(jTextPaneInfo == null) {
			jTextPaneInfo = new JTextPane();
			jTextPaneInfo.setText("TODO");
		}
		return jTextPaneInfo;
	}

}
