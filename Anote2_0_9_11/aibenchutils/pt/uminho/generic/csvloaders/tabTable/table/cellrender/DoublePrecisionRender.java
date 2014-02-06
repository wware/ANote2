package pt.uminho.generic.csvloaders.tabTable.table.cellrender;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DoublePrecisionRender extends DefaultTableCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int precision;

	public DoublePrecisionRender(int precision) {
		this.precision = precision;
	}

	
	public Component getTableCellRendererComponent(JTable table,
											        Object value,
											        boolean isSelected,
											        boolean hasFocus,
											        int row, int column) {
		
		
		super.getTableCellRendererComponent(table, value, isSelected,
		     hasFocus, row, column);
		
		
		if(value!=null ){
    		Double v = (Double)value;
    		if (!Double.isNaN(v) && v != 0){
	        	
	        	DecimalFormat form = new DecimalFormat();
	    		form.setMaximumFractionDigits(precision);
	    		form.setMinimumFractionDigits(1);
	    		form.setGroupingUsed(false);
	    		value = form.format(v);
	    		
	    		Double t = 0.0;
	    		try{
	    			t = Double.parseDouble(value.toString());
	    		}catch (Exception e) {
					e.printStackTrace();
				}
	    		
	    		
	    		if(t==0){
//			    			form.setGroupingUsed(true);
	    			form=new DecimalFormat("0."+ repeat("#", precision) + "E0");
	    			value = form.format(v);
	    		}

    		
    			setText(value.toString());
    		}else setText(value.toString());
    	}else
			setText("");
       
        setHorizontalAlignment(JLabel.CENTER);
		return this;
	}
	
	private String repeat(String str, int n) {
	    String ret = "";
	    for (int i =0; i < n ; i++) {
	    	ret += str;
	    }
	    return ret;
	}
}
