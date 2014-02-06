package pt.uminho.generic.csvloaders.tabTable.table.celleditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton button;
	
	public ButtonEditor() {
		this.button = new JButton();
		button.addActionListener(this);
	}
	
	public ButtonEditor(String caption) {
		this.button = new JButton(caption);
		button.addActionListener(this);
	}
	
	public ButtonEditor(Icon icon) {
		this.button = new JButton(icon);
		button.addActionListener(this);
	}
		
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,int row, int column) {
//		if(button.getText() == null && button.getIcon() == null)
//    		button.setText(value.toString());
		return button;
	}
	
	public Object getCellEditorValue() {
		return button.getText();
	}
	
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}
	
	public void cancelCellEditing() {
		super.cancelCellEditing();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		fireEditingStopped();
	}
}