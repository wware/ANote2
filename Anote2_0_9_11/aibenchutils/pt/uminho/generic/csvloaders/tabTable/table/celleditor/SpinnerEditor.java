package pt.uminho.generic.csvloaders.tabTable.table.celleditor;

import java.awt.Component;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor, ChangeListener, FocusListener {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected JTable table;
    protected SpinnerModel model;
    protected JSpinner spinner;
    TextField err = new TextField();
 
    public SpinnerEditor(){
    	this(new SpinnerNumberModel(0.0f, null, null, 0.1f));
    }
    
    public SpinnerEditor(SpinnerModel model){
    	this.model = model;
    	
    	spinner = new JSpinner(model);
    	spinner.addChangeListener(this);
//    	spinner.addFocusListener(this);
    }
    
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
    	
    	
    	
//    	Component ret = spinner;
    	if(value==null){
    		spinner.setValue(0.0);
    		
    	}else
    		spinner.setValue(value);
    	
//    	fireEditingStopped();
        return spinner;
    }
 
    public Object getCellEditorValue() {
    	Object t = spinner.getValue();
    	
    	if(t.getClass().isAssignableFrom(Number.class))
    		t=null;
        return t;
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

	@Override
	public void stateChanged(ChangeEvent e) {
		fireEditingStopped();
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		fireEditingStopped();
		
	}
}