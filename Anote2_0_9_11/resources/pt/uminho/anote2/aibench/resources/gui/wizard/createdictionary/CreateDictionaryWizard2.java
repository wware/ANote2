package pt.uminho.anote2.aibench.resources.gui.wizard.createdictionary;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateDictionaryWizard2 extends WizardStandard{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -9013962427848421067L;
	private JRadioButton jRadioButtonEmpty;
	private JRadioButton jRadioButtonAddFlatFiles;
	private JPanel jPanelUpPanel;

	
	public CreateDictionaryWizard2(List<Object> param) {
		super(param);
		initGUI();
		this.setTitle("Dictionary Content");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void initGUI() {
		setEnableDoneButton(true);
		setEnableNextButton(false);
	}

	protected void changeButtonEmpty() {
		if(jRadioButtonEmpty.isSelected())
		{
			jRadioButtonAddFlatFiles.setSelected(false);
			setEnableDoneButton(true);
			setEnableNextButton(false);
		}
		else
		{
			jRadioButtonAddFlatFiles.setSelected(true);
			setEnableDoneButton(false);
			setEnableNextButton(true);
		}		
	}

	protected void changeButtons() {
		if(jRadioButtonAddFlatFiles.isSelected())
		{
			jRadioButtonEmpty.setSelected(false);
			setEnableDoneButton(false);
			setEnableNextButton(true);
		}
		else
		{
			jRadioButtonEmpty.setSelected(true);
			setEnableDoneButton(true);
			setEnableNextButton(false);
		}
	}

	public void done() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
		Dictionaries dic = (Dictionaries) items.get(0).getUserData();	
		closeView();
		createDic(dic);
	}

	public void goBack() {
		closeView();
		new CreateDictionaryWizard1(getParam());
	}


	public void goNext() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
		Dictionaries dic = (Dictionaries) items.get(0).getUserData();	
		closeView();
		new CreateDictionaryWizard3(dic,(String) getParam().get(0),(String) getParam().get(1));
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
				closeView();
				return;
			}
		}
		
	}

	@Override
	public JComponent getMainComponent() {
		try {
			{
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
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jPanelUpPanel;
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Create#Select_Empty_or_Existing_Dictionary";
	}

}
