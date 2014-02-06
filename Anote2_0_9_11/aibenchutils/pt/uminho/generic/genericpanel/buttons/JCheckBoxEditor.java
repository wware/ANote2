package pt.uminho.generic.genericpanel.buttons;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


  
public class JCheckBoxEditor extends AbstractCellEditor implements TableCellEditor  
{  
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JCheckBox checkbox ;  
	ActionListener lister ;  
	
    public JCheckBoxEditor(ActionListener lister)
    {  
    	this.checkbox = new JCheckBox();
        this.checkbox.addActionListener(lister);
    }  
      
    public Object getCellEditorValue() {  
        return checkbox.isSelected();  
    }  
  
      
    public boolean isCellEditable(EventObject arg0) {  
        return true;  
    }  
  
      
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col)  
    {  
    	checkbox.setSelected(((Boolean) value).booleanValue());
        return checkbox;  
    }  
  
}  