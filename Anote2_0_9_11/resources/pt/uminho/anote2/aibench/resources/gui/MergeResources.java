package pt.uminho.anote2.aibench.resources.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;


public abstract class MergeResources extends DialogGenericViewInputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private JPanel jPanelUpperPanel;
	private JPanel jPanelNewResourceInfo;
	private JTextField jTextFieldName;
	private ButtonGroup buttonGroup1;
	private JTable jTableDestiny;
	private JPanel jPanelDestinyTable;
	private JComboBox jComboBoxDestiny;
	private JPanel jPanelDestiny;
	private JComboBox jComboBoxSource;
	private JTable jTableSourceClasses;
	private JPanel jPanelTableSourceClasses;
	private JPanel jPanelComboBoxResourceSource;
	private JPanel jPanelResourceSource;
	private JPanel jPanelResourceDestiny;
	private JRadioButton jRadioButtonNewResourceYes;
	private JRadioButton jRadioButtonNewResourceNo;
	private JLabel jLabelNewResourceOption;
	private JTextField jTextFieldNotes;
	private JLabel jLabelNotes;
	private JLabel jLabelName;
	private JPanel jPanelResourcesInformation;
	private JPanel jPanelNewResource;
	private Class<?> classe;
	
	private IResource<IResourceElement> resource;
	private JCheckBox box;

	public MergeResources(Class<?> classe)
	{
		this.classe = classe;
		try {
			initGUI();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}	
	}
	

	private void initGUI() throws DatabaseLoadDriverException, SQLException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelUpperPanel = new JPanel();
				GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
				getContentPane().add(jPanelUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelUpperPanelLayout.rowWeights = new double[] {0.025, 0.1, 0.0};
				jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7};
				jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
				jPanelUpperPanelLayout.columnWidths = new int[] {7};
				jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
				{
					jPanelNewResource = new JPanel();
					GridBagLayout jPanelNewResourceLayout = new GridBagLayout();
					jPanelNewResource.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), getResourceType()+"Merge", TitledBorder.LEADING, TitledBorder.TOP));
					jPanelUpperPanel.add(jPanelNewResource, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelNewResourceLayout.rowWeights = new double[] {0.1};
					jPanelNewResourceLayout.rowHeights = new int[] {7};
					jPanelNewResourceLayout.columnWeights = new double[] {0.05, 0.1, 0.1};
					jPanelNewResourceLayout.columnWidths = new int[] {7, 7, 7};
					jPanelNewResource.setLayout(jPanelNewResourceLayout);
					{
						jLabelNewResourceOption = new JLabel();
						jPanelNewResource.add(jLabelNewResourceOption, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelNewResourceOption.setText("Create New "+ getResourceType()+"  : ");
					}
					{
						jRadioButtonNewResourceNo = getJButtonResourcesNO();
					}
					{
						jRadioButtonNewResourceYes = getJRadioResourcesYes();
					}
				}
				{
					jPanelResourcesInformation = new JPanel();
					GridBagLayout jPanelResourcesInformationLayout = new GridBagLayout();
					jPanelUpperPanel.add(jPanelResourcesInformation, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelResourcesInformationLayout.rowWeights = new double[] {0.1};
					jPanelResourcesInformationLayout.rowHeights = new int[] {7};
					jPanelResourcesInformationLayout.columnWeights = new double[] {0.1, 0.1};
					jPanelResourcesInformationLayout.columnWidths = new int[] {7, 7};
					jPanelResourcesInformation.setLayout(jPanelResourcesInformationLayout);
					{
						jPanelResourceSource = new JPanel();
						GridBagLayout jPanelResourceSourceLayout = new GridBagLayout();
						jPanelResourceSource.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Source " +getResourceType(), TitledBorder.LEADING, TitledBorder.TOP));
						jPanelResourcesInformation.add(jPanelResourceSource, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelResourceSourceLayout.rowWeights = new double[] {0.05, 0.1};
						jPanelResourceSourceLayout.rowHeights = new int[] {7, 7};
						jPanelResourceSourceLayout.columnWeights = new double[] {0.1};
						jPanelResourceSourceLayout.columnWidths = new int[] {7};
						jPanelResourceSource.setLayout(jPanelResourceSourceLayout);
						{
							jPanelComboBoxResourceSource = new JPanel();
							GridBagLayout jPanelComboBoxResourceSourceLayout = new GridBagLayout();
							jPanelResourceSource.add(jPanelComboBoxResourceSource, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelComboBoxResourceSourceLayout.rowWeights = new double[] {0.1};
							jPanelComboBoxResourceSourceLayout.rowHeights = new int[] {7};
							jPanelComboBoxResourceSourceLayout.columnWeights = new double[] {0.1};
							jPanelComboBoxResourceSourceLayout.columnWidths = new int[] {7};
							jPanelComboBoxResourceSource.setLayout(jPanelComboBoxResourceSourceLayout);
							{
								jComboBoxSource = getComboBoxSource();
							}
						}
						{
							jPanelTableSourceClasses = new JPanel();
							jPanelTableSourceClasses.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Class Content", TitledBorder.LEADING, TitledBorder.TOP));
							GridBagLayout jPanelTableClassesLayout = new GridBagLayout();
							jPanelResourceSource.add(jPanelTableSourceClasses, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelTableClassesLayout.rowWeights = new double[] {0.1};
							jPanelTableClassesLayout.rowHeights = new int[] {7};
							jPanelTableClassesLayout.columnWeights = new double[] {0.1};
							jPanelTableClassesLayout.columnWidths = new int[] {7};
							jPanelTableSourceClasses.setLayout(jPanelTableClassesLayout);
							{
								TableModel jTableSourceClassesModel = getJTableSourceClasses();					
								jTableSourceClasses = new JTable()
								{
									private static final long serialVersionUID = 1L;

									public boolean isCellEditable(int x,int y){
										if(y==1)
											return true;
										return false;
									}
									
									public Class<?> getColumnClass(int column){
										if(column == 1)
											return Boolean.class;
										return String.class;
									}
								};
								jPanelTableSourceClasses.add(jTableSourceClasses, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jTableSourceClasses.setModel(jTableSourceClassesModel);
								completeSourceTable(jTableSourceClasses);
							}
						}
					}
					{
						jPanelResourceDestiny = new JPanel();
						GridBagLayout jPanelResourceDestinyLayout = new GridBagLayout();
						jPanelResourceDestiny.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Target "+getResourceType(), TitledBorder.LEADING, TitledBorder.TOP));
						jPanelResourcesInformation.add(jPanelResourceDestiny, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelResourceDestinyLayout.rowWeights = new double[] {0.05, 0.1};
						jPanelResourceDestinyLayout.rowHeights = new int[] {7, 7};
						jPanelResourceDestinyLayout.columnWeights = new double[] {0.1};
						jPanelResourceDestinyLayout.columnWidths = new int[] {7};
						jPanelResourceDestiny.setLayout(jPanelResourceDestinyLayout);
						{
							jPanelDestiny = new JPanel();
							
							GridBagLayout jPanelDestinyLayout = new GridBagLayout();
							jPanelResourceDestiny.add(jPanelDestiny, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelDestinyLayout.rowWeights = new double[] {0.1};
							jPanelDestinyLayout.rowHeights = new int[] {7};
							jPanelDestinyLayout.columnWeights = new double[] {0.1};
							jPanelDestinyLayout.columnWidths = new int[] {7};
							jPanelDestiny.setLayout(jPanelDestinyLayout);
							{
								jComboBoxDestiny = getComboBoxDestiny();
							}
						}
						{
							jPanelDestinyTable = new JPanel();
							jPanelDestinyTable.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Class Content", TitledBorder.LEADING, TitledBorder.TOP));
							GridBagLayout jPanelDestinyTableLayout = new GridBagLayout();
							jPanelResourceDestiny.add(jPanelDestinyTable, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelDestinyTableLayout.rowWeights = new double[] {0.1};
							jPanelDestinyTableLayout.rowHeights = new int[] {7};
							jPanelDestinyTableLayout.columnWeights = new double[] {0.1};
							jPanelDestinyTableLayout.columnWidths = new int[] {7};
							jPanelDestinyTable.setLayout(jPanelDestinyTableLayout);
							{
								TableModel jTableDestinyModel = getJTableDestinyClassesResources();
								jTableDestiny = new JTable()
								{
									private static final long serialVersionUID = 1L;

									public boolean isCellEditable(int x,int y){
										if(y==1)
											return true;
										return false;
									}
									
									public Class<?> getColumnClass(int column){
										if(column == 1)
											return Boolean.class;
										return String.class;
									}
								};
								jPanelDestinyTable.add(jTableDestiny, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jTableDestiny.setModel(jTableDestinyModel);
								completeSourceTable(jTableDestiny);
								jTableDestiny.setEnabled(false);
							}
						}
					}
				}
				{
					jPanelNewResourceInfo = new JPanel();
					jPanelNewResourceInfo.setEnabled(false);
					GridBagLayout jPanelNewResourceInfoLayout = new GridBagLayout();
					jPanelNewResourceInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "New "+getResourceType()+" Details", TitledBorder.LEADING, TitledBorder.TOP));
					jPanelUpperPanel.add(jPanelNewResourceInfo, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelNewResourceInfoLayout.rowWeights = new double[] {0.1, 0.1};
					jPanelNewResourceInfoLayout.rowHeights = new int[] {7, 7};
					jPanelNewResourceInfoLayout.columnWeights = new double[] {0.025, 0.1};
					jPanelNewResourceInfoLayout.columnWidths = new int[] {7, 7};
					jPanelNewResourceInfo.setLayout(jPanelNewResourceInfoLayout);
					{
						jLabelName = new JLabel();
						jPanelNewResourceInfo.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelName.setText("Name : ");
					}
					{
						jLabelNotes = new JLabel();
						jPanelNewResourceInfo.add(jLabelNotes, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelNotes.setText("Notes : ");
					}
					{
						jTextFieldName = new JTextField();
						jPanelNewResourceInfo.add(jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jTextFieldNotes = new JTextField();
						jPanelNewResourceInfo.add(jTextFieldNotes, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
		}
		getButtonGroup1();
	}

	protected JComboBox getComboBoxDestiny() {
		if(jComboBoxDestiny == null)
		{
			DefaultComboBoxModel jComboBoxDestinyModel = new DefaultComboBoxModel();
			resource = (IResource<IResourceElement>) HelpAibench.getSelectedItem(classe);
			jComboBoxDestinyModel.addElement(resource);
			jComboBoxDestiny = new JComboBox();
			jPanelDestiny.add(jComboBoxDestiny, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jComboBoxDestiny.setModel(jComboBoxDestinyModel);
		}
		return jComboBoxDestiny;
	}

	protected JComboBox getComboBoxSource() {
		if(jComboBoxSource == null)
		{
			ComboBoxModel jComboBoxSourceModel = getSourceResources();
			jComboBoxSource = new JComboBox();
			jPanelComboBoxResourceSource.add(jComboBoxSource, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jComboBoxSource.setModel(jComboBoxSourceModel);
			jComboBoxSource.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						changeSourceResource();
					} catch (DatabaseLoadDriverException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					} catch (SQLException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jComboBoxSource;
	}

	protected JRadioButton getJRadioResourcesYes() {
		if(jRadioButtonNewResourceYes==null)
		{
			jRadioButtonNewResourceYes = new JRadioButton();
			jPanelNewResource.add(jRadioButtonNewResourceYes, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jRadioButtonNewResourceYes.setText("Yes");
			jRadioButtonNewResourceYes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeNewResource();
				}
			});
		}
		return jRadioButtonNewResourceYes;
	}

	protected JRadioButton getJButtonResourcesNO() {
		if(jRadioButtonNewResourceNo==null)
		{
			jRadioButtonNewResourceNo = new JRadioButton();
			jPanelNewResource.add(jRadioButtonNewResourceNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jRadioButtonNewResourceNo.setText("No");
			jRadioButtonNewResourceNo.setSelected(true);
			jRadioButtonNewResourceNo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					changeNewResource();
				}
			});
		}return jRadioButtonNewResourceNo;
	}
	
	private ComboBoxModel getSourceResources() {
		DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
		List<ClipboardItem> list = Core.getInstance().getClipboard().getItemsByClass(classe);
		for(ClipboardItem item:list)
		{
			IResource<IResourceElement> res = (IResource<IResourceElement>) item.getUserData();
//			if(res.getID()!=resource.getID())
			{
				comboModel.addElement(res);
			}
		}
		return comboModel;
	}

	private TableModel getJTableDestinyClassesResources() throws DatabaseLoadDriverException, SQLException {
		String[] columns = new String[] {"Class",""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[] data;
		int i = 0;
		data = new Object[2];	
		if(jComboBoxDestiny.getSelectedItem()!=null)
		{
			for(Integer classContentID: ((IResource<IResourceElement>) jComboBoxDestiny.getSelectedItem()).getClassContent())
			{
				data[0] = ClassProperties.getClassIDClass().get(classContentID);
				data[1] = Boolean.TRUE;
				tableModel.addRow(data);
				i++;
			}
		}
		return tableModel;	
	}

	private void completeSourceTable(JTable jTableSourceClasses2) {
		box = new JCheckBox();
		jTableSourceClasses2.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jTableSourceClasses2.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(box));	
		jTableSourceClasses2.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jTableSourceClasses2.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		jTableSourceClasses2.updateUI();
		
	}

	private TableModel getJTableSourceClasses() throws DatabaseLoadDriverException, SQLException {
		String[] columns = new String[] {"Class",""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[] data;
		int i = 0;
		data = new Object[2];		
		for(Integer classContentID: ((IResource<IResourceElement>) jComboBoxSource.getSelectedItem()).getClassContent())
		{
			data[0] = ClassProperties.getClassIDClass().get(classContentID);
			data[1] = Boolean.TRUE;
			tableModel.addRow(data);
			i++;
		}
		return tableModel;	
	}

	protected void changeSourceResource() throws DatabaseLoadDriverException, SQLException {
		TableModel table = getJTableSourceClasses();
		jTableSourceClasses.setModel(table);
		completeSourceTable(jTableSourceClasses);
		jTableSourceClasses.updateUI();
	}

	protected void changeNewResource() {
		if(jRadioButtonNewResourceYes.isSelected())
		{
			jPanelNewResourceInfo.setEnabled(true);
			jTableDestiny.setEnabled(true);
			jPanelResourceSource.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "First Source", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelResourceDestiny.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Second Source", TitledBorder.LEADING, TitledBorder.TOP));
		}
		else
		{
			jPanelNewResourceInfo.setEnabled(false);
			jTableDestiny.setEnabled(false);
			jPanelResourceSource.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), getResourceType()+" Source", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelResourceDestiny.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), getResourceType()+" Target", TitledBorder.LEADING, TitledBorder.TOP));
		}
	}

	public abstract String getResourceType();
	
	private ButtonGroup getButtonGroup1() {
		if(buttonGroup1 == null) {
			buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(jRadioButtonNewResourceNo);
			buttonGroup1.add(jRadioButtonNewResourceYes);
		}
		return buttonGroup1;
	}
	
	protected Set<Integer> getResourceSourceSelectedClasses()
	{
		Set<Integer> classIDs = new HashSet<Integer>();
		int rowSize = jTableSourceClasses.getModel().getRowCount();
		for(int i=0;i<rowSize;i++)
		{
			boolean select = (Boolean) jTableSourceClasses.getModel().getValueAt(i, 1);
			if(select)
			{
				String classe = (String) jTableSourceClasses.getModel().getValueAt(i, 0);
				classIDs.add(ClassProperties.getClassClassID().get(classe));
			}
		}
		return classIDs;
	}
	
	protected Set<Integer> getResourceDestinySelectedClasses()
	{
		Set<Integer> classIDs = new HashSet<Integer>();
		if(jRadioButtonNewResourceYes.isSelected())
		{
			int rowSize = jTableDestiny.getModel().getRowCount();
			for(int i=0;i<rowSize;i++)
			{
				boolean select = (Boolean) jTableDestiny.getModel().getValueAt(i, 1);
				if(select)
				{
					String classe = (String) jTableDestiny.getModel().getValueAt(i, 0);
					classIDs.add(ClassProperties.getClassClassID().get(classe));
				}
			}
		}
		return classIDs;
	}
	
	public String getNewResourceName()
	{
		return jTextFieldName.getText();
	}
	
	public String getNewResourceNotes()
	{
		return jTextFieldNotes.getText();
	}

}
