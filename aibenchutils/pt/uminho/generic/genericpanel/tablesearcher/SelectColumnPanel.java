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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SelectColumnPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	//private ButtonGroup buttonGroup;
	private int numberOfColumns;
	private int maxOfColumns = 4;
	private int columnsLayout;
	private int rowsLayout;
	private ArrayList<JRadioButton> buttonGroup;
	
	public static final String CHANGE_BUTTON_COMAND = "changeBottonComand";

	public SelectColumnPanel(String[] columnIdentifiers){
		
		numberOfColumns = columnIdentifiers.length;
		buttonGroup = new ArrayList<JRadioButton>();

		calculateRowAndComlumnLayout();
			
		initGUI();
		createButtons(columnIdentifiers);
	}
	
	public void setMaxOfColumns(int maxColumnLayout){
		maxOfColumns = maxColumnLayout;
	}
	
	
	private void createButtons(String[] textButtons){
		
		int row = 0;
		int column = 0;
		
		for(int i =0; i<numberOfColumns; i++){
			JRadioButton button = new JRadioButton();
			button.setText(textButtons[i]);
			button.setSelected(true);
			buttonGroup.add(button);
			this.add(button, new GridBagConstraints(column,row ,1 , 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			button.setSize(150, 25);
			button.setPreferredSize(new java.awt.Dimension(150, 25));
			button.setActionCommand(CHANGE_BUTTON_COMAND);
			
			int rowTest = (i+1)%columnsLayout;
			if(rowTest == 0){
				row++;
				column =0;
			}else{
				column++;
			}

		}
	
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
			
				int[] columnWidths = new int[columnsLayout];
				double[] columnWeights = new double[columnsLayout];
				int[] rowHeights = new int[rowsLayout];
				double[] rowWeights = new double[rowsLayout];
				
				
				for(int i =0; i < columnsLayout; i++){
					columnWidths[i] = 7;
					columnWeights[i] = 1/columnsLayout;
				}
				
				for(int i =0; i < rowsLayout; i++){
					rowHeights[i] = 30;
					rowWeights[i] = 0;
				}
				
				thisLayout.columnWidths = columnWidths;
				thisLayout.rowWeights = rowWeights;
				thisLayout.rowHeights = rowHeights;
				thisLayout.columnWeights = columnWeights;
				this.setLayout(thisLayout);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private void calculateRowAndComlumnLayout(){

		int bestRest = maxOfColumns -(numberOfColumns % maxOfColumns)+1;
		int bestcolumn = maxOfColumns;		
		for(int i = maxOfColumns; i>1; i--){
			if((numberOfColumns % i) == 0){
				columnsLayout =i;
				rowsLayout = numberOfColumns/columnsLayout;
				i =0;
			}else{
				int rest = maxOfColumns -(numberOfColumns % i);
				if(rest<bestRest){
					bestRest = rest;
					bestcolumn = i;
				}else{
					columnsLayout = bestcolumn;
					rowsLayout = numberOfColumns/columnsLayout;
					i=0;
				}
			}
			
		}
	}
	
	public ArrayList<Integer> getSelectedIndexes(){
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		for(int i =0;i<buttonGroup.size();i++)
			if(buttonGroup.get(i).isSelected())
				ret.add(i);
		
		return(ret);
	}
	
	public void addGeneKnockoutActionListener(ActionListener actionListener){
		for(int i =0;i<buttonGroup.size();i++)
			buttonGroup.get(i).addActionListener(actionListener);
	}
	
	public void selectAll(){
		for(int i =0;i<buttonGroup.size();i++)
			buttonGroup.get(i).setSelected(true);
	}
	
	public void deselectAll(){
		for(int i =0;i<buttonGroup.size();i++)
			buttonGroup.get(i).setSelected(true);
	}

	public void enabled(boolean b){
		for(int i =0;i<buttonGroup.size();i++)
			buttonGroup.get(i).setEnabled(b);
		
	}
	
	
	public void addColumnButtonsActionListener(ActionListener actionListener){
		for(JRadioButton button:buttonGroup)
			button.addActionListener(actionListener);
	}
	
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
	}
	
	public boolean isAnythingSelected(){
		
		boolean ret = !buttonGroup.get(0).isSelected();
		
		for(int i =1;i<buttonGroup.size() && ret ;i++)
			ret = !buttonGroup.get(i).isSelected();
		
		return(!ret);
	}

	public static void main(String...args){
		
		JDialog dialog = new JDialog();
		String[] names = {"teste1","teste2","teste3","teste4","teste","teste","teste","teste"};
		SelectColumnPanel teste = new SelectColumnPanel(names);
		
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0};
		thisLayout.rowHeights = new int[] {7};
		thisLayout.columnWeights = new double[] {0};
		
		thisLayout.columnWidths = new int[] {7};
		dialog.getContentPane().setLayout(thisLayout);
		teste.setSize(200,300);		
		dialog.getContentPane().add(teste, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
//		dialog.setSize(1000,100);
		dialog.setVisible(true);
		
   }

	
}
