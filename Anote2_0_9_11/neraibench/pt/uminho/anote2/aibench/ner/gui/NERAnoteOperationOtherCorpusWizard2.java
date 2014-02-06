package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class NERAnoteOperationOtherCorpusWizard2 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JPanel jPanelNERSelection;
	private JLabel jLabelStopWords;
	private JLabel jLabelResourceStopWord;
	private JLabel jLabelPreProcessing;
	private JLabel jLabelCaseSensitiveYesOrNo;
	private JLabel jLabelCaseSensitive;
	private JTable jTableResources;
	private JScrollPane jScrollPaneResources;
	private JPanel jPanelResources;
	private JLabel jTextPaneResourceID;
	private JLabel jpaneFieldYesORNo;
	private JPanel jPanelProperties;
	private JComboBox jComboBoxNERAnnotations;
	private boolean inactiveResources = false;
	private JLabel jLabelNormalization;
	private JLabel jpaneNormalizationYesNo;

	public NERAnoteOperationOtherCorpusWizard2(List<Object> param) throws SQLException, DatabaseLoadDriverException {
		super(param);
		initGUI();
		changeIEProcess();
		this.setTitle("NER @Note - Select Configutaions from NER Process");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);	
	}


	private void initGUI() {
		setEnableDoneButton(true);
		setEnableNextButton(false);
	}
	
	@SuppressWarnings("unchecked")
	private DefaultComboBoxModel getProcessesModel() throws SQLException, DatabaseLoadDriverException
	{
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		List<IProcess> lis = (List<IProcess>) getParam().get(0);
		Corpus corpus = (Corpus) getParam().get(1);
		List<IIEProcess> processes = corpus.getIProcesses();
		for(IProcess pro:lis)
		{
			IIEProcess process = (IIEProcess) pro;
			if(process.getName().equals(GlobalNames.nerAnote)&&!processes.contains(pro))
			{
				model.addElement(pro);
			}
		}	
		return model;
	}
	
	protected void changeIEProcess() throws SQLException, DatabaseLoadDriverException {
		fillProperties();		
	}
	
	
	private void fillProperties() throws SQLException, DatabaseLoadDriverException {
		IIEProcess processIE = (IIEProcess) jComboBoxNERAnnotations.getSelectedItem();
		Properties prop = processIE.getProperties();
		if(prop.containsKey(GlobalNames.nerpreProcessing))
		{
			jLabelPreProcessing.setText(prop.getProperty(GlobalNames.nerpreProcessing));
		}
		else
		{
			jLabelPreProcessing.setText("No");
		}
		if(prop.containsKey(GlobalNames.normalization) && Boolean.valueOf(prop.getProperty(GlobalNames.normalization)))
		{
			jpaneNormalizationYesNo.setText("Yes");
		}
		else
		{
			jpaneNormalizationYesNo.setText("No");

		}
		if(prop.containsKey(GlobalNames.casesensitive) && Boolean.valueOf(prop.getProperty(GlobalNames.casesensitive)))
		{
			jLabelCaseSensitiveYesOrNo.setText("Yes");
		}
		else
		{
			jLabelCaseSensitiveYesOrNo.setText("No");

		}
		List<GenericPair<IResource<IResourceElement>,List<String>>> propInfo = fillResourcesClasses();
		DefaultTableModel jTable1Model = getDefaultTableModel(propInfo);
		jTableResources.setModel(jTable1Model);
		completeTable(propInfo);
		jTableResources.updateUI();
	}
	
	private void completeTable(final List<GenericPair<IResource<IResourceElement>, List<String>>> propInfo) {
		jTableResources.setRowHeight(25);	
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Classes");
		viewColumn.setMinWidth(200);
		viewColumn.setMaxWidth(200);
		viewColumn.setPreferredWidth(200);
		viewColumn.setCellRenderer(new TableCellRenderer(){

			public JComboBox getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				DefaultComboBoxModel model = getDefaultComboBoxModel(row,propInfo);
				JComboBox viewButton = new JComboBox(model);
				return viewButton;
			}
				
		});
		
		viewColumn.setCellEditor(new TableCellEditor(){

			public JComboBox getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				DefaultComboBoxModel model = getDefaultComboBoxModel(row,propInfo);
				JComboBox viewButton = new JComboBox(model);
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return true;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return true;
			}

			public boolean stopCellEditing() {
				return true;
			}
		});
		jTableResources.addColumn(viewColumn);	
	}
	
	protected DefaultComboBoxModel getDefaultComboBoxModel(int row,List<GenericPair<IResource<IResourceElement>, List<String>>> propInfo) {
		DefaultComboBoxModel amodel = new DefaultComboBoxModel();
		{
			List<String> classes = propInfo.get(row).getY();
			for(String cl:classes)
				amodel.addElement(cl);
		}
		return amodel;
	}

	private List<GenericPair<IResource<IResourceElement>,List<String>>> fillResourcesClasses() throws SQLException, DatabaseLoadDriverException {
		List<GenericPair<IResource<IResourceElement>,List<String>>> result = new ArrayList<GenericPair<IResource<IResourceElement>,List<String>>>();
		IIEProcess processIE = (IIEProcess) jComboBoxNERAnnotations.getSelectedItem();
		Properties prop = processIE.getProperties();
		List<String> listClassContent ;
		for(String prop_name:prop.stringPropertyNames())
		{
			try {
				if(Integer.parseInt(prop_name)>0)
				{
					
					String value = prop.getProperty(prop_name);
					if(value.equals(GlobalNames.allclasses))
					{
						listClassContent = new ArrayList<String>();
						listClassContent.add(GlobalNames.allclasses);
					}
					else
					{
						listClassContent = getClassContent(value);
					}
					IResource<IResourceElement> resource = getResourceInfo(Integer.parseInt(prop_name));
					if(resource!=null)
					{
						GenericPair<IResource<IResourceElement>, List<String>> pair = new GenericPair<IResource<IResourceElement>, List<String>>(resource,listClassContent);
						result.add(pair);
					}
				}
			}
			catch(NumberFormatException e) {
//				e.printStackTrace();	
			}
		}
		return result;
	}
	
	private IResource<IResourceElement> getResourceInfo(int resourceID) throws SQLException, DatabaseLoadDriverException{						 
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getResourcesInfo);
		IResource<IResourceElement> resource = null;
		statement.setInt(1, resourceID);
		ResultSet rs = statement.executeQuery();
		int id;
		String type,name,note;
		boolean active;
		if(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			active = rs.getBoolean(5);
			resource = makeResource(id,type,name,note,active);
		}
		rs.close();
		statement.close();
		return resource;
	}
	
	private IResource<IResourceElement> makeResource(int id, String type,String name, String note,boolean active) {
		if(type.equals(GlobalOptions.resourcesDictionaryName))
		{
			return new Dictionary(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesLookupTableName))
		{
			return new LookupTable(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesOntologyName))
		{
			return new Ontology(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesRuleSetName))
		{
			return new RulesSet(id, name, note,active);
		}
		if(!active)
		{
			inactiveResources = true;
		}
		return null;
	}
	
	private List<String> getClassContent(String value) {
		String[] split = value.split("\\,");
		List<String> classContentID = new ArrayList<String>();
		for(String contentID:split)
		{
			if(contentID.length()>0)
			{
				String classe = ClassProperties.getClassIDClass().get(Integer.valueOf(contentID));
				classContentID.add(classe);
			}
		}
		return classContentID;
	}
	
	private DefaultTableModel getDefaultTableModel(List<GenericPair<IResource<IResourceElement>, List<String>>> propInfo) {
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"Resource"};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;
		data = new Object[propInfo.size()][4];
		for(GenericPair<IResource<IResourceElement>, List<String>> resource: propInfo)
		{
			data[i][0] = resource.getX().toString();
			tableModel.addRow(data[i]);
			i++;
		}	
		return tableModel;
	}
	

	public void done()
	{
		if(inactiveResources)
		{
			if(overwriteDatabase())
			{
				
			}
			else
			{
				return;
			}
		}
		Corpus corpus = (Corpus) getParam().get(1);	
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("Corpus", Corpus.class,corpus, null),
				new ParamSpec("Process", IIEProcess.class,jComboBoxNERAnnotations.getSelectedItem(), null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.neranotecorpus")){	
				closeView();
				Workbench.getInstance().executeOperation(def, paramsSpec);
			}
		}
	}
	
	private boolean overwriteDatabase(){
		Object[] options = new String[]{"Yes", "No"};
		int opt = HelpAibench.showOptionPane("Inactive Resources", "Exist Inactive resources ( this resources can not be used) Do you wish continue ?", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}	

	public void goBack() {
		closeView();
		try {
			new NERAnoteOperationWizard1(false);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	public void goNext() {

	}

	public JComponent getMainComponent() {

		if(jPanelUpperPanel == null)
		{
			jPanelUpperPanel = new JPanel();
			try {
				GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
				jPanelUpperPanelLayout.rowWeights = new double[] {0.05, 0.1};
				jPanelUpperPanelLayout.rowHeights = new int[] {7, 7};
				jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
				jPanelUpperPanelLayout.columnWidths = new int[] {7};
				jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
				{
					jPanelNERSelection = new JPanel();
					jPanelNERSelection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "IE Process Selection", TitledBorder.LEADING, TitledBorder.TOP));
					GridBagLayout jPanelNERSelectionLayout = new GridBagLayout();
					jPanelUpperPanel.add(jPanelNERSelection, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelNERSelectionLayout.rowWeights = new double[] {0.05};
					jPanelNERSelectionLayout.rowHeights = new int[] {7};
					jPanelNERSelectionLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
					jPanelNERSelectionLayout.columnWidths = new int[] {7, 7, 7};
					jPanelNERSelection.setLayout(jPanelNERSelectionLayout);
					{
						jComboBoxNERAnnotations = new JComboBox();
						jComboBoxNERAnnotations.addActionListener(new ActionListener() {				
							public void actionPerformed(ActionEvent arg0) {
								try {
									changeIEProcess();
								} catch (SQLException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								} catch (DatabaseLoadDriverException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								}			
							}
						});
						jComboBoxNERAnnotations.setModel(getProcessesModel());

						jPanelNERSelection.add(jComboBoxNERAnnotations, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jPanelNERSelection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Load Configurations from NER Process", TitledBorder.LEADING, TitledBorder.TOP));
					{

					}
					{
						jPanelProperties = new JPanel();
						jPanelUpperPanel.add(jPanelProperties, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelProperties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Properties", TitledBorder.LEADING, TitledBorder.TOP));
						GridBagLayout jPanelPropertiesLayout = new GridBagLayout();
						jPanelPropertiesLayout.rowWeights = new double[] {0.0, 0.1};
						jPanelPropertiesLayout.rowHeights = new int[] {7, 7};
						jPanelPropertiesLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.0, 0.1, 0.0};
						jPanelPropertiesLayout.columnWidths = new int[] {7, 7, 7, 7, 20};
						jPanelProperties.setLayout(jPanelPropertiesLayout);
						{
							jLabelStopWords = new JLabel();
							jPanelProperties.add(jLabelStopWords, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
							jLabelStopWords.setText("Pre Processing :");

						}
						{
							jpaneFieldYesORNo = new JLabel();
							jPanelProperties.add(jpaneFieldYesORNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0));
						}
						{
							jLabelResourceStopWord = new JLabel();
							jPanelProperties.add(jLabelResourceStopWord, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.EAST, new Insets(0, 0, 0, 0), 0, 0));
							jLabelResourceStopWord.setText("Resource (ID):");
							jLabelResourceStopWord.setVisible(false);
						}
						{
							jTextPaneResourceID = new JLabel();
							jPanelProperties.add(jTextPaneResourceID, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0));
							jTextPaneResourceID.setVisible(false);

						}
						{
							jLabelNormalization = new JLabel();
							jPanelProperties.add(jLabelNormalization, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
							jLabelNormalization.setText("Normalization :");
						}
						{
							jpaneNormalizationYesNo = new JLabel();
							jpaneNormalizationYesNo.setText("No");
							jPanelProperties.add(jpaneNormalizationYesNo, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
						}
						{
							jPanelResources = new JPanel();
							GridBagLayout jPanelResourcesLayout = new GridBagLayout();
							jPanelProperties.add(jPanelResources, new GridBagConstraints(0, 1, 9, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelProperties.add(getJLabelCaseSensitive(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
							jPanelProperties.add(getJLabelCaseSensitiveYesOrNo(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jPanelProperties.add(getJLabelPreProcessing(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jPanelResourcesLayout.rowWeights = new double[] {0.1};
							jPanelResourcesLayout.rowHeights = new int[] {7};
							jPanelResourcesLayout.columnWeights = new double[] {0.1};
							jPanelResourcesLayout.columnWidths = new int[] {7};
							jPanelResources.setLayout(jPanelResourcesLayout);
							{
								jScrollPaneResources = new JScrollPane();
								jPanelResources.add(jScrollPaneResources, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jTableResources = new JTable();
								jScrollPaneResources.setViewportView(jTableResources);
							}
						}

					}

				} 
			}
			catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		}
		return jPanelUpperPanel;
	}
	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Create_Annotation_Schema_By_NER_Already_Configurated#Select_Configuration";
	}
	
	private JLabel getJLabelCaseSensitive() {
		if(jLabelCaseSensitive == null) {
			jLabelCaseSensitive = new JLabel();
			jLabelCaseSensitive.setText("Case Sensitive : ");
		}
		return jLabelCaseSensitive;
	}
	
	private JLabel getJLabelCaseSensitiveYesOrNo() {
		if(jLabelCaseSensitiveYesOrNo == null) {
			jLabelCaseSensitiveYesOrNo = new JLabel();
			jLabelCaseSensitiveYesOrNo.setText("No");
		}
		return jLabelCaseSensitiveYesOrNo;
	}
	
	private JLabel getJLabelPreProcessing() {
		if(jLabelPreProcessing == null) {
			jLabelPreProcessing = new JLabel();
			jLabelPreProcessing.setText("No");
		}
		return jLabelPreProcessing;
	}

}
