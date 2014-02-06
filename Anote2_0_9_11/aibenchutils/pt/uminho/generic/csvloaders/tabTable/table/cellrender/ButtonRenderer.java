package pt.uminho.generic.csvloaders.tabTable.table.cellrender;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer implements TableCellRenderer {
    protected JButton button;
    
    public ButtonRenderer(){
    	button = new JButton();
    }
    
    public ButtonRenderer(String caption){
        button = new JButton(caption);
    }
    
    public ButtonRenderer(Icon icon){
        button = new JButton(icon);
    }
	 
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
    	
    	if(button.getText() == null && button.getIcon() == null)
    		button.setText(value.toString());
    	
        return button;
    }
}