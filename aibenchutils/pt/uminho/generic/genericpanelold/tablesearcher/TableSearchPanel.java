package pt.uminho.generic.genericpanelold.tablesearcher;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import pt.uminho.generic.components.table.tablelineremover.TableModelLineRemover;
import pt.uminho.generic.components.table.tablesearcher.TableModelSearcher;
import pt.uminho.generic.genericpanelold.tablesearcher.searchPanel.SearchPanel;

public class TableSearchPanel extends JPanel implements KeyListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	public static final String SEARCH_OPTION_BUTTON_ACTION_COMMAND="searchOptionButtonActionCommand";
	protected TableModelSearcher tableModelSearcher;
	protected TableModelLineRemover tableModelLineRemover;
	protected JTable mainTable;
	protected SearchPanel searchPanel;
	protected JToggleButton searchOptionsButton;
	protected SelectColumnPanel selectColumnPanel;	
	//protected FilterRowPanel filterRowPanel;
	
	public TableSearchPanel(TableModel tableModel){
		createTableModelSearcher(tableModel);
		String[] columnIdentifiersArray = tableModelSearcher.getColumnIdentifiers(); 
		selectColumnPanel = new SelectColumnPanel(columnIdentifiersArray);
		selectColumnPanel.addColumnButtonsActionListener(this);
		selectColumnPanel.setBorder(BorderFactory.createTitledBorder(null, "Searchable Columns", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
		//filterRowPanel = new FilterRowPanel();
		//filterRowPanel.setBorder(BorderFactory.createTitledBorder(null, "Filter Rows", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
		initGUI();
		searchPanel.addSearchTextFieldKeyListener(this);
		searchOptionsButton.addActionListener(this);
		//filterRowPanel.addRemoveZeroRowsCheckBoxActionListener(this);
	}
	
	public TableSearchPanel(){
		tableModelSearcher = null;
		
		mainTable = new JTable();				
		
		initGUI();
		searchPanel.addSearchTextFieldKeyListener(this);
		searchOptionsButton.addActionListener(this);
		searchOptionsButton.setEnabled(false);
	}
	
	protected Set<Integer> createValidColumnSet(TableModel originalTableModel){
		int numberOfColumns = originalTableModel.getColumnCount();
		Set<Integer> validColumnSet = new TreeSet<Integer>();
		
		for(int i = 0;i < numberOfColumns;i++)
			validColumnSet.add(i);
		
		return validColumnSet;
	}

	public void initGUI(){
	{	
		GridBagLayout thisLayout1 = new GridBagLayout();
		thisLayout1.rowWeights = new double[] {0.0,0.0,0.0,0,0.1,0.0};
		thisLayout1.rowHeights = new int[] {7, 38,0,0,100,7};
		thisLayout1.columnWeights = new double[] {0.0, 0.1,0,0.0};
		thisLayout1.columnWidths = new int[] {7, 7,7,7};
		
		this.setLayout(thisLayout1);
		{
			searchPanel = new SearchPanel();
			add(searchPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			searchOptionsButton = new JToggleButton("Advanced Options"); //TODO por gráfico
			searchOptionsButton.setActionCommand(SEARCH_OPTION_BUTTON_ACTION_COMMAND);
			this.add(searchOptionsButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			JScrollPane tableScrollPane = new JScrollPane(mainTable);
			this.add(tableScrollPane, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
//		setLayout(new BorderLayout());
//		mainTable = new JTable();
//		mainTable.setModel(tableModelSearcher);
//		JScrollPane scrollPane = new JScrollPane(mainTable);
//		add(scrollPane,BorderLayout.CENTER);
	}
	
	public void setValidColumnIndex(Set<Integer> validColumnIndex){
		String[] columnIdentifiersArray = tableModelSearcher.getColumnIdentifiers(); 
		String[] newColumnIdentifierArray = new String[validColumnIndex.size()];
		int i = 0;
		for(Integer index:validColumnIndex){
			newColumnIdentifierArray[i] = columnIdentifiersArray[index];
			i++;
		}
		selectColumnPanel = new SelectColumnPanel(newColumnIdentifierArray);
		tableModelLineRemover.setValidColumnIndexSet(validColumnIndex);
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		processSearch();
	}

	@Override
	public void keyTyped(KeyEvent event){
		
	}

	public void setModel(TableModel tableModel) {
		if(tableModelSearcher == null)
			createTableModelSearcher(tableModel);
		else
			tableModelSearcher.setOriginalTableModel(tableModel);
		
		String[] columnIdentifiersArray = tableModelSearcher.getColumnIdentifiers(); 
		selectColumnPanel =  new SelectColumnPanel(columnIdentifiersArray);
		selectColumnPanel.addColumnButtonsActionListener(this);
		searchOptionsButton.setEnabled(true);
		selectColumnPanel.setBorder(BorderFactory.createTitledBorder(null, "Searchable Columns", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
		mainTable.updateUI();
	}

	protected void createTableModelSearcher(TableModel tableModel) {
		Set<Integer> validColumnSet = createValidColumnSet(tableModel);
		tableModelLineRemover = new TableModelLineRemover(tableModel,validColumnSet);
		tableModelSearcher = new TableModelSearcher(tableModelLineRemover,validColumnSet);
		mainTable = new JTable(tableModelSearcher);		
	}

	public TableModel getModel(){
		return tableModelSearcher.getOriginalTableModel();
	}

	public void setTableSelectionMode(int selectionMode) {
		mainTable.setSelectionMode(selectionMode);
	}

	public ListSelectionModel getSelectionModel() {
		return mainTable.getSelectionModel();
	}

	public Object getValueAt(int rowIndex, int columnIndex){
		return mainTable.getValueAt(rowIndex,columnIndex);
	}

	//////
	public JTable getMainTable(){return mainTable;}
	////
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		if(actionCommand.equals(SEARCH_OPTION_BUTTON_ACTION_COMMAND))
			searchOptionButtonPressed();
		else if(actionCommand.equals(SelectColumnPanel.CHANGE_BUTTON_COMAND))
			selectedRowsButtonPressed();
//		else if(actionCommand.equals(FilterRowPanel.REMOVE_ZERO_ROWS_CHECKBOX_ACTION_COMMAND))
//			removeZeroButtonPressed();
		
	}
	
//	protected void removeZeroButtonPressed() {
//		if(filterRowPanel.isRemoveZeroValueSelected()){
//			tableModelLineRemover.addToNonVisibleStringList("0.0");
//		}else 
//			tableModelLineRemover.clearNonVisibleStringList();
//		
//		processSearch();
//		mainTable.updateUI();
//		
//	}

	protected void selectedRowsButtonPressed() {
		List<Integer> columnIndex = selectColumnPanel.getSelectedIndex();
		tableModelSearcher.setValidColumnIndex(new TreeSet<Integer>(columnIndex));
	}

	protected void searchOptionButtonPressed(){
		boolean isSearchOptionsButtonTopggle = searchOptionsButton.isSelected();
		
		if(isSearchOptionsButtonTopggle){
			add(selectColumnPanel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		//	add(filterRowPanel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}else{
			remove(selectColumnPanel);
		//	remove(filterRowPanel);
		}
		updateUI();
	}
	
	protected void processSearch(){
		if(tableModelSearcher != null){
			String searchString = searchPanel.getSearchText();
			boolean caseSensitiveSearch = searchPanel.isSearchCaseSensitive();
			boolean matchExactSearchTerm = searchPanel.isSearchWholeWord() ;
			tableModelSearcher.search(searchString,caseSensitiveSearch,matchExactSearchTerm);
			mainTable.updateUI();
		}
	}
	
	public TableColumnModel getTableColumnModel(){
		return mainTable.getColumnModel(); 
	}
	
	public int getSelectedRow()
	{
		return mainTable.getSelectedRow();
	}

	public int getSelectedRowTrueIndex() {
		int selectedRow = mainTable.getSelectedRow();
		int tableSearchRowIndex = tableModelLineRemover.getModelRow(selectedRow);
		int trueRowIndex = tableModelSearcher.getModelRow(tableSearchRowIndex);
		return trueRowIndex;
	}
	
	public void setEditableColumn(Set<Integer> list)
	{
//		resultsTable = new JTable(){
//		
//		private static final long serialVersionUID = -2422590849569066990L;
//		
//		@Override
//		public boolean isCellEditable(int x,int y){
//			if(y==0||y==4)
//				return true;
//			return false;
//		}
//		
//		@SuppressWarnings("unchecked")
//		@Override
//		public Class getColumnClass(int column){
//			if(column == 4)
//				return Boolean.class;
//			return String.class;
//		}
//	};
	}
	
	public void setRowHeight(int size)
	{
		this.mainTable.setRowHeight(20);
	}
	
	public void setBackGroundColor(Color color)
	{
		mainTable.setSelectionForeground(color);
	}
}
