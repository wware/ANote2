package pt.uminho.anote2.aibench.resources.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
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
public class CreateDictionaryWizard2 extends WizartStandard{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -9013962427848421067L;
	private JRadioButton jRadioButtonEmpty;
	private JRadioButton jRadioButtonAddFlatFiles;
	private JPanel jPanelUpPanel;

	
	public CreateDictionaryWizard2(int sizeH, int sizeV, List<Object> param) {
		super(sizeH, sizeV, param);
		initGUI();
		this.setTitle("Dictionary Content");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);
	}
	
	private void initGUI() {
		try {
			{
				setEnableDoneButton(false);
				{
					jPanelUpPanel = new JPanel();
					GridBagLayout jPanelUpPanelLayout = new GridBagLayout();					
					jPanelUpPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
					jPanelUpPanelLayout.rowHeights = new int[] {7, 20, 7};
					jPanelUpPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
					jPanelUpPanelLayout.columnWidths = new int[] {7, 7, 7};
					jPanelUpPanel.setLayout(jPanelUpPanelLayout);
					{
						jRadioButtonEmpty = new JRadioButton();
						jPanelUpPanel.add(jRadioButtonEmpty, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jRadioButtonEmpty.setText("Empty");
						jRadioButtonEmpty.setSelected(true);
						jRadioButtonEmpty.addActionListener(new ActionListener(){
							
							public void actionPerformed(ActionEvent arg0) {
								changeButtonEmpty();
							}
						});
					}
					{
						jRadioButtonAddFlatFiles = new JRadioButton();
						jPanelUpPanel.add(jRadioButtonAddFlatFiles, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jRadioButtonAddFlatFiles.setText("Add Terms");
						jRadioButtonAddFlatFiles.addActionListener(new ActionListener(){
							
							public void actionPerformed(ActionEvent arg0) {
								changeButtons();
							}
						});

					}
				}
				getJScrollPaneUpPanel().setViewportView(jPanelUpPanel);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void changeButtonEmpty() {
		if(jRadioButtonEmpty.isSelected())
		{
			jRadioButtonAddFlatFiles.setSelected(false);
		}
		else
		{
			jRadioButtonAddFlatFiles.setSelected(true);
		}		
	}

	protected void changeButtons() {
		if(jRadioButtonAddFlatFiles.isSelected())
		{
			jRadioButtonEmpty.setSelected(false);
		}
		else
		{
			jRadioButtonEmpty.setSelected(true);
		}
	}

	public void done() {	
	}

	public void goBack() {
		new CreateDictionaryWizard1();
		this.setVisible(false);
		
	}


	@Override
	public void goNext() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
		Dictionaries dic = (Dictionaries) items.get(0).getUserData();	
		if(jRadioButtonEmpty.isSelected())
		{
			createDic(dic);
			this.setVisible(false);
		}
		else if(jRadioButtonAddFlatFiles.isSelected())
		{
			try {
				new CreateDictionaryWizard3(dic,(String) getParam().get(0),(String) getParam().get(1));
			} catch (NonExistingConnection e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (MissingDatatypesException e) {
				e.printStackTrace();
			}
			this.setVisible(false);
		}
		
	}

	private void createDic(Dictionaries dic) {
		
		ParamSpec[] paramsIntroduced = new ParamSpec[]{
				new ParamSpec("dictionaries",Dictionaries.class,dic,null),
				new ParamSpec("name",String.class,this.getParam().get(0),null),
				new ParamSpec("info",String.class,this.getParam().get(1),null)
		};	
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.createdicionary")){			
				Workbench.getInstance().executeOperation(def,paramsIntroduced);
				return;
			}
		}
		
	}
}
