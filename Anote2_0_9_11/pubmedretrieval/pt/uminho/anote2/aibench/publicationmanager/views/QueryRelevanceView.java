package pt.uminho.anote2.aibench.publicationmanager.views;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.listeners.IChangeRelevancePublicationsListener;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.listeners.IChangeSelectionPublicationsListener;
import pt.uminho.anote2.aibench.publicationmanager.gui.PublicationResumeGUI;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.RelevanceInfoPane;
import pt.uminho.anote2.aibench.publicationmanager.views.help.ButtonTableRelevanceRender;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.RelevanceType;
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
 * 
 * Aibench View for QueryInformationRetrievalExtension
 * 
 * @author Hugo COsta
 *
 */
public class QueryRelevanceView extends JPanel implements Observer,IChangeSelectionPublicationsListener,IChangeRelevancePublicationsListener{

	private static final long serialVersionUID = 5682081329569397494L;
	
	private TableSearchPanel publicationTable;
	private JLabel jLabelIrrelevant;
	private JCheckBox jCheckBoxRelevant;
	private JCheckBox jCheckBoxAll;
	private JTextField irrelevantTextField;
	private JTextField relatedTextField;
	private JTextField relevantsTextField;
	private JLabel jLabelRelated;
	private JCheckBox jCheckBoxRelated;
	private JCheckBox jCheckBoxIrrelevant;
	private JLabel jLabelRelevant;
	private List<IPublication> pubs;
	private JTextField jTextFieldTotal;
	private JLabel jLabelTotal;
	private JLabel jLabelSellectDocs;
	private JButton jButtonJournalRetrieval;
	private JPanel jPanelJournalRetrieval;
	private QueryInformationRetrievalExtension query;
	private int selectedCount;
	private JPanel jPanelRelevance;
	private JButton jButtonHelp;
	private JPanel jPanelUpper;
	private boolean availableToChange = true;
	private RelevanceInfoPane jPanelRelevanceInfoNone;
	private RelevanceInfoPane jPanelRelevanceInfoIrrelevant;
	private RelevanceInfoPane jPanelRelevanceInfoRelated;
	private RelevanceInfoPane jPanelRelevanceInfoRelevant;

	private JLabel jLabelWithout;

	private JTextField jTextFieldWithout;


