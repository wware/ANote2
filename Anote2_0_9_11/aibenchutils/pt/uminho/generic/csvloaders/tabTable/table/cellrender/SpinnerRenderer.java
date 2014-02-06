package pt.uminho.generic.csvloaders.tabTable.table.cellrender;


import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellRenderer;

public class SpinnerRenderer implements TableCellRenderer {
    
	
	SpinnerNumberModel model = new SpinnerNumberModel(Double.NaN, null, null, 1);
    JSpinner spinner = new JSpinner(model);
    
    JSpinner spinnerNull = new JSpinner(new SpinnerNullModel());
 
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
    	if(value == null || !value.getClass().isAssignableFrom(Number.class))
    		return spinnerNull;
        
    	spinner.setValue(value);
        return spinner;
    }
}