package pt.uminho.anote2.aibench.resources.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

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
public class CreateDictionaryWizard1 extends WizartStandard{



	private static final long serialVersionUID = 1L;
	private JLabel jLabelName;
	private JLabel jLabelNote;
	private JTextArea jTextAreaNote;
	private JTextPane jTextPaneName;
	private JPanel jPanelUpper;


	public CreateDictionaryWizard1() {
		super(600,400,new ArrayList<Object>());
		initGUI();
		this.setTitle("Create Dictionary");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);
		
	}

	private void initGUI() 
	{
		setEnableBackButton(false);
		setEnableDoneButton(false);
		{
			jPanelUpper = new JPanel();
			jPanelUpper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
			getContentPane().add(jPanelUpper, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelUpperLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
			jPanelUpperLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelUpperLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
			jPanelUpper.setLayout(jPanelUpperLayout);
			{
				jLabelName = new JLabel();
				jPanelUpper.add(jLabelName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelName.setText("Name :");
			}
			{
				jLabelNote = new JLabel();
				jPanelUpper.add(jLabelNote, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelNote.setText("Note :");
			}
			{
				jTextPaneName = new JTextPane();
				jPanelUpper.add(jTextPaneName, new GridBagConstraints(1, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextAreaNote = new JTextArea();
				jPanelUpper.add(jTextAreaNote, new GridBagConstraints(1, 2, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			getJScrollPaneUpPanel().setViewportView(jPanelUpper);
		}
	}

//		if(!this.jTextPaneName.getText().equals(""))
//		{
//			List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
//			Dictionaries dic = (Dictionaries) items.get(0).getUserData();
//			paramsRec.paramsIntroduced( new ParamSpec[]{ 
//					new ParamSpec("dictionaries",Dictionaries.class,dic,null),
//					new ParamSpec("name",String.class,this.jTextPaneName.getText(),null),
//					new ParamSpec("info",String.class,this.jTextAreaNote.getText(),null)
//			});		
//		}
//		else
//			Workbench.getInstance().warn("Please insert a Dictionary name!");



	public void done() {}

	public void goBack() {}

	public void goNext() {
		if(jTextPaneName.getText().equals(""))
		{
			Workbench.getInstance().warn("Please insert a Dictionary name!");
		}
		else
		{
			List<Object> obj = new ArrayList<Object>();
			obj.add(jTextPaneName.getText());
			obj.add(jTextAreaNote.getText());
			new CreateDictionaryWizard2(600,400,obj);
			this.setVisible(false);
		}
	}
	
	public static void main(String[] args) throws IOException{
		new CreateDictionaryWizard1();
		
	}

}
