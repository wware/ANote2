package pt.uminho.anote2.aibench.corpus.gui.wizard.reexport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.settings.cytoscape.CytoscapeDefaultSettings;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfiguration;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfigurationAutoOpen;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.IRESchema;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfiguration;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAdvanceOptions;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAutoOpen;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationEntityOptions;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationRelationOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class REToXGMMLWizardStep4 extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jUpperPanel;
	private JTextField jTextFieldTextChooser;
	private JFileChooser filePath;
	private JButton jButtonFileChooser;
	protected boolean cancel_choose;
	private JPanel jPanelCytoScapePanel;
	private JCheckBox jCheckBoxCytoscapeAutoOpen;

	public REToXGMMLWizardStep4(List<Object> param) {
		super(param);
		InitGUI();
		defaultParameters();
		this.setTitle("RE Process to Cytoscape (XGMML) File - Select Output File");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	
	
	private void defaultParameters() {
		jCheckBoxCytoscapeAutoOpen.setSelected(Boolean.valueOf(PropertiesManager.getPManager().getProperty(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE).toString()));
	}



	private void InitGUI() {
		setEnableNextButton(false);	
	}



	@Override
	public JComponent getMainComponent() {
		if(jUpperPanel == null)
		{
			jUpperPanel = new JPanel();
			filePath = new JFileChooser(); 
			filePath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
						cancel_choose = true;
				}
			});
		
			filePath.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			String homeDirectory = SessionSettings.getSessionSettings().getSearchDirectory();
			if(homeDirectory!=null)
				filePath.setCurrentDirectory(new File(homeDirectory));
			jUpperPanel.setBorder(BorderFactory.createTitledBorder("Select File"));
			GridBagLayout jUpperPanelLayout = new GridBagLayout();
			jUpperPanelLayout.rowWeights = new double[] {0.6, 0.4};
			jUpperPanelLayout.rowHeights = new int[] {7, 7};
			jUpperPanelLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.0};
			jUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jUpperPanel.setLayout(jUpperPanelLayout);
			jUpperPanel.setPreferredSize(new java.awt.Dimension(477, 455));
			{
				jTextFieldTextChooser = new JTextField();
				jTextFieldTextChooser.setEditable(false);
				jUpperPanel.add(jTextFieldTextChooser, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			}
			{
				jUpperPanel.add(getJButtonFileChooser(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
				jUpperPanel.add(getJPanelCytoScapePanel(), new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jUpperPanel;
	}
	
	private JButton getJButtonFileChooser() {
		if(jButtonFileChooser == null) {
			jButtonFileChooser = new JButton();
			jButtonFileChooser.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Folder26.png")));
			jButtonFileChooser.addActionListener(new ActionListener(){

				@SuppressWarnings("static-access")
				public void actionPerformed(ActionEvent arg0) {

					int rel = filePath.showOpenDialog(new JFrame());
					if(rel == filePath.CANCEL_OPTION)
					{

					}
					else
					{
						File save_path = filePath.getSelectedFile();
						if(save_path.isDirectory())
						{
							SessionSettings.getSessionSettings().setSearchDirectory(save_path.getAbsolutePath());
							jTextFieldTextChooser.setText(save_path.getAbsolutePath()+"/network_reprocess_"+((RESchema) getParam().get(0)).getID()+".xgmml");
						}
						else
						{
							if(save_path.getAbsolutePath().endsWith(".xgmml"))
							{
								SessionSettings.getSessionSettings().setSearchDirectory(save_path.getAbsolutePath());
								jTextFieldTextChooser.setText(save_path.getAbsolutePath());
							}
							else
							{
								SessionSettings.getSessionSettings().setSearchDirectory(save_path.getAbsolutePath());
								jTextFieldTextChooser.setText(save_path.getAbsolutePath()+".xgmml");
							}
						}
						cancel_choose=false;
					}
				}		
			});
		}
		return jButtonFileChooser;
	}

	public void goNext() {}

	@Override
	public void goBack() {
		closeView();
		new REToXGMMLWizardStep3(getParam());
	}

	@Override
	public void done() {
		if(!jTextFieldTextChooser.getText().equals(""))
		{
			File file = new File(jTextFieldTextChooser.getText());
			IRESchema reProcess = (IRESchema) getParam().get(0);
			IREToNetworkConfigurationEntityOptions entityOptions = (IREToNetworkConfigurationEntityOptions) getParam().get(1); 
			IREToNetworkConfigurationRelationOptions relationOptions = (IREToNetworkConfigurationRelationOptions) getParam().get(2);
			IREToNetworkConfigurationAdvanceOptions advanced = (IREToNetworkConfigurationAdvanceOptions) getParam().get(3);
			IREToNetworkConfigurationAutoOpen autoOpen = new REToNetworkConfigurationAutoOpen(jCheckBoxCytoscapeAutoOpen.isSelected());
			IREToNetworkConfiguration configuration = new REToNetworkConfiguration(entityOptions, relationOptions , advanced,autoOpen);
			// TODO Auto-generated method stub
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("process", IRESchema.class,reProcess, null),
					new ParamSpec("configurations", IREToNetworkConfiguration.class,configuration, null),
					new ParamSpec("file", File.class,file, null)
			};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.exportretoxgmml")){			
					Workbench.getInstance().executeOperation(def, paramsSpec);
					closeView();
					return;
				}
			}	
		}
		else
		{
			Workbench.getInstance().warn("Please Select file");
		}
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"REProcess_Export_To_XGMML_File#Select_File";
	}
	
	private JPanel getJPanelCytoScapePanel() {
		if(jPanelCytoScapePanel == null) {
			jPanelCytoScapePanel = new JPanel();
			GridBagLayout jPanelCytoScapePanelLayout = new GridBagLayout();
			jPanelCytoScapePanel.setBorder(BorderFactory.createTitledBorder(null, "Cytoscape Auto Open", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			jPanelCytoScapePanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelCytoScapePanelLayout.rowHeights = new int[] {7, 7, 7};
			jPanelCytoScapePanelLayout.columnWeights = new double[] {0.1};
			jPanelCytoScapePanelLayout.columnWidths = new int[] {7};
			jPanelCytoScapePanel.setLayout(jPanelCytoScapePanelLayout);
			jPanelCytoScapePanel.add(getJCheckBoxCytoscapeAutoOpen(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		}
		return jPanelCytoScapePanel;
	}
	
	private JCheckBox getJCheckBoxCytoscapeAutoOpen() {
		if(jCheckBoxCytoscapeAutoOpen == null) {
			jCheckBoxCytoscapeAutoOpen = new JCheckBox();
			jCheckBoxCytoscapeAutoOpen.setText("Open XGMML File in Cytoscape after operation ends");
		}
		return jCheckBoxCytoscapeAutoOpen;
	}

}
