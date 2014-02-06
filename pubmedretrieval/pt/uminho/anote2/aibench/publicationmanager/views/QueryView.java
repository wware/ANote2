package pt.uminho.anote2.aibench.publicationmanager.views;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.PublicationResumeGUI;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;




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
* AiBench View - QueryInformationRetrivalExtension
*/
public class QueryView extends JPanel implements Observer{

	private static final long serialVersionUID = 5682081329569397494L;
	
	private JTable resultsTable;
	private JScrollPane jScrollPane1;
	private JComboBox searchComboBox;
	private JPanel seachPanel;
	private JTextField searchTextField;

	private List<IPublication> pubs;
	private IDatabase db;
	private JPanel jPanelQueryInformation;
	private JTextPane jTextPaneKeywords;
	private JCheckBox jCheckBoxKeySensetive;
	private JButton jButtonNewPub;
	private JScrollPane jScrollPane2;
	private JTable jTableQueryProperties;
	private JButton jButtonHelp;
	private JCheckBox jCheckBoxAll;
	private JTextField jTextFieldTotal;
	private JTextField jTextFieldSelected;
	private JLabel jLabelTotal;
	private JLabel jLabelSellectDocs;
	private JButton jButtonJournalRetrieval;
	private JPanel jPanelJournalRetrieval;
	private JButton jButtonUpdateQuery;
	private JTextPane jTextPaneDate;
	private JLabel jLabelDate;
	private JTextPane jTextPaneToDate;
	private JTextPane jTextPaneAuthors;
	private JLabel jLabelToDate;
	private JLabel jLabelAuthors;
	private JTextPane jTextPaneFromDate;
	private JTextPane jTextPaneOrganism;
	private JLabel jLabelFromDate;
	private JLabel jLabelOrganism;
	private JLabel jLabelKeywords;
	private QueryInformationRetrievalExtension query;
	private int selectedCount;

	private JCheckBox box;

	public QueryView(QueryInformationRetrievalExtension query){
		super();
		this.query=query;
		this.db=query.getPubManager().getDb();
		try {
			this.pubs = getResultsTable("");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NonExistingConnection e) {
			e.printStackTrace();
		}
		initGUI();
		this.query.addObserver(this);
		fillSelectedTextFieldTotal();
		fillTotalTextField();
	}
	
