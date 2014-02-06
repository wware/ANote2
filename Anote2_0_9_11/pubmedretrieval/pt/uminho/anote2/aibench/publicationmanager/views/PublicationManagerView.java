package pt.uminho.anote2.aibench.publicationmanager.views;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.RemoveQueryGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;


/**
* Aibench View - For Publication Manager
* 
* @author Hugo Costa
* @author Rafael Carreira
*/
public class PublicationManagerView extends JPanel implements Observer{

	private static final long serialVersionUID = 7315096041623533479L;
	private PublicationManager pubManager;
	private JComboBox jComboSort;
	private JButton jButtonHelp;
	private JButton newQueryButton;
	private TableSearchPanel jtableSearch;
	private List<QueryInformationRetrievalExtension> queries;

	public PublicationManagerView(PublicationManager pubManager){
		super();	
		this.pubManager = pubManager;
		try {
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		this.pubManager.addObserver(this);	
	}
				
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.5, 0.1};
		thisLayout.rowHeights = new int[] {113, 20};
		thisLayout.columnWeights = new double[] {0.2, 0.2, 0.2, 0.2, 0.2};
		thisLayout.columnWidths = new int[] {159, 184, 96, 161, 7};
		this.setLayout(thisLayout);
		{
			this.add(getSearchPanelQueries(), new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(7, 4, 0, 0), 0, 0));
			this.add(getNewQueryButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJButtonHelp(), new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
	
	private JPanel getSearchPanelQueries() throws SQLException, DatabaseLoadDriverException {
		if(jtableSearch==null)
		{
			jtableSearch = new TableSearchPanel(false);
			jtableSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Queries", TitledBorder.LEADING, TitledBorder.TOP));
			jtableSearch.setModel(fillModelQuerysTable());
			jtableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			constructQueryTable(jtableSearch.getMainTable());
		}
		return jtableSearch;
	}

	private TableModel fillModelQuerysTable() throws SQLException, DatabaseLoadDriverException{

		DefaultTableModel tableModel = new DefaultTableModel()
		{
			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 0 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4 )?(Integer.class):String.class;
			}
			
		};
		Properties prop;
		ResultSet rs2 = null;
		String queryDB = QueriesProcess.selectQueriesPubmed;
		Statement statement;
			statement = Configuration.getDatabase().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(queryDB);
			queries = new ArrayList<QueryInformationRetrievalExtension>();
			while(rs.next())
			{	
				Connection conn = Configuration.getDatabase().getConnection();
				PreparedStatement ps = conn.prepareStatement(QueriesIRProcess.selectQueryProperties);
				ps.setInt(1,rs.getInt(1));
				prop = new Properties();
				rs2 = ps.executeQuery();		
				while(rs2.next())
				{
					prop.put(rs2.getString(1),rs2.getString(2));
				}
			
				QueryInformationRetrievalExtension query = new QueryInformationRetrievalExtension(
						rs.getInt(1),
						rs.getString(2),
						rs.getTimestamp(3),
						rs.getString(4),
						rs.getString(5),
						rs.getInt(6),
						rs.getInt(7),
						0,
						rs.getBoolean(8),
						prop,
						pubManager);
				
				queries.add(query);
				rs2.close();
			}
			if(rs!=null)
			{
				rs.close();
			}		
		String[] columns = new String[] {"Date","Query Details","Publications","Abstracts","ID","DB","Load"};
		tableModel.setColumnIdentifiers(columns);
		Object data[][];
		data = new Object[queries.size()][7];	
		int i=0;
		for(QueryInformationRetrievalExtension res: queries)
		{
			data[i][0] = Utils.SimpleDataFormat.format(res.getDate());
			data[i][1] = res.getQueryResumeWithProperties();
			data[i][2] = res.getDocumentMathing();
			data[i][3] = res.getAvailable_abstracts();
			data[i][4] = res.getID();
			data[i][5] = "";
			data[i][6] = "";
			tableModel.addRow(data[i]);
			i++;
		}
		return tableModel;		
	}

	private JButton getNewQueryButton() {
		if (newQueryButton == null) {
			newQueryButton = new JButton();
			newQueryButton.setText("New Query");
			newQueryButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pubmed.png")));
			newQueryButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.pubmedsearch")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
		}
		return newQueryButton;
	}
	
	public void update(Observable arg0, Object arg1) {
		TableModel querysTableModel;
		try {
			querysTableModel = fillModelQuerysTable();
			jtableSearch.setModel(querysTableModel);
			constructQueryTable(jtableSearch.getMainTable());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

	private void constructQueryTable(JTable jtable)
	{
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.datecompletefieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.datecompletefieldminWith);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.datecompletefieldpreferredWith);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setMaxWidth(150);
		jtable.getColumnModel().getColumn(2).setMinWidth(85);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(100);	
		jtable.getColumnModel().getColumn(3).setMaxWidth(150);
		jtable.getColumnModel().getColumn(3).setMinWidth(85);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(100);	
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(5).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(5).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);
		jtable.getColumnModel().getColumn(6).setMaxWidth(PreferencesSizeComponents.loadfieldmaxWith);
		jtable.getColumnModel().getColumn(6).setMinWidth(PreferencesSizeComponents.loadfieldminWith);
		jtable.getColumnModel().getColumn(6).setPreferredWidth(PreferencesSizeComponents.loadfieldpreferredWith);	
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();		
		// mudar numeros de colunas
		for (int j = 0; j < 5; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
		}
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png"));
		jtable.getColumn("Load").setCellRenderer((new ButtonLoadQuery(icon)));
		jtable.getColumn("Load").setCellEditor(new ButtonLoadQueryEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer((new ButtonRemoveDBQueryRender(icon2)));
		jtable.getColumn("DB").setCellEditor(new ButtonRemoveDBEditor());
	}

	
	private void removeFromDB() {
		if(this.jtableSearch.getSelectedRowsInOriginalModel()[0]==-1)
		{
			
		}
		else
		{
			new RemoveQueryGUI(queries.get(this.jtableSearch.getSelectedRowsInOriginalModel()[0]));
		}
	}
	
	public void loadQuery(){
		if(this.jtableSearch.getSelectedRowsInOriginalModel()[0]==-1)
		{
			
		}
		else
		{
			pubManager.addQueryInformationRetrievalExtension(queries.get(this.jtableSearch.getSelectedRowsInOriginalModel()[0]));
		}
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Publication_Manager_View");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	class ButtonRemoveDBQueryRender extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;
		
		public ButtonRemoveDBQueryRender(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() {
			removeFromDB();
		}

	}

	class ButtonRemoveDBEditor extends ButtonTableCellEditor {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			removeFromDB();
		}

	}
	
	class ButtonLoadQuery extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadQuery(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() {
			loadQuery();
		}

	}

	class ButtonLoadQueryEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			loadQuery();
		}

	}
		
}
