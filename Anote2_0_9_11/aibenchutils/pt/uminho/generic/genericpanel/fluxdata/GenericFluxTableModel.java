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
package pt.uminho.generic.genericpanel.fluxdata;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class GenericFluxTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String[] columnNames = {"Flux Id","Flux Value"};
	protected boolean isValueCellsEditable;
	protected List<String> variableNamesList;
	protected List<Double> variableFluxValue;
	
	public GenericFluxTableModel(List<String> variableNamesList) {
		this.variableNamesList = variableNamesList;
		variableFluxValue = new ArrayList<Double>();
		initializeVariableFluxValue();
		isValueCellsEditable = true;
	}
	
	public GenericFluxTableModel(String[] columnNames,List<String> variableNamesList) throws InvalidNumberOfColumnNamesException {
		this.variableNamesList = variableNamesList;
		variableFluxValue = new ArrayList<Double>();
		initializeVariableFluxValue();
		isValueCellsEditable = true;
		if(columnNames.length != 2)
			throw new InvalidNumberOfColumnNamesException();
		this.columnNames = columnNames;
	}
	
	
	public GenericFluxTableModel(List<String> variableNameList,List<Double> variableFluxValue){
		this.variableNamesList = variableNameList;
		this.variableFluxValue = variableFluxValue;
		isValueCellsEditable = false;
	}
	
	public GenericFluxTableModel(List<String> variableNameList,List<Double> variableFluxValue,boolean isValueCellsEditable){
		this.variableNamesList = variableNameList;
		this.variableFluxValue = variableFluxValue;
		this.isValueCellsEditable = isValueCellsEditable;
	}

    public GenericFluxTableModel(String[] columnNames, List<String> variableNamesList, List<Double> variableValues) {
        this.columnNames = columnNames;
        this.variableNamesList = variableNamesList;
        this.variableFluxValue = variableValues;
    }
    

    protected void initializeVariableFluxValue() {
		for(int i = 0; i < variableNamesList.size();i++)
			variableFluxValue.add(null);
	}

	public String getColumnName(int col) {
        return columnNames[col];
    }
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return variableNamesList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex){
		if(columnIndex < 1)
			return variableNamesList.get(rowIndex);
		return variableFluxValue.get(rowIndex);
	}
	
	 public void setValueAt(Object value, int row, int col) {   
		 if((col > 0) && (isValueCellsEditable) && (value != null)){
			 String valueString = value.toString();
			 if(valueString.equals(""))
				 variableFluxValue.set(row,null);
			 else{
				 double valueDouble = Double.valueOf(valueString);
				 variableFluxValue.set(row,valueDouble);
			 }
		 }else
			 variableFluxValue.set(row,null);
			 
	     fireTableCellUpdated(row, col);
	}

	
	public boolean isCellEditable(int row, int col) {
        if (col < 1) 
            return false;
        return isValueCellsEditable;
    }

	public Class getColumnClass(int column) {
        Class returnValue;
        returnValue = getValueAt(0, column).getClass();
        return returnValue;
      } 
}
