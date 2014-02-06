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

import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.WorkflowsEnum;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;
import pt.uminho.anote2.workflow.settings.utils.REProcessChangeSettingBox;

public class WorkflowBasicRESettings extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> initial_props;
	private JPanel jPanelREAvailable;
	private JPanel jPanelSettingsPanel;
	private JComboBox jComboBoxAvailableRE;
	private Map<String, Object> defaultProps;

	public WorkflowBasicRESettings(Map<String, Object> initial_props) {
		super();
		this.initial_props = initial_props;
		initGUI();
		defaultSettings();
	}

	private void defaultSettings() {
		jComboBoxAvailableRE.setSelectedItem(initial_props.get(WorkflowBasicsDefaulSettings.RE));
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
				jPanelREAvailable = new JPanel();
				GridBagLayout jPanelNERAvailableLayout = new GridBagLayout();
				this.add(jPanelREAvailable, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelREAvailable.setBorder(BorderFactory.createTitledBorder("Select RE"));
				jPanelNERAvailableLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelNERAvailableLayout.rowHeights = new int[] {7, 7, 7};
				jPanelNERAvailableLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelNERAvailableLayout.columnWidths = new int[] {7, 7, 7};
				jPanelREAvailable.setLayout(jPanelNERAvailableLayout);
				{
					ComboBoxModel jComboBoxAvailableNERModel = 
							new DefaultComboBoxModel(REWorkflowProcessesAvailableEnum.values());
					jComboBoxAvailableRE = new JComboBox();
					jPanelREAvailable.add(jComboBoxAvailableRE, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxAvailableRE.setModel(jComboBoxAvailableNERModel);
					jComboBoxAvailableRE.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							changePanel();
						}
					});
				}
			}
			{
				jPanelSettingsPanel = (JPanel) REProcessChangeSettingBox.getInstance().getChangeSettings(WorkflowsEnum.BASIC, REWorkflowProcessesAvailableEnum.Relation);
				this.add(jPanelSettingsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}

	protected void changePanel() {
		jPanelSettingsPanel = (JPanel) REProcessChangeSettingBox.getInstance().getChangeSettings(WorkflowsEnum.BASIC, REWorkflowProcessesAvailableEnum.Relation);
	}

	public Map<String,Object> getProperties() {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put(WorkflowBasicsDefaulSettings.RE, jComboBoxAvailableRE.getSelectedItem());
		for(int i=0;i<jComboBoxAvailableRE.getItemCount();i++)
		{
			result.putAll(REProcessChangeSettingBox.getInstance().getChangeSettings(WorkflowsEnum.BASIC, REWorkflowProcessesAvailableEnum.Relation).getProperties());
		}
		return result;
	}

	public boolean haveChanged() {
		if(initial_props.get(WorkflowBasicsDefaulSettings.RE).equals(jComboBoxAvailableRE.getSelectedItem()))
		{
			return true;
		}
		for(int i=0;i<jComboBoxAvailableRE.getItemCount();i++)
		{
			if(REProcessChangeSettingBox.getInstance().getChangeSettings(WorkflowsEnum.BASIC, REWorkflowProcessesAvailableEnum.Relation).haveChanged())
			{
				return true;
			}
		}
		return false;
	}

}
