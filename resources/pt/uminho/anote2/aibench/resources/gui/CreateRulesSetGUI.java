package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Workbench;


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
public class CreateRulesSetGUI extends DialogGenericView{
	
	private static final long serialVersionUID = 1L;
	private JLabel jLabelName;
	private JLabel jLabelNote;
	private JTextArea jTextAreaNote;
	private JTextPane jTextPaneName;
	private JPanel jPanelUpper;

	public CreateRulesSetGUI() {
		confirmDataTypes();
		setTitle("Create RulesSet");
		initGUI(); 
	}
	
	private void confirmDataTypes() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(RulesSet.class);
		if(items==null||items.size()==0)
		{
			Workbench.getInstance().warn("No RuleSet on clipboard");
			return;
		}
	}
	
	private void initGUI() 
	{
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1, 0.0, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);
		{
			jPanelUpper = new JPanel();
			jPanelUpper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "RulesSet Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
			getContentPane().add(jPanelUpper, new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelUpperLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
			jPanelUpperLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelUpperLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7};
			jPanelUpper.setLayout(jPanelUpperLayout);
			{
				jLabelName = new JLabel();
				jPanelUpper.add(jLabelName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelName.setText("Name");
			}
			{
				jLabelNote = new JLabel();
				jPanelUpper.add(jLabelNote, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelNote.setText("Note");
			}
			{
				jTextPaneName = new JTextPane();
				jPanelUpper.add(jTextPaneName, new GridBagConstraints(2, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextAreaNote = new JTextArea();
				jPanelUpper.add(jTextAreaNote, new GridBagConstraints(2, 2, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		this.setSize(600, 400);
	}

	protected void okButtonAction() {
		
	if(!this.jTextPaneName.getText().equals(""))
	{
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(RulesSet.class);
		RulesSet rules = (RulesSet) items.get(0).getUserData();
		paramsRec.paramsIntroduced( new ParamSpec[]{ 
				new ParamSpec("lookuptables",RulesSet.class,rules,null),
				new ParamSpec("name",String.class,this.jTextPaneName.getText(),null),
				new ParamSpec("info",String.class,this.jTextAreaNote.getText(),null)
			});		
	}
	else
		Workbench.getInstance().warn("Please insert a Ontologies name!");
	}

}
