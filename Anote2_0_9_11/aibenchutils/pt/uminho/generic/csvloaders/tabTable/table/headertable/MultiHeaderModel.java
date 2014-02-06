package pt.uminho.generic.csvloaders.tabTable.table.headertable;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class MultiHeaderModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Map<String, Boolean> iscelleditable = new HashMap<String, Boolean>();
	
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		
		String key = arg0 + ""+arg1;
		
		Boolean ret = iscelleditable.get(key);
		if(ret==null)
			ret=false;
		
		return ret;
	}

	public void setEditable(int row, int column, boolean b) {
		String key = row + ""+column;
		iscelleditable.remove(key);
		if(b);
			iscelleditable.put(key, b);
	}
	
}
