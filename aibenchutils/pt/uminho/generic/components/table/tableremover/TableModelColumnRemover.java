package pt.uminho.generic.components.table.tableremover;

import java.util.List;
import java.util.Set;

import javax.swing.table.TableModel;

import pt.uminho.generic.components.table.TableModelExtension;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 2, 2009
 * Time: 5:43:51 PM
 */
public class TableModelColumnRemover extends TableModelExtension{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Set<Integer> validColumnIndexSet;
    protected List<Integer> validRowIndexList;
    protected boolean enableRemove;
    
    public TableModelColumnRemover(TableModel tableModel, Set<Integer> validColumnIndexSet) {
        super(tableModel);
        this.validColumnIndexSet = validColumnIndexSet;
        tableModel.addTableModelListener(new TableRemoverModelHandler(this));
        
    }
    
    public Set<Integer> getValidColumnIndexSet(){
    	return validColumnIndexSet;
    }
    
    @Override
    public int getModelRow(int rowIndex) {
        return validRowIndexList.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        if(validRowIndexList.size() == 0)
            return tableModel.getRowCount();
        return validRowIndexList.size();
    }

    public void remove(String valueToRemove){
         for(Integer columnIndex:validColumnIndexSet){
            for(int j = 0; j < tableModel.getRowCount();j++){
                String cellValue = (String) tableModel.getValueAt(j,columnIndex);
                if(!cellValue.equals(valueToRemove))
                    validRowIndexList.add(j);
            }
         }
    }

    public void clearRemove(){
        validRowIndexList.clear();
    }

    public void setEnableRemove(boolean enableRemove) {
        this.enableRemove = enableRemove;
    }

    public boolean isEnableRemove() {
        return enableRemove;
    }
}