	private void fillSelectedTextFieldTotal() {
		this.jTextFieldSelected.setText(String.valueOf(this.pubs.size()));	
	}

	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 1.0, 0.0};
				thisLayout.rowHeights = new int[] {7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
				this.setLayout(thisLayout);
				{
					jScrollPane1 = new JScrollPane();
					this.add(jScrollPane1, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
					jScrollPane1.setFocusTraversalKeysEnabled(false);
					jScrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Information", TitledBorder.LEADING, TitledBorder.TOP));
					resultsTable = new JTable(){

						private static final long serialVersionUID = -2422590849569066990L;

						public boolean isCellEditable(int x,int y){
							if(y==0||y==5)
								return true;
							return false;
						}
					
						public Class<?> getColumnClass(int column){
							if(column == 5)
								return Boolean.class;
							return String.class;
						}
						
//						public boolean isColumnSelected(int column)
//						{
//							if(column==5)
//							{
//								return false;
//							}
//							return false;
//						}
					};
					
					TableModel pubTableModel = fillModelPubTable();				
					jScrollPane1.setViewportView(resultsTable);
					resultsTable.setModel(pubTableModel);
					complementTable();				
				}
				{
					jPanelQueryInformation = new JPanel();
					GridBagLayout jPanelQueryInformationLayout = new GridBagLayout();
					this.add(jPanelQueryInformation, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelQueryInformationLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.0};
					jPanelQueryInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
					jPanelQueryInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.1, 0.0};
					jPanelQueryInformationLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
					jPanelQueryInformation.setLayout(jPanelQueryInformationLayout);
					jPanelQueryInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query Information", TitledBorder.LEADING, TitledBorder.TOP));

					{
						jLabelKeywords = new JLabel();
						jPanelQueryInformation.add(jLabelKeywords, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
						jLabelKeywords.setText("Keywords :");
					}
					{
						jLabelOrganism = new JLabel();
						jPanelQueryInformation.add(jLabelOrganism, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
						jLabelOrganism.setText("Organism :");
					}
					{
						jLabelFromDate = new JLabel();
						jPanelQueryInformation.add(jLabelFromDate, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
						jLabelFromDate.setText("FromDate :");
					}
					{
						jTextPaneKeywords = new JTextPane();
						jPanelQueryInformation.add(jTextPaneKeywords, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextPaneKeywords.setEditable(false);
						jTextPaneKeywords.setBackground(Color.WHITE);
						jTextPaneKeywords.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						jTextPaneKeywords.setText(query.getKeyWords());
					}
					{
						jTextPaneOrganism = new JTextPane();
						jPanelQueryInformation.add(jTextPaneOrganism, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextPaneOrganism.setEditable(false);
						jTextPaneOrganism.setBackground(Color.WHITE);
						jTextPaneOrganism.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						jTextPaneOrganism.setText(query.getOrganism());
					}
					{
						jTextPaneFromDate = new JTextPane();
						jPanelQueryInformation.add(jTextPaneFromDate, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextPaneFromDate.setEditable(false);
						jTextPaneFromDate.setBackground(Color.WHITE);
						jTextPaneFromDate.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						if(query.getProperties().get("fromDate")!=null)
						{	
							jTextPaneFromDate.setText(String.valueOf(query.getProperties().get("fromDate")));
						}
						else
						{
							jTextPaneFromDate.setText(String.valueOf(PublicationManager.searchYearStarting));

						}
					}
					{
						jLabelAuthors = new JLabel();
						jPanelQueryInformation.add(jLabelAuthors, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
						jLabelAuthors.setText("Authors :");
					}
					{
						jLabelToDate = new JLabel();
						jPanelQueryInformation.add(jLabelToDate, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
						jLabelToDate.setText("ToDate :");
					}
					{
						jTextPaneAuthors = new JTextPane();
						jPanelQueryInformation.add(jTextPaneAuthors, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextPaneAuthors.setEditable(false);
						jTextPaneAuthors.setBackground(Color.WHITE);
						jTextPaneAuthors.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						if(query.getProperties().get("authors")!=null)
						{	
							jTextPaneAuthors.setText(String.valueOf(query.getProperties().get("authors")));
						}
					}
					{
						jTextPaneToDate = new JTextPane();
						jPanelQueryInformation.add(jTextPaneToDate, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextPaneToDate.setEditable(false);
						jTextPaneToDate.setBackground(Color.WHITE);
						jTextPaneToDate.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						if(query.getProperties().get("toDate")!=null)
						{	
							jTextPaneToDate.setText(String.valueOf(query.getProperties().get("toDate")));
						}
						else
						{
							jTextPaneToDate.setText(String.valueOf(new GregorianCalendar().get(Calendar.YEAR)));
						}
					}
					{
						jLabelDate = new JLabel();
						jPanelQueryInformation.add(jLabelDate, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 2, 0, 0), 0, 0));
						jLabelDate.setText("Date :");
					}
					{
						jTextPaneDate = new JTextPane();
						jTextPaneDate.setEditable(false);
						jTextPaneDate.setBackground(Color.WHITE);
						jTextPaneDate.setText(query.getDate().toString());
						jPanelQueryInformation.add(jTextPaneDate, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextPaneDate.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					}
					{
						jButtonUpdateQuery = new JButton();
						jPanelQueryInformation.add(jButtonUpdateQuery, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonUpdateQuery.setText("Update Query");
						jButtonUpdateQuery.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
						jButtonUpdateQuery.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								updateQuery();
							}

						});
					}
					{
						jCheckBoxAll = new JCheckBox();
						jPanelQueryInformation.add(jCheckBoxAll, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelQueryInformation.add(getJScrollPane2(), new GridBagConstraints(4, 0, 2, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelQueryInformation.add(getJButtonNewPub(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jCheckBoxAll.setText("All");
						jCheckBoxAll.setSelected(true);
						jCheckBoxAll.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								selectAll();
							}
						});
						
					}
				}
				{
					seachPanel = new JPanel();
					GridBagLayout seachPanelLayout = new GridBagLayout();
					seachPanelLayout.rowWeights = new double[] {0.0};
					seachPanelLayout.rowHeights = new int[] {22};
					seachPanelLayout.columnWeights = new double[] {0.1, 0.0};
					seachPanelLayout.columnWidths = new int[] {100, 7};
					seachPanel.setLayout(seachPanelLayout);					
					seachPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Search", TitledBorder.LEADING, TitledBorder.TOP));
					{
						searchTextField = new JTextField();
						seachPanel.add(searchTextField, new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						searchTextField
							.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory
									.createEtchedBorder(BevelBorder.LOWERED),
								null));
						searchTextField.setPreferredSize(new java.awt.Dimension(54, 17));
						searchTextField.addKeyListener(new KeyAdapter() {
							@Override
							public void keyTyped(KeyEvent evt) {
								if(jCheckBoxKeySensetive.isSelected())
								{
									searchInTable(evt);
								}
								else
								{
									searchInTableKeySensetive(evt);
								}
							}
						});
					}
					{
						ComboBoxModel searchComboBoxModel = new DefaultComboBoxModel(
							new String[] { "Title","Abstract", "Authors", "Date","All" });
						searchComboBox = new JComboBox();
						seachPanel.add(searchComboBox, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						seachPanel.add(getJCheckBoxKeySensetive(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						searchComboBox.setModel(searchComboBoxModel);
					}
					this.add(seachPanel, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jPanelJournalRetrieval = new JPanel();
					GridBagLayout jPanelJournalRetrievalLayout = new GridBagLayout();
					this.add(jPanelJournalRetrieval, new GridBagConstraints(4, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelJournalRetrievalLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelJournalRetrievalLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelJournalRetrievalLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
					jPanelJournalRetrievalLayout.columnWidths = new int[] {7, 7, 7};
					jPanelJournalRetrieval.setLayout(jPanelJournalRetrievalLayout);
					jPanelJournalRetrieval.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Journal Retrieval", TitledBorder.LEADING, TitledBorder.TOP));

					{
						jButtonJournalRetrieval = new JButton();
						jPanelJournalRetrieval.add(jButtonJournalRetrieval, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonJournalRetrieval.setText("Journal Retrieval");
						jButtonJournalRetrieval.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));
						jButtonJournalRetrieval.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								pubmedRetrieval();
							}
						});
						
					}
					{
						jLabelSellectDocs = new JLabel();
						jPanelJournalRetrieval.add(jLabelSellectDocs, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelSellectDocs.setText("Selected");
					}
					{
						jLabelTotal = new JLabel();
						jPanelJournalRetrieval.add(jLabelTotal, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelTotal.setText("Total");
					}
					{
						jTextFieldSelected = new JTextField();
						jPanelJournalRetrieval.add(jTextFieldSelected, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldSelected.setEditable(false);
						jTextFieldSelected.setBackground(Color.WHITE);
					}
					{
						jTextFieldTotal = new JTextField();
						jPanelJournalRetrieval.add(jTextFieldTotal, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldTotal.setEditable(false);
						jTextFieldTotal.setBackground(Color.WHITE);
					}
					{
						jPanelJournalRetrieval.add(getJButtonHelp(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	


	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Query_View");
				}
			});
		}
		return jButtonHelp;
	}
		
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"", "Title", "Authors", "Date"};
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
	
	private void initViewGUI(){
		IPublication publication = this.pubs.get(resultsTable.getSelectedRow());
		new PublicationResumeGUI(query,publication, null);
	}
	
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
					if(pub.getAbstractSection()!=null)
					{
						if(pub.getAbstractSection().contains(text))
							rows.add(new Integer(i));
					}
					i++;
				}
				break;
			}
			case 2:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getAuthors().contains(text))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			case 3:
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
					if(pub.getTitle().contains(text) || pub.getAuthors().contains(text) || String.valueOf(pub.getDate()).contains(text)|| pub.getAbstractSection().contains(text))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
				
		}
		
		setTableChangesSearch(rows, selectionModel);	
	}
	
	protected void searchInTableKeySensetive(KeyEvent evt) {
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
					if(pub.getTitle().toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			case 1:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getAbstractSection()!=null)
					{
						if(pub.getAbstractSection().toLowerCase().contains(text.toLowerCase()))
							rows.add(new Integer(i));
					}
					i++;
				}
				break;
			}
			case 2:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getAuthors().toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			case 3:
			{
				for(IPublication pub: this.pubs)
				{
					if(String.valueOf(pub.getDate()).toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
			default:
			{
				for(IPublication pub: this.pubs)
				{
					if(pub.getTitle().toLowerCase().contains(text.toLowerCase()) || 
							pub.getAuthors().toLowerCase().contains(text.toLowerCase()) || 
							String.valueOf(pub.getDate()).toLowerCase().contains(text.toLowerCase()) ||
							pub.getAbstractSection().toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
					i++;
				}
				break;
			}
				
		}
		
		setTableChangesSearch(rows, selectionModel);	
		
	}

	@SuppressWarnings("static-access")
	private void setTableChangesSearch(ArrayList<Integer> rows,
			DefaultListSelectionModel selectionModel) {
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
		if(rows.size()>0)
		{
			this.resultsTable.scrollRectToVisible(this.resultsTable.getCellRect(rows.get(0), 0, true));
		}
	}
	
	public void update(Observable arg0, Object arg1) {
		
		try {
			this.pubs = getResultsTable("");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NonExistingConnection e) {
			e.printStackTrace();
		}
		TableModel pubTableModel = fillModelPubTable();
		resultsTable.setModel(pubTableModel);
		complementTable();
		fillSelectedTextFieldTotal();
		fillTotalTextField();
		
	}
	
	private void complementTable(){
		
		box = new JCheckBox();
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
		resultsTable.getColumnModel().getColumn(0).setMaxWidth(15);
		resultsTable.getColumnModel().getColumn(0).setMinWidth(15);
		resultsTable.getColumnModel().getColumn(0).setPreferredWidth(15);
		
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
		
		//resultsTable.setSelectionForeground(Color.WHITE);
		
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
				int id = pubs.get(row).getID();
				String id_path = PublicationManager.saveDocs+"id"+String.valueOf(id)+".pdf";
				String id_path_otherID = PublicationManager.saveDocs+"id"+id+"-"+pmid+".pdf";
				String pdf_path = PublicationManager.saveDocs+pmid+".pdf";
				if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
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
				//viewButton.setBackground(Color.WHITE);
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
				//viewButton.setBackground(Color.WHITE);
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
	}
	
	public List<IPublication> getResultsTable(String order) throws SQLException, NonExistingConnection {
		
		db.openConnection();

		List<IPublication> publications = new ArrayList<IPublication>();
		
		String stat = QueriesPublication.selectPublicationsQueryExtra;
			
		stat += order;
		
		PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement(stat);    
		ps.setInt(1,query.getID());
		
		ResultSet rs = ps.executeQuery();

		while(rs.next())
		{
			publications.add(new Publication(
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
		return publications;
	}
	
	private void selectAll(){
		boolean select = jCheckBoxAll.isSelected();
		DefaultTableModel model = ((DefaultTableModel) resultsTable.getModel());
		
		for(int i=0;i<this.resultsTable.getModel().getRowCount();i++)
			model.setValueAt(select, i, 0);
			
		resultsTable.repaint();
		
		if(select)
		{
			selectedCount = Integer.decode(jTextFieldTotal.getText());
			
		}
		else
		{
			selectedCount=0;
		}
		
		jTextFieldSelected.setText(String.valueOf(selectedCount));
		fillTotalTextField();
		
	}
	
	/**
	 * Alterar aqui
	 * 
	 */
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
		ParamSpec[] paramsSpec = new ParamSpec[]{ 
				new ParamSpec("query",QueryInformationRetrievalExtension.class,this.query, null),
				new ParamSpec("publications",ArrayList.class,toDownload, null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.journalretrievalmuldoc")){			
				Workbench.getInstance().executeOperation(def, paramsSpec);
				return;
			}
		}	
	}
	

	private void updateQuery() {
		ParamSpec[] paramsSpec = new ParamSpec[]{ 
				new ParamSpec("query",QueryInformationRetrievalExtension.class,this.query, null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.pubmedsearchupdate")){			
				Workbench.getInstance().executeOperation(def, paramsSpec);
				return;
			}
		}	
		
	}

	private JTable getJTableQueryProperties() {
		if(jTableQueryProperties == null) {
			
			jTableQueryProperties = new JTable();
			DefaultTableModel tableModel = new DefaultTableModel();
			String[] columns = new String[] {"Property", "Option"};
			tableModel.setColumnIdentifiers(columns);
			Properties porProperties = this.query.getProperties();
			
			Object[][] data = new Object[porProperties.size()][4];
			
			int i=0;
			Enumeration<Object> itObj = porProperties.keys();
			while(itObj.hasMoreElements())
			{
				Object propertyNameObj = itObj.nextElement();
				String propertyName = (String) propertyNameObj ;
				Object propertyValueObj = porProperties.getProperty(propertyName);
				data[i][0] = propertyName;
				data[i][1] = propertyValueObj.toString();				
				tableModel.addRow(data[i]);
				i++;						
			}		
			jTableQueryProperties.setModel(tableModel);
		}
		return jTableQueryProperties;
	}

	private JScrollPane getJScrollPane2() {
		if(jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			//jScrollPane2.setBorder(BorderFactory.createTitledBorder("Query Properties"));
			jScrollPane2.setViewportView(getJTableQueryProperties());
		}
		return jScrollPane2;
	}
	
	private JButton getJButtonNewPub() {
		if(jButtonNewPub == null) {
			jButtonNewPub = new JButton();
			jButtonNewPub.setText("Add Publication");
			jButtonNewPub.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Open18.png")));
			jButtonNewPub.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					addPublication();
				}
			});
		}
		return jButtonNewPub;
	}

	protected void addPublication() {
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.addpublicationquery")){			
				Workbench.getInstance().executeOperation(def);
				return;
			}
		}	
		
	}
	
	private JCheckBox getJCheckBoxKeySensetive() {
		if(jCheckBoxKeySensetive == null) {
			jCheckBoxKeySensetive = new JCheckBox();
			jCheckBoxKeySensetive.setText("Case Sensitive");
			jCheckBoxKeySensetive.setSelected(true);
		}
		return jCheckBoxKeySensetive;
	}
}