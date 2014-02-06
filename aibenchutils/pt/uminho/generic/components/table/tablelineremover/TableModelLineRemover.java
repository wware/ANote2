package pt.uminho.generic.components.table.tablelineremover;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.table.TableModel;

import pt.uminho.generic.components.table.TableModelExtension;

public class TableModelLineRemover extends TableModelExtension{
	private static final long serialVersionUID = 1L;
	protected List<Integer> validRowIndexList;
    protected Set<Integer> validColumnIndexSet;
    protected List<String> nonVisibleStringList;
	
    
    public TableModelLineRemover(TableModel tableModel,Set<Integer> validColumnIndexSet,List<String> nonVisibleStringList) {
		super(tableModel);
		this.validColumnIndexSet = validColumnIndexSet;
		this.nonVisibleStringList = nonVisibleStringList;
		this.validRowIndexList = new ArrayList<Integer>();
		calculateRowIndexList();
	}

	public TableModelLineRemover(TableModel tableModel,Set<Integer> validColumnIndexSet) {
		super(tableModel);
		this.validColumnIndexSet = validColumnIndexSet;
		this.nonVisibleStringList = new ArrayList<String>();
		this.validRowIndexList = new ArrayList<Integer>();
		calculateRowIndexList();
	}

	protected void calculateRowIndexList(){
		int numberOfRows = tableModel.getRowCount();
		validRowIndexList.clear(); 
		for(int i = 0;i < numberOfRows;i++){
			Iterator<Integer> columnIndexIterator = validColumnIndexSet.iterator();
			boolean notPresent = true;
			while(columnIndexIterator.hasNext()){
				Integer columnIndex = columnIndexIterator.next();
				String cellValue = tableModel.getValueAt(i,columnIndex).toString();
				if(nonVisibleStringList.contains(cellValue)){
					notPresent = false;
					break;
				}
			}
			
			if(notPresent)
				validRowIndexList.add(i);
		}
	}

	 public int getRowCount() {
	        if(validRowIndexList.size() == 0)
	            return tableModel.getRowCount();
	        
	        return validRowIndexList.size();
	}
	
	 public int getModelRow(int rowIndex){
	    	if(validRowIndexList.size() == 0)
	    		return rowIndex;
	    	
	    	return validRowIndexList.get(rowIndex);
	    }
	
	
	public void setNonVisibleStringList(List<String> nonVisibleStringList){
		this.nonVisibleStringList = nonVisibleStringList;
		calculateRowIndexList();
		fireTableStructureChanged();
	}
	
	public void setValidColumnIndexSet(Set<Integer> validColumnIndexSet){
		this.validColumnIndexSet = validColumnIndexSet;
		fireTableStructureChanged();
	}

	public void clearNonVisibleStringList() {
		nonVisibleStringList.clear();
		calculateRowIndexList();
		fireTableStructureChanged();
	}

	public void addToNonVisibleStringList(String string) {
		nonVisibleStringList.add(string);
		calculateRowIndexList();
		fireTableStructureChanged();
	}

	
	
	
	

}
