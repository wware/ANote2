package pt.uminho.generic.components.table.tablesearcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.TableModel;

import pt.uminho.generic.components.table.TableModelExtension;

public class TableModelSearcher extends TableModelExtension {
	private static final long serialVersionUID = 1L;
	protected List<Integer> validRowIndexList;
    protected Set<Integer> validColumnIndexSet;
    protected boolean isSearching;

    public TableModelSearcher(TableModel tableModel,Set<Integer> validColumnSet) {
        super(tableModel);
        this.validColumnIndexSet = validColumnSet;
        tableModel.addTableModelListener(new TableSearcherModelHandler(this));
        validRowIndexList = new ArrayList<Integer>();
        isSearching = false;
    }
    

    @Override
    public int getRowCount() {
        
    	if(validRowIndexList.size() == 0)
            return tableModel.getRowCount();
        
        if(validRowIndexList.size() > tableModel.getRowCount()){
        	validRowIndexList.clear();
        	return tableModel.getRowCount();
        }
        
        return validRowIndexList.size();
    }

    public void clearSearch(){
        validRowIndexList.clear();
    }

    public int getModelRow(int rowIndex){
    	if(validRowIndexList.size() == 0)
    		return rowIndex;
    	return validRowIndexList.get(rowIndex);
    }

    public void search(String searchString,boolean isCaseSensitive,boolean isMatchExactWord){
        clearSearch();
        if(searchString != null && !searchString.equals("")){
            isSearching = true;
            processSearch(searchString,isCaseSensitive,isMatchExactWord);
            isSearching = false;
            fireTableStructureChanged();
        }
    }

    protected void processSearch(String searchString,boolean isCaseSensitive,boolean isMatchExactWord){
    	 for(int j = 0; j < tableModel.getRowCount();j++){
    		 for(Integer columnIndex:validColumnIndexSet){
                String cellValue =  tableModel.getValueAt(j,columnIndex).toString();
                if(processTableCellSearch(isCaseSensitive, isMatchExactWord,searchString, cellValue)){
                    validRowIndexList.add(j);
                    break;
                }
            }
        }
    }
    
    
    protected boolean processTableCellSearch(boolean isCaseSensitive,boolean isMatchExactWord,String searchString,String cellValue){
		
    	if(isCaseSensitive && isMatchExactWord)
			return searchString.equals(cellValue);
		
    	if(isMatchExactWord)
    		return searchString.compareToIgnoreCase(cellValue) == 0;
    	    			
    	return cellValue.toLowerCase().contains(searchString.toLowerCase());
    }

    public boolean isSearching() {
        return isSearching;
    }
    
    public String[] getColumnIdentifiers(){
    	int numberOfColumns = tableModel.getColumnCount();
    	String[] columnsIdentifier = new String[numberOfColumns];
    	
    	for(int i = 0; i < numberOfColumns; i++){
    		columnsIdentifier[i] = tableModel.getColumnName(i);
    	}
    	return columnsIdentifier;
    }

	public void setValidColumnIndex(Set<Integer> validColumnIndexSet){
		this.validColumnIndexSet = validColumnIndexSet;
		fireTableStructureChanged();
	}

	public void setOriginalTableModel(TableModel tableModel) {
		clearSearch();
		this.tableModel = tableModel; 
		fireTableStructureChanged();
	}


	public void removeFromRowIndex(int rowIndex) {
		validRowIndexList.remove(new Integer(rowIndex));
		
	}
	

}
