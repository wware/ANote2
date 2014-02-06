package pt.uminho.anote2.aibench.corpus.gui.wizard.createcorpus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.comparate.AlphanumericComparator;
import pt.uminho.generic.components.table.tablerowsorter.TableRowSorterWhitOtherTypes;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateCorpusWizard3 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JList jListQueries;
	private TableSearchPanel jTablePubs;
	private JButton jButtonRemoveAll;
	private JButton jButtonSelectAllAndRemoveALL;
	private JTabbedPane jTabbedPane1;
	private JTabbedPane jTabbedPaneOptions;
	private ButtonGroup buttonGroupContent;
	private JScrollPane jScrollPanePubs;	
	private JRadioButton jRadioButtonAbstractOrFullTExt;
	private JRadioButton jRadioButtonFullText;
	private JRadioButton jRadioButtonAbstracts;
	private JRadioButton jRadioButtonIrrelevant;
	private JRadioButton jRadioButtonRelated;
	private JRadioButton jRadioButtonRelevant;
	private JRadioButton jRadioButtonAll;
	private JPanel jPanelCorpusProperties;
	private JPanel jPanelRelevanceOptionPAne;
	private JPanel jPanelPubsAndOptions;
	private JScrollPane jScrollPaneQueries;
	private JPanel jPanelQueries;
	private JCheckBox box;
	private JPanel upperPAnel;
	
	/**
	 * Queries whit all publications
	 */
	private Map<Integer,Set<Integer>> queryIDAllpublictaionSet; 
	/**
	 * Filter by Text composition
	 */
	private Map<Integer,Set<Integer>> queryIDSelectedPublictaionSet;
	/**
	 * Filter by Relevance
	 */
	private Map<Integer,Set<Integer>> queryIDRelevance;
	/**
	 * List of queries
	 */
	private List<Query> queries;
	/**
	 * Where relevance has saved
	 */
	private Map<Integer,Map<Integer,GenericPair<IPublication,String>>> queryPublicationRelevance;
	private boolean selelectAll;;


	public CreateCorpusWizard3(List<Object> param) {
		super(param);
		initGUI();
		this.setTitle("Create Corpus - Select Publications");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void fillQueryPublications() throws SQLException, DatabaseLoadDriverException {
		queryIDAllpublictaionSet = new HashMap<Integer, Set<Integer>>();
		queryIDSelectedPublictaionSet = new HashMap<Integer, Set<Integer>>();
		queryPublicationRelevance = new HashMap<Integer, Map<Integer,GenericPair<IPublication,String>>>();
		List<Object> para = getParam();
		Object listQueries = para.get(1);
		queries = (List<Query>) listQueries;
		for(Query query:queries)
		{
			Set<Integer> pubIds = query.getIds();
			queryIDAllpublictaionSet.put(query.getID(), pubIds);
			queryIDSelectedPublictaionSet.put(query.getID(),pubIds);
			queryPublicationRelevance.put(query.getID(),getResultsTable("",query.getID()));
		}
		queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
	}

	private void initGUI() {
		setEnableDoneButton(true);
		setEnableNextButton(false);
	}
	
	protected void changeQuery() 
	{	
		jTablePubs.setModel(changeTAbleModel());
		constructTable();
		jTablePubs.updateUI();
	}

	private void constructTable() {
		box = new JCheckBox();
		box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(box.isSelected())
					add();
				else
					remove();
			}
		});
		JTable jtable = jTablePubs.getMainTable();
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(1).setMaxWidth(PreferencesSizeComponents.pmidotherIDfieldmaxWith);
		jtable.getColumnModel().getColumn(1).setMinWidth(PreferencesSizeComponents.pmidotherIDfieldminWith);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(PreferencesSizeComponents.pmidotherIDfieldpreferredWith);
		jtable.getColumnModel().getColumn(3).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(3).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(box));
		Map<Integer, Comparator<?>> columnComparators = new HashMap<Integer, Comparator<?>>();
		columnComparators.put(2, new AlphanumericComparator());
		TableRowSorter<TableModel> sortedModel = new TableRowSorterWhitOtherTypes(columnComparators);
		sortedModel.setModel(jTablePubs.getModel());
		jTablePubs.getMainTable().setRowSorter(sortedModel);
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for (int j = 0; j < 3; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}

	protected void add() {
		int queryID = ((Query)jListQueries.getSelectedValue()).getID();
		int selectedPubID = (Integer) jTablePubs.getValueAt(jTablePubs.getSelectedRowsInOriginalModel()[0],0);
		queryIDSelectedPublictaionSet.get(queryID).add(selectedPubID);
		queryIDRelevance.get(queryID).add(selectedPubID);
	}

	protected void remove() {
		int queryID = ((Query)jListQueries.getSelectedValue()).getID();
		int selectedPubID = (Integer) jTablePubs.getValueAt(jTablePubs.getSelectedRowsInOriginalModel()[0],0);
		queryIDSelectedPublictaionSet.get(queryID).remove(selectedPubID);
		queryIDRelevance.get(queryID).remove(selectedPubID);
	}
	
	/**
	 * Change model when select other query
	 */
	private TableModel changeTAbleModel(){
		
		String[] columns = new String[] {"ID", "PMID/OtherID","Title",""};
		DefaultTableModel tableModel = new DefaultTableModel(){
			public boolean isCellEditable(int x,int y){
				if(y==3)
					return true;
				return false;
			}

			public Class<?> getColumnClass(int column){
				if(column == 3)
					return Boolean.class;
				return String.class;
			}
		};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;
		int queryID = ((Query)jListQueries.getSelectedValue()).getID();	
		data = new Object[queryPublicationRelevance.get(queryID).size()][4];		
		for(Integer pubID: queryPublicationRelevance.get(queryID).keySet())
		{
			IPublication pub = queryPublicationRelevance.get(queryID).get(pubID).getX();
			data[i][0] = pub.getID();
			data[i][1] = pub.getOtherID();
			data[i][2] = pub.getTitle();
			if(!queryIDRelevance.get(queryID).contains(pubID))
			{
				data[i][3] = new Boolean(false);
			}
			else if(jRadioButtonAbstractOrFullTExt.isSelected())
			{
				data[i][3] = new Boolean(true);
			}
			else if(!queryIDSelectedPublictaionSet.get(queryID).contains(pubID))
			{
				data[i][3] = new Boolean(false);
			}
			else
			{
				applySetting(data, i,pub);
			}
			if((Boolean)data[i][3])
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID());
			}
			else
			{
				queryIDSelectedPublictaionSet.get(queryID).remove(pub.getID());
			}
			tableModel.addRow(data[i]);
			i++;
		}
		return tableModel;		
	}
	
	/**
	 * Change TAble Model Actual
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	private void alterModel() throws SQLException, DatabaseLoadDriverException
	{
		int selectqueryID = ((Query)jListQueries.getSelectedValue()).getID();
		if(selectqueryID!=-1)
		{
			int rowSize = jTablePubs.getModel().getRowCount();
			for(int i=0;i<rowSize;i++)
			{
				int pubID = (Integer) jTablePubs.getValueAt(i, 0);
				if(!queryIDRelevance.get(selectqueryID).contains(pubID))
				{
					jTablePubs.getModel().setValueAt(false, i, 3);
				}
				else if(!queryIDSelectedPublictaionSet.get(selectqueryID).contains(pubID))
				{
					jTablePubs.getModel().setValueAt(false, i, 3);
				}
				else if(jRadioButtonAbstracts.isSelected())
				{
					IPublication pub = queryPublicationRelevance.get(selectqueryID).get(pubID).getX();
					if(pub.getAbstractSection()==null)
					{
						jTablePubs.getModel().setValueAt(pub.getAbstractSection().equals(""), i, 3);
					}
					else
					{
						jTablePubs.getModel().setValueAt(!pub.getAbstractSection().equals(""), i, 3);
					}	
				}
				else if(jRadioButtonFullText.isSelected())
				{
					IPublication pub = queryPublicationRelevance.get(selectqueryID).get(pubID).getX();
					if(pub.isPDFAvailable())
					{
						jTablePubs.getModel().setValueAt(true, i, 3);
					}
					else
					{
						String text = pub.getFullTextFromDatabase();
						if(text == null || text.length()<1)
						{
							jTablePubs.getModel().setValueAt(false, i, 3);
						}
						else
						{
							jTablePubs.getModel().setValueAt(true, i, 3);
						}
					}
				}
				else if(jRadioButtonAbstractOrFullTExt.isSelected())
				{
					IPublication pub = queryPublicationRelevance.get(selectqueryID).get(pubID).getX();
					String text = pub.getFullTextFromDatabase();
					if(pub.getAbstractSection()!=null)
					{
						jTablePubs.getModel().setValueAt(true, i, 3);
					}
					else if(pub.isPDFAvailable())
					{
						jTablePubs.getModel().setValueAt(true, i, 3);
					}
					else if(text != null && text.length()>0)
					{
						jTablePubs.getModel().setValueAt(true, i, 3);
					}
					else
					{
						jTablePubs.getModel().setValueAt(false, i, 3);
					}
					
				}
			}
		}
	}

	private void applySetting(Object[][] data, int i,IPublication pub) {
		
		if(jRadioButtonAbstracts.isSelected())
		{
			if(pub.getAbstractSection()==null)
			{
				data[i][3] = new Boolean(false);
			}
			else
			{
				data[i][3] = new Boolean(pub.getAbstractSection().equals(""));
			}
		}
		else if(jRadioButtonFullText.isSelected())
		{
			if(pub.isPDFAvailable())
			{
				data[i][3] = new Boolean(true);
			}
			else
			{
				data[i][3] = new Boolean(false);
			}
		}
		data[i][3] = new Boolean(true);
	}
	
	
	
	public Map<Integer,GenericPair<IPublication,String>> getResultsTable(String order,int queryID) throws SQLException, DatabaseLoadDriverException {

		Map<Integer,GenericPair<IPublication,String>> queryPubsREl = new HashMap<Integer,GenericPair<IPublication,String>>();
		String stat = QueriesPublication.selectAllPublicationQueryInf;
		stat += order;
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(stat);    
		ps.setInt(1,queryID);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			IPublication pub = new Publication(
					rs.getInt(1),
					rs.getString(2),//int pmid
					rs.getString(3),//String title
					rs.getString(4),//String authors
					rs.getString(5),//int date
					rs.getString(6),//String status
					rs.getString(7),//String journal
					rs.getString(8),//String volume
					rs.getString(9),//String issue
					rs.getString(10),//String pages
					rs.getString(11), // String externalID
					rs.getString(12),//String aBstract
					rs.getBoolean(13)
			);		
			String relevance = rs.getString(14);
			if(relevance==null)
			{
				relevance="na";
			}
			queryPubsREl.put(pub.getID(),new GenericPair<IPublication, String>(pub,relevance));
		}
		rs.close();
		ps.close();
		return queryPubsREl;
	}
	
	private  DefaultComboBoxModel fillModelQuerysTable(){
		DefaultComboBoxModel list = new DefaultComboBoxModel();
		for(Query res: queries)
		{
			list.addElement(res);
		}
		return list;
	}

	private void changeAllPubsTExtMode() throws SQLException, DatabaseLoadDriverException {	
		for(Integer queryID:queryPublicationRelevance.keySet())
		{
			Map<Integer,GenericPair<IPublication,String>> pubIDMap = queryPublicationRelevance.get(queryID);
			if(jRadioButtonAbstracts.isSelected())
			{
				changeQueryAbstract(queryID,pubIDMap);
			}
			else if(jRadioButtonFullText.isSelected())
			{
				changeQueryFullText(queryID,pubIDMap);
			}
			else if(jRadioButtonAbstractOrFullTExt.isSelected())
			{
				changeQueryFullTextAndAbstract(queryID,pubIDMap);
			}
		}
	}

	private void changeQueryFullTextAndAbstract(Integer queryID,Map<Integer, GenericPair<IPublication, String>> pubIDMap) throws SQLException, DatabaseLoadDriverException {
		for(Integer pubID:pubIDMap.keySet())
		{
			IPublication pub = pubIDMap.get(pubID).getX();	
			String text = pub.getFullTextFromDatabase();
			if(pub.isPDFAvailable())		
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID()); 
			}
			else if(text != null && text.length()>0)
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID());
			}
			else if(pub==null||pub.getAbstractSection()==null||pub.getAbstractSection().equals(""))
			{
				queryIDSelectedPublictaionSet.get(queryID).remove(pub.getID()); 
			}
			else
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID()); 
			}
		}
		
	}

	private void changeQueryFullText(Integer queryID,Map<Integer, GenericPair<IPublication, String>> pubIDMap) throws SQLException, DatabaseLoadDriverException {
		for(Integer pubID:pubIDMap.keySet())
		{
			IPublication pub = pubIDMap.get(pubID).getX();	
			if(pub.isPDFAvailable())
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID()); 
			}
			else
			{
				String text = pub.getFullTextFromDatabase();
				if(text == null || text.length()<1)
				{
					queryIDSelectedPublictaionSet.get(queryID).remove(pub.getID()); 
				}
				else
				{
					queryIDSelectedPublictaionSet.get(queryID).add(pub.getID()); 
				}
			}
		}
	}

	private void changeQueryAbstract(int queryID,Map<Integer, GenericPair<IPublication, String>> pubIDMap) {
	
		for(Integer pubID:pubIDMap.keySet())
		{
			IPublication pub = pubIDMap.get(pubID).getX();
			if(pub==null||pub.getAbstractSection()==null||pub.getAbstractSection().equals(""))
			{
				queryIDSelectedPublictaionSet.get(queryID).remove(pub.getID()); 
			}
			else
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID()); 
			}
		}	
	}

	public void done() {
		
		Set<Integer> ids = new HashSet<Integer>();
		Set<Integer> queries = new HashSet<Integer>();
		for(Integer queryID:queryIDSelectedPublictaionSet.keySet())
		{
			Set<Integer> queryIds = queryIDSelectedPublictaionSet.get(queryID);
			for(Integer id:queryIds)
			{
				if(queryIDRelevance.get(queryID).contains(id))
				{
					ids.add(id);
					if(!queries.contains(queryID))
						queries.add(queryID);
				}
			}
		}
		if(ids.size()==0)
		{
			Workbench.getInstance().warn("Please joice at least one publication");
		}
		else
		{
			List<Object> param = getParam();
			param.add(ids);
			Properties prop = new Properties();
			if(jRadioButtonFullText.isSelected())
			{
				prop.put(GlobalNames.textType,GlobalNames.fullText);
			}
			else if(jRadioButtonAbstracts.isSelected())
			{
				prop.put(GlobalNames.textType,GlobalNames.abstracts);
			}
			else
			{
				prop.put(GlobalNames.textType,GlobalNames.abstractOrFullText);
			}
			String sourceQueries = new String();
			for(int idQuery :queries)
			{
				sourceQueries = sourceQueries + idQuery+" ,";
			}
			sourceQueries = sourceQueries.substring(0,sourceQueries.length()-2);
			prop.put(GlobalNames.sourceQueries,sourceQueries);
			param.add(prop);
			String description = getParam().get(0).toString();
			List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);
			Corpora project = (Corpora) items.get(0).getUserData();
			
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("project", Corpora.class,project, null),
					new ParamSpec("name", String.class,description, null),
					new ParamSpec("prop", Properties.class,prop, null),
					new ParamSpec("ruleClass", Set.class,ids, null)
			};
			
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.initcreatecorpuspublicationmanager")){			
					Workbench.getInstance().executeOperation(def, paramsSpec);
					closeView();
					return;
				}
			}
		}
	}

	public void goBack() {
		List<Object> pama = getParam();
		closeView();
		new CreateCorpusWizard2(pama);
	}

	public void goNext() {
	
		
	}
	
	private JPanel getJPanelQueries() {
		if(jPanelQueries == null) {
			jPanelQueries = new JPanel();
			jPanelQueries.setBorder(BorderFactory.createTitledBorder("Queries Selection"));
			GridBagLayout jPanelQueriesLayout = new GridBagLayout();
			jPanelQueriesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelQueriesLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelQueriesLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelQueriesLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelQueries.setLayout(jPanelQueriesLayout);
			jPanelQueries.add(getJScrollPaneQueries(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		}
		return jPanelQueries;
	}
	
	private JScrollPane getJScrollPaneQueries() {
		if(jScrollPaneQueries == null) {
			jScrollPaneQueries = new JScrollPane();
			jScrollPaneQueries.setViewportView(getJListQueries());
		}
		return jScrollPaneQueries;
	}

	private JList getJListQueries() {
		if(jListQueries == null) {
			ListModel jList1Model = fillModelQuerysTable();			
			jListQueries = new JList();
			jListQueries.setFocusable(true);
			jListQueries.setModel(jList1Model);
			jListQueries.addMouseListener(new MouseListener() {			
				public void mouseClicked(MouseEvent arg0) {
					changeQuery();
				}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
			});
			jListQueries.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {}
				public void keyPressed(KeyEvent e) {
					changeQuery();
				}
			});
		}
		return jListQueries;
	}
	
	private JPanel getJPanelPubsAndOptions() {
		if(jPanelPubsAndOptions == null) {
			jPanelPubsAndOptions = new JPanel();
			jPanelPubsAndOptions.setBorder(BorderFactory.createTitledBorder("Publications"));
			GridBagLayout jPanelPubsAndOptionsLayout = new GridBagLayout();
			jPanelPubsAndOptionsLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.0};
			jPanelPubsAndOptionsLayout.rowHeights = new int[] {20, 7, 7, 7, 20};
			jPanelPubsAndOptionsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPubsAndOptionsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPubsAndOptions.setLayout(jPanelPubsAndOptionsLayout);
			jPanelPubsAndOptions.add(getJTablePubs(), new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPubsAndOptions.add(getJTabbedPane1(), new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPubsAndOptions.add(getJButtonSelectAllAndRemoveALL(), new GridBagConstraints(3, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPubsAndOptions.add(getJButtonRemoveAll(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelPubsAndOptions;
	}
	
	private TableSearchPanel getJTablePubs() {
		if(jTablePubs == null) {
			TableModel jTablePubsModel = new DefaultTableModel(){
				public boolean isCellEditable(int x,int y){
					if(y==3)
						return true;
					return false;
				}

				public Class<?> getColumnClass(int column){
					if(column == 3)
						return Boolean.class;
					return String.class;
				}
			};
			jTablePubs = new TableSearchPanel();
			jTablePubs.setModel(jTablePubsModel);
		}
		return jTablePubs;
	}
	
	private JPanel getJPanelRelevanceOptionPAne() {
		if(jPanelRelevanceOptionPAne == null) {
			jPanelRelevanceOptionPAne = new JPanel();
			GridBagLayout jPanelRelevanceOptionPAneLayout = new GridBagLayout();
			jPanelRelevanceOptionPAneLayout.rowWeights = new double[] {0.1};
			jPanelRelevanceOptionPAneLayout.rowHeights = new int[] {7};
			jPanelRelevanceOptionPAneLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelRelevanceOptionPAneLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelRelevanceOptionPAne.setLayout(jPanelRelevanceOptionPAneLayout);
			jPanelRelevanceOptionPAne.add(getJRadioButtonAllRelevances(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelevanceOptionPAne.add(getJRadioButtonRelevant(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelevanceOptionPAne.add(getJRadioButton3(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelevanceOptionPAne.add(getJRadioButton4(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		}
		return jPanelRelevanceOptionPAne;
	}
	
	private JPanel getJPanelCorpusProperties() {
		if(jPanelCorpusProperties == null) {
			jPanelCorpusProperties = new JPanel();
			GridBagLayout jPanelCorpusPropertiesLayout = new GridBagLayout();
			jPanelCorpusPropertiesLayout.rowWeights = new double[] {0.1};
			jPanelCorpusPropertiesLayout.rowHeights = new int[] {7};
			jPanelCorpusPropertiesLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			jPanelCorpusPropertiesLayout.columnWidths = new int[] {7, 7, 7};
			jPanelCorpusProperties.setLayout(jPanelCorpusPropertiesLayout);
			jPanelCorpusProperties.add(getJRadioButtonAbstracts(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCorpusProperties.add(getJRadioButtonFullText(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCorpusProperties.add(getJRadioButtonAbstractOrFullTExt(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		}
		return jPanelCorpusProperties;
	}
	
	private JRadioButton getJRadioButtonAllRelevances() {
		if(jRadioButtonAll == null) {
			jRadioButtonAll = new JRadioButton();
			jRadioButtonAll.setText("All");
			jRadioButtonAll.setSelected(true);
			jRadioButtonAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						changeRelevanceAll();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});	
		}
		return jRadioButtonAll;
	}
	
	protected void changeRelevanceAll() throws SQLException, DatabaseLoadDriverException {	
		if(jRadioButtonAll.isSelected())
		{
			queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
			jRadioButtonRelevant.setSelected(false);
			jRadioButtonRelated.setSelected(false);
			jRadioButtonIrrelevant.setSelected(false);
			alterModel();
			jTablePubs.updateUI();
		}
		else
		{
			jRadioButtonAll.setSelected(true);
		}		
	}

	private JRadioButton getJRadioButtonRelevant() {
		if(jRadioButtonRelevant == null) {
			jRadioButtonRelevant = new JRadioButton();
			jRadioButtonRelevant.setText("Relevant");
			jRadioButtonRelevant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						addRelevanceRelevant();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});	
		}
		return jRadioButtonRelevant;
	}
	
	
	
	protected void addRelevanceRelevant() throws SQLException, DatabaseLoadDriverException {
		if(jRadioButtonRelevant.isSelected())
		{
			if(jRadioButtonAll.isSelected())
			{
				cleanRelevance();
				jRadioButtonAll.setSelected(false);
			}
			addrelevanceIDs("relevant");
		}
		else
		{
			remrelevenceIDs("relevant");
			if(jRadioButtonIrrelevant.isSelected()||jRadioButtonRelated.isSelected())
			{
				
			}
			else
			{
				jRadioButtonAll.setSelected(true);
				queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
			}
		}
		alterModel();
		jTablePubs.updateUI();
	}

	private void remrelevenceIDs(String string) {
		for(Integer queryID:queryPublicationRelevance.keySet())
		{
			Set<Integer> ids = getRelevanceIDs(queryID,string);
			queryIDRelevance.get(queryID).removeAll(ids);
		}	
	}



	private void addrelevanceIDs(String string) {
		for(Integer queryID:queryPublicationRelevance.keySet())
		{
			Set<Integer> ids = getRelevanceIDs(queryID,string);
			queryIDRelevance.get(queryID).addAll(ids);
		}	
	}
	
	private void cleanRelevance()
	{
		for(Integer queryID:queryPublicationRelevance.keySet())
		{
			queryIDRelevance.put(queryID, new HashSet<Integer>());
		}	
	}
	
	private Set<Integer> getRelevanceIDs(Integer queryID,String relevance) {
		Map<Integer,GenericPair<IPublication,String>> pubQuery = queryPublicationRelevance.get(queryID);
		Set<Integer> ids = new HashSet<Integer>();
		for(Integer pubID:pubQuery.keySet())
		{
			GenericPair<IPublication,String> gne = pubQuery.get(pubID);
			if(gne.getY().equals(relevance))
			{
				ids.add(gne.getX().getID());
			}
		}
		return ids;
	}

	private JRadioButton getJRadioButton3() {
		if(jRadioButtonRelated == null) {
			jRadioButtonRelated = new JRadioButton();
			jRadioButtonRelated.setText("Related");
			jRadioButtonRelated.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						addRelevanceRelated();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});	
		}
		return jRadioButtonRelated;
	}
	
	protected void addRelevanceRelated() throws SQLException, DatabaseLoadDriverException {
		if(jRadioButtonRelated.isSelected())
		{
			if(jRadioButtonAll.isSelected())
			{
				cleanRelevance();
				jRadioButtonAll.setSelected(false);
			}
			addrelevanceIDs("related");
		}
		else
		{
			remrelevenceIDs("related");
			if(jRadioButtonIrrelevant.isSelected()||jRadioButtonRelevant.isSelected())
			{
				
			}
			else
			{
				jRadioButtonAll.setSelected(true);
				queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
			}
		}
		alterModel();
		jTablePubs.updateUI();	
	}

	private JRadioButton getJRadioButton4() {
		if(jRadioButtonIrrelevant == null) {
			jRadioButtonIrrelevant = new JRadioButton();
			jRadioButtonIrrelevant.setText("Irrelevant");
			jRadioButtonIrrelevant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						addRelevanceIrrelevant();
					} catch (SQLException e) {						
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});	
		}
		return jRadioButtonIrrelevant;
	}
	
	
	
	protected void addRelevanceIrrelevant() throws SQLException, DatabaseLoadDriverException {
		if(jRadioButtonIrrelevant.isSelected())
		{
			if(jRadioButtonAll.isSelected())
			{
				cleanRelevance();
				jRadioButtonAll.setSelected(false);
			}
			addrelevanceIDs("irrelevant");
		}
		else
		{
			remrelevenceIDs("irrelevant");
			if(jRadioButtonRelated.isSelected()||jRadioButtonRelevant.isSelected())
			{
				
			}
			else
			{
				jRadioButtonAll.setSelected(true);
				queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
			}
		}
		alterModel();
		jTablePubs.updateUI();	
	}

	/** START TEXT CONPONENT */
	
	private JRadioButton getJRadioButtonAbstracts() {
		if(jRadioButtonAbstracts == null) {
			jRadioButtonAbstracts = new JRadioButton();
			jRadioButtonAbstracts.setText("Abstracts");
			jRadioButtonAbstracts.setSelected(true);
			jRadioButtonAbstracts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						generalAbstract();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});	
		}
		return jRadioButtonAbstracts;
	}
	
	protected void generalAbstract() throws SQLException, DatabaseLoadDriverException {
		changeAllPubsTExtMode();
		alterModel();
		jTablePubs.updateUI();

	}
	
	private JRadioButton getJRadioButtonFullText() {
		if(jRadioButtonFullText == null) {
			jRadioButtonFullText = new JRadioButton();
			jRadioButtonFullText.setText("Full Text");
			jRadioButtonFullText.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						generalPdf();
					} catch (SQLException e) {					
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jRadioButtonFullText;
	}
	

	protected void generalPdf() throws SQLException, DatabaseLoadDriverException {
		changeAllPubsTExtMode();
		alterModel();
		jTablePubs.updateUI();

	}
	
	private JRadioButton getJRadioButtonAbstractOrFullTExt() {
		if(jRadioButtonAbstractOrFullTExt == null) {
			jRadioButtonAbstractOrFullTExt = new JRadioButton();
			jRadioButtonAbstractOrFullTExt.setText("Abstract OR Full Text");
			jRadioButtonAbstractOrFullTExt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						generalAllButton();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {					
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			
		}
		return jRadioButtonAbstractOrFullTExt;
	}
	
	protected void generalAllButton() throws SQLException, DatabaseLoadDriverException {
		changeAllPubsTExtMode();
		alterModel();
		jTablePubs.updateUI();

	}
	
	private ButtonGroup getButtonGroupContent() {
		if(buttonGroupContent == null) {
			buttonGroupContent = new ButtonGroup();
			buttonGroupContent.add(jRadioButtonFullText);
			buttonGroupContent.add(jRadioButtonAbstracts);
			buttonGroupContent.add(jRadioButtonAbstractOrFullTExt);
		}
		return buttonGroupContent;
	}

	public JComponent getMainComponent() {
		try {

		fillQueryPublications();
		if(upperPAnel == null)
		{
			upperPAnel = new JPanel();
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			upperPAnel.setLayout(thisLayout);
			upperPAnel.add(getJPanelQueries(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			upperPAnel.add(getJPanelPubsAndOptions(), new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		getButtonGroupContent();	
			changeAllPubsTExtMode();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		return upperPAnel;
	}
	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Create_Corpus_By_Publication_Manager#Select_Publications";
	}
	
	private JTabbedPane getJTabbedPane1() {
		if(jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.addTab("Text Type", null, getJPanelCorpusProperties(), null);
			jTabbedPane1.addTab("Relevance", null, getJPanelRelevanceOptionPAne(), null);
		}
		return jTabbedPane1;
	}
	
	private JButton getJButtonSelectAllAndRemoveALL() {
		if(jButtonSelectAllAndRemoveALL == null) {
			jButtonSelectAllAndRemoveALL = new JButton();
			jButtonSelectAllAndRemoveALL.setText("Select All");
			selelectAll = true;
			jButtonSelectAllAndRemoveALL.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					processAllButton();
				}
			});
		}
		return jButtonSelectAllAndRemoveALL;
	}

	protected void processAllButton() {
		for(int queryID : queryIDSelectedPublictaionSet.keySet())
		{
			queryIDSelectedPublictaionSet.put(queryID, queryIDAllpublictaionSet.get(queryID));
		}
		jRadioButtonAll.setSelected(true);
		jRadioButtonIrrelevant.setSelected(false);
		jRadioButtonRelated.setSelected(false);
		jRadioButtonRelevant.setSelected(false);
		if(jTablePubs.getMainTable()!=null)
		{
			jTablePubs.setModel(changeTAbleModel());
			constructTable();
		}

	}
	
	private JButton getJButtonRemoveAll() {
		if(jButtonRemoveAll == null) {
			jButtonRemoveAll = new JButton();
			jButtonRemoveAll.setText("Remove All");
			jButtonRemoveAll.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					removeAllButton();			
				}
			});
		}
		return jButtonRemoveAll;
	}

	protected void removeAllButton() {
		
		for(int queryID : queryIDSelectedPublictaionSet.keySet())
		{
			queryIDSelectedPublictaionSet.put(queryID, new HashSet<Integer>());
		}
		jRadioButtonAll.setSelected(true);
		jRadioButtonIrrelevant.setSelected(false);
		jRadioButtonRelated.setSelected(false);
		jRadioButtonRelevant.setSelected(false);
		if(jTablePubs.getMainTable()!=null)
		{
			jTablePubs.setModel(changeTAbleModel());
			constructTable();
		}
	}


}
