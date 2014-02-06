package pt.uminho.generic.csvloaders.tabTable.table.cellrender;

import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DoubleRenderer extends DefaultTableCellRenderer {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected NumberFormat nf;
	
    public DoubleRenderer() {
        this(NumberFormat.getCurrencyInstance());
    }
 
    public DoubleRenderer(NumberFormat nf){
    	this.nf = nf;
    	setHorizontalAlignment(RIGHT);
    }
    
  
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected,
                                            hasFocus, row, column);
        setText(nf.format(((Double)value).doubleValue()));
        return this;
    }
}