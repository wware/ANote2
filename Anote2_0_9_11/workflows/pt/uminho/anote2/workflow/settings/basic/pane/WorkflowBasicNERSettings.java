package pt.uminho.anote2.workflow.settings.basic.pane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pt.uminho.anote2.workflow.datastructures.NERWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.WorkflowsEnum;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;
import pt.uminho.anote2.workflow.settings.utils.NERProcessChangeSettingBox;

public class WorkflowBasicNERSettings extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> initial_props;
	private JPanel jPanelNERAvailable;
	private JPanel jPanelSettingsPanel;
	private JComboBox jComboBoxAvailableNER;
	private Map<String, Object> defaultProps;

	public WorkflowBasicNERSettings(Map<String, Object> initial_props) {
		super();
		this.initial_props = initial_props;
		initGUI();
		defaultSettings();
	}

	private void defaultSettings() {
		jComboBoxAvailableNER.setSelectedItem(initial_props.get(WorkflowBasicsDefaulSettings.NER));
		changePanel();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(698, 518));
			thisLayout.rowWeights = new double[] {0.25, 0.75};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelNERAvailable = new JPanel();
				GridBagLayout jPanelNERAvailableLayout = new GridBagLayout();
				this.add(jPanelNERAvailable, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelNERAvailable.setBorder(BorderFactory.createTitledBorder("Select NER"));
				jPanelNERAvailableLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelNERAvailableLayout.rowHeights = new int[] {7, 7, 7};
				jPanelNERAvailableLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelNERAvailableLayout.columnWidths = new int[] {7, 7, 7};
				jPanelNERAvailable.setLayout(jPanelNERAvailableLayout);
				{
					ComboBoxModel jComboBoxAvailableNERModel = 
							new DefaultComboBoxModel(NERWorkflowProcessesAvailableEnum.values());
					jComboBoxAvailableNER = new JComboBox();
					jPanelNERAvailable.add(jComboBoxAvailableNER, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxAvailableNER.setModel(jComboBoxAvailableNERModel);
					jComboBoxAvailableNER.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							changePanel();
						}
					});
				}
			}
			{
				jPanelSettingsPanel = (JPanel) NERProcessChangeSettingBox.getInstance().getChangeSettingsAndUpdate(WorkflowsEnum.BASIC, (NERWorkflowProcessesAvailableEnum)jComboBoxAvailableNER.getSelectedItem());
				this.add(jPanelSettingsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}

	protected void changePanel() {
		jPanelSettingsPanel = (JPanel) NERProcessChangeSettingBox.getInstance().getChangeSettingsAndUpdate(WorkflowsEnum.BASIC, (NERWorkflowProcessesAvailableEnum)jComboBoxAvailableNER.getSelectedItem());
	}

	public Map<String,Object> getProperties() {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put(WorkflowBasicsDefaulSettings.NER, jComboBoxAvailableNER.getSelectedItem());
		for(int i=0;i<jComboBoxAvailableNER.getItemCount();i++)
		{
			result.putAll(NERProcessChangeSettingBox.getInstance().getProperties(WorkflowsEnum.BASIC, (NERWorkflowProcessesAvailableEnum)jComboBoxAvailableNER.getSelectedItem()).getProperties());
		}
		return result;
	}

	public boolean haveChanged() {
		if(!initial_props.get(WorkflowBasicsDefaulSettings.NER).equals(jComboBoxAvailableNER.getSelectedItem()))
		{
			return true;
		}
		for(int i=0;i<jComboBoxAvailableNER.getItemCount();i++)
		{
			if(NERProcessChangeSettingBox.getInstance().getProperties(WorkflowsEnum.BASIC, (NERWorkflowProcessesAvailableEnum)jComboBoxAvailableNER.getSelectedItem()).haveChanged())
			{
				return true;
			}
		}
		return false;
	}
	
	

}
