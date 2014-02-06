package pt.uminho.anote2.aibench.corpus.gui.wizard.reexport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfigurationEntityOptions;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REToXGMMLWizardStep1 extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jUpperPanel;
	private TableSearchPanel tableSearch;
	private RESchema reProcess;
	private JButton jButtonAll;
	private boolean jButtonAllSelected = true;
	
	public REToXGMMLWizardStep1() {
		super(new ArrayList<Object>());
		initGUI();
		try {
			updateTable();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		this.setModal(true);
	}
	
	public REToXGMMLWizardStep1(List<Object> objects) throws SQLException, DatabaseLoadDriverException {
		super(new ArrayList<Object>());
		this.reProcess = (RESchema) objects.get(0);
		initGUI();
		updateTable();
		fillClasses(objects.get(1));
		this.setModal(true);
		this.setTitle("RE Process to Cytoscape (XGMML) File - Class Filter");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}
	
	public REToXGMMLWizardStep1(RESchema reSchema) throws SQLException, DatabaseLoadDriverException
	{
		super(new ArrayList<Object>());
		this.reProcess = reSchema;
		initGUI();
		updateTable();
		this.setModal(true);
		this.setTitle("RE Process to Cytoscape (XGMML) File - Class Filter");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}

	private void fillClasses(Object object) {
		REToNetworkConfigurationEntityOptions entitiesOption = (REToNetworkConfigurationEntityOptions) object;
		for(int i=0;i<tableSearch.getMainTable().getModel().getRowCount();i++)
		{
			String classe = (String) tableSearch.getMainTable().getValueAt(i, 0);
			int classID = ClassProperties.getClassClassID().get(classe);
			if(entitiesOption.getClassIdsAllowed().contains(classID))
			{
				tableSearch.getMainTable().setValueAt(true, i, 2);
			}
			else
			{
				tableSearch.getMainTable().setValueAt(false, i, 2);
			}
		}
	}

	private void getREProcess() {
		reProcess = (RESchema) HelpAibench.getSelectedItem(RESchema.class);
	}

	private void initGUI() {
		setEnableBackButton(false);
		setEnableDoneButton(false);	
	}	



	public void goNext() {
		Set<Integer> classIdsAllowed = getClassIdsSelecte();
		if(classIdsAllowed.size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one class");
		}
		else
		{
			List<Object> parameters = getParam();
			parameters.add(reProcess);
			parameters.add(new REToNetworkConfigurationEntityOptions(classIdsAllowed));
			closeView();
			new REToXGMMLWizardStep2(parameters);
		}
	}


	private Set<Integer> getClassIdsSelecte() {
		Set<Integer> classIDSelected = new HashSet<Integer>();
		for(int i=0;i<this.tableSearch.getOriginalTableModel().getRowCount();i++)
		{
			boolean selected = ((Boolean) tableSearch.getValueAt(i,2)).booleanValue();
			if(selected)
			{
				classIDSelected.add(ClassProperties.getClassClassID().get(tableSearch.getValueAt(i,0)));
			}
			
		}		
		return classIDSelected;
	}

	public void goBack() {}


	public void done() {}


	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"REProcess_Export_To_XGMML_File#Entity_Selection_-_Classes_.28Nodes.29";
	}

	public JComponent getMainComponent() {
		getREProcess();
		if(jUpperPanel== null)
		{
			jUpperPanel = new JPanel();
			jUpperPanel.setBorder(BorderFactory.createTitledBorder(null, "Select Entity Classes", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			jUpperPanel.setLayout(thisLayout);
			{
				tableSearch = new TableSearchPanel(false);
				jUpperPanel.add(tableSearch, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jUpperPanel.add(getChangeButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jUpperPanel;
	}

	private void updateTable() throws SQLException, DatabaseLoadDriverException {
		tableSearch.setModel(getTableModal());
		completeTable(tableSearch.getMainTable());
	}

	private JButton getChangeButton() {
		if(jButtonAll == null)
		{
			jButtonAll = new JButton();
			jButtonAll.setText("Unselect");
			jButtonAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					changeOriginalTableModel();
				}
			});
		}
		return jButtonAll;
	}

	protected void changeOriginalTableModel() {
		boolean selectedAll = !jButtonAllSelected;
		for(int i=0;i<this.tableSearch.getOriginalTableModel().getRowCount();i++)
		{
			this.tableSearch.getOriginalTableModel().setValueAt(selectedAll, i, 2);
		}			
		if(selectedAll)
			jButtonAll.setText("Unselect");
		else
			jButtonAll.setText("Select All");
		jButtonAllSelected = selectedAll;
		tableSearch.updateUI();
	}

	private void completeTable(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setMinWidth(150);
		jtable.getColumnModel().getColumn(2).setMaxWidth(30);
		jtable.getColumnModel().getColumn(2).setMinWidth(30);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(30);
	}

	private TableModel getTableModal() throws SQLException, DatabaseLoadDriverException {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0 )
					return String.class;
				else if(columnIndex == 1)
					return Integer.class;
				else			
					return Boolean.class;
			}
		};
		String[] columns = new String[] {"Class", "Number of Occurrences",""};
		tableModel.setColumnIdentifiers(columns);		
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.processEntitiesClassStats);
		ps.setInt(1, reProcess.getID());
		ResultSet rs = ps.executeQuery();
		Object[] data = new Object[3];	
		while(rs.next())
		{
			data[0] = rs.getString(1);
			data[1] = rs.getInt(2);
			data[2] = Boolean.TRUE;
			tableModel.addRow(data);
		}
		rs.close();
		ps.close();
		return tableModel;
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(RESchema.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No RE Process selected on clipboard");
			dispose();
		} else
			try {
				if(((RESchema) obj).getAllEvents().size() == 0)
				{
					Workbench.getInstance().warn("No Relations to Export");
					dispose();
				}
				else
				{
					this.setTitle("RE Process to XGMML File - Class Filter");
					Utilities.centerOnOwner(this);
					this.setVisible(true);
				}
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
	}

}
