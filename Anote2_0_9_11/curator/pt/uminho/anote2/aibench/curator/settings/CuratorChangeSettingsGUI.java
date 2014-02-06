package pt.uminho.anote2.aibench.curator.settings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;


public class CuratorChangeSettingsGUI extends javax.swing.JPanel implements IPropertiesPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> initial_props;
	private JPanel jPanelLinkOuts;
	private JCheckBox jCheckBoxOnlyShowLinkOutsExistenForTerm;
	private JCheckBox jCheckBoxUsingTermSerchOnLinkouts;
	private Map<String, Object> defaultProps;
	
	public CuratorChangeSettingsGUI(Map<String, Object> initial_props, Map<String, Object> defaultProps)
	{
		super();
		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		this.initial_props = initial_props;
		this.defaultProps = defaultProps;
		initGUI();
		fillSettings();
	}
	
	public CuratorChangeSettingsGUI()
	{
		
	}
	
	
	protected void fillSettings()
	{
		jCheckBoxOnlyShowLinkOutsExistenForTerm.setSelected(Boolean.valueOf(initial_props.get(CuratorDefaultSettings.ONLY_TERM_EXTERNALIDS_AVAILABLE).toString()));
		jCheckBoxUsingTermSerchOnLinkouts.setSelected(Boolean.valueOf(initial_props.get(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH).toString()));
	}
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.add(getJPanelLinkOuts(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}


	@Override
	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(CuratorDefaultSettings.ONLY_TERM_EXTERNALIDS_AVAILABLE, jCheckBoxOnlyShowLinkOutsExistenForTerm.isSelected());
		settings.put(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH, jCheckBoxUsingTermSerchOnLinkouts.isSelected());
		return settings;
	}

	@Override
	public boolean haveChanged() {
		if(!initial_props.get(CuratorDefaultSettings.ONLY_TERM_EXTERNALIDS_AVAILABLE).equals(jCheckBoxOnlyShowLinkOutsExistenForTerm.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH).equals(jCheckBoxUsingTermSerchOnLinkouts.isSelected()))
		{
			return true;
		}
		return false;
	}
	
	private JPanel getJPanelLinkOuts() {
		if(jPanelLinkOuts == null) {
			jPanelLinkOuts = new JPanel();
			GridBagLayout jPanelLinkOutsLayout = new GridBagLayout();
			jPanelLinkOuts.setBorder(BorderFactory.createTitledBorder("Link-outs"));
			jPanelLinkOutsLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelLinkOutsLayout.rowHeights = new int[] {7, 7};
			jPanelLinkOutsLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelLinkOutsLayout.columnWidths = new int[] {7, 7, 7};
			jPanelLinkOuts.setLayout(jPanelLinkOutsLayout);
			jPanelLinkOuts.add(getJCheckBoxOnlyShowLinkOutsExistenForTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLinkOuts.add(getJCheckBoxUsingTermSerchOnLinkouts(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLinkOuts;
	}
	
	private JCheckBox getJCheckBoxOnlyShowLinkOutsExistenForTerm() {
		if(jCheckBoxOnlyShowLinkOutsExistenForTerm == null) {
			jCheckBoxOnlyShowLinkOutsExistenForTerm = new JCheckBox();
			jCheckBoxOnlyShowLinkOutsExistenForTerm.setText("Only show existent term link-outs database");
		}
		return jCheckBoxOnlyShowLinkOutsExistenForTerm;
	}
	
	private JCheckBox getJCheckBoxUsingTermSerchOnLinkouts() {
		if(jCheckBoxUsingTermSerchOnLinkouts == null) {
			jCheckBoxUsingTermSerchOnLinkouts = new JCheckBox();
			jCheckBoxUsingTermSerchOnLinkouts.setText("Using term name to search on Link-outs");
		}
		return jCheckBoxUsingTermSerchOnLinkouts;
	}

}
