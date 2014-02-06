package pt.uminho.generic.components.table;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 2, 2009
 * Time: 5:37:45 PM
 */
public abstract class TableModelExtension extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	protected TableModel tableModel;

    public TableModelExtension(TableModel tableModel) {
        this.tableModel = tableModel;
    }
    
    public TableModel getOriginalTableModel(){
    	return tableModel;
    }

    @Override
    public int getRowCount() {
        return tableModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return tableModel.getColumnCount();
    }

    @Override
    public String getColumnName(int i) {
        return tableModel.getColumnName(i);
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return tableModel.getColumnClass(i);
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return tableModel.isCellEditable(getModelRow(i),i1);
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return tableModel.getValueAt(getModelRow(i),i1);
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        tableModel.setValueAt(o,getModelRow(i),i1);
    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {
        tableModel.addTableModelListener(tableModelListener);
    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {
        tableModel.removeTableModelListener(tableModelListener);
    }

   public abstract int getModelRow(int rowIndex);

   
}
