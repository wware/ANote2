package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class NewClassGUI extends DialogGenericView{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String classe;
	private JTextField jTextFieldClass;
	private JLabel jLabelNewClass;
	private boolean newClass=false;

	public NewClassGUI()
	{
		initGUI();
		Utilities.centerOnWindow(this);
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.075, 0.1, 0.1, 0.1};

			getContentPane().setLayout(thisLayout);
			{
				jTextFieldClass = new JTextField();
				getContentPane().add(jTextFieldClass, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jLabelNewClass = new JLabel();
				getContentPane().add(jLabelNewClass, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelNewClass.setText("New Class :");
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
	}

	protected void okButtonAction() {
		classe = jTextFieldClass.getText();
		if(classe.equals(""))
		{
			Workbench.getInstance().warn("The class can't be empty.");
		}
		else if(ClassProperties.getClassClassID().containsKey(classe))
		{
			Workbench.getInstance().warn("This class already exist.");
		}
		else
		{
			newClass=true;
			finish();
		}
	}

	public boolean isNewClass() {
		return newClass;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getClasse() {
		return classe;
	}

	@Override
	protected String getHelpLink() {
		return null;
	}

}
