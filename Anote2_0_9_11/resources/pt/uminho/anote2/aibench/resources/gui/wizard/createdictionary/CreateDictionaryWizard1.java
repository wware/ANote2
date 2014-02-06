package pt.uminho.anote2.aibench.resources.gui.wizard.createdictionary;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateDictionaryWizard1 extends WizardStandard{



	private static final long serialVersionUID = 1L;
	private JLabel jLabelName;
	private JLabel jLabelNote;
	private JTextField jTextFieldNote;
	private JTextField jTextFieldName;
	private JPanel jPanelUpper;


	public CreateDictionaryWizard1() {
		super(new ArrayList<Object>());
		initGUI();
		this.setTitle("Create Dictionary");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public CreateDictionaryWizard1(List<Object> objects) {
		super(new ArrayList<Object>());
		initGUI();
		fillWithPreviousSettings(objects);
		this.setTitle("Create Dictionary");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillWithPreviousSettings(List<Object> objects) {
		String name = (String) objects.get(0);
		if(name!=null)
		{
			jTextFieldName.setText(name);
		}
		String info = (String) objects.get(1);
		if(info!=null)
		{
			jTextFieldNote.setText(info);
		}
	}

	private void initGUI() 
	{
		setEnableBackButton(false);
		setEnableDoneButton(false);
	}

	public void done() {}

	public void goBack() {}

	public void goNext() {
		if(jTextFieldName.getText().equals(""))
		{
			Workbench.getInstance().warn("Please insert a Dictionary name");
		}
		else
		{
			List<Object> obj = new ArrayList<Object>();
			obj.add(jTextFieldName.getText());
			obj.add(jTextFieldNote.getText());
			closeView();
			new CreateDictionaryWizard2(obj);
		}
	}

	public JComponent getMainComponent() {
		{
			jPanelUpper = new JPanel();
			jPanelUpper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Information", TitledBorder.LEADING, TitledBorder.TOP));
			getContentPane().add(jPanelUpper, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
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
		return jPanelUpper;
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Create#Select_Name_and_Notes";
	}


}
