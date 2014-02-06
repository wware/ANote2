package pt.uminho.anote2.workflow.wizards.basic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextFont;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.workflow.gui.panes.ResourcesSelectDictionaries;
import pt.uminho.anote2.workflow.text.WorkflowBasicsTextFont;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class WorkflowBasics2DictionarySelection extends WizardStandard{

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField jTextFieldInfo;
	private ResourcesSelectDictionaries jpaneleDictionaries;


	public WorkflowBasics2DictionarySelection(List<Object> param) {
		super(param);
		initGUI();
		if(param.size()==3)
		{
			fill(param.get(2));
			param.remove(2);
		}
		this.setTitle("Basic Workflow - Step 2");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void fill(Object object) {
		List<IResource<IResourceElement>> resources = (List<IResource<IResourceElement>>) object;
		jpaneleDictionaries.setResourcesSelection(resources);	
	}

	public void initGUI()
	{
		setEnableDoneButton(false);
		setEnableBackButton(true);
		setEnableNextButton(true);
	}

	@Override
	public JComponent getMainComponent() {
		if(mainPanel ==null)
			mainPanel = new JPanel();
			try {
				GridBagLayout mainPanelLayout = new GridBagLayout();
				mainPanelLayout.rowWeights = new double[] {0.1, 0.1};
				mainPanelLayout.rowHeights = new int[] {7, 7};
				mainPanelLayout.columnWeights = new double[] {0.1};
				mainPanelLayout.columnWidths = new int[] {7};
				mainPanel.setLayout(mainPanelLayout);
				mainPanel.add(getJTextFieldInfo(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				mainPanel.add(getJPanelDicionariesSelection(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		return mainPanel;
	}
	
	private JPanel getJPanelDicionariesSelection() throws SQLException, DatabaseLoadDriverException {
		if(jpaneleDictionaries == null)
		{
			jpaneleDictionaries = new ResourcesSelectDictionaries();
		}
		return jpaneleDictionaries;
	}

	private JTextField getJTextFieldInfo() {
		if(jTextFieldInfo == null) {
			jTextFieldInfo = new JTextField();
			jTextFieldInfo.setText(WorkflowBasicsTextFont.WorkflowBasicStep2);
			jTextFieldInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));
			jTextFieldInfo.setEditable(false);
			jTextFieldInfo.setFont(GlobalTextFont.largeFontItalic);
		}
		return jTextFieldInfo;
	}

	@Override
	public void goNext() {
		List<IResource<IResourceElement>> resources = jpaneleDictionaries.getSelectResources();
		if(resources.size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one dictionary");
		}
		else
		{
			closeView();
			getParam().add(resources);
			new WorkflowBasics3ClassSelection(getParam());
		}
	}


	@Override
	public void goBack() {
		closeView();
		new WorkflowBasics1PubmedSearch(getParam());
	}

	@Override
	public void done() {}

	@Override
	public String getHelpLink() {
		return "Workflow_:_Information_Retrieval_and_Extraction_Basic#Step_2:_Dictionaries_Selection";

	}

}
