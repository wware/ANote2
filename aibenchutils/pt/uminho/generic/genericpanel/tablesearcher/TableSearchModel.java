/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.generic.genericpanel.tablesearcher;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.MathUtils;


public class TableSearchModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private Boolean hasNumbers = false;
	private TableModel model;
	
	public TableSearchModel(TableModel model){
		this.model = model;
		int col = model.getColumnCount();
		if(model.getRowCount()>0)
			for(int i =0; i < col && !hasNumbers; i++){
				Object obj = model.getValueAt(0,i);
				if(obj != null)
					if(obj.getClass().isAssignableFrom(Double.class) || obj.getClass().isAssignableFrom(Integer.class))
						hasNumbers = true;
			}
	

	}
	
	@Override
	public Object getValueAt(int arg0, int arg1){
		Object ret = model.getValueAt(arg0, arg1);
		if(ret == null)
			return null;
		if(hasNumbers && ret.getClass().isAssignableFrom(Double.class))
			if(!Double.isNaN((Double)ret))
			ret = MathUtils.round((Double)ret, GlobalOptions.getDoublePrecision());
		
		return ret;
		
	}
	
	public Object getValueToSave(int arg0, int arg1){
		return model.getValueAt(arg0, arg1);
	}

	public boolean hasNumValues(){
		return hasNumbers;
	}

	@Override
	public int getColumnCount() {
		return model.getColumnCount();
	}

	@Override
	public int getRowCount() {
		return model.getRowCount();
	}
	
	public String getColumnName(int col) {
		String name = null;
		try{
			name = model.getColumnName(col);
		}catch (Exception e) {
			
		}
        return name;
    }
	

	public boolean isCellEditable(int row, int col) {
		boolean teste = false;
		try{
			teste = model.isCellEditable(row, col);
		}catch (Exception e) {
			
		}
        return teste;
	}
	
	 public Class getColumnClass(int column) {
		 Class teste = String.class;
		try{
			teste = model.getColumnClass(column);
		}catch (Exception e) {
			
		}
        return teste;
      }
	 
	
	public void setValueAt(Object value, int row, int col) {
		model.setValueAt(value, row,col);
	}


	 public TableModel getOriginalTableModel(){
		 return model;
	 }
}
