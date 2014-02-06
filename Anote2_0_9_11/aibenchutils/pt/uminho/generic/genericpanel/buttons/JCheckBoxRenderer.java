package pt.uminho.generic.genericpanel.buttons;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
  
public class JCheckBoxRenderer extends JCheckBox implements TableCellRenderer {   
       
	private static final long serialVersionUID = 1L;

	public JCheckBoxRenderer() {
		super();
	}   
  
    public Component getTableCellRendererComponent(JTable table, Object value,   
        boolean isSelected, boolean hasFocus, int row, int column) {   
      if (isSelected) {   
        setForeground(table.getSelectionForeground());   
        setBackground(table.getSelectionBackground());   
      } else {   
        setForeground(table.getForeground());   
        setBackground(table.getBackground());   
      }   
      setSelected((value != null && ((Boolean) value).booleanValue()));    
      return this;   
    }
    
    
    
}  