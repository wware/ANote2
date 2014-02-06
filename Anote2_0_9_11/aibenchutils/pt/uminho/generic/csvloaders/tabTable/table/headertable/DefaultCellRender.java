package pt.uminho.generic.csvloaders.tabTable.table.headertable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DefaultCellRender implements TableCellRenderer{

	
	protected JButton button;
    
    public DefaultCellRender(){
    	button = new JButton("teste");
//    	button.setSize(button.getWidth(), 20);
    	
    	button.setBackground(Color.LIGHT_GRAY);  
    	button.setForeground(Color.black);
    	button.setOpaque(true);
//    	button.setBorderPainted(false);
    }
	 
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
    	
    	if(value!=null)
    	button.setText(value.toString());
    	
        return button;
    }

}
