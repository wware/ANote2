/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.anote2.aibench.utils.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
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

import pt.uminho.anote2.core.configuration.ISaveModule;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class SaveGUI extends DialogGenericViewInputGUI {
	
	
	private static final long serialVersionUID = 1L;
	private ParamsReceiver rec;
	private JTable projectListTable;
	private JScrollPane projectListScrollPane;
	private HashMap<String,ISaveModule> projectsHash;
	private JPanel filePanel;
	private JButton selectFile;
	private JTextField textFile;
	private JFileChooser chooser;

	public SaveGUI(){
		super("Save Session");
		projectsHash = new HashMap<String, ISaveModule>();
		this.setModal(true);
		initGUI();
		populateTable();
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
	
	private void initGUI(){
		
		getContentPane().setLayout(new BorderLayout());
		{
			getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		}
		{
			projectListScrollPane = new JScrollPane();
			projectListScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Select Projects", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			getContentPane().add(projectListScrollPane, BorderLayout.CENTER);
			{
				
				projectListTable = new JTable();
				projectListScrollPane.setViewportView(projectListTable);
			}
		}
		{
			filePanel = new JPanel();
			getContentPane().add(filePanel, BorderLayout.NORTH);
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
		pack();
		chooser = new JFileChooser();
	}

	@Override
	public void finish() {
		this.setModal(false);
		setVisible(false);
		dispose();
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.rec = arg0;
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}

	@Override
	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}
	
	

	@Override
	protected void okButtonAction() {
		ArrayList<ISaveModule> projects2save = new ArrayList<ISaveModule>();
		for(int i = 0; i< projectListTable.getRowCount(); i++){
			Boolean isSelected = (Boolean)projectListTable.getValueAt(i, 1);
			if(isSelected)
				projects2save.add(projectsHash.get(projectListTable.getValueAt(i, 0)));
		}
		if(projects2save.size()>0 && textFile.getText().equals(""))
		{
			Workbench.getInstance().warn("Select File");
		}
		else
		{
			File file = new File(textFile.getText());
			this.rec.paramsIntroduced(new ParamSpec[]{
					new ParamSpec("savemodule",ArrayList.class,projects2save,null),
					new ParamSpec("file",File.class,file,null),			
			});
		}
	}
	
	public void selectFile(ActionEvent e) {
		int returnVal = chooser.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			textFile.setText(chooser.getSelectedFile().getAbsolutePath());
		}

	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Save_Session";
	}

}
