package pt.uminho.anote2.aibench.corpus.gui.wizard.createcorpus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateCorpusWizard2 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;

	private JPanel jPanelUpperPanel;
	private JScrollPane jScrollPaneTable;
	private JTable jTableQueries;

	private JCheckBox box;


	public CreateCorpusWizard2(List<Object> param) {
		super(param);
		initGUI();
		constructTable();
		if(param.size()==2)
		{
			fillQueries(param.get(1));
			getParam().remove(1);
		}
		this.setTitle("Create a Corpus - Select Queries");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillQueries(Object object) {
		List<Query> queries = (List<Query>) object;
		Set<Integer> ids = new HashSet<Integer>();
		for(Query query:queries)
		{
			ids.add(query.getID());
		}
		int rows = jTableQueries.getRowCount();
		for(int i=0;i<rows;i++)
		{
			Query resource = (Query) jTableQueries.getModel().getValueAt(i, 0);
			if(ids.contains(resource.getID()))
			{
				jTableQueries.getModel().setValueAt(true, i, 1);
			}
			else
			{
				jTableQueries.getModel().setValueAt(false, i, 1);
			}
		}		
	}

	private void initGUI() {
		setEnableDoneButton(false);
	}
	
	
	private  TableModel fillModelQuerysTable(String order) throws SQLException, DatabaseLoadDriverException{
		String[] columns = new String[] {"Queries",""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);		
		Properties prop;
		ResultSet rs2 = null;
		String queryDB = QueriesProcess.selectQueriesPubmed;
		queryDB = queryDB+order;	
		PreparedStatement statement;
		statement = Configuration.getDatabase().getConnection().prepareStatement(queryDB);

		PreparedStatement statement2 = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.selectQueryProperties);
		ResultSet rs = statement.executeQuery();	
		while(rs.next())
		{	
			prop = new Properties();
			statement2.setInt(1,rs.getInt(1));
			rs2 = statement2.executeQuery();
			while(rs2.next())
			{
				prop.put(rs2.getString(1),rs2.getString(2));
			}
			Query query = new Query(
					rs.getInt(1),
					rs.getString(2),
					rs.getDate(3),
					rs.getString(4),
					rs.getString(5),
					rs.getInt(6),
					rs.getInt(7),
					0,
					rs.getBoolean(8),
					prop);	
			Object rowData[] = new Object[2];
			rowData[0] = query;
			rowData[1] = false;
			tableModel.addRow(rowData );
			rs2.close();
		}
		rs.close();
		statement.close();
		statement2.close();
		return tableModel;
	}

	private void constructTable() {
		box = new JCheckBox();
		jTableQueries.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jTableQueries.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(box));	
		jTableQueries.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jTableQueries.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		jTableQueries.updateUI();
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for (int j = 0; j < 1; j++) {
			jTableQueries.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jTableQueries.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}
	
	public void done() {}

	public void goBack() {
		closeView();
		new CreateCorpusWizard1(getParam());
	}

	public void goNext() {
		int rows = jTableQueries.getRowCount();
		List<Query> queries = new ArrayList<Query>();
		for(int i=0;i<rows;i++)
		{
			boolean isactive = (Boolean) jTableQueries.getValueAt(i, 1);
			if(isactive)
			{
				Query resource = (Query) jTableQueries.getModel().getValueAt(i, 0);
				queries.add(resource);
			}
		}
		if(queries.size()==0)
		{
			Workbench.getInstance().warn("Please select at least one query");
		}
		else
		{
			List<Object> objs = getParam();
			objs.add(queries);
			closeView();
			new CreateCorpusWizard3(objs);
		}
	}

	public JPanel getMainComponent() {
		if(jPanelUpperPanel == null)
		{
			jPanelUpperPanel = new JPanel();
			jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder("Select Queries"));
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jScrollPaneTable = new JScrollPane();
				jPanelUpperPanel.add(jScrollPaneTable, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel jTableQueriesModel = new DefaultTableModel();
					try {
						jTableQueriesModel = fillModelQuerysTable("");
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
					jTableQueries = new JTable()
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
					jScrollPaneTable.setViewportView(jTableQueries);
					jTableQueries.setModel(jTableQueriesModel);
				}
			}
		}
		return jPanelUpperPanel;
	}
	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Create_Corpus_By_Publication_Manager#Select_Queries";
	}
}
