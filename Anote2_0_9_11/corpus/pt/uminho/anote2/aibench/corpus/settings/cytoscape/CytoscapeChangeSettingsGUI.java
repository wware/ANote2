package pt.uminho.anote2.aibench.corpus.settings.cytoscape;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.corpus.cytoscape.CytoscapeOpen;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.datastructures.exceptions.InvalidDirectoryException;
import pt.uminho.anote2.datastructures.exceptions.UnknownOperationSystemException;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;


public class CytoscapeChangeSettingsGUI extends javax.swing.JPanel implements IPropertiesPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> initial_props;
	private JTextField jTextFieldCytoscapePath;
	private JButton jButtonSelectCytoscapeDirectory;
	private JCheckBox jCheckBoxAutoOpenCytoscapeOption;
	private Map<String, Object> defaultProps;
	private JFileChooser filePath;


	public CytoscapeChangeSettingsGUI(Map<String, Object> initial_props, Map<String, Object> defaultProps)
	{
		super();

		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		this.initial_props = initial_props;
		this.defaultProps = defaultProps;
		initGUI();
		fillSettings();
	}
	
	public CytoscapeChangeSettingsGUI()
	{
		
	}
	
	
	protected void fillSettings()
	{
		jCheckBoxAutoOpenCytoscapeOption.setSelected(Boolean.valueOf(PropertiesManager.getPManager().getProperty(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE).toString()));
		activedisableoptions();
		jTextFieldCytoscapePath.setText(PropertiesManager.getPManager().getProperty(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY).toString());
	}
	
	private void initGUI()
	{
		{
			filePath = new JFileChooser(); 
			filePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			this.setBorder(BorderFactory.createTitledBorder("Cytoscape Auto Open"));
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.025, 0.1, 0.0, 0.025};
			thisLayout.columnWidths = new int[] {7, 7, 20, 7};
			this.setLayout(thisLayout);
			this.add(getJCheckBoxAutoOpenCytoscapeOption(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
			this.add(getJTextFieldCytoscapePath(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
			this.add(getJButtonSelectCytoscapeDirectory(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}

	}
	
	@Override
	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE, jCheckBoxAutoOpenCytoscapeOption.isSelected());
		settings.put(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY, jTextFieldCytoscapePath.getText());
		return settings;
	}

	@Override
	public boolean haveChanged() {
		if(!initial_props.get(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE).equals(jCheckBoxAutoOpenCytoscapeOption.isSelected()))
		{
			if(jCheckBoxAutoOpenCytoscapeOption.isSelected() && !jTextFieldCytoscapePath.getText().isEmpty())
			{
				return true;
			}
			else if(!jCheckBoxAutoOpenCytoscapeOption.isSelected())
			{
				return true;
			}
		}
		else if(!initial_props.get(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY).equals(jTextFieldCytoscapePath.getText()))
		{
			return true;
		}
		return false;
	}
	
	private JCheckBox getJCheckBoxAutoOpenCytoscapeOption() {
		if(jCheckBoxAutoOpenCytoscapeOption == null) {
			jCheckBoxAutoOpenCytoscapeOption = new JCheckBox();
			jCheckBoxAutoOpenCytoscapeOption.setText("Auto Open Cytoscape after XGMML File Creation");
			jCheckBoxAutoOpenCytoscapeOption.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					activedisableoptions();
				}

			});
		}
		return jCheckBoxAutoOpenCytoscapeOption;
	}
	
	private void activedisableoptions() {
		if(jCheckBoxAutoOpenCytoscapeOption.isSelected())
		{
			jTextFieldCytoscapePath.setEnabled(true);
			jButtonSelectCytoscapeDirectory.setEnabled(true);
		}
		else
		{
			jTextFieldCytoscapePath.setEnabled(false);
			jButtonSelectCytoscapeDirectory.setEnabled(false);
		}
	}
	
	private JTextField getJTextFieldCytoscapePath() {
		if(jTextFieldCytoscapePath == null) {
			jTextFieldCytoscapePath = new JTextField();
			jTextFieldCytoscapePath.setEditable(false);
			jTextFieldCytoscapePath.setEnabled(false);
		}
		return jTextFieldCytoscapePath;
	}
	
	private JButton getJButtonSelectCytoscapeDirectory() {
		if(jButtonSelectCytoscapeDirectory == null) {
			jButtonSelectCytoscapeDirectory = new JButton();
			jButtonSelectCytoscapeDirectory.setText("Select Directory");
			jButtonSelectCytoscapeDirectory.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int rel = filePath.showOpenDialog(new JFrame());
					if(rel == filePath.CANCEL_OPTION)
					{

					}
					else
					{
						File save_path = filePath.getSelectedFile();
						try {
							CytoscapeOpen.validateCytoscapeDirectory(save_path.getAbsolutePath());
							jTextFieldCytoscapePath.setText(save_path.getAbsolutePath());
						} catch (InvalidDirectoryException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (UnknownOperationSystemException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
					
				}
			});
		}
		return jButtonSelectCytoscapeDirectory;
	}

}
