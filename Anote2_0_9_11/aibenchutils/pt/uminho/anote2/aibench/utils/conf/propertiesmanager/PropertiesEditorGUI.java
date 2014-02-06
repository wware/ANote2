package pt.uminho.anote2.aibench.utils.conf.propertiesmanager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManagerException;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class PropertiesEditorGUI extends JDialog implements TreeSelectionListener, ActionListener,InputGUI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSplitPane splitpane;
	private Map<String, IPropertyNode> opt_properties;
	private OptionsPanel optionsPanel;
	private JPanel selectedPanel;
	private OkCancelMiniPanel okCancelMiniPanel;
	private String currentTreeNode = "NONE";
	private Map<String, IPropertiesPanel> panels;
	
	protected ParamsReceiver rec;
	private boolean hasImportExport = true;

	
	public PropertiesEditorGUI() {
		super(Workbench.getInstance().getMainFrame());
		this.opt_properties = PropertiesManager.getPManager().getNodes();
		this.panels = new HashMap<String, IPropertiesPanel>();
		for(String s : opt_properties.keySet()){
			panels.put(s, opt_properties.get(s).getPropertiesPanel());
		}
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {7,7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(GlobalOptions.superWidth, GlobalOptions.superHeight));
			{
				optionsPanel = new OptionsPanel(opt_properties.keySet(), hasImportExport);
				optionsPanel.addNodeTreeSelectionListener(this);
				if(hasImportExport)
					optionsPanel.addButtonsActionListener(this);
				
				JScrollPane pane = new JScrollPane();
				selectedPanel = new JPanel();
				pane.setViewportView(selectedPanel);
				selectedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        optionsPanel, pane);
				this.add(splitpane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				splitpane.setOneTouchExpandable(true);
				splitpane.setDividerLocation(150);
				
				okCancelMiniPanel = new OkCancelMiniPanel();
				getContentPane().add(okCancelMiniPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				okCancelMiniPanel.addButtonsActionListener(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
		
		TreeNode[] nodes = node.getPath();
		
		StringBuilder sb = new StringBuilder();


		sb.append(nodes[1].toString());
		if(nodes.length>=2){
			for(int i = 2;i < nodes.length;i++) {
				sb.append(".").append(nodes[i].toString());
			}
		}
		String propertiesNode = sb.toString();
		if(this.opt_properties.keySet().contains(propertiesNode)){

//			if(!currentTreeNode.equals("NONE")){
//				IPropertyNode propNode = this.opt_properties.get(propertiesNode);
//				propNode.setProperties(panels.get(propNode).getProperties());
//			}
			
			currentTreeNode = propertiesNode;
			selectedPanel = (JPanel) panels.get(propertiesNode);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(selectedPanel);
			splitpane.setRightComponent(pane);
			splitpane.setDividerLocation(150);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals(OkCancelMiniPanel.OK_BUTTON_ACTION_COMMAND)){
			boolean toSave = false;
			if(!currentTreeNode.equals("NONE")){
				IPropertyNode propNode = this.opt_properties.get(currentTreeNode);
				try {
					propNode.setProperties(((IPropertiesPanel)selectedPanel).getProperties());
				} catch (PropertiesManagerException e) {
					Workbench.getInstance().error(e);
				}
			}
			
			for(String propNode : opt_properties.keySet()){
				if(panels.get(propNode).haveChanged())
					try {
						opt_properties.get(propNode).setProperties(panels.get(propNode).getProperties());
					} catch (PropertiesManagerException e) {
						Workbench.getInstance().error(e);
					}
					toSave = true;
			}
			PropertiesManager.getPManager().saveNodes();
			finish();
		}
		
		if(arg0.getActionCommand().equals(OkCancelMiniPanel.CANCEL_BUTTON_ACTION_COMMAND)){
			finish();
		}
		
		if(arg0.getActionCommand().equalsIgnoreCase("import") || arg0.getActionCommand().equalsIgnoreCase("export")){
			//import
			String file = null;
			if(!currentTreeNode.equals("NONE")){
				IPropertyNode propNode = this.opt_properties.get(currentTreeNode);
				try {
					propNode.setProperties(((IPropertiesPanel)selectedPanel).getProperties());
				} catch (PropertiesManagerException e) {
					Workbench.getInstance().error(e);
				}
			}
			
			for(String propNode : opt_properties.keySet()){
				if(panels.get(propNode).haveChanged())
					try {
						opt_properties.get(propNode).setProperties(panels.get(propNode).getProperties());
					} catch (PropertiesManagerException e) {
						Workbench.getInstance().error(e);
					}
			}
			PropertiesManager.getPManager().saveNodes();
			
			JFileChooser fc = new JFileChooser();
			String homeDirectory = SessionSettings.getSessionSettings().getSearchDirectory();
			if(homeDirectory!=null)
				fc.setCurrentDirectory(new File(homeDirectory));
			
			FileFilter filter = new FileFilter() {
				
				@Override
				public String getDescription() {
					return "Configuration files" + String.format(" (*%s)", ".conf");
				}
				
				@Override
				public boolean accept(File file) {
					if (file.isDirectory()) {
			            return true;
			        }
			        return file.getName().endsWith(".conf");
				}
			};
			
			fc.setFileFilter(filter);

			int returnVal = fc.showDialog(/*Workbench.getInstance().getMainFrame()*/ null , "OK");
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File selected = fc.getSelectedFile();
				SessionSettings.getSessionSettings().setSearchDirectory(selected.getParent());
				if(!selected.getName().endsWith(".conf"))
				{
					file = selected.getAbsolutePath()+".conf";
				}
				else
				{
					file = selected.getAbsolutePath();

				}
				if(arg0.getActionCommand().equalsIgnoreCase("import")){
					PropertiesManager.getPManager().importProperties(file);
					reloadPanels();
					new ShowMessagePopup("Import of configuration file was successful.");
				}
				if(arg0.getActionCommand().equalsIgnoreCase("export"))
				{
					PropertiesManager.getPManager().exportProperties(file);
					new ShowMessagePopup("Export configuration to file was successful.");
				}	
			}
		}
	}

	private void reloadPanels() {
		
		this.opt_properties = PropertiesManager.getPManager().getNodes();
		this.panels = new HashMap<String, IPropertiesPanel>();
		for(String s : opt_properties.keySet()){
			panels.put(s, opt_properties.get(s).getPropertiesPanel());
		}
		
		//ReloadUI?
		if(this.opt_properties.keySet().contains(currentTreeNode)){

			currentTreeNode = currentTreeNode;
			selectedPanel = (JPanel) panels.get(currentTreeNode);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(selectedPanel);
			splitpane.setRightComponent(pane);
			splitpane.setDividerLocation(150);
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		setVisible(false);
		dispose();
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		// TODO Auto-generated method stub
		rec = arg0;
		setTitle(arg1.getName());
		setModal(true);
	    pack();
	    Utilities.centerOnOwner(this);
	    setVisible(true);
	}

	@Override
	public void onValidationError(Throwable arg0) {
		// TODO Auto-generated method stub
		Workbench.getInstance().error(arg0);
	}
	
	
}
