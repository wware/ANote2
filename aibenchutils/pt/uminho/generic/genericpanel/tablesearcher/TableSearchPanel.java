/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.generic.genericpanel.tablesearcher;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import pt.uminho.anote2.aibench.utils.conf.Delimiter;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.tablesearcher.searchPanel.CsvFileChooser;
import pt.uminho.generic.genericpanel.tablesearcher.searchPanel.SearchBasicOptionsPanel;
import pt.uminho.generic.genericpanel.tablesearcher.searchPanel.SearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;

public class TableSearchPanel<I> extends JPanel implements KeyListener,ActionListener{

	private static final long serialVersionUID = 1L;
	
	public static final String SAVE_OPTION_BUTTON_ACTION_COMMAND = "savecomandoption";
	public static final String SEARCH_OPTION_BUTTON_ACTION_COMMAND = "searchOptionButtonActionCommand";
	protected TableSearchModel originalTabelModel;
	
	protected JTable mainTable;
	protected SearchPanel searchPanel;
	protected JToggleButton searchOptionsButton;
	protected SelectColumnPanel selectColumnPanel;
	protected FilterRowPanel filterRowPanel;
	
	protected JButton saveButton;
	protected boolean isPermissionSave = true;
	protected CsvFileChooser filechooser;
	
	protected Set<Integer> validColumnIndex= null;
	
	protected Set<Integer> doubleColumns = null;
	
	public TableSearchPanel(TableModel tableModel,boolean isPermissionSave) {
		this(tableModel);
		this.isPermissionSave = isPermissionSave;
	}
	
	public TableSearchPanel(boolean isPermissionSave){
		this();
		this.isPermissionSave = isPermissionSave;
	}
	
	public TableSearchPanel(TableModel tableModel) {
		originalTabelModel = new TableSearchModel(tableModel);
		mainTable = new JTable();
		mainTable.setAutoCreateRowSorter(true);
		
		String[] columnIdentifiersArray = getColumnIdentifiers(originalTabelModel);
		
		
		selectColumnPanel = new SelectColumnPanel(columnIdentifiersArray);
		selectColumnPanel.addColumnButtonsActionListener(this);
		selectColumnPanel.setBorder(BorderFactory.createTitledBorder(null,
				"Searchable Columns", TitledBorder.LEADING,
				TitledBorder.BELOW_TOP));
		if(originalTabelModel.hasNumValues()){
			filterRowPanel = new FilterRowPanel();
			filterRowPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Filter Rows", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			filterRowPanel.addRemoveZeroRowsCheckBoxActionListener(this);
		}

		
		initGUI();
		searchPanel.addSearchTextFieldKeyListener(this);
		searchOptionsButton.addActionListener(this);
		searchPanel.addActionListener(this);
		mainTable.setModel(originalTabelModel);
		atributeDoubleColumnsInTable();
	}
	
	public TableSearchPanel(TableModel tableModel, TableRowSorter<TableModel> rowSorter){
		originalTabelModel = new TableSearchModel(tableModel);
		mainTable = new JTable();

		
		String[] columnIdentifiersArray = getColumnIdentifiers(originalTabelModel);
		selectColumnPanel = new SelectColumnPanel(columnIdentifiersArray);
		selectColumnPanel.addColumnButtonsActionListener(this);
		selectColumnPanel.setBorder(BorderFactory.createTitledBorder(null, "Searchable Columns", TitledBorder.LEADING, TitledBorder.BELOW_TOP));

		

		if(originalTabelModel.hasNumValues()){
			filterRowPanel = new FilterRowPanel();
			filterRowPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Filter Rows", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			filterRowPanel.addRemoveZeroRowsCheckBoxActionListener(this);
		}

		initGUI();
		searchPanel.addSearchTextFieldKeyListener(this);
		searchOptionsButton.addActionListener(this);
				
		mainTable.setRowSorter(rowSorter);
		searchPanel.addActionListener(this);
		mainTable.setModel(originalTabelModel);
		atributeDoubleColumnsInTable();
	}
	
	
	 static private String[] getColumnIdentifiers(TableModel tableModel){
    	int numberOfColumns = tableModel.getColumnCount();
    	String[] columnsIdentifier = new String[numberOfColumns];
    	
    	for(int i = 0; i < numberOfColumns; i++){
    		columnsIdentifier[i] = tableModel.getColumnName(i);
    	}
    	return columnsIdentifier;
    }
	 
	 
	public TableSearchPanel(){
		originalTabelModel = null;
		mainTable = new JTable();
		mainTable.setAutoCreateRowSorter(true);
		initGUI();
		searchPanel.addSearchTextFieldKeyListener(this);
		searchOptionsButton.addActionListener(this);
		searchOptionsButton.setEnabled(false);
		searchPanel.addActionListener(this);

	}

