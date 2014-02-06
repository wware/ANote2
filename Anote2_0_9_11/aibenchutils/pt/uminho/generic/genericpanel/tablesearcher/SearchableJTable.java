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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;



public class SearchableJTable extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel searchPanel;
	private JTextField searchTextField;
	private JTable table;
	private JScrollPane scrollPane;
	private JCheckBox caseCheckBox;
	private JLabel searchLabel;
	private JPanel containerPanel;

	private TableModel model;
	private String orientation = BorderLayout.NORTH;
	private boolean caseSensitive = false;
	private int[] searchable_indexes = {};
	private int selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

	public SearchableJTable(){
		super();
		this.model = new DefaultTableModel();
		initGUI();
	}

	public SearchableJTable(TableModel model){
		super();
		this.model = model;
		initGUI();
	}

	public SearchableJTable(String orientation){
		super();
		this.orientation = orientation;
		this.model = new DefaultTableModel();
		initGUI();
	}

	public SearchableJTable(TableModel model, String orientation, boolean cs, int... indexes){
		super();
		this.model = model;
		this.orientation = orientation;
		this.caseSensitive = cs;
		this.searchable_indexes = indexes;
		initGUI();
	}


	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				this.setLayout(thisLayout);
				this.add(getContainerPanel());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	}

	private JPanel getContainerPanel() {
		if(containerPanel == null) {
			containerPanel = new JPanel();
			BorderLayout containerPanelLayout = new BorderLayout();
			containerPanel.setLayout(containerPanelLayout);
			containerPanel.setPreferredSize(new java.awt.Dimension(759, 511));
			{
				searchPanel = new JPanel();
				GridBagLayout searchPanelLayout = new GridBagLayout();
				containerPanel.add(searchPanel, this.orientation);
				containerPanel.add(getScrollPane(), BorderLayout.CENTER);
				searchPanel.setPreferredSize(new java.awt.Dimension(759, 40));
				searchPanelLayout.rowWeights = new double[] {0.1};
				searchPanelLayout.rowHeights = new int[] {7};
				searchPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				searchPanelLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
				searchPanel.setLayout(searchPanelLayout);
				{
					searchLabel = new JLabel();
					searchPanel.add(searchLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					searchLabel.setText("search :");
					searchLabel.setFont(new java.awt.Font("Arial",3,12));
					searchLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
					searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
					searchLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons2/search.png")));
				}
				{
					caseCheckBox = new JCheckBox();
					searchPanel.add(caseCheckBox, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					caseCheckBox.setText("case sensitive ?");
					caseCheckBox.setSelected(caseSensitive);
					caseCheckBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							caseCheckBoxActionPerformed(evt);
						}
					});
				}
				{
					searchTextField = new JTextField();
					searchPanel.add(searchTextField, new GridBagConstraints(1, 0, 8, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					searchTextField.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							searchFieldKeyTyped(evt);
						}
					});

				}
			}
		}
		return containerPanel;
	}

	public void search(String text){
		this.searchTextField.setText(text);
		this.searchFieldKeyTyped(null);
	}

	private void searchFieldKeyTyped(KeyEvent evt) {
		String text = new String();

		if(evt!=null){
			if(!searchTextField.getText().equalsIgnoreCase("") && evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
				text = searchTextField.getText();
			else text = searchTextField.getText()+evt.getKeyChar();
		}
		else{
			text = searchTextField.getText();
		}

		if(!caseSensitive)
			text = text.toLowerCase(); //NOTE: case insensitive

		int row=0;
		ArrayList<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selection = new DefaultListSelectionModel();

		for(int x=0; x<this.searchable_indexes.length ; x++){


			for(int i=0;i<model.getRowCount();i++){

				Object valueobj = model.getValueAt(i, x);
				String value;
				if(Object.class.isAssignableFrom(String.class))
					value = (String) valueobj;
				else throw new IllegalArgumentException("Try to search in a field that was not a String instance: row ["+i+"] column ["+x+"]");


				if(!caseSensitive && value!=null)
					value =value.toLowerCase();

				if(value != null && value.contains(text)){
					row=i;
					rows.add(new Integer(i));
				}
			}
		}

		for(Integer i:rows)
			selection.addSelectionInterval(i.intValue(), i.intValue());		
		this.table.setSelectionMode(this.getSelectionMode());
		this.table.setSelectionModel(selection);
		if(selection.isSelectionEmpty()&& !(this.searchTextField.getText().equalsIgnoreCase("")))
			this.searchTextField.setBackground(Color.RED);
		else this.searchTextField.setBackground(Color.WHITE);
		this.table.scrollRectToVisible(this.table.getCellRect(row, 0, true));

	}

	private JScrollPane getScrollPane() {
		if(scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	public JTable getTable() {
		if(table == null) {
			table = new JTable();
			table.setModel(model);
		}
		return table;
	}

	private void caseCheckBoxActionPerformed(ActionEvent evt) {
		if(this.caseCheckBox.isSelected()){
			this.caseCheckBox.setSelected(true);
			this.caseSensitive = true;
		}else{
			this.caseCheckBox.setSelected(false);
			this.caseSensitive = false;
		}

	}

	/**
	 * @return the model
	 */
	public TableModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(TableModel model) {
		this.model = model;
		this.getTable().setModel(this.getModel());
	}

	/**
	 * @return the orientation
	 */
	public String getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the caseSensitive
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		this.caseCheckBox.setSelected(caseSensitive);
	}

	/**
	 * @return the searchable_indexes
	 */
	public int[] getSearchable_indexes() {
		return searchable_indexes;
	}

	/**
	 * @param searchable_indexes the searchable_indexes to set
	 */
	public void setSearchable_indexes(int[] searchable_indexes) {
		this.searchable_indexes = searchable_indexes;
	}

	/**
	 * @return the selectionMode
	 */
	public int getSelectionMode() {
		return selectionMode;
	}

	/**
	 * @param selectionMode the selectionMode to set
	 */
	public void setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
	}

	public void addListSelectionListener(MouseListener listener){
		table.addMouseListener(listener);
	}

	public int getSelectedRow(){
		return table.getSelectedRow();
	}


}