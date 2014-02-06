package pt.uminho.anote2.aibench.utils.gui.panes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.configuration.ISaveModule;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Workbench;

public class ExitSavePane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable projectListTable;
	private JScrollPane projectListScrollPane;
	private JPanel filePanel;
	private JButton selectFile;
	private JTextField textFile;
	private JFileChooser chooser;
	private JButton cancelButton;
	private JButton okButton;
	private JPanel buttonsPanel;	
	private JButton jButtonHelp;
	
	private HashMap<String,ISaveModule> projectsHash;
	
	public ExitSavePane()
	{		
		projectsHash = new HashMap<String, ISaveModule>();
		initGUI();
		populateTable();
	}

	private void initGUI() {

		this.setLayout(new BorderLayout());
		{
			this.add(getButtonsPanel(), BorderLayout.SOUTH);
		}
		{
			projectListScrollPane = new JScrollPane();
			projectListScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Select Projects", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			this.add(projectListScrollPane, BorderLayout.CENTER);
			{
				
				projectListTable = new JTable();
				projectListScrollPane.setViewportView(projectListTable);
			}
		}
		{
			filePanel = new JPanel();
			this.add(filePanel, BorderLayout.NORTH);
			GridBagLayout filePanelLayout = new GridBagLayout();
			filePanelLayout.rowWeights = new double[] {0.1};
			filePanelLayout.rowHeights = new int[] {7};
			filePanelLayout.columnWeights = new double[] {0.1, 0.0};
			filePanelLayout.columnWidths = new int[] {7, 7};
			filePanel.setLayout(filePanelLayout);
			filePanel.setBorder(BorderFactory.createTitledBorder(null, "Select File", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			{
				textFile = new JTextField();
				filePanel.add(textFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				textFile.setPreferredSize(new java.awt.Dimension(6, 30));
			}
			{
				selectFile = new JButton();
				filePanel.add(selectFile, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				selectFile.setText("File...");
				selectFile.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						selectFile(e);
						
					}
				});
				selectFile.setPreferredSize(new java.awt.Dimension(100, 30));
			}
		}
		chooser = new JFileChooser();
	}
	
	private void selectFile(ActionEvent e) {
		int returnVal = chooser.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			textFile.setText(chooser.getSelectedFile().getAbsolutePath());
		}

	}
	
	private void populateTable(){
		List<ClipboardItem> projectItems = Core.getInstance().getClipboard().getRootItems();		
		for(int i = 0; i< projectItems.size();){
			ClipboardItem item = projectItems.get(i);
			if(item.getUserData() instanceof ISaveModule)
			{
				ISaveModule saveModule = (ISaveModule) item.getUserData();			
				String projectName = saveModule.getName() ;
				projectsHash.put(projectName, saveModule);
				i++;
			}
		}
		
		Object[][] objectsModel = new Object[projectsHash.size()][2];
		int i=0;
		for(ISaveModule saveModule:projectsHash.values())
		{
			objectsModel[i][0] = saveModule.getName();
			objectsModel[i][1] = true;
			i++;
		}
					
		TableModel projectListTableModel = new DefaultTableModel(
				objectsModel,
				new String[] { "Project Name ", "" }){

					private static final long serialVersionUID = 1L;
					
					public boolean isCellEditable(int rowIndex, int vColIndex) {
						if(vColIndex==1)
							return true;
						else return false;
			        }			
		};
		
		projectListTable.setModel(projectListTableModel);
		projectListTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		projectListTable.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {

			public Component getTableCellRendererComponent(
					JTable table, Object value, boolean isSelected,
					boolean isFocused, int row, int col) {
				boolean marked = (Boolean) value;
				JCheckBox rendererComponent = new JCheckBox();
				if (marked) {
					rendererComponent.setSelected(true);
				}
				return rendererComponent;
			}
		});
		projectListTable.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		projectListTable.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		projectListTable.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		

	}
	
	
	private JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			GridBagLayout buttonsPanelLayout = new GridBagLayout();
			buttonsPanelLayout.rowWeights = new double[] {0.1};
			buttonsPanelLayout.rowHeights = new int[] {7};
			buttonsPanelLayout.columnWidths = new int[] {7, 7};
			buttonsPanel.setLayout(buttonsPanelLayout);
			{
				buttonsPanelLayout.columnWeights = new double[] {0.1, 0.0,0.1};
				buttonsPanel.add(getJButtonButtonSaveOk(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
				buttonsPanel.add(getJButtonHelp(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 4, 3, 1), 0, 0));

			}
			buttonsPanel.add(getJButtonCancel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 4, 3, 1), 0, 0));
			buttonsPanel.setOpaque(false);
		}
		return buttonsPanel;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {

			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Exit_Program");
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}
	

	public JButton getJButtonButtonSaveOk() {
		if(okButton == null) {
			okButton = new JButton();
			okButton.setText("Save and Exit");
			okButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ok22.png")));	
		}
		return okButton;
	}
	
	public JButton getJButtonCancel() {
		if(cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			URL str = getClass().getClassLoader().getResource("icons/cancel22.png");
			cancelButton.setIcon(new ImageIcon(str));
		}
		return cancelButton;
	}
	
	public boolean validateFields()
	{
		List<ISaveModule> projects2save = new ArrayList<ISaveModule>();
		for(int i = 0; i< projectListTable.getRowCount(); i++){
			Boolean isSelected = (Boolean)projectListTable.getValueAt(i, 1);
			if(isSelected)
				projects2save.add(projectsHash.get(projectListTable.getValueAt(i, 0)));
		}
		if(projects2save.size()>0 && textFile.getText().equals(""))
		{
			Workbench.getInstance().warn("Select File");
			return false;
		}
		return true;
	}
	
	public List<ISaveModule> getProjectToSave()
	{
		List<ISaveModule> projects2save = new ArrayList<ISaveModule>();
		for(int i = 0; i< projectListTable.getRowCount(); i++){
			Boolean isSelected = (Boolean)projectListTable.getValueAt(i, 1);
			if(isSelected)
				projects2save.add(projectsHash.get(projectListTable.getValueAt(i, 0)));
		}
		return projects2save;
	}
	
	public File getFileToSave()
	{
		File file = new File(textFile.getText());
		return file;
	}
	

}