	protected Set<Integer> createValidColumnSet(TableModel originalTableModel) {
		int numberOfColumns = originalTableModel.getColumnCount();
		Set<Integer> validColumnSet = new TreeSet<Integer>();

		for (int i = 0; i < numberOfColumns; i++)
			validColumnSet.add(i);

		return validColumnSet;
	}

	public void initGUI() {
		{
			
			
			GridBagLayout thisLayout1 = new GridBagLayout();
			thisLayout1.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0};
			thisLayout1.rowHeights = new int[] {0, 0, 0, 0, 100, 7};
			thisLayout1.columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};
			thisLayout1.columnWidths = new int[] { 7, 7, 7, 7 };

			this.setLayout(thisLayout1);
			{
				searchPanel = new SearchPanel();
				this.add(searchPanel, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				searchOptionsButton = new JToggleButton("Options"); // TODO por
																	// gráfico
				searchOptionsButton.setActionCommand(SEARCH_OPTION_BUTTON_ACTION_COMMAND);
				
				
				
				if(isPermissionSave){
					saveButton = new JButton("Save");
					this.add(saveButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					saveButton.setPreferredSize(new java.awt.Dimension(90, 25));
					saveButton.setActionCommand(SAVE_OPTION_BUTTON_ACTION_COMMAND);
					filechooser = new CsvFileChooser();
					
					FileNameExtensionFilter filtertxt = new FileNameExtensionFilter(
					        "Text file - *.txt", "txt");
				
					filechooser.setFileFilter(filtertxt);
					
					
					
					saveButton.addActionListener(this);

					this.add(searchOptionsButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					searchOptionsButton.setPreferredSize(new java.awt.Dimension(90, 25));
				}else
					this.add(searchOptionsButton, new GridBagConstraints(2, 1, -1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				
				JScrollPane tableScrollPane = new JScrollPane(mainTable);
				
				tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				//tableScrollPane.setHorizontalScrollBar(new JScrollBar());

				this.add(tableScrollPane, new GridBagConstraints(1, 3, 2, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(tableScrollPane, new GridBagConstraints(1, 4, 2, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			}
			
		
		}
	
	}

	public void setValidColumnIndex(Set<Integer> validColumnIndex) {
		this.validColumnIndex = validColumnIndex;
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {


		processSearch();

	}

	private void search(String searchString, boolean caseSensitiveSearch,
			boolean matchExactSearchTerm) {
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) mainTable.getRowSorter();
		
//		if (regExpFilterZeros == ""){
//			for(int i = 1; i < GlobalOptions.getDoublePrecision()-1; i++){
//				regExpFilterZeros+=i+"|";	
//			}
//			regExpFilterZeros+=GlobalOptions.getDoublePrecision();
//		}
		
		int num = selectColumnPanel.getSelectedIndexes().size();
		int[] selectedColum = new int[num];
		
		for(int i =0; i<num;i++) selectedColum[i] = (int)selectColumnPanel.getSelectedIndexes().get(i);
		
		String regexp = createRegExp(searchString, caseSensitiveSearch, matchExactSearchTerm);
		
		List< RowFilter<Object,Object>> andFilter = new ArrayList<RowFilter<Object,Object>>(2);
	
		if(filterRowPanel!=null && filterRowPanel.isRemoveZeroValueSelected())
			andFilter.add(RowFilter.notFilter(RowFilter.regexFilter("(^0(\\.0)?$)",selectedColum)));
		
		andFilter.add(RowFilter.regexFilter(regexp, selectedColum));
		sorter.setRowFilter(RowFilter.andFilter(andFilter));
		
		
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

	public void setModel(TableModel tableModel) {
		originalTabelModel = new TableSearchModel(tableModel);
		
		String[] columnIdentifiersArray = getColumnIdentifiers(originalTabelModel);

		selectColumnPanel = new SelectColumnPanel(columnIdentifiersArray);
		selectColumnPanel.addColumnButtonsActionListener(this);
		searchOptionsButton.setEnabled(true);

		selectColumnPanel.setBorder(BorderFactory.createTitledBorder(null,
				"Searchable Columns", TitledBorder.LEADING,
				TitledBorder.BELOW_TOP));

		if(originalTabelModel.hasNumValues()){
			filterRowPanel = new FilterRowPanel();
			filterRowPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Filter Rows", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			filterRowPanel.addRemoveZeroRowsCheckBoxActionListener(this);
		}

		mainTable.setModel(originalTabelModel);
		atributeDoubleColumnsInTable();
		mainTable.updateUI();
	}



	public TableModel getModel() {
		return mainTable.getModel();
	}

	public TableModel getOriginalTableModel() {
		return originalTabelModel.getOriginalTableModel();
	}
	
	public void setTableSelectionMode(int selectionMode) {
		mainTable.setSelectionMode(selectionMode);
	}

	public ListSelectionModel getSelectionModel() {
		return mainTable.getSelectionModel();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return mainTable.getValueAt(rowIndex, columnIndex);
	}

	// ////
	public JTable getMainTable() {
		return this.mainTable;
	}

	// //

	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();

		if (actionCommand.equals(SEARCH_OPTION_BUTTON_ACTION_COMMAND))
			searchOptionButtonPressed();
		else if (actionCommand.equals(SelectColumnPanel.CHANGE_BUTTON_COMAND)){
			JRadioButton button = (JRadioButton)event.getSource();
			if (!selectColumnPanel.isAnythingSelected())
				button.setSelected(true);
			else
				processSearch();
			
//			if()
		}else if (actionCommand
				.equals(FilterRowPanel.REMOVE_ZERO_ROWS_CHECKBOX_ACTION_COMMAND))
			processSearch();
		else if(actionCommand.equals(SearchBasicOptionsPanel.Case_Sensitive_Pressed))
			processSearch();
		else if(actionCommand.equals(SearchBasicOptionsPanel.Match_Whole_Word_Pressed))
			processSearch();	
		else if(actionCommand.equals(SAVE_OPTION_BUTTON_ACTION_COMMAND)){
			
			int returnVal = filechooser.showSaveDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            
	        	File file = new File(filechooser.getSelectedFile().getAbsolutePath());
	        	if (!filechooser.accept(file))
	        		file = new File(filechooser.getSelectedFile().getAbsolutePath()+".txt");

	            Boolean isOnlyWriteVisibleData = filechooser.isOnlyWriteVisibleData();
	            Boolean onlyAllowedColumns = filechooser.isOnlyAllowedColumns();
	            
	            try {
					saveDataInFile(file,filechooser.getDelimiter(),isOnlyWriteVisibleData,onlyAllowedColumns);
				} catch (IOException e) {
					Workbench.getInstance().error(e);
					e.printStackTrace();
				}
	            
	            
	        }
		}

	}

	private void saveDataInFile(
			File file, 
			Delimiter delimiter,
			Boolean isOnlyWriteVisibleData,
			Boolean onlyAllowedColumns) throws IOException {
		
		if(isOnlyWriteVisibleData || onlyAllowedColumns)
			saveOnlyVisibleData(file, delimiter,onlyAllowedColumns);
		else
			saveAllData(file, delimiter);
		
	}
	
	
	private void saveOnlyVisibleData(File file, Delimiter delim, Boolean onlyAllowedColumns) throws IOException{
		
		FileWriter filew = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(filew);
		ArrayList<Integer> allowedColumns = selectColumnPanel.getSelectedIndexes();
		
		for(int i = 0; i < mainTable.getRowCount(); i++){
			int row = mainTable.getRowSorter().convertRowIndexToModel(i);
			writer.write(originalTabelModel.getValueToSave(row, allowedColumns.get(0)).toString());
			for(int j =1; j< allowedColumns.size();j++){
				writer.write(delim.toString()+originalTabelModel.getValueToSave(row, allowedColumns.get(j)).toString());
			}
			writer.write("\n");
		}
		writer.close();
		filew.close();
		
	}
	
	
	private void saveAllData(File file, Delimiter delim) throws IOException{
		
		FileWriter filew = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(filew);		
		for(int i = 0; i < originalTabelModel.getRowCount(); i++){
			for(int j =0; j< originalTabelModel.getColumnCount();j++){
				Object valueToWrite = originalTabelModel.getValueToSave(i, j);
				if(valueToWrite == null)
					writer.write(delim.toString());
				else
					writer.write(originalTabelModel.getValueToSave(i, j).toString()+delim.toString());
			}
			writer.write("\n");
		}
		writer.close();
		filew.close();
	}
	

	protected void searchOptionButtonPressed() {
		boolean isSearchOptionsButtonTopggle = searchOptionsButton.isSelected();

		if (isSearchOptionsButtonTopggle) {
			add(selectColumnPanel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			if(filterRowPanel!=null)
				add(filterRowPanel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		} else {
			remove(selectColumnPanel);
			if(filterRowPanel!=null)
				remove(filterRowPanel);
		}
		updateUI();
	}

	private String createRegExp(String searchString, boolean caseSensitiveSearch,
			boolean matchExactSearchTerm){

		String pattern = searchString;
		if(!pattern.equals("")){
			if(matchExactSearchTerm){
				pattern = "^"+pattern+"$";
			}

			if(!caseSensitiveSearch){
				pattern = "(?i)"+pattern;
			}
		}
		return pattern;
	}

	protected void processSearch() {
		
			String searchString = searchPanel.getSearchText();
			boolean caseSensitiveSearch = searchPanel.isSearchCaseSensitive();
			boolean matchExactSearchTerm = searchPanel.isSearchWholeWord();
			search(searchString, caseSensitiveSearch,
					matchExactSearchTerm);
			
			mainTable.updateUI();
	}

	public int[] getSelectedRowsInOriginalModel() {
		int[] index;
		index = mainTable.getSelectedRows();
		for (int i = 0; i < index.length; i++)
			index[i] = mainTable.getRowSorter().convertRowIndexToModel(index[i]);

		return index;
	}

	public void adaptColumnWidth(boolean isResizable){
		mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		for(int i = 0;i < mainTable.getModel().getColumnCount();i++)
			setColumnWidth(i,isResizable);
		
		mainTable.updateUI();
	}
	
	protected void setColumnWidth(int col,boolean resizable) {

		// The margin to place either side of the component in a cell
		int margin = 20;

		// Get the column and make it non resiable by the user
		TableColumn column = mainTable.getColumnModel().getColumn(col);
		column.setResizable(resizable);

		// Set the width to be the header width of this column
		
//		Component comp = headerRenderer.getTableCellRendererComponent(mainTable,column.getHeaderValue(), false, false, 0, 0);
		int width = 150;//getMaximumRenderWidth();//comp.getMaximumSize().width;

		
		
		// Set the width to be the larger of the header or a cell width in this
		// column
		for (int i = 0; i < mainTable.getRowCount(); i++) {
			TableCellRenderer renderer = mainTable.getCellRenderer(i, col);
			Component c = renderer.getTableCellRendererComponent(mainTable,getValueAt(i, col), false, false, i, col);
			width = Math.max(width, c.getPreferredSize().width);
		}

		// Usethe width of the largest component (header or cell) plus a margin
		// on other side.
		column.setPreferredWidth(width + 2 * margin);
		column.setMinWidth(width + 2 * margin);
		column.setMaxWidth(width + 2 * margin);

	}

	private int getMaximumRenderWidth() {
		TableModel mainTableModel = mainTable.getModel();
		int maximumWidth = 0;
		for(int i = 0; i< mainTableModel.getColumnCount();i++)
			for(int j = 0; j < mainTableModel.getRowCount();j++){
				TableCellRenderer cellRenderer = mainTable.getCellRenderer(j,i);
				Component component = cellRenderer.getTableCellRendererComponent(mainTable,null,false,false,0,0);
				int width = component.getWidth();
				if(width > maximumWidth)
					maximumWidth = width+100;
		}
		
		return maximumWidth;
	}
	
	private void atributeDoubleColumnsInTable(){
		
		TableModel model = mainTable.getModel();
		for(int i =0; i < model.getColumnCount(); i++){
			if(model.getColumnClass(i).equals(Double.class)){
				mainTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
			        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			            //JLabel teste = new JLabel();
			        	Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			            // Create and display formatted double in cell
			            DecimalFormat form = (DecimalFormat)NumberFormat.getInstance();
			    		form.setMaximumFractionDigits(GlobalOptions.getDoublePrecision());
			    		form.setMinimumFractionDigits(1);
			    		if(value!=null)
			    			setText(((Double)value).toString());//setText(form.format((Double)value));
			    		else
			    			setText("");
			            // Reset right justify again
			            setHorizontalAlignment(JLabel.CENTER);
			            return renderer;
			        }
			    });
			}
		}
		
	}


}