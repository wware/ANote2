package pt.uminho.generic.csvloaders.tabTable.table.cellrender;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboRenderer implements TableCellRenderer{

	
	protected JComboBox combo;
	
	public ComboRenderer(){	
		combo = new JComboBox();
	}
	
	public ComboRenderer(Object[] items){
		
		combo = new JComboBox(items);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		
		if(value != null)
			combo.setSelectedItem(value.toString());	
		return combo;
	}

}
