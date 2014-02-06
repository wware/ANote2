package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.RelevanceChanged;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesDocument;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/

/**
 * 
 * AibenchGUI for JournalRetrivalListDocsOperation Operation
 * 
 * 
 */
public class JournalRetrievalGUI extends JDialog implements InputGUI{

	private static final long serialVersionUID = 5682081329569397494L;
	
	private JTable resultsTable;
	private JScrollPane jScrollPane1;
	private JLabel jLabelIrrelevant;
	private JCheckBox jCheckBoxRelevante;
	private JCheckBox jCheckBoxAll;
	private JTextField irrelevantTextField;
	private JTextField relatedTextField;
	private JTextField relevantsTextField;
	private JLabel jLabelRelated;
	private JCheckBox jCheckBoxRelated;
	private JComboBox jComboBoxQuery;
	private JLabel jLabelOrder;
	private JCheckBox jCheckBoxIrrelevant;
	private JLabel jLabelRelevant;
	private JComboBox searchComboBox;
	private JPanel seachPanel;
	private JTextField searchTextField;

	private List<IPublication> pubs;
	private IDatabase db;
	private JTextField jTextFieldTotal;
	private JTextField jTextFieldSelected;
	private JLabel jLabelTotal;
	private JLabel jLabelSellectDocs;
	private JButton jButtonJournalRetrieval;
	private JPanel jPanelJournalRetrieval;
	private QueryInformationRetrievalExtension query;
	private int selectedCount;
	private JPanel jPanelUpper;

	private JComboBox orderComboBox;

	private HashMap<Integer, String> pmid_relevance;

	private RelevanceChanged rel_changed;
	
	private Map<String,QueryInformationRetrievalExtension> queries;

	protected ParamsReceiver paramsRec = null;
	

	public JournalRetrievalGUI(){
		super(Workbench.getInstance().getMainFrame());
		{
			queries=new HashMap<String, QueryInformationRetrievalExtension>();
			getQueries();
			this.db=query.getPubManager().getDb();
			try {
				
				this.pubs = getPublicationByID("");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NonExistingConnection e) {
				e.printStackTrace();
			}
			initGUI();
			fillSelectedTextFieldTotal();
			fillTotalTextField();
		}
	}
	
	private void fillSelectedTextFieldTotal() {
		this.jTextFieldSelected.setText(String.valueOf(this.pubs.size()));	
	}
	
