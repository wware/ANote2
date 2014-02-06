package pt.uminho.anote2.aibench.corpus.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class ViewIEProcessDetails  extends DialogGenericViewOkButtonOnly{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JLabel jLabelStopWords;
	private JLabel jLabelResourceStopWord;
	private JTable jTableResources;
	private JScrollPane jScrollPaneResources;
	private JPanel jPanelResources;
	private JLabel jTextPaneResourceID;
	private JLabel jpaneFieldYesORNo;
	private JLabel jTextPaneClassAnnotated;
	private JLabel jLabelClasseAnnotated;
	private JLabel jLabelTotalAnnotations;
	private JTable jTableClassEntities;
	private JScrollPane jScrollPaneClass;
	private JPanel jPanelClassTAble;
	private JTextPane jTextPaneAnnotations;
	private JPanel jPanelProperties;
	private JPanel jPanelStats;
	private JTabbedPane jTabbedPaneStatsAndProperties;
		private int totalEntities;
	private int totalClasses;
	private JLabel jpaneNormalizationYesNo;
	private JLabel jLabelNormalization;
	private IEProcess process;

	public ViewIEProcessDetails(IEProcess process) throws SQLException, DatabaseLoadDriverException {
		this.process = process;
		this.setTitle("IE Process Detais"+process.toString());
		initGUI();
		fillFields();
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);	
	}


	private void initGUI() {

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			jPanelUpperPanel = new JPanel();
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			getContentPane().add(jPanelUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanelLayout.rowWeights = new double[] {0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jTabbedPaneStatsAndProperties = new JTabbedPane();

				jPanelUpperPanel.add(jTabbedPaneStatsAndProperties, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelStats = new JPanel();
					jPanelStats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Entity IE Process "+GlobalTextInfoSmall.statistics, TitledBorder.LEADING, TitledBorder.TOP));
	
					GridBagLayout jPanelStatsLayout = new GridBagLayout();
					jTabbedPaneStatsAndProperties.addTab(GlobalTextInfoSmall.statistics,jPanelStats);
					jPanelStatsLayout.rowWeights = new double[] {0.0, 0.1};
					jPanelStatsLayout.rowHeights = new int[] {7, 7};
					jPanelStatsLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.1};
					jPanelStatsLayout.columnWidths = new int[] {7, 7, 20, 20, 7};
					jPanelStats.setLayout(jPanelStatsLayout);
					{
						jLabelTotalAnnotations = new JLabel();
						jPanelStats.add(jLabelTotalAnnotations, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelTotalAnnotations.setText("Annotations : ");
					}
					{
						jTextPaneAnnotations = new JTextPane();
						jPanelStats.add(jTextPaneAnnotations, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jPanelClassTAble = new JPanel();
						GridBagLayout jPanelClassTAbleLayout = new GridBagLayout();
						jPanelStats.add(jPanelClassTAble, new GridBagConstraints(0, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelClassTAbleLayout.rowWeights = new double[] {0.1};
						jPanelClassTAbleLayout.rowHeights = new int[] {7};
						jPanelClassTAbleLayout.columnWeights = new double[] {0.1};
						jPanelClassTAbleLayout.columnWidths = new int[] {7};
						jPanelClassTAble.setLayout(jPanelClassTAbleLayout);
						{
							jScrollPaneClass = new JScrollPane();
							jPanelClassTAble.add(jScrollPaneClass, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							{
								jTableClassEntities = new JTable();
								jScrollPaneClass.setViewportView(jTableClassEntities);
							}
						}
					}
					{
						jLabelClasseAnnotated = new JLabel();
						jPanelStats.add(jLabelClasseAnnotated, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jLabelClasseAnnotated.setText("Classes Annotated: ");
					}
					{
						jTextPaneClassAnnotated = new JLabel();
						jPanelStats.add(jTextPaneClassAnnotated, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jPanelProperties = new JPanel();
					jPanelProperties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Properties", TitledBorder.LEADING, TitledBorder.TOP));
					GridBagLayout jPanelPropertiesLayout = new GridBagLayout();
					jTabbedPaneStatsAndProperties.addTab("Properties", jPanelProperties);
					jPanelPropertiesLayout.rowWeights = new double[] {0.0, 0.1};
					jPanelPropertiesLayout.rowHeights = new int[] {7, 7};
					jPanelPropertiesLayout.columnWeights = new double[] {0.0, 0.025, 0.0, 0.05, 0.0,0.025};
					jPanelPropertiesLayout.columnWidths = new int[] {7, 7, 7, 7, 20};
					jPanelProperties.setLayout(jPanelPropertiesLayout);
					{
						jLabelStopWords = new JLabel();
						jPanelProperties.add(jLabelStopWords, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.EAST, new Insets(0, 0, 0, 0), 0, 0));
						jLabelStopWords.setText("StopWords :");
					}
					{
						jpaneFieldYesORNo = new JLabel();
						jPanelProperties.add(jpaneFieldYesORNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jLabelResourceStopWord = new JLabel();
						jPanelProperties.add(jLabelResourceStopWord, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.EAST, new Insets(0, 0, 0, 0), 0, 0));
						jLabelResourceStopWord.setText("Resource (ID):");
					}
					{
						jTextPaneResourceID = new JLabel();
						jPanelProperties.add(jTextPaneResourceID, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jLabelNormalization = new JLabel();
						jPanelProperties.add(jLabelNormalization, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.EAST, new Insets(0, 0, 0, 0), 0, 0));
						jLabelNormalization.setText("Normalization :");
					}
					{
						jpaneNormalizationYesNo = new JLabel();
						jpaneNormalizationYesNo.setText("No");
						jPanelProperties.add(jpaneNormalizationYesNo, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jPanelResources = new JPanel();
						GridBagLayout jPanelResourcesLayout = new GridBagLayout();
						jPanelProperties.add(jPanelResources, new GridBagConstraints(0, 1, 9, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}


	private void fillFields() throws SQLException, DatabaseLoadDriverException {
		fillStats();
		fillProperties();
	}


	private void fillProperties() throws SQLException, DatabaseLoadDriverException {
		Properties prop = process.getProperties();
		if(prop.containsKey(GlobalNames.stopWords) && Boolean.valueOf(prop.getProperty(GlobalNames.stopWords)))
		{
			String valueID = prop.getProperty(GlobalNames.stopWordsResourceID);
			int value = Integer.valueOf(valueID);
			if(value>0)
			{
				jTextPaneResourceID.setText(new LexicalWords(value, "", "").toString());
				jpaneFieldYesORNo.setText("YES");
			}
		}
		else
		{
			jTextPaneResourceID.setText("");
			jpaneFieldYesORNo.setText("NO");
		}
		if(prop.containsKey(GlobalNames.normalization) && Boolean.valueOf(prop.getProperty(GlobalNames.normalization)))
		{
			jpaneNormalizationYesNo.setText("YES");
		}
		List<GenericPair<IResource<IResourceElement>,List<String>>> propInfo = fillResourcesClasses();
		DefaultTableModel jTable1Model = getDefaultTableModel(propInfo);
		jTableResources.setModel(jTable1Model);
		completeTable(propInfo);
		jTableResources.updateUI();
	}


	private void completeTable(final List<GenericPair<IResource<IResourceElement>, List<String>>> propInfo) {
		jTableResources.setRowHeight(25);	
		jTableResources.getColumnModel().getColumn(0).setMinWidth(100);
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Classes");
		viewColumn.setMinWidth(100);
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


	private List<GenericPair<IResource<IResourceElement>,List<String>>> fillResourcesClasses() throws SQLException, DatabaseLoadDriverException {
		List<GenericPair<IResource<IResourceElement>,List<String>>> result = new ArrayList<GenericPair<IResource<IResourceElement>,List<String>>>();
		Properties prop = process.getProperties();
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
		if(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			resource = makeResource(id,type,name,note);
		}
		rs.close();
		statement.close();
		return resource;
	}
	
	private IResource<IResourceElement> makeResource(int id, String type,String name, String note) {
		if(type.equals(GlobalOptions.resourcesDictionaryName))
		{
			return new Dictionary(id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesLookupTableName))
		{
			return new LookupTable(id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesOntologyName))
		{
			return new Ontology(id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesRuleSetName))
		{
			return new RulesSet( id, name, note);
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


	private void fillStats() throws SQLException, DatabaseLoadDriverException {
		jTableClassEntities.setModel(getJTableClassEntitiesModel());
		completeTableClassEntities(jTableClassEntities);
		jTextPaneAnnotations.setText(String.valueOf(totalEntities));
		jTextPaneClassAnnotated.setText(String.valueOf(totalClasses));
	}


	private void completeTableClassEntities(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setMinWidth(150);
	}


	private TableModel getJTableClassEntitiesModel() throws SQLException, DatabaseLoadDriverException {
		totalClasses = 0;
		totalEntities = 0;
		List<GenericPair<String,Integer>> stats = getIEProcessStats(); 
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"Class", "Annotations (Count)"};
		tableModel.setColumnIdentifiers(columns);
		Object[][] data;
		int i = 0;
		data = new Object[stats.size()][4];
		for(GenericPair<String,Integer> pair: stats)
		{
			data[i][0] = pair.getX();
			data[i][1] = pair.getY();
			tableModel.addRow(data[i]);
			totalEntities = totalEntities + pair.getY();
			totalClasses++;
			i++;
		}	
		return tableModel;
	}


	private List<GenericPair<String, Integer>> getIEProcessStats() throws SQLException, DatabaseLoadDriverException {
		List<GenericPair<String, Integer>> result = new ArrayList<GenericPair<String,Integer>>();
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.classNumberAnnotationsCorpus);
		ps.setInt(1, process.getID());
		ps.setInt(2,process.getCorpus().getID());
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			result.add(new GenericPair<String, Integer>(rs.getString(1),rs.getInt(2)));
		}
		rs.close();
		ps.close();
		return result;
	}

	protected void okButtonAction() {
		finish();		
	}


	@Override
	protected String getHelpLink() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
