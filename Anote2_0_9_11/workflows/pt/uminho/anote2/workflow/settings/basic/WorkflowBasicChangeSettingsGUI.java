package pt.uminho.anote2.workflow.settings.basic;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;
import pt.uminho.anote2.workflow.settings.basic.pane.WorkflowBasicCorpusSettings;
import pt.uminho.anote2.workflow.settings.basic.pane.WorkflowBasicNERSettings;
import pt.uminho.anote2.workflow.settings.basic.pane.WorkflowBasicRESettings;


public class WorkflowBasicChangeSettingsGUI extends javax.swing.JPanel implements IPropertiesPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> initial_props;
	private JTabbedPane jTabbedPaneSettings;
	private WorkflowBasicRESettings jPanelRE;
	private WorkflowBasicNERSettings jPanelNER;
	private WorkflowBasicCorpusSettings jPanelCorpus;
	
	public WorkflowBasicChangeSettingsGUI(Map<String, Object> initial_props, Map<String, Object> defaultProps)
	{
		super();
		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		this.initial_props = initial_props;
		{
			initGUI();
		}
	}
	
	
	public WorkflowBasicChangeSettingsGUI() {
		// TODO Auto-generated constructor stub
	}


	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(708, 491));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.add(getJTabbedPaneSettings(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}


	@Override
	public Map<String, Object> getProperties() {
		
		Map<String, Object> result = new HashMap<String, Object>();
		result = jPanelCorpus.getProperties();
		result.putAll(jPanelNER.getProperties());
		result.putAll(jPanelRE.getProperties());
		return result;
	}

	@Override
	public boolean haveChanged() {
		if(jPanelCorpus.haveChanged())
			return true;
		if(jPanelNER.haveChanged())
			return true;
		if(jPanelRE.haveChanged())
			return true;
		return false;
	}
	
	private JTabbedPane getJTabbedPaneSettings() {
		if(jTabbedPaneSettings == null) {
			jTabbedPaneSettings = new JTabbedPane();
			jTabbedPaneSettings.addTab("Corpus", null, getJPanelCorpus(), null);
			jTabbedPaneSettings.addTab("NER", null, getJPanelNER(), null);
			jTabbedPaneSettings.addTab("RE", null, getJPanelRE(), null);
		}
		return jTabbedPaneSettings;
	}
	
	private JPanel getJPanelCorpus() {
		if(jPanelCorpus == null) {
			jPanelCorpus = new WorkflowBasicCorpusSettings(this.initial_props);
		}
		return jPanelCorpus;
	}
	
	private JPanel getJPanelNER() {
		if(jPanelNER == null) {
			jPanelNER = new WorkflowBasicNERSettings(this.initial_props);
		}
		return jPanelNER;
	}
	
	private JPanel getJPanelRE() {
		if(jPanelRE == null) {
			jPanelRE = new WorkflowBasicRESettings(this.initial_props);
		}
		return jPanelRE;
	}

}