	public void getQueries()
	{
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class);
		if(items.size()==0)
		{
			Workbench.getInstance().warn("Don´t have Queries on Clipboard");
			return;
		}
		else
		{
			for(ClipboardItem item:items)
			{
				QueryInformationRetrievalExtension query = (QueryInformationRetrievalExtension) item.getUserData();
				queries.put("Query :"+query.getID(), query);
			}
			QueryInformationRetrievalExtension query1 = (QueryInformationRetrievalExtension) items.get(0).getUserData();
			this.query=query1;
		}
	}
	
	private void initGUI() {

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.0, 0.9, 0.05};
		thisLayout.rowHeights = new int[] {39, 526, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.0, 0.0, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 150, 156, 7};
		this.setLayout(thisLayout);
		this.setSize(837, 700);
		{
			jScrollPane1 = new JScrollPane();
			this.add(jScrollPane1, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
			jScrollPane1.setFocusTraversalKeysEnabled(false);
			jScrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Information", TitledBorder.LEADING, TitledBorder.TOP));
			resultsTable = new JTable(){

				private static final long serialVersionUID = -2422590849569066990L;

				@Override
				public boolean isCellEditable(int x,int y){
					if(y==0||y==5)
						return true;
					return false;
				}

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public Class getColumnClass(int column){
					if(column == 5)
						return Boolean.class;
					return String.class;
				}
			};


			TableModel pubTableModel = fillModelPubTable();

			jScrollPane1.setViewportView(resultsTable);

			resultsTable.setModel(pubTableModel);

			complementTable();

		}
		{
			seachPanel = new JPanel();
			GridBagLayout seachPanelLayout = new GridBagLayout();
			seachPanelLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
			seachPanelLayout.rowHeights = new int[] {22, 7, 0, 0, 28};
			seachPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.0};
			seachPanelLayout.columnWidths = new int[] {20, 100, 7};
			seachPanel.setLayout(seachPanelLayout);					
			seachPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Search", TitledBorder.LEADING, TitledBorder.TOP));
			{
				searchTextField = new JTextField();
				seachPanel.add(searchTextField, new GridBagConstraints(0, 3, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				searchTextField
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED),
						null));
				searchTextField.setPreferredSize(new java.awt.Dimension(54, 17));
				searchTextField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent evt) {
						searchInTable(evt);
					}
				});
			}
			{
				ComboBoxModel searchComboBoxModel = new DefaultComboBoxModel(
						new String[] { "Title", "Authors", "Date", "All" });
				searchComboBox = new JComboBox();
				seachPanel.add(searchComboBox, new GridBagConstraints(2, 4, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				searchComboBox.setModel(searchComboBoxModel);
			}
			{
				jLabelOrder = new JLabel();
				seachPanel.add(jLabelOrder, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelOrder.setText("Sort :");
			}
			{
				orderComboBox = new JComboBox();
				ComboBoxModel orderComboBoxModel = new DefaultComboBoxModel(
						new String[] { "Title", "Authors", "Relevance Year" , "Relevance",  "Date"});
				orderComboBox = new JComboBox();
				orderComboBox.setModel(orderComboBoxModel);
				orderComboBox.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						int index = orderComboBox.getSelectedIndex();
						String order = " ORDER BY ";

						switch (index){
						case 0 : {
							order += "title ASC";
							break;
						}
						case 1 : {
							order += "authors ASC";
							break;
						}
						case 2 : {
							order += "date DESC ,relevance DESC";
							break;
						}
						case 3 : {
							order += "relevance DESC";
							break;
						}
						default: {
							order += "date ASC";
							break;
						}
						}


						try {
							pubs = getPublicationByID(order);
							TableModel pubTableModel = fillModelPubTable();	
							resultsTable.setModel(pubTableModel);	
							complementTable();
							setTableRenderer();
							refreshRelevanceFields();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NonExistingConnection e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}	
				});
			}
			seachPanel.add(orderComboBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			this.add(seachPanel, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			jPanelJournalRetrieval = new JPanel();
			GridBagLayout jPanelJournalRetrievalLayout = new GridBagLayout();
			this.add(jPanelJournalRetrieval, new GridBagConstraints(4, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelJournalRetrievalLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelJournalRetrievalLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelJournalRetrievalLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.1};
			jPanelJournalRetrievalLayout.columnWidths = new int[] {82, 45, 7, 7};
			jPanelJournalRetrieval.setLayout(jPanelJournalRetrievalLayout);
			jPanelJournalRetrieval.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Journal Retrieval", TitledBorder.LEADING, TitledBorder.TOP));

			{
				jButtonJournalRetrieval = new JButton();
				jPanelJournalRetrieval.add(jButtonJournalRetrieval, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButtonJournalRetrieval.setText("Journal Retrieval");
				jButtonJournalRetrieval.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/journalretrieval.png")));
				jButtonJournalRetrieval.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						pubmedRetrieval();
					}
				});
			}
			{
				jLabelSellectDocs = new JLabel();
				jPanelJournalRetrieval.add(jLabelSellectDocs, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelSellectDocs.setText("Selected :");
			}
			{
				jLabelTotal = new JLabel();
				jPanelJournalRetrieval.add(jLabelTotal, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelTotal.setText("Total :");
			}
			{
				jTextFieldSelected = new JTextField();
				jTextFieldSelected.setText(String.valueOf(pubs.size()));
				jPanelJournalRetrieval.add(jTextFieldSelected, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldTotal = new JTextField();
				jTextFieldTotal.setText(String.valueOf(pubs.size()));
				jPanelJournalRetrieval.add(jTextFieldTotal, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jLabelRelevant = new JLabel();
				jPanelJournalRetrieval.add(jLabelRelevant, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelRelevant.setText("Relevant :");
				jLabelRelevant.setOpaque(true);
				jLabelRelevant.setBackground(Color.RED);
			}
			{
				jLabelRelated = new JLabel();
				jPanelJournalRetrieval.add(jLabelRelated, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelRelated.setText("Related :");
				jLabelRelated.setOpaque(true);
				jLabelRelated.setBackground(Color.YELLOW);

			}
			{
				jLabelIrrelevant = new JLabel();
				jPanelJournalRetrieval.add(jLabelIrrelevant, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelIrrelevant.setText("Irrelevant :");
				jLabelIrrelevant.setOpaque(true);
				jLabelIrrelevant.setBackground(Color.GREEN);
			}
			{
				relevantsTextField = new JTextField();
				jPanelJournalRetrieval.add(relevantsTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				relatedTextField = new JTextField();
				jPanelJournalRetrieval.add(relatedTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				irrelevantTextField = new JTextField();
				jPanelJournalRetrieval.add(irrelevantTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			jPanelUpper = new JPanel();
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
			this.add(jPanelUpper, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperLayout.rowWeights = new double[] {0.1};
			jPanelUpperLayout.rowHeights = new int[] {7};
			jPanelUpperLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.0, 0.1};
			jPanelUpperLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 87, 105, 7};
			jPanelUpper.setLayout(jPanelUpperLayout);
			{
				jCheckBoxAll = new JCheckBox();
				jPanelUpper.add(jCheckBoxAll, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxAll.setText("All");
				jCheckBoxAll.setSelected(true);
				jCheckBoxAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						selectAll();
					}
				});


			}
			{
				jCheckBoxRelevante = new JCheckBox();
				jPanelUpper.add(jCheckBoxRelevante, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxRelevante.setText("Relevance");
				jCheckBoxRelevante.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						selectRelevant();
					}
				});
			}
			{
				jCheckBoxRelated = new JCheckBox();
				jPanelUpper.add(jCheckBoxRelated, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxRelated.setText("Related");
				jCheckBoxRelated.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						selectRelated();
					}
				});
			}
			{
				jCheckBoxIrrelevant = new JCheckBox();
				jPanelUpper.add(jCheckBoxIrrelevant, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxIrrelevant.setText("Irrelevant");
				jCheckBoxIrrelevant.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						selectIrrelevant();
					}
				});
			}
			{
				jComboBoxQuery = new JComboBox();
				jPanelUpper.add(jComboBoxQuery, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				ComboBoxModel jComboBoxQueryModel = getComboBoxModelQueries();
				jComboBoxQuery.setModel(jComboBoxQueryModel);
				jComboBoxQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						changeTable();
					}
				});
			}
		}
		
		
		try {
			initRelevanceHash();
		} catch (NonExistingConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshRelevanceFields();
		setTableRenderer();
	}
	
	protected void changeTable() {
		String sel = (String) jComboBoxQuery.getSelectedItem();
		query = this.queries.get(sel);
		this.db=query.getPubManager().getDb();
		try {
			
			this.pubs = getPublicationByID("");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NonExistingConnection e) {
			e.printStackTrace();
		}
		
		TableModel pubTableModel = fillModelPubTable();
		
		resultsTable.setModel(pubTableModel);
	
		complementTable();
		
		
		try {
			initRelevanceHash();
		} catch (NonExistingConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshRelevanceFields();
		setTableRenderer();
		selectAll();
		
	}

	private ComboBoxModel getComboBoxModelQueries()
	{
		String[] qs = new String[queries.size()];
		Iterator<String> it = queries.keySet().iterator();
		int i=0;
		while(it.hasNext())
		{
			qs[i] = it.next();
			i++;
		}
		ComboBoxModel orderComboBoxModel = new DefaultComboBoxModel(qs);
		if(HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class)!=null)
		{
			Object data = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
			QueryInformationRetrievalExtension query = (QueryInformationRetrievalExtension)data;
			orderComboBoxModel.setSelectedItem("Query :"+query.getID());
		}
		return orderComboBoxModel;
	}
		
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"","Title", "Authors", "Date"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		data = new Object[this.pubs.size()][4];
		for(IPublication pub: this.pubs)
		{
			data[i][0] = new Boolean(true);
			data[i][1] = pub.getTitle();
			data[i][2] = pub.getAuthors();
			data[i][3] = pub.getDate();
					
			tableModel.addRow(data[i]);
			
			i++;
		}
		
			
		return tableModel;		
	}
	
	private void fillSelectedTextField(){
		this.jTextFieldSelected.setText(String.valueOf(selectedCount));
	}
	
	private void fillTotalTextField(){
		this.jTextFieldTotal.setText(String.valueOf(this.pubs.size()));
	}
	
	@SuppressWarnings("static-access")
	public void searchInTable(KeyEvent evt){
		
		String text;
		ArrayList<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		
		if(searchTextField.getText().compareTo("")!=0 && evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
			text = searchTextField.getText();
		else
			text = searchTextField.getText()+evt.getKeyChar();
		
		int i=0;
		
		switch(this.searchComboBox.getSelectedIndex())
		{
			case 0:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getTitle().contains(text))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			case 1:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getAuthors().contains(text))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			case 2:
			{
				for(IPublication pub: this.pubs)
				{
					if(String.valueOf(pub.getDate()).contains(text))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			default:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getTitle().contains(text) || pub.getAuthors().contains(text) || String.valueOf(pub.getDate()).contains(text))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
				
		}
		
		int row = 0;
		for(Integer r: rows)
		{
			row = r.intValue();
			selectionModel.addSelectionInterval(row, row);
		}
		
		this.resultsTable.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.resultsTable.setSelectionModel(selectionModel);
		if(selectionModel.isSelectionEmpty()&& (this.searchTextField.getText().compareTo("")!=0))
		{
			this.searchTextField.setForeground(new java.awt.Color(255,0,0));
			searchTextField.setBackground(new java.awt.Color(174,174,174));
		}
		else
		{
			this.searchTextField.setForeground(Color.BLACK);
			this.searchTextField.setBackground(Color.WHITE);
		}
		
		this.resultsTable.scrollRectToVisible(this.resultsTable.getCellRect(row, 0, true));
		
	}
	
	private void complementTable(){
		
		final JCheckBox box = new JCheckBox();
		box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(box.isSelected())
					selectedCount++;
				else
					selectedCount--;
				fillSelectedTextField();
			}
		});
		
		resultsTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(box));
		resultsTable.getColumnModel().getColumn(0).setMaxWidth(20);
		resultsTable.getColumnModel().getColumn(0).setMinWidth(20);
		resultsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		
		resultsTable.getColumnModel().getColumn(3).setMaxWidth(50);
		resultsTable.getColumnModel().getColumn(3).setMinWidth(50);
		resultsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		
		resultsTable.getColumnModel().getColumn(2).setMaxWidth(180);
		resultsTable.getColumnModel().getColumn(2).setMinWidth(180);
		resultsTable.getColumnModel().getColumn(2).setPreferredWidth(180);
		
		resultsTable.setRowHeight(20);
																	
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("");
		viewColumn.setMinWidth(25);
		viewColumn.setMaxWidth(25);
		viewColumn.setPreferredWidth(25);
		viewColumn.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				final int selectedRow = row;
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/find.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						initViewGUI();
	                      resultsTable.getSelectionModel().clearSelection();
	                      resultsTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
					}
				});
				return viewButton;
			}
				
		});
		
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/find.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						initViewGUI();
					}
				});
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
		
		resultsTable.setSelectionForeground(Color.BLUE);
		
		resultsTable.addColumn(viewColumn);

		
		TableColumn viewColumn2 = new TableColumn();
		viewColumn2.setHeaderValue("Pdf");
		viewColumn2.setMinWidth(30);
		viewColumn2.setMaxWidth(30);
		viewColumn2.setPreferredWidth(30);
		viewColumn2.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				final int selectedRow = row;
				JButton viewButton = new JButton();
				String pmid = pubs.get(row).getOtherID();
				String id = PublicationManager.saveDocs+"id"+String.valueOf(pubs.get(row).getID())+".pdf";
				String pdf_path = PublicationManager.saveDocs+pmid+".pdf";
				if(new File(pdf_path).exists()||new File(id).exists())
				{
					viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));

				}
				else
				{		
//					String url = pubs.get(row).getUrl();
//					if(url!=null&&url.equals("na"))
//					{
//						viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/sentidoProibido.png")));
//					}
				}
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						initViewGUI();
	                      resultsTable.getSelectionModel().clearSelection();
	                      resultsTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
					}
				});
				return viewButton;
			}
				
		});
		
		viewColumn2.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						initViewGUI();
					}
				});
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
				return false;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return false;
			}

			public boolean stopCellEditing() {
				return true;
			}
				
		});
		resultsTable.addColumn(viewColumn2);
		resultsTable.getColumnModel().moveColumn(4, 0);
		resultsTable.getColumnModel().moveColumn(1, 5);
		//resultsTable.getColumnModel().moveColumn(1, 5);

		
		resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {  

			private static final long serialVersionUID = -8183629931912150659L;

			public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {  
		         super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
		         if (row % 2 == 0) {  
		        	 setBackground(Color.decode("#F0F0F0"));  
		         } else {  
		        	 setBackground(null);  
		         }  
		         return this;  
		    }  
		}); 
	}
	
	public ArrayList<IPublication> getPublicationByID(String order) throws SQLException, NonExistingConnection {

		db.openConnection();
		
		ArrayList<IPublication> pubs = new ArrayList<IPublication>();
		
		if(db.getConnection()==null)
			return pubs;

		String stat = QueriesPublication.selectAllPublicationQueryInfo;
		stat += order;

		PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement(stat);    
		ps.setInt(1,query.getID());

		ResultSet rs = ps.executeQuery();

		while(rs.next())
		{
			pubs.add(new Publication(
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
					));
		} 
		//db.closeConnection();

		return pubs;
	}
	
	private void refreshRelevanceFields(){
		int a=0, b=0, c=0;
		
		for(String r : pmid_relevance.values())
		{
			if(r!=null)
			{
				if(r.equals("irrelevant"))
					a++;
				else if(r.equals("related"))
					b++;
				else // relevant
					c++;
			}
		}
		
		this.irrelevantTextField.setText(String.valueOf(a));
		this.relatedTextField.setText(String.valueOf(b));
		this.relevantsTextField.setText(String.valueOf(c));
	}
	
	private void initRelevanceHash() throws NonExistingConnection{
	
		pmid_relevance = new HashMap<Integer, String>();
		db.openConnection();
		PreparedStatement add_pub_stat;
		try {
			add_pub_stat = db.getConnection().prepareStatement(QueriesDocument.selectPublicationRelevanceForQuery);
			add_pub_stat.setInt(1,this.query.getID());
			ResultSet rs = add_pub_stat.executeQuery();	
			while(rs.next())
					this.pmid_relevance.put(rs.getInt(1), rs.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}
	
	private void initViewGUI(){
		final IPublication publication = this.pubs.get(this.resultsTable.getSelectedRow());
		final String prev_relevance = this.pmid_relevance.get(publication.getID());
		rel_changed = new RelevanceChanged(this.query.getID());
		
		(new PublicationResumeGUI(this.query,publication, this.rel_changed)).addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent arg0) {}
			
			public void windowClosing(WindowEvent arg0) {}
			
			public void windowDeactivated(WindowEvent arg0) {}
			
			public void windowDeiconified(WindowEvent arg0) {}
			
			public void windowIconified(WindowEvent arg0) {}
			
			public void windowOpened(WindowEvent arg0) {}
			
			public void windowClosed(WindowEvent arg0) {
				if(rel_changed.getNew_relevance()!=null)
				{
					if(prev_relevance==null || !prev_relevance.equals(rel_changed.getNew_relevance()))
					{
						pmid_relevance.remove(publication.getID());
						pmid_relevance.put(publication.getID(), rel_changed.getNew_relevance());
						setTableRenderer();
						resultsTable.repaint();	
						refreshRelevanceFields();
					}
				}
			}
		});
	}
	
	private void setTableRenderer() {
		
		resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {  

			private static final long serialVersionUID = -8183629931912150659L;
	
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
				
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
			   				
				String relevance = pmid_relevance.get(pubs.get(row).getID()); 
				
				if(relevance == null)
					setBackground(null);
				else if(relevance.equals("irrelevant"))
					setBackground(Color.decode("#C1FFC1"));
				else if(relevance.equals("related"))
					setBackground(Color.decode("#FFF68F"));
				else // Relevant "#F08080"
					setBackground(Color.decode("#F08080"));
				
				return this;  
			}  
		});
	}
	
	private void selectAll(){
		boolean select = jCheckBoxAll.isSelected();
		DefaultTableModel model = ((DefaultTableModel) resultsTable.getModel());
		
		for(int i=0;i<this.resultsTable.getModel().getRowCount();i++)
			model.setValueAt(select, i, 0);
			
		resultsTable.repaint();
		
		if(select)
			selectedCount = Integer.decode(jTextFieldTotal.getText());
		else
			selectedCount=0;
		
		if(!select)
		{
			selectRelated();
			selectIrrelevant();
			selectRelevant();
		}
	
		jTextFieldSelected.setText(String.valueOf(selectedCount));
		fillTotalTextField();	
	}
	
	private void pubmedRetrieval() {
		
		ArrayList<IPublication> toDownload = new ArrayList<IPublication>();
		for(int i=0;i<this.resultsTable.getRowCount();i++)
		{
			boolean selected = ((Boolean) resultsTable.getValueAt(i,5)).booleanValue();
			if(selected)
			{
				String pmid = this.pubs.get(i).getOtherID();
				String pdf_path = PublicationManager.saveDocs + pmid + ".pdf";
				String otherID = PublicationManager.saveDocs+ "id" + PublicationManager.saveDocs + this.pubs.get(i)+".pdf";
				if(new File(pdf_path).exists()||new File(otherID).exists())
				{
					
				}
				else
				{		
					toDownload.add(this.pubs.get(i));
				}
			}
		}
		this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
				new ParamSpec("query",QueryInformationRetrievalExtension.class,query,null),
				new ParamSpec("publications",ArrayList.class,toDownload,null)
			});
			
	}
	
	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		this.paramsRec = arg0;
		this.setSize(900,600);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}

	public void finish() {
		this.setVisible(false);
		this.dispose();	
	}

	public void onValidationError(Throwable arg0) {}
	
	private void selectRelevant()
	{
		boolean isSelectedRelevance = jCheckBoxRelevante.isSelected();
		boolean isSelectedAll = jCheckBoxAll.isSelected();
		if(isSelectedAll)
		{
			
		}
		else
		{
			DefaultTableModel model = ((DefaultTableModel) resultsTable.getModel());

			for(int i=0;i<this.resultsTable.getModel().getRowCount();i++)
			{
				int id = pubs.get(i).getID();
				String relevance = this.pmid_relevance.get(id);
				if(relevance!=null && relevance.equals("relevant"))
				{
					boolean after = ((Boolean) resultsTable.getValueAt(i,0)).booleanValue();
					model.setValueAt(isSelectedRelevance, i, 0);
					if(after&&!isSelectedRelevance)
					{
						selectedCount--;
					}
					else if(!after&&isSelectedRelevance)
					{
						selectedCount++;
					}
						
				}
				else
				{

				}
			}
			resultsTable.repaint();
		}
		jTextFieldSelected.setText(String.valueOf(selectedCount));
	}
	
	private void selectRelated()
	{
		boolean isSelectedRelated = jCheckBoxRelated.isSelected();
		boolean isSelectedAll = jCheckBoxAll.isSelected();
		if(isSelectedAll)
		{
			
		}
		else
		{
			DefaultTableModel model = ((DefaultTableModel) resultsTable.getModel());

			for(int i=0;i<this.resultsTable.getModel().getRowCount();i++)
			{
				int id = pubs.get(i).getID();
				String relevance = this.pmid_relevance.get(id);
				if(relevance!=null && relevance.equals("related"))
				{
					boolean after = ((Boolean) resultsTable.getValueAt(i,0)).booleanValue();
					model.setValueAt(isSelectedRelated, i, 0);
					if(after&&!isSelectedRelated)
					{
						selectedCount--;
					}
					else if(!after&&isSelectedRelated)
					{
						selectedCount++;
					}
				}
				else
				{

				}
			}

			resultsTable.repaint();
		}
		jTextFieldSelected.setText(String.valueOf(selectedCount));
	}
	
	private void selectIrrelevant()
	{
		boolean isSelectedIrelevante = jCheckBoxIrrelevant.isSelected();
		boolean isSelectedAll = jCheckBoxAll.isSelected();
		if(isSelectedAll)
		{
			
		}
		else
		{
			DefaultTableModel model = ((DefaultTableModel) resultsTable.getModel());

			for(int i=0;i<this.resultsTable.getModel().getRowCount();i++)
			{
				int id = pubs.get(i).getID();
				String relevance = this.pmid_relevance.get(id);
				if(relevance!=null && relevance.equals("irrelevant"))
				{
					boolean after = ((Boolean) resultsTable.getValueAt(i,0)).booleanValue();
					model.setValueAt(isSelectedIrelevante, i, 0);
					boolean after2 = ((Boolean) resultsTable.getValueAt(i,0)).booleanValue();
					if(after&&!isSelectedIrelevante)
					{
						selectedCount--;
					}
					else if(!after&&isSelectedIrelevante)
					{
						selectedCount++;
					}
				}
				else
				{

				}
			}

			resultsTable.repaint();
		}
		jTextFieldSelected.setText(String.valueOf(selectedCount));
	}
}
