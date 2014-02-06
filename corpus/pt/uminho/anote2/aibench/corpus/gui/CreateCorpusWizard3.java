package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.structures.QueryInfornationRetrieval;
import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class CreateCorpusWizard3 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JList jListQueries;
	private JTable jTablePubs;
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
	
	private IDatabase database;
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
	private List<QueryInfornationRetrieval> queries;
	/**
	 * Where relevance has saved
	 */
	private Map<Integer,Map<Integer,GenericPair<IPublication,String>>> queryPublicationRelevance;
	private JCheckBox box;

	public CreateCorpusWizard3(int sizeH, int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		this.setSize(800,600);
		getDatabase();
		fillQueryPublications();
		initGUI();
		this.setTitle("Select Articles 3/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
	}

	@SuppressWarnings("unchecked")
	private void fillQueryPublications() {
		queryIDAllpublictaionSet = new HashMap<Integer, Set<Integer>>();
		queryIDSelectedPublictaionSet = new HashMap<Integer, Set<Integer>>();
		queryPublicationRelevance = new HashMap<Integer, Map<Integer,GenericPair<IPublication,String>>>();
		List<Object> para = getParam();
		Object listQueries = para.get(1);
		queries = (List<QueryInfornationRetrieval>) listQueries;
		for(QueryInfornationRetrieval query:queries)
		{
			Set<Integer> pubIds = query.getIds(database);
			queryIDAllpublictaionSet.put(query.getID(), pubIds);
			queryIDSelectedPublictaionSet.put(query.getID(),pubIds);
			try {
				queryPublicationRelevance.put(query.getID(),getResultsTable("",query.getID()));
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NonExistingConnection e) {
				e.printStackTrace();
			}
		}
		queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
	}

	private void initGUI() {
		setEnableDoneButton(false);		
		GridBagLayout thisLayout = new GridBagLayout();
		this.setVisible(true);
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		this.add(getJPanelQueries(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJPanelPubsAndOptions(), new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJPaneButtons(), new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));			
	}
	
	private void getDatabase()
	{
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("DB-Host");
		elements.add("DB-Port");
		elements.add("DB-Schema");
		elements.add("DB-User");
		elements.add("DB-Pwd");
		ArrayList<String> data = Configuration.getElementByXMLFile("conf/settings.conf",elements);
		database = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
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
		
		jTablePubs.getColumnModel().getColumn(0).setMaxWidth(40);
		jTablePubs.getColumnModel().getColumn(0).setMinWidth(40);
		jTablePubs.getColumnModel().getColumn(0).setPreferredWidth(40);
		jTablePubs.getColumnModel().getColumn(1).setMaxWidth(75);
		jTablePubs.getColumnModel().getColumn(1).setMinWidth(75);
		jTablePubs.getColumnModel().getColumn(1).setPreferredWidth(75);
		jTablePubs.getColumnModel().getColumn(3).setMaxWidth(25);
		jTablePubs.getColumnModel().getColumn(3).setMinWidth(15);
		jTablePubs.getColumnModel().getColumn(3).setPreferredWidth(15);
		jTablePubs.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(box));
	}

	protected void add() {
		int queryID = ((QueryInfornationRetrieval)jListQueries.getSelectedValue()).getID();
		int selectedPubID = (Integer) jTablePubs.getValueAt(jTablePubs.getSelectedRow(),0);
		queryIDSelectedPublictaionSet.get(queryID).add(selectedPubID);
		queryIDRelevance.get(queryID).add(selectedPubID);
	}

	protected void remove() {
		int queryID = ((QueryInfornationRetrieval)jListQueries.getSelectedValue()).getID();
		int selectedPubID = (Integer) jTablePubs.getValueAt(jTablePubs.getSelectedRow(),0);
		queryIDSelectedPublictaionSet.get(queryID).remove(selectedPubID);
		queryIDRelevance.get(queryID).remove(selectedPubID);
	}
	
	/**
	 * Change model when select other query
	 */
	private TableModel changeTAbleModel(){
		
		String[] columns = new String[] {"Id", "Other ID","Title",""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;
		int queryID = ((QueryInfornationRetrieval)jListQueries.getSelectedValue()).getID();	
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
	 */
	private void alterModel()
	{
		int selectqueryID = ((QueryInfornationRetrieval)jListQueries.getSelectedValue()).getID();
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
				else if(jRadioButtonAbstractOrFullTExt.isSelected())
				{	
					jTablePubs.getModel().setValueAt(true, i, 3);
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
					String id_path = Corpora.saveDocs+"id"+pub.getID()+".pdf";
					String id_path_otherID = Corpora.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf";
					String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
					if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
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
			data[i][3] = new Boolean(pub.getAbstractSection().equals(""));
		}
		else if(jRadioButtonFullText.isSelected())
		{
			String id_path = Corpora.saveDocs+"id"+pub.getID()+".pdf";
			String id_path_otherID = Corpora.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf";
			String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
			if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
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
	
	
	
	public Map<Integer,GenericPair<IPublication,String>> getResultsTable(String order,int queryID) throws SQLException, NonExistingConnection {

		Map<Integer,GenericPair<IPublication,String>> queryPubsREl = new HashMap<Integer,GenericPair<IPublication,String>>();
		String stat = QueriesPublication.selectAllPublicationQueryInfo;
		stat += order;
		PreparedStatement ps = (PreparedStatement) database.getConnection().prepareStatement(stat);    
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
					rs.getString(11),
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
		return queryPubsREl;
	}
	
	private  DefaultComboBoxModel fillModelQuerysTable(){
		DefaultComboBoxModel list = new DefaultComboBoxModel();
		for(QueryInfornationRetrieval res: queries)
		{
			list.addElement(res);
		}
		return list;
	}

	private void changeAllPubsTExtMode() {	
		for(Integer queryID:queryPublicationRelevance.keySet())
		{
			Map<Integer,GenericPair<IPublication,String>> pubIDMap = queryPublicationRelevance.get(queryID);
			if(jRadioButtonAbstractOrFullTExt.isSelected())
			{
				queryIDSelectedPublictaionSet.get(queryID).addAll(queryIDAllpublictaionSet.get(queryID)); 
			}
			else if(jRadioButtonAbstracts.isSelected())
			{
				changeQueryAbstract(queryID,pubIDMap);
			}
			else if(jRadioButtonFullText.isSelected())
			{
				changeQueryFullText(queryID,pubIDMap);
			}
		}
	}

	private void changeQueryFullText(Integer queryID,Map<Integer, GenericPair<IPublication, String>> pubIDMap) {
		for(Integer pubID:pubIDMap.keySet())
		{
			IPublication pub = pubIDMap.get(pubID).getX();	
			String id_path = Corpora.saveDocs+"id"+pub.getID()+".pdf";
			String id_path_otherID = Corpora.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf";
			String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
			if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
			{
				queryIDSelectedPublictaionSet.get(queryID).add(pub.getID()); 
			}
			else
			{
				queryIDSelectedPublictaionSet.get(queryID).remove(pub.getID()); 
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

	public void done() {}

	public void goBack() {
		List<Object> pama = getParam();
		pama.remove(1);
		new CreateCorpusWizard2(600,400,pama);
		this.setVisible(false);
		this.dispose();
		this.setModal(false);
	}

	public void goNext() {
	
		Set<Integer> ids = new HashSet<Integer>();
		for(Integer queryID:queryIDSelectedPublictaionSet.keySet())
		{
			Set<Integer> queryIds = queryIDSelectedPublictaionSet.get(queryID);
			for(Integer id:queryIds)
			{
				if(queryIDRelevance.get(queryID).contains(id))
				{
					ids.add(id);
				}
			}
		}
		if(ids.size()==0)
		{
			Workbench.getInstance().warn("Please joice at least one publication");
		}
		else
		{
			this.setVisible(false);
			List<Object> param = getParam();
			param.add(ids);
			Properties prop = new Properties();
			if(jRadioButtonFullText.isSelected())
			{
				prop.put("textType","full text");
			}
			else if(jRadioButtonAbstracts.isSelected())
			{
				prop.put("textType","abstract");
			}
			else
			{
				prop.put("textType","abstract OR full text");
			}
			param.add(prop);
			new CreateCorpusWizard4(600,400,param);
			this.setVisible(false);
			this.dispose();
			this.setModal(false);
		}
	}
	
	private JPanel getJPanelQueries() {
		if(jPanelQueries == null) {
			jPanelQueries = new JPanel();
			jPanelQueries.setBorder(BorderFactory.createTitledBorder("Queries Selected"));
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
		}
		return jListQueries;
	}
	
	private JPanel getJPanelPubsAndOptions() {
		if(jPanelPubsAndOptions == null) {
			jPanelPubsAndOptions = new JPanel();
			jPanelPubsAndOptions.setBorder(BorderFactory.createTitledBorder("Publications"));
			GridBagLayout jPanelPubsAndOptionsLayout = new GridBagLayout();
			jPanelPubsAndOptionsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0, 0.0};
			jPanelPubsAndOptionsLayout.rowHeights = new int[] {7, 7, 7, 20, 7};
			jPanelPubsAndOptionsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPubsAndOptionsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPubsAndOptions.setLayout(jPanelPubsAndOptionsLayout);
			jPanelPubsAndOptions.add(getJScrollPanePubs(), new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPubsAndOptions.add(getJPanelRelevanceOptionPAne(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPubsAndOptions.add(getJPanelCorpusProperties(), new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelPubsAndOptions;
	}
	
	private JScrollPane getJScrollPanePubs() {
		if(jScrollPanePubs == null) {
			jScrollPanePubs = new JScrollPane();
			jScrollPanePubs.setViewportView(getJTablePubs());
		}
		return jScrollPanePubs;
	}
	
	private JTable getJTablePubs() {
		if(jTablePubs == null) {
			TableModel jTablePubsModel = new DefaultTableModel();
			jTablePubs = new JTable(){

				private static final long serialVersionUID = -4090662450740771673L;

				@Override
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
			jTablePubs.setModel(jTablePubsModel);
		}
		return jTablePubs;
	}
	
	private JPanel getJPanelRelevanceOptionPAne() {
		if(jPanelRelevanceOptionPAne == null) {
			jPanelRelevanceOptionPAne = new JPanel();
			GridBagLayout jPanelRelevanceOptionPAneLayout = new GridBagLayout();
			jPanelRelevanceOptionPAne.setBorder(BorderFactory.createTitledBorder("Relevance Options"));
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
			jPanelCorpusProperties.setBorder(BorderFactory.createTitledBorder("Content Options"));
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
					changeRelevanceAll();
				}
			});	
		}
		return jRadioButtonAll;
	}
	
	protected void changeRelevanceAll() {	
		if(jRadioButtonAll.isSelected())
		{
			queryIDRelevance = new HashMap<Integer, Set<Integer>>(queryIDAllpublictaionSet);
			jRadioButtonRelevant.setSelected(false);
			jRadioButtonRelated.setSelected(false);
			jRadioButtonIrrelevant.setSelected(false);
			alterModel();
			try{
				jTablePubs.updateUI();
			} catch (Exception e) {
			}	
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
					addRelevanceRelevant();
				}
			});	
		}
		return jRadioButtonRelevant;
	}
	
	
	
	protected void addRelevanceRelevant() {
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
					addRelevanceRelated();
				}
			});	
		}
		return jRadioButtonRelated;
	}
	
	protected void addRelevanceRelated() {
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
		try{
			jTablePubs.updateUI();
		} catch (Exception e) {
		}	
	}

	private JRadioButton getJRadioButton4() {
		if(jRadioButtonIrrelevant == null) {
			jRadioButtonIrrelevant = new JRadioButton();
			jRadioButtonIrrelevant.setText("Irrelevant");
			jRadioButtonIrrelevant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					addRelevanceIrrelevant();
				}
			});	
		}
		return jRadioButtonIrrelevant;
	}
	
	
	
	protected void addRelevanceIrrelevant() {
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
		try{
			jTablePubs.updateUI();
		} catch (Exception e) {
		}	
	}

	/** START TEXT CONPONENT */
	
	private JRadioButton getJRadioButtonAbstracts() {
		if(jRadioButtonAbstracts == null) {
			jRadioButtonAbstracts = new JRadioButton();
			jRadioButtonAbstracts.setText("Abstracts");
			jRadioButtonAbstracts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					generalAbstract();
				}
			});	
		}
		return jRadioButtonAbstracts;
	}
	
	/**
	 * 
	 * @return
	 */
	protected void generalAbstract() {
		if(jRadioButtonAbstracts.isSelected())
		{
			jRadioButtonAbstractOrFullTExt.setSelected(false);
			jRadioButtonFullText.setSelected(false);
		}
		else
		{
			jRadioButtonAbstracts.setSelected(true);
		}
		changeAllPubsTExtMode();
		alterModel();
		try{
			jTablePubs.updateUI();
		} catch (Exception e) {
		}
	}
	
	private JRadioButton getJRadioButtonFullText() {
		if(jRadioButtonFullText == null) {
			jRadioButtonFullText = new JRadioButton();
			jRadioButtonFullText.setText("Full Text");
			jRadioButtonFullText.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					generalPdf();
				}
			});
		}
		return jRadioButtonFullText;
	}
	

	protected void generalPdf() {
		if(jRadioButtonFullText.isSelected())
		{
			jRadioButtonAbstracts.setSelected(false);
			jRadioButtonAbstractOrFullTExt.setSelected(false);
		}
		else
		{
			jRadioButtonFullText.setSelected(true);
		}
		changeAllPubsTExtMode();
		alterModel();
		jTablePubs.updateUI();

	}
	
	private JRadioButton getJRadioButtonAbstractOrFullTExt() {
		if(jRadioButtonAbstractOrFullTExt == null) {
			jRadioButtonAbstractOrFullTExt = new JRadioButton();
			jRadioButtonAbstractOrFullTExt.setText("Abstract OR Full Text");
			jRadioButtonAbstractOrFullTExt.setSelected(true);
			jRadioButtonAbstractOrFullTExt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					generalAllButton();
				}
			});
			
		}
		return jRadioButtonAbstractOrFullTExt;
	}
	
	protected void generalAllButton() {
		if(jRadioButtonAbstractOrFullTExt.isSelected())
		{
			jRadioButtonAbstracts.setSelected(false);
			jRadioButtonFullText.setSelected(false);
		}
		else
		{
			jRadioButtonAbstractOrFullTExt.setSelected(true);
		}
		changeAllPubsTExtMode();
		alterModel();
		jTablePubs.updateUI();

	}

}
