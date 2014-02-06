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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;

public class GenericFluxPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected TableModel tableModel = null;
	protected List<String> variableNamesList;
	protected TableSearchPanel fluxMeasureTable;
	protected String[] columnNames = {"Flux Name","Flux Value"};
    protected List<Double> variableValues;

    
    public GenericFluxPanel(List<String> variableNamesList,List<Double> variableValues) throws InvalidNumberOfColumnNamesException {
		this.variableNamesList = variableNamesList;
        this.variableValues = variableValues;
		initGUI();
	}
    
    public GenericFluxPanel(List<String> variableNamesList) throws InvalidNumberOfColumnNamesException {
		this.variableNamesList = variableNamesList;
        initGUI();
	}
	
	public GenericFluxPanel(String[] columnNames) throws InvalidNumberOfColumnNamesException {
		this.columnNames = columnNames;
		variableNamesList = new ArrayList<String>();
        initGUI();
	}
	
	public GenericFluxPanel(String[] columnNames,List<String> variableNamesList) throws InvalidNumberOfColumnNamesException {
		this.columnNames = columnNames;
		this.variableNamesList = variableNamesList;
        initGUI();
	}

    public GenericFluxPanel(String[] columnNames,List<String> variableNamesList,List<Double> variableValues) throws InvalidNumberOfColumnNamesException {
        this.columnNames = columnNames;
        this.variableNamesList = variableNamesList;
        this.variableValues = variableValues;
        initGUI();
    }
    
    public GenericFluxPanel(TableModel tableModel) throws InvalidNumberOfColumnNamesException{
    	this.tableModel = tableModel;
    	initGUI();
    }
	
	public void initGUI() throws InvalidNumberOfColumnNamesException{
		setLayout(new BorderLayout());
		
		if(tableModel == null){
			if(variableValues == null)
				tableModel = new GenericFluxTableModel(columnNames,variableNamesList);
			else
				tableModel = new GenericFluxTableModel(columnNames,variableNamesList,variableValues);
		}
		
		fluxMeasureTable = new TableSearchPanel(tableModel);
		add(fluxMeasureTable);
	}
	
	public void setVariableNamesList(List<String> variableNamesList) throws InvalidNumberOfColumnNamesException{
		this.variableNamesList = variableNamesList;
		TableModel tableModel = new GenericFluxTableModel(columnNames,variableNamesList);
		fluxMeasureTable.setModel(tableModel);
	}
	
	public  List<GenericPair<String,Double>> getValues(){
		List<GenericPair<String,Double>> valueList = new ArrayList<GenericPair<String,Double>>();
		TableModel tableModel = fluxMeasureTable.getModel();
		
		int numberOfRows = tableModel.getRowCount();
		
		for(int i = 0; i < numberOfRows;i++){
			Double value = (Double)tableModel.getValueAt(i,1);
			//TODO: VERIFY THIS, MAYBE DANGEROUS HAVING NULLS IN FLUXVALUELIST
//			if(value != null){
				String variableName = (String) tableModel.getValueAt(i,0);
				GenericPair<String,Double> newValue = new GenericPair<String,Double>(variableName,value);
				valueList.add(newValue);
//			}
		}
		
		return valueList;
	}
	
	public static void main(String[] args) throws InvalidNumberOfColumnNamesException{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		List<String> stringList = new ArrayList<String>();
		stringList.add("PT");
		stringList.add("Tyler");
		stringList.add("Durden");
		GenericFluxPanel panel = new GenericFluxPanel(stringList);
		panel.initGUI();
		
		panel.setOpaque(true);
		frame.setContentPane(panel);

		frame.pack();
		frame.setVisible(true);
	}

    public void addTableModelListener(TableModelListener tableModelListener){
        //fluxMeasureTable.getModel().addTableModelListener(tableModelListener);
    	TableModel tableModel = fluxMeasureTable.getModel();
    	tableModel.addTableModelListener(tableModelListener);
    }
    
    public TableModel getTableModel(){
    	return fluxMeasureTable.getModel();
    }
}