	public QueryRelevanceView(QueryInformationRetrievalExtension query){
		super();
		this.query=query;
		try {
			this.pubs = query.getPublications();
			initGUI();
			refreshRelevanceFields();
			this.query.addObserver(this);
			fillTotalTextField();
			selectedCount = this.pubs.size();
			this.query.registerIChangeSelectionPublicationsListener(2, this);
			this.query.registerIChangeRelevancePublicationsListener(2, this);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.0, 0.9, 0.0};
		thisLayout.rowHeights = new int[] {21, 526, 7};
		thisLayout.columnWeights = new double[] {0.25, 0.5};
		thisLayout.columnWidths = new int[] {150, 7};
		this.setLayout(thisLayout);
		{
			publicationTable = new TableSearchPanel();
			publicationTable.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Information", TitledBorder.LEADING, TitledBorder.TOP));
			TableModel pubTableModel = fillModelPubTable();
			this.add(publicationTable, new GridBagConstraints(-1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
			publicationTable.setModel(pubTableModel);
			constructQueryTable(publicationTable.getMainTable());
			publicationTable.getModel().addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent arg0) {
					if(availableToChange)
					{
						changePublicationSelected(arg0);
					}
				}
			});

		}
		{
			jPanelJournalRetrieval = new JPanel();
			GridBagLayout jPanelJournalRetrievalLayout = new GridBagLayout();
			this.add(jPanelJournalRetrieval, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelJournalRetrievalLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelJournalRetrievalLayout.rowHeights = new int[] {7, 7, 7};
			jPanelJournalRetrievalLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelJournalRetrievalLayout.columnWidths = new int[] {7, 7};
			jPanelJournalRetrieval.setLayout(jPanelJournalRetrievalLayout);
			jPanelJournalRetrieval.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Document Retrieve", TitledBorder.LEADING, TitledBorder.TOP));

			{
				jButtonJournalRetrieval = new JButton();
				jPanelJournalRetrieval.add(jButtonJournalRetrieval, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
				jPanelJournalRetrieval.add(jLabelSellectDocs, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelSellectDocs.setText("Selected :");
				jLabelSellectDocs.setVisible(false);
			}
			{
				jLabelTotal = new JLabel();
				jPanelJournalRetrieval.add(jLabelTotal, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelTotal.setText("Total :");
				jLabelTotal.setVisible(false);
			}
			{
				jTextFieldTotal = new JTextField();
				jTextFieldTotal.setText(String.valueOf(pubs.size()));
				jPanelJournalRetrieval.add(jTextFieldTotal, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldTotal.setVisible(false);
			}
			{
				jPanelJournalRetrieval.add(getJButtonHelp(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			jPanelUpper = new JPanel();
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
			this.add(jPanelUpper, new GridBagConstraints(-1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJPanelRelevance(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
						try {
							selectAll();
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});


			}
			{
				jCheckBoxRelevant = new JCheckBox();
				jPanelUpper.add(jCheckBoxRelevant, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxRelevant.setText("Relevant");
				jCheckBoxRelevant.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						try {
							selectRelevant(true);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
			}
			{
				jCheckBoxRelated = new JCheckBox();
				jPanelUpper.add(jCheckBoxRelated, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxRelated.setText("Related");
				jCheckBoxRelated.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						try {
							selectRelated(true);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
			}
			{
				jCheckBoxIrrelevant = new JCheckBox();
				jPanelUpper.add(jCheckBoxIrrelevant, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxIrrelevant.setText("Irrelevant");
				jCheckBoxIrrelevant.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						try {
							selectIrrelevant(true);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
			}
		}
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Query_Relevance_View");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
		
	private TableModel fillModelPubTable() throws SQLException, DatabaseLoadDriverException{
		String[] columns = new String[] {"Details","Title", "Relevance", "Date","PMID/OtherID","PDF",""};
		DefaultTableModel tableModel = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 3 )
				{				
					return Integer.class;
				}
				else if(columnIndex== 4)
				{
					return Object.class;
				}
				else if(columnIndex == 2 )
				{
					return RelevanceType.class;
				}
				else if(columnIndex == 5 || columnIndex == 6 )
				{
					return Boolean.class;
				}
				else
					return String.class;
			}
			
			public boolean isCellEditable(int row, int col) {  
				if (col == 5 || col == 1) {  
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
			data[i][2] = query.getDocumentRelevanceType(pub.getID());
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
			data[i][5] = pub.isPDFAvailable();
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
	
	private void constructQueryTable(JTable jtable){			
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);		
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.moreinfofieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.moreinfofieldminWith);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.moreinfofieldpreferredWith);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setMaxWidth(100);
		jtable.getColumnModel().getColumn(2).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(100);
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
		jtable.setRowHeight(20);
		jtable.getColumn("PMID/OtherID").setCellRenderer(new ButtonTablePMIDRender());
		jtable.getColumn("PMID/OtherID").setCellEditor(new ButtonTablePMIDCellEditor());
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/find.png"));
		jtable.getColumn("Details").setCellRenderer(new ButtonTableFindRenderer(icon));
		jtable.getColumn("Details").setCellEditor(new ButtonTableFindCellEditor());
		jtable.getColumn("PDF").setCellRenderer(new ButtonTablePDFRenderer());
		jtable.getColumn("PDF").setCellEditor(new ButtonTablePDFCellEditor());
		jtable.setDefaultRenderer(RelevanceType.class, new ButtonTableRelevanceRender());
		Map<Integer, Comparator<?>> columnComparators = new HashMap<Integer, Comparator<?>>();
		columnComparators.put(4, new AlphanumericComparator());
		TableRowSorter<TableModel> sortedModel = new TableRowSorterWhitOtherTypes(columnComparators);
		sortedModel.setModel(jtable.getModel());
		jtable.setRowSorter(sortedModel);
	}
	
	private void fillTotalTextField(){
		this.jTextFieldTotal.setText(String.valueOf(this.pubs.size()));
	}	
	
	public void update(Observable arg0, Object arg1) {
		try {
			this.pubs = query.getPublications();
			TableModel pubTableModel = fillModelPubTable();
			publicationTable.setModel(pubTableModel);	
			constructQueryTable(publicationTable.getMainTable());
			selectAll();
			refreshRelevanceFields();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}	

	}
	
	private void refreshRelevanceFields() throws SQLException, DatabaseLoadDriverException{
		int a=0, b=0, c=0 , d=0;
		
		for(IPublication pub : this.pubs)
		{
			RelevanceType rt = query.getDocumentRelevanceType(pub.getID()); 
			if(rt == RelevanceType.irrelevant)
				a++;
			else if(rt == RelevanceType.related)
				b++;
			else if(rt == RelevanceType.relevant)// relevant
				c++;
			else
				d++;
		}

		this.irrelevantTextField.setText(String.valueOf(a));
		this.relatedTextField.setText(String.valueOf(b));
		this.relevantsTextField.setText(String.valueOf(c));
		this.jTextFieldWithout.setText(String.valueOf(d));
	}
	
	private void initViewGUI(){
		if(this.publicationTable.getSelectedRowsInOriginalModel()[0]!=-1)
		{
			IPublication publication = this.pubs.get(this.publicationTable.getSelectedRowsInOriginalModel()[0]);
			new PublicationResumeGUI(this.query,publication);
		}
	}	

	private void pubmedRetrieval() {
		
		List<IPublication> toDownload = new ArrayList<IPublication>();
		for(int i=0;i<this.publicationTable.getOriginalTableModel().getRowCount();i++)
		{
			boolean selected = ((Boolean) publicationTable.getValueAt(i,6)).booleanValue();
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
	
	private void selectAll() throws SQLException, DatabaseLoadDriverException{
		boolean select = jCheckBoxAll.isSelected();
		if(select)
		{
			Set<Integer> ids = new HashSet<Integer>();
			for(int i=0;i<this.publicationTable.getModel().getRowCount();i++)
			{
				publicationTable.getOriginalTableModel().setValueAt(select, i, 6);
				int id = pubs.get(i).getID();
				if(select)
				{
					ids.add(id);
				}
			}
			this.query.setSelectingPublications(ids);
			selectedCount = Integer.decode(jTextFieldTotal.getText());
		}
		else
		{
			selectedCount=0;
			Set<Integer> related = selectRelated(false);
			Set<Integer> irrelevant = selectIrrelevant(false);
			Set<Integer> relevant = selectRelevant(false);
			Set<Integer> total = new HashSet<Integer>();
			total.addAll(related);
			total.addAll(irrelevant);
			total.addAll(relevant);
			this.query.setSelectingPublications(total);
		}
		updateAllSelectionListenes();
		fillTotalTextField();	
	}
	
	private Set<Integer> selectRelevant(boolean updateNow) throws SQLException, DatabaseLoadDriverException
	{
		Set<Integer> relevant = new HashSet<Integer>();
		boolean isSelectedRelevance = jCheckBoxRelevant.isSelected();
		boolean isSelectedAll = jCheckBoxAll.isSelected();
		if(isSelectedAll)
		{
			
		}
		else
		{
			DefaultTableModel model = ((DefaultTableModel) publicationTable.getOriginalTableModel());
			for(int i=0;i<this.publicationTable.getOriginalTableModel().getRowCount();i++)
			{
				int id = pubs.get(i).getID();
				RelevanceType relevance = this.query.getDocumentRelevanceType(id);
				if(relevance == RelevanceType.relevant)
				{
					boolean after = ((Boolean) publicationTable.getOriginalTableModel().getValueAt(i,6)).booleanValue();
					model.setValueAt(isSelectedRelevance, i, 6);
					if(after&&!isSelectedRelevance)
					{
						if(updateNow)
							this.query.removePublicationToSelctedPublications(id);
						selectedCount--;
					}
					else if(!after&&isSelectedRelevance)
					{
						if(updateNow)
							this.query.addPublicationToSelctedPublications(id);
						selectedCount++;
						relevant.add(id);
					}
						
				}
			}
			publicationTable.repaint();
		}
		if(updateNow)
			updateAllSelectionListenes();
		return relevant;

	}
	
	private Set<Integer> selectRelated(boolean updateNow) throws SQLException, DatabaseLoadDriverException
	{
		Set<Integer> related = new HashSet<Integer>();
		boolean isSelectedRelated = jCheckBoxRelated.isSelected();
		boolean isSelectedAll = jCheckBoxAll.isSelected();
		if(isSelectedAll)
		{
			
		}
		else
		{
			DefaultTableModel model = ((DefaultTableModel) publicationTable.getOriginalTableModel());

			for(int i=0;i<this.publicationTable.getOriginalTableModel().getRowCount();i++)
			{
				int id = pubs.get(i).getID();
				RelevanceType relevance = this.query.getDocumentRelevanceType(id);
				if(relevance == RelevanceType.related)
				{
					boolean after = ((Boolean) publicationTable.getOriginalTableModel().getValueAt(i,6)).booleanValue();
					model.setValueAt(isSelectedRelated, i, 6);
					if(after&&!isSelectedRelated)
					{
						if(updateNow)
							this.query.removePublicationToSelctedPublications(id);
						selectedCount--;
					}
					else if(!after&&isSelectedRelated)
					{
						if(updateNow)
							this.query.addPublicationToSelctedPublications(id);
						selectedCount++;
						related.add(id);
					}
				}
				else
				{

				}
			}
		}
		if(updateNow)
			updateAllSelectionListenes();
		return related;
	}
	
	private Set<Integer> selectIrrelevant(boolean updateNow) throws SQLException, DatabaseLoadDriverException
	{
		Set<Integer> irrelevant = new HashSet<Integer>();
		boolean isSelectedIrelevante = jCheckBoxIrrelevant.isSelected();
		boolean isSelectedAll = jCheckBoxAll.isSelected();
		if(isSelectedAll)
		{
			
		}
		else
		{
			DefaultTableModel model = ((DefaultTableModel) publicationTable.getOriginalTableModel());

			for(int i=0;i<this.publicationTable.getOriginalTableModel().getRowCount();i++)
			{
				int id = pubs.get(i).getID();
				RelevanceType relevance = this.query.getDocumentRelevanceType(id);
				if(relevance == RelevanceType.irrelevant)
				{				
					boolean after = ((Boolean) publicationTable.getOriginalTableModel().getValueAt(i,6)).booleanValue();
					model.setValueAt(isSelectedIrelevante, i, 6);
					if(after&&!isSelectedIrelevante)
					{
						if(updateNow)
							this.query.removePublicationToSelctedPublications(id);
						selectedCount--;
					}
					else if(!after&&isSelectedIrelevante)
					{
						if(updateNow)
							this.query.addPublicationToSelctedPublications(id);
						selectedCount++;
						irrelevant.add(id);
					}
				}
				else
				{

				}
			}
			publicationTable.repaint();
		}
		if(updateNow)
			updateAllSelectionListenes();
		return irrelevant;
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
		private JButton viewButton;
	
		public ButtonTablePDFRenderer()
		{
			viewButton = new JButton();
		}
		
		public void whenClick() {
			
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if((Boolean) value)
			{
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
			}
			else
			{
				viewButton.setIcon(null);
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
	

	

	class ButtonTablePMIDRender implements TableCellRenderer{

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
		String externalLink = pubs.get(publicationTable.getSelectedRowsInOriginalModel()[0]).getExternalLink();
		if(externalLink==null || externalLink.length() < 1)
		{
			Workbench.getInstance().warn("The document does not contains external link... Please update External Link in Publication Resume GUI");
		}
		else
		{
			Help.internetAccess(externalLink);
		}
	}

	@Override
	public void updateSelectionPublication(int listenerID) {
		if(listenerID!=2)
		{
			int i=0;
			for(IPublication pub:this.pubs)
			{
				boolean bool = this.query.publicationINSelectingPublication(pub.getID());
				publicationTable.getOriginalTableModel().setValueAt(bool, i, 6);
				i++;
			}
		}
	}
	
	private void changePublicationSelected(TableModelEvent arg0) {
		int selectRow = arg0.getFirstRow();
		if(arg0.getColumn() == 6)
		{
			boolean isselecetd = (Boolean) publicationTable.getValueAt(selectRow, arg0.getColumn());
			int id = pubs.get(selectRow).getID();
			if(isselecetd)
			{
				query.addPublicationToSelctedPublications(id);
			}
			else
			{
				query.removePublicationToSelctedPublications(id);
			}
			updateAllSelectionListenes();
		}
		
	}

	private void updateAllSelectionListenes() {
		availableToChange = false;
		this.query.updateAllSelectionListeners(2);
		availableToChange = true;
	}
	
	private JPanel getJPanelRelevance() {
		if(jPanelRelevance == null) {
			jPanelRelevance = new JPanel();
			jPanelRelevance.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Relevance Details", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelRelevanceLayout = new GridBagLayout();
			jPanelRelevanceLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelRelevanceLayout.rowHeights = new int[] {7, 7, 7, 20, 7};
			jPanelRelevanceLayout.columnWeights = new double[] {0.5, 0.0, 0.2};
			jPanelRelevanceLayout.columnWidths = new int[] {7, 20, 7};
			jPanelRelevance.setLayout(jPanelRelevanceLayout);
			{
				jLabelRelevant = new JLabel();
				jPanelRelevance.add(jLabelRelevant, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelRelevant.setText("Relevant :");
			}
			{
				relevantsTextField = new JTextField();
				jPanelRelevance.add(relevantsTextField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			}
			{
				jLabelRelated = new JLabel();
				jPanelRelevance.add(jLabelRelated, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelRelated.setText("Related :");
				
			}
			{
				relatedTextField = new JTextField();
				jPanelRelevance.add(relatedTextField, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			}
			{
				jLabelIrrelevant = new JLabel();
				jPanelRelevance.add(jLabelIrrelevant, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelIrrelevant.setText("Irrelevant :");
			}
			{
				irrelevantTextField = new JTextField();
				jPanelRelevance.add(irrelevantTextField, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
				jPanelRelevance.add(getJLabelWithout(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRelevance.add(getJTextFieldWithout(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
				jPanelRelevance.add(getJPanelRelevanceInfoRelevant(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRelevance.add(getJPanelRelevanceInfoRelated(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRelevance.add(getJPanelRelevanceInfoIrrelevant(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRelevance.add(getJPanelRelevanceInfoNone(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jPanelRelevance;
	}
	
	private JLabel getJLabelWithout() {
		if(jLabelWithout == null) {
			jLabelWithout = new JLabel();
			jLabelWithout.setText("None :");
		}
		return jLabelWithout;
	}
	
	private JTextField getJTextFieldWithout() {
		if(jTextFieldWithout == null) {
			jTextFieldWithout = new JTextField();
			jTextFieldWithout.setText("0.0");
		}
		return jTextFieldWithout;
	}

	@Override
	public void changeRelevanceListner() {
		int i=0;
		int a=0, b=0, c=0 , d=0;
		try {
			for(IPublication pub:this.pubs)
			{
				RelevanceType rt;
				rt = this.query.getDocumentRelevanceType(pub.getID());
				if(rt == RelevanceType.irrelevant)
					a++;
				else if(rt == RelevanceType.related)
					b++;
				else if(rt == RelevanceType.relevant)// relevant
					c++;
				else
					d++;
				publicationTable.getOriginalTableModel().setValueAt((RelevanceType) rt , i, 2);
				i++;
			}
			this.irrelevantTextField.setText(String.valueOf(a));
			this.relatedTextField.setText(String.valueOf(b));
			this.relevantsTextField.setText(String.valueOf(c));
			this.jTextFieldWithout.setText(String.valueOf(d));
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private RelevanceInfoPane getJPanelRelevanceInfoRelevant() {
		if(jPanelRelevanceInfoRelevant == null) {
			jPanelRelevanceInfoRelevant = new RelevanceInfoPane(RelevanceType.relevant);
		}
		return jPanelRelevanceInfoRelevant;
	}
	
	private RelevanceInfoPane getJPanelRelevanceInfoRelated() {
		if(jPanelRelevanceInfoRelated == null) {
			jPanelRelevanceInfoRelated = new RelevanceInfoPane(RelevanceType.related);
		}
		return jPanelRelevanceInfoRelated;
	}
	
	private RelevanceInfoPane getJPanelRelevanceInfoIrrelevant() {
		if(jPanelRelevanceInfoIrrelevant == null) {
			jPanelRelevanceInfoIrrelevant = new RelevanceInfoPane(RelevanceType.irrelevant);
		}
		return jPanelRelevanceInfoIrrelevant;
	}
	
	private RelevanceInfoPane getJPanelRelevanceInfoNone() {
		if(jPanelRelevanceInfoNone == null) {
			jPanelRelevanceInfoNone = new RelevanceInfoPane(RelevanceType.none);
		}
		return jPanelRelevanceInfoNone;
	}

}