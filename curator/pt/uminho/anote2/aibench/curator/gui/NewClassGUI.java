package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NewClassGUI  extends DialogGenericView{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String classe;
	private JTextPane jTextPaneClass;
	private JLabel jLabelNewClass;
	private Set<String> classes;
	private boolean newClass=false;

	public NewClassGUI(Set<String> classes)
	{
		this.classes=classes;
		initGUI();
		this.setVisible(true);
		this.setModal(true);
		Utilities.centerOnOwner(this);
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
				jTextPaneClass = new JTextPane();
				getContentPane().add(jTextPaneClass, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
		this.setSize(300, 200);
	}

	protected void okButtonAction() {
		classe = jTextPaneClass.getText();
		if(classes.contains(""))
		{
			Workbench.getInstance().warn("The class can´t be empty!!!");
		}
		if(classes.contains(classe))
		{
			Workbench.getInstance().warn("This class already exist!!!");
		}
		else
		{
			newClass=true;
			this.setVisible(false);
			this.setModal(false);
			this.dispose();
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

}
