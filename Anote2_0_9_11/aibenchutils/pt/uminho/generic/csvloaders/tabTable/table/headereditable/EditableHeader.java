package pt.uminho.generic.csvloaders.tabTable.table.headereditable;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class EditableHeader extends AbstractCellEditor implements TableCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTable table;
	protected JTextField textField = new JTextField();
	
	@Override
	public Object getCellEditorValue() {
		return textField.getText();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		if(value != null)
			textField.setText(value.toString());
		else
			textField.setText("");
		
		return textField;
	}
	
	public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
 
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
 
    public void cancelCellEditing() {
        super.cancelCellEditing();
    }

}
