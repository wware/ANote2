package pt.uminho.anote2.aibench.publicationmanager.gui.help;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.PublicationResumeGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.generic.comparate.AlphanumericComparator;
import pt.uminho.generic.components.table.tablerowsorter.TableRowSorterWhitOtherTypes;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;


public class SelectQueryDocumentPanel extends ASelectQueryDocumentsPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QueryInformationRetrievalExtension query;
	private List<IPublication> pubs;
	private TableSearchPanel jtableSearch;
	private Set<IPublication> formCorpus;

	public SelectQueryDocumentPanel(QueryInformationRetrievalExtension query) throws SQLException, DatabaseLoadDriverException
	{
		this.query = query;
		this.pubs = query.getPublications();
		if(query!=null)
		{
			initGUI();
		}
	}
	

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createTitledBorder("Select Publications"));
			this.add(getTableSearch(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}


	@Override
	public boolean validateOptions() {
		getPublicationsForm();
		if(formCorpus.size() > 0)
		{
			return true;
		}
		else
		{
			Workbench.getInstance().warn("Please select at least one publication");
			return false;
		}
	}


	private void getPublicationsForm() {
		formCorpus = new HashSet<IPublication>();
		for(int i=0;i<this.jtableSearch.getOriginalTableModel().getRowCount();i++)
		{
			boolean selected = ((Boolean) jtableSearch.getOriginalTableModel().getValueAt(i,4)).booleanValue();
			if(selected)
			{
				formCorpus.add(pubs.get(i));
			}
		}
	}

	@Override
	public Set<IPublication> getPublications() {
		if(validateOptions())
		{
			return formCorpus;
		}
		return null;
	}
	
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Details","Title","PMID/OtherID","PDF",""};
		DefaultTableModel tableModel = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 2)
					return Object.class;
				if(columnIndex == 3 || columnIndex == 4 )
					return Boolean.class;
				return String.class;
			}
			
			public boolean isCellEditable(int row, int col) {  
				if (col == 3 || col == 1) {  
					return false;  
				} else {  
					return true;  
				}         
			} 

		};
		
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;	
		data = new Object[this.pubs.size()][5];	
		for(IPublication pub: this.pubs)
		{
			data[i][0] = "";
			data[i][1] = pub.getTitle();
			if(pub.getOtherID()!=null && !pub.getOtherID().equals("") && Utils.isIntNumber(pub.getOtherID()))				
				data[i][2] = Integer.valueOf(pub.getOtherID());
			else if(pub.getOtherID()!=null && !pub.getOtherID().equals("") )
				data[i][2] = pub.getOtherID();
			else
				data[i][2] = 0;
			data[i][3] = new Boolean(pub.isPDFAvailable());
			if(query.publicationINSelectingPublication(pub.getID()))
			{
				data[i][4] = new Boolean(true);
			}
			else
			{
				data[i][4] = new Boolean(false);
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
		jtable.getColumnModel().getColumn(2).setMaxWidth(PreferencesSizeComponents.pmidotherIDfieldmaxWith);
		jtable.getColumnModel().getColumn(2).setMinWidth(PreferencesSizeComponents.pmidotherIDfieldminWith);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(PreferencesSizeComponents.pmidotherIDfieldpreferredWith);
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.pdfavailablefieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.pdfavailablefieldminWith);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.pdfavailablefieldpreferredWith);;
		jtable.getColumnModel().getColumn(4).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(4).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);	
		jtable.setRowHeight(20);
		jtable.getColumn("PMID/OtherID").setCellRenderer(new ButtonTablePMIDRender());
		jtable.getColumn("PMID/OtherID").setCellEditor(new ButtonTablePMIDCellEditor());
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/find.png"));
		jtable.getColumn("Details").setCellRenderer(new ButtonTableFindRenderer(icon));
		jtable.getColumn("Details").setCellEditor(new ButtonTableFindCellEditor());
		jtable.getColumn("PDF").setCellRenderer(new ButtonTablePDFRenderer());
		jtable.getColumn("PDF").setCellEditor(new ButtonTablePDFCellEditor());
		Map<Integer, Comparator<?>> columnComparators = new HashMap<Integer, Comparator<?>>();
		columnComparators.put(2, new AlphanumericComparator());
		TableRowSorter<TableModel> sortedModel = new TableRowSorterWhitOtherTypes(columnComparators);
		sortedModel.setModel(jtable.getModel());
		jtable.setRowSorter(sortedModel);
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
	

	class ButtonTablePMIDRender extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		@Override
		public void whenClick() {
			try {
				lanchPMID();
			} catch (IOException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if(!value.toString().equals("0"))
			{
				this.setText(value.toString());
				this.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						whenClick();
					}
				});
			}
			else
			{
				this.setEnabled(false);
			}
			return this;
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
		String link = this.pubs.get(jtableSearch.getSelectedRowsInOriginalModel()[0]).getExternalLink();
		if(link!=null && link.length() > 0)
		{
			Help.internetAccess(link);
		}
		else
		{
			Workbench.getInstance().warn("The document does not contains external link... Please update External Link in Publication Resume GUI");
		}
	}
	
	private void initViewGUI(){
		IPublication publication = pubs.get(jtableSearch.getSelectedRowsInOriginalModel()[0]);
		new PublicationResumeGUI(query,publication);
	}
	
	private JPanel getTableSearch() {
		if(jtableSearch == null) {
			jtableSearch = new TableSearchPanel(fillModelPubTable(),false);
			constructQueryTable(jtableSearch.getMainTable());
		}
		return jtableSearch;
	}


	@Override
	public void updateSelectedPublications(Set<IPublication> publications) {
		Set<Integer> ids = new HashSet<Integer>();
		for(IPublication pub:publications)
			ids.add(pub.getID());
		for(int i=0;i<this.jtableSearch.getMainTable().getModel().getRowCount();i++)
		{
			IPublication pub = pubs.get(i);
			if(ids.contains(pub.getID()))
			{
				this.jtableSearch.getMainTable().setValueAt(true, i, 4);
			}
			else
			{
				this.jtableSearch.getMainTable().setValueAt(false, i, 4);
			}
		}
	}

}
