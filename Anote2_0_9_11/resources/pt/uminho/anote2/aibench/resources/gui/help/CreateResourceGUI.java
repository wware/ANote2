package pt.uminho.anote2.aibench.resources.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;


public abstract class CreateResourceGUI extends DialogGenericViewInputGUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel jLabelName;
	private JLabel jLabelNote;
	private JTextField jTextFieldNote;
	private JTextField jTextFieldName;
	private JPanel jPanelUpper;

	public CreateResourceGUI()
	{
		super();
		initGUI(); 
		completeBorders(getResourceType());
		this.setTitle("Create "+getResourceType());
	}

	private void completeBorders(String resourceType) {
		jPanelUpper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),  resourceType+" Information", TitledBorder.LEADING, TitledBorder.TOP));
	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			jPanelUpper = new JPanel();
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
			getContentPane().add(jPanelUpper, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelUpperLayout.rowHeights = new int[] {7, 7};
			jPanelUpperLayout.columnWeights = new double[] {0.025, 0.1};
			jPanelUpperLayout.columnWidths = new int[] {7, 7};
			jPanelUpper.setLayout(jPanelUpperLayout);
			{
				jLabelName = new JLabel();
				jPanelUpper.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelName.setText("Name : ");
			}
			{
				jLabelNote = new JLabel();
				jPanelUpper.add(jLabelNote, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelNote.setText("Description : ");
			}
			{
				jTextFieldName = new JTextField();
				jPanelUpper.add(jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldNote = new JTextField();
				jPanelUpper.add(jTextFieldNote, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}	
	}
	
	public JTextField getjTextFieldNote() {
		return jTextFieldNote;
	}

	public JTextField getjTextFieldName() {
		return jTextFieldName;
	}
	
	public abstract String getResourceType();

}
