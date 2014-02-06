package pt.uminho.anote2.aibench.publicationmanager.views;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.listeners.IChangeSelectionPublicationsListener;
import pt.uminho.anote2.aibench.publicationmanager.gui.PublicationResumeGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.comparate.AlphanumericComparator;
import pt.uminho.generic.components.table.tablerowsorter.TableRowSorterWhitOtherTypes;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;


/**
* AiBench View - QueryInformationRetrivalExtension
*/
public class QueryView extends JPanel implements Observer,IChangeSelectionPublicationsListener{

	private static final long serialVersionUID = 5682081329569397494L;
	

	private JPanel operationPAnel;

	private TableSearchPanel publictionstableSearch;
	private JPanel jPanelQueryInformation;
	private JTextPane jTextPaneKeywords;
	private JButton jButtonWorkflow;
	private JTextPane jTextPaneInfo;
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
	private List<IPublication> pubs;
	private int selectedCount;
	private boolean availableToChange = true;
	private JPanel jPanelOtherInformation;

	public QueryView(QueryInformationRetrievalExtension query){
		super();
		this.query=query;
		try {
			this.pubs = query.getPublications();
			initGUI();
			this.query.addObserver(this);
			fillSelectedTextFieldTotal();
			fillTotalTextField();
			selectedCount = this.pubs.size();
			this.query.registerIChangeSelectionPublicationsListener(1, this);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}
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
					this.add(getSearchTAblePasnel(), new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));					
			
				}
				{
					if(query.isFromPubmedSearch())
					{
						this.add(getJpanelInfo(), new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				
					}
					else
					{
						this.add(getOtherPAnel(), new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					operationPAnel = new JPanel();
					GridBagLayout seachPanelLayout = new GridBagLayout();
					seachPanelLayout.rowWeights = new double[] {0.1};
					seachPanelLayout.rowHeights = new int[] {7};
					seachPanelLayout.columnWeights = new double[] {0.5, 0.5, 0.1};
					seachPanelLayout.columnWidths = new int[] {7, 7, 20};
					operationPAnel.setLayout(seachPanelLayout);					
					operationPAnel.setBorder(BorderFactory.createTitledBorder("Operations"));
					this.add(operationPAnel, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						if(query.isFromPubmedSearch())
						{
							operationPAnel.add(getUpdateButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						}
						operationPAnel.add(getJButtonNewPub(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.queryworkflow")){			
								operationPAnel.add(getJButtonWorkflow(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								break;
							}
						}	

					}
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
					jPanelJournalRetrieval.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Document Retrieve", TitledBorder.LEADING, TitledBorder.TOP));

					{
						jButtonJournalRetrieval = new JButton();
						jPanelJournalRetrieval.add(jButtonJournalRetrieval, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonJournalRetrieval.setText("Document Retrieve");
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
						jLabelSellectDocs.setVisible(false);
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
						jTextFieldSelected.setVisible(false);
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
	


	private JButton getUpdateButton() {
		if(jButtonUpdateQuery==null)
		{
			jButtonUpdateQuery = new JButton();
			jButtonUpdateQuery.setText("Update Query");
			jButtonUpdateQuery.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
			jButtonUpdateQuery.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					updateQuery();
				}		
			});
		}
		return jButtonUpdateQuery;
	}

	private JPanel getOtherPAnel() {
		if(jPanelOtherInformation==null)
		{
			jPanelOtherInformation = new JPanel();
			GridBagLayout jPanelOtherInformationLayout = new GridBagLayout();
			jPanelOtherInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query Information", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelOtherInformationLayout.rowWeights = new double[] {0.1};
			jPanelOtherInformationLayout.rowHeights = new int[] {7};
			jPanelOtherInformationLayout.columnWeights = new double[] {0.1};
			jPanelOtherInformationLayout.columnWidths = new int[] {7};
			jPanelOtherInformation.setLayout(jPanelOtherInformationLayout);
			jPanelOtherInformation.add(getJTextPaneInfo(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelOtherInformation;
	}

	private JPanel getJpanelInfo() {
		if(jPanelQueryInformation==null)
		{
			jPanelQueryInformation = new JPanel();
			GridBagLayout jPanelQueryInformationLayout = new GridBagLayout();
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
				jTextPaneDate.setText(Utils.SimpleDataFormat.format(query.getDate()));
				jPanelQueryInformation.add(jTextPaneDate, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextPaneDate.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				jCheckBoxAll = new JCheckBox();
				jPanelQueryInformation.add(jCheckBoxAll, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelQueryInformation.add(getJScrollPane2(), new GridBagConstraints(4, 0, 2, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxAll.setText("All");
				jCheckBoxAll.setSelected(true);
				jCheckBoxAll.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						selectAll();
					}
				});
				
			}

		}
		return jPanelQueryInformation;
	}

	private JPanel getSearchTAblePasnel() {
		if(publictionstableSearch == null)
		{
			publictionstableSearch = new TableSearchPanel(true);
			publictionstableSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publications", TitledBorder.LEADING, TitledBorder.TOP));
			publictionstableSearch.setModel(fillModelPubTable());
			publictionstableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			constructQueryTable(publictionstableSearch.getMainTable());
			publictionstableSearch.getOriginalTableModel().addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent arg0) {
					if(availableToChange)
					{
						changePublicationSelected(arg0);
					}
				}
			});
		}
		return publictionstableSearch;
	}
	
	private void changePublicationSelected(TableModelEvent arg0) {
		int selectRow = arg0.getFirstRow();
		if(arg0.getColumn() == 6)
		{
			boolean isselecetd = (Boolean) publictionstableSearch.getOriginalTableModel().getValueAt(selectRow, arg0.getColumn());
			int id = pubs.get(selectRow).getID();
			if(isselecetd)
			{
				query.addPublicationToSelctedPublications(id);
			}
			else
			{
				query.removePublicationToSelctedPublications(id);
			}
		}
		updateAllSelctionListeners();
	}

	private void updateAllSelctionListeners() {
		availableToChange = false;
		this.query.updateAllSelectionListeners(1);
		availableToChange = true;
	}

	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Query_View");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
		
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Details","Title", "Authors", "Date","PMID/OtherID","PDF","Sel"};
		DefaultTableModel tableModel = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;

			
			
			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 3)
					return Integer.class;
				if(columnIndex== 4)
					return Object.class;
				if(columnIndex == 5 || columnIndex == 6 )
					return Boolean.class;
				return String.class;
			}
			
			public boolean isCellEditable(int row, int col) {  
				if (col == 5 || col == 3 || col == 2 || col == 1) {  
					return false;  
				} else {  
					return true;  
				}         
			} 

		};
		
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;	
		data = new Object[this.pubs.size()][7];	
		for(IPublication pub: this.pubs)
		{
			data[i][0] = "";
			data[i][1] = pub.getTitle();
			data[i][2] = pub.getAuthors();
			data[i][3] = pub.getYearDate();
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
			data[i][5] = new Boolean(pub.isPDFAvailable());
			if(this.query.publicationINSelectingPublication(pub.getID()))
			{
				data[i][6] = new Boolean(true);
			}
			else
			{
				data[i][6] = new Boolean(false);
			}
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
		IPublication publication = this.pubs.get(publictionstableSearch.getSelectedRowsInOriginalModel()[0]);
		new PublicationResumeGUI(query,publication);
	}
	
	
	public void update(Observable arg0, Object arg1) {
		query.setFirst();
		try {
			this.pubs = query.getPublications();
			publictionstableSearch.setModel(fillModelPubTable());
			constructQueryTable(publictionstableSearch.getMainTable());
			if(jCheckBoxAll!=null)
				jCheckBoxAll.setSelected(true);
			fillSelectedTextFieldTotal();
			fillTotalTextField();	
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}

	}
	
	private void constructQueryTable(JTable jtable){
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);		
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.moreinfofieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.moreinfofieldminWith);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.moreinfofieldpreferredWith);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setMaxWidth(350);
		jtable.getColumnModel().getColumn(2).setMinWidth(150);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(180);
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.datefieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.datefieldminWith);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.datefieldpreferredWith);
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.pmidotherIDfieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.pmidotherIDfieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.pmidotherIDfieldpreferredWith);
		jtable.getColumnModel().getColumn(5).setMaxWidth(PreferencesSizeComponents.pdfavailablefieldmaxWith);
		jtable.getColumnModel().getColumn(5).setMinWidth(PreferencesSizeComponents.pdfavailablefieldminWith);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(PreferencesSizeComponents.pdfavailablefieldpreferredWith);;
		jtable.getColumnModel().getColumn(6).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(6).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(6).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);	
		jtable.getColumn("PMID/OtherID").setCellRenderer(new ButtonTablePMIDRender());
		jtable.getColumn("PMID/OtherID").setCellEditor(new ButtonTablePMIDCellEditor());
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/find.png"));
		jtable.getColumn("Details").setCellRenderer(new ButtonTableFindRenderer(icon));
		jtable.getColumn("Details").setCellEditor(new ButtonTableFindCellEditor());
		jtable.getColumn("PDF").setCellRenderer(new ButtonTablePDFRenderer());
		jtable.getColumn("PDF").setCellEditor(new ButtonTablePDFCellEditor());
		Map<Integer, Comparator<?>> columnComparators = new HashMap<Integer, Comparator<?>>();
		columnComparators.put(4, new AlphanumericComparator());
		TableRowSorter<TableModel> sortedModel = new TableRowSorterWhitOtherTypes(columnComparators);
		sortedModel.setModel(jtable.getModel());
		jtable.setRowSorter(sortedModel);
	}

	protected void updateCount() {
		selectedCount = 0;
		for(int i=0;i<this.publictionstableSearch.getMainTable().getRowCount();i++)
		{
			boolean selected = ((Boolean) publictionstableSearch.getValueAt(i,6)).booleanValue();
			if(selected)
			{
				selectedCount ++;
			}
			
		}
		fillSelectedTextField();
	}
	
	private void selectAll(){
		boolean select = jCheckBoxAll.isSelected();
		TableModel model =  publictionstableSearch.getModel();
		Set<Integer> ids = new HashSet<Integer>();
		for(int i=0;i<this.publictionstableSearch.getModel().getRowCount();i++)
		{
			model.setValueAt(select, i, 6);
			int id = pubs.get(i).getID();
			if(select)
			{
				ids.add(id);
			}
		}
		this.query.setSelectingPublications(ids);
		updateAllSelctionListeners();
		publictionstableSearch.repaint();	
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
	

	private void pubmedRetrieval() {
		
		ArrayList<IPublication> toDownload = new ArrayList<IPublication>();
		for(int i=0;i<this.publictionstableSearch.getMainTable().getRowCount();i++)
		{
			boolean selected = ((Boolean) publictionstableSearch.getValueAt(i,6)).booleanValue();
			if(selected)
			{
				toDownload.add(this.pubs.get(i));
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
	
	class ButtonTablePDFCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
		}
		
	}
	
	class ButtonTablePDFRenderer extends ButtonTableRenderer
	{

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JButton viewButton = new JButton();
			if((Boolean) value)
			{
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
			}
			return viewButton;
		}
		
	}
	
	class ButtonTableFindRenderer extends ButtonTableRenderer
	{
		private static final long serialVersionUID = 1L;
		
		public ButtonTableFindRenderer(ImageIcon icon)
		{
			super(icon);
		}

		public void whenClick() {
			initViewGUI();
		}
		
	}
	
	class ButtonTableFindCellEditor extends ButtonTableCellEditor
	{

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			initViewGUI();
		}
		
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

		public void whenClick() {
			try {
				lanchPMID();
			} catch (IOException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}
		
	}

	public void lanchPMID() throws IOException {
		String externalLink = pubs.get(publictionstableSearch.getSelectedRowsInOriginalModel()[0]).getExternalLink();
		if(externalLink==null || externalLink.length() < 1)
		{
			Workbench.getInstance().warn("The document does not contains external link... Please update External Link in Publication Resume GUI");
		}
		else
		{
			Help.internetAccess(externalLink);
		}
	}
	
	private JTextPane getJTextPaneInfo() {
		if(jTextPaneInfo == null) {
			jTextPaneInfo = new JTextPane();
			jTextPaneInfo.setEditable(false);
			jTextPaneInfo.setText(query.getKeyWords());
		}
		return jTextPaneInfo;
	}
	
	private JButton getJButtonWorkflow() {
		if(jButtonWorkflow == null) {
			jButtonWorkflow = new JButton();
			jButtonWorkflow.setText("Workflow Information Extraction");
			jButtonWorkflow.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/query.png")));
			jButtonWorkflow.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					launchWorkflow();
				}
			});
		}
		return jButtonWorkflow;
	}

	protected void launchWorkflow() {
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.queryworkflow")){	
				Workbench.getInstance().executeOperation(def);
				break;
			}
		}	
	}

	@Override
	public void updateSelectionPublication(int id) {
		if(id != 1)
		{
			int i=0;
			for(IPublication pub:this.pubs)
			{
				boolean bool = this.query.publicationINSelectingPublication(pub.getID());
				publictionstableSearch.getOriginalTableModel().setValueAt(bool, i, 6);
				i++;
			}
		}
	}
	
	

}