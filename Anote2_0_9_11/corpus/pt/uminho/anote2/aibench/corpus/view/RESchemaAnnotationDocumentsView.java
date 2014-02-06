package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.stats.DocumentRelationsAndEntitiesStats;
import pt.uminho.anote2.aibench.corpus.view.help.ComboRelationAndEntitiesAnnotationsRenderer;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.comparate.AlphanumericComparator;
import pt.uminho.generic.components.table.tablerowsorter.TableRowSorterWhitOtherTypes;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;


public class RESchemaAnnotationDocumentsView extends JPanel{

	private static final long serialVersionUID = 1L;
	private JPanel jPanelPublications;
	private JPanel jPanelOptions;
	private JPanel jPanelOtherInformation;
	private JTable jTableArticle;
	private JButton jButtonHelp;
	private JScrollPane jScrollPanePubbs;	
	private RESchema reProcess;
	private PreparedStatement ps;
	private PreparedStatement ps2;
	private TableSearchPanel jTableSearch;
	private Map<Integer,DocumentRelationsAndEntitiesStats> docStatistics;
	
	public RESchemaAnnotationDocumentsView(RESchema process) 
	{
		reProcess = process;
		this.docStatistics = new HashMap<Integer, DocumentRelationsAndEntitiesStats>();
		try {
			InitPrepareStatment();
			initGUI();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void InitPrepareStatment() throws SQLException, DatabaseLoadDriverException {
		ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.classNumberAnnotationsPublication);
		ps.setInt(1,reProcess.getId());
		ps.setInt(2,reProcess.getCorpus().getID());
		ps2 = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.relationsAnnotations);
		ps2.setInt(1,reProcess.getId());
		ps2.setInt(2,reProcess.getCorpus().getID());
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.05, 0.1, 0.1, 0.05};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
		{
			jPanelPublications = new JPanel();
			GridBagLayout jPanelPublicationsLayout = new GridBagLayout();
			this.add(jPanelPublications, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPublicationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPublicationsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelPublicationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPublicationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPublications.setLayout(jPanelPublicationsLayout);
			{
				jPanelPublications.add(getSearchPAnel(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			jPanelOptions = new JPanel();
			this.add(jPanelOptions, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			{
				jButtonHelp = new JButton();
				jPanelOptions.add(jButtonHelp);
				jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
				jButtonHelp.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						try {
							Help.internetAccess(GlobalOptions.wikiGeneralLink+"Process_Load_Annotated_Document");
						} catch (IOException e1) {
							TreatExceptionForAIbench.treatExcepion(e1);
						}
					}
				});
			}
		}
		{
			jPanelOtherInformation = new JPanel();
			this.add(jPanelOtherInformation, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		}
	}
	
	private JPanel getSearchPAnel() throws SQLException, DatabaseLoadDriverException {
		if(jTableSearch == null) {
			jTableSearch = new TableSearchPanel();
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableSearch.setModel(getTableModel());
			completeTable(jTableSearch.getMainTable());
		}
		return jTableSearch;
	}
	
	private TableModel getTableModel() throws SQLException, DatabaseLoadDriverException {
		String[] columns = new String[] {"Title", "Authors", "Date","ID","PMID/OtherID","Annotations","Load"};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public Class<?> getColumnClass(int columnIndex){
//				if(columnIndex == 3 || columnIndex == 4 || columnIndex == 6)
//					return Integer.class;
//				else if(columnIndex == 5)
					return Object.class;
//				else 
//					return String.class;
			}		
		};
		tableModel.setColumnIdentifiers(columns);		
		Object[][] data;	
		data = new Object[reProcess.getAllProcessDocs().size()][7];
		Iterator<Integer> itDocs = reProcess.getAllProcessDocs().keySet().iterator();
		int i=0;
		while(itDocs.hasNext())
		{
			IAnnotatedDocument pub = reProcess.getAllProcessDocs().get(itDocs.next());
			data[i][0] = pub.getTitle();
			data[i][1] = pub.getAuthors();
			data[i][2] = pub.getYearDate();
			data[i][3] = pub.getID();
			if(pub.getOtherID()!=null && !pub.getOtherID().equals("") && Utils.isIntNumber(pub.getOtherID()))		
			{
				data[i][4] = Integer.valueOf(pub.getOtherID());
			}
			else if(pub.getOtherID()!=null)
			{
				data[i][4] = pub.getOtherID();
			}
			else
			{
				data[i][4] = new String();
			}
			DocumentRelationsAndEntitiesStats doc =  getNumberOfRelations(pub);
			this.docStatistics.put(pub.getID(), doc);
			data[i][5] = doc.getRelationsNumber();
			data[i][6] = "";
			tableModel.addRow(data[i]);
			i++;
		}	
		return tableModel;
	}

	private DocumentRelationsAndEntitiesStats getNumberOfRelations(IAnnotatedDocument doc) throws SQLException {
		DocumentRelationsAndEntitiesStats docStats = new DocumentRelationsAndEntitiesStats(doc.getID());
		ps2.setInt(3,doc.getID());
		ResultSet rs2 = ps2.executeQuery();
		if(rs2.next())
		{
			docStats.addNumberOFRelations(rs2.getInt(1));
		}
		ps.setInt(3,doc.getID());
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			docStats.addEntityClassStatistics(rs.getInt(1), rs.getInt(2));
		}
		return docStats;
	}

	private void completeTable(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setMaxWidth(350);
		jtable.getColumnModel().getColumn(1).setMinWidth(75);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(100);
		jtable.getColumnModel().getColumn(2).setMaxWidth(PreferencesSizeComponents.datefieldmaxWith);
		jtable.getColumnModel().getColumn(2).setMinWidth(PreferencesSizeComponents.datefieldminWith);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(PreferencesSizeComponents.datefieldpreferredWith);	
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);	
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.pmidotherIDfieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.pmidotherIDfieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.pmidotherIDfieldpreferredWith);
		jtable.getColumnModel().getColumn(5).setMaxWidth(750);
		jtable.getColumnModel().getColumn(5).setMinWidth(250);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(250);
		jtable.getColumnModel().getColumn(6).setMaxWidth(PreferencesSizeComponents.loadfieldmaxWith);
		jtable.getColumnModel().getColumn(6).setMinWidth(PreferencesSizeComponents.loadfieldminWith);
		jtable.getColumnModel().getColumn(6).setPreferredWidth(PreferencesSizeComponents.loadfieldpreferredWith);
		jtable.getColumn("PMID/OtherID").setCellRenderer(new ButtonTablePMIDRender());
		jtable.getColumn("PMID/OtherID").setCellEditor(new ButtonTablePMIDCellEditor());;
		ComboRelationAndEntitiesAnnotationsRenderer combo = new ComboRelationAndEntitiesAnnotationsRenderer(docStatistics);
		jtable.getColumn("Annotations").setCellRenderer(combo);
		jtable.getColumn("Annotations").setCellEditor(combo);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/colorscm.png"));
		jtable.getColumn("Load").setCellRenderer(new ButtonLoadResourceRenderer(icon));
		jtable.getColumn("Load").setCellEditor(new ButtonLoadResourceCellEditor());	
		Map<Integer, Comparator<?>> columnComparators = new HashMap<Integer, Comparator<?>>();
		columnComparators.put(4, new AlphanumericComparator());
		TableRowSorter<TableModel> sortedModel = new TableRowSorterWhitOtherTypes(columnComparators);
		sortedModel.setModel(jtable.getModel());
		jtable.setRowSorter(sortedModel);
		
	}
	
	private void addAnnotationDoc() throws SQLException, DatabaseLoadDriverException {
		IAnnotatedDocument doc = reProcess.getAllProcessDocs().get(jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0],3));
		reProcess.addAnnotatedDocument((REDocumentAnnotation) doc);
	}
	
	class ButtonTablePMIDRender  implements TableCellRenderer{
		
		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			JButton jbutton = new JButton();
			if(value.equals("") || value.equals(0))
			{
				jbutton.setText("Without");
			}
			else
			{
				jbutton.setText(value.toString());
			}
			jbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						lanchPMID();
					} catch (SQLException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					} catch (DatabaseLoadDriverException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
			return jbutton;
		}
	}

	class ButtonTablePMIDCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			try {
				lanchPMID();
			} catch (IOException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}

	}

	public void lanchPMID() throws SQLException, DatabaseLoadDriverException, IOException {
		int id = (Integer) jTableSearch.getOriginalTableModel().getValueAt(jTableSearch.getSelectedRowsInOriginalModel()[0], 3);
		String externalLink = reProcess.getAllProcessDocs().get(id).getExternalLink();
		if(externalLink==null || externalLink.length() < 1)
		{
			Workbench.getInstance().warn("The document does not contains external link... Please update External Link in Publication Resume GUI");
		}
		else
		{
			Help.internetAccess(externalLink);
		}
	}
	
	

	class ButtonLoadResourceRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadResourceRenderer(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			addAnnotationDoc();
		}

	}

	class ButtonLoadResourceCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			addAnnotationDoc();
		}
	}

}
