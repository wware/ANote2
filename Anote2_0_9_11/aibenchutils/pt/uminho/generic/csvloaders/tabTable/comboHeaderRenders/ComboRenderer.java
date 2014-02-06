package pt.uminho.generic.csvloaders.tabTable.comboHeaderRenders;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboRenderer extends JComboBox implements TableCellRenderer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComboRenderer(Object[] objects) {
	      for (int i = 0; i < objects.length; i++) {
	        addItem(objects[i].toString());
	      }
	    }

	    public Component getTableCellRendererComponent(JTable table,
	        Object value, boolean isSelected, boolean hasFocus, int row,
	        int column) {
	      setSelectedItem(value);
	      return this;
	    }
	  
}
