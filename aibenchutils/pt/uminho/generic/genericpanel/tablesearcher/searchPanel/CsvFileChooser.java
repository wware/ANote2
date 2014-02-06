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
package pt.uminho.generic.genericpanel.tablesearcher.searchPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.conf.Delimiter;
import pt.uminho.generic.genericpanel.delimiters.ChoseDelimitersPanel;

public class CsvFileChooser extends JFileChooser implements ActionListener{
	
	
	
	private static final long serialVersionUID = 1L;
	
	
	public static String EXTRA_OPTIONS = "extraoptionsactionlistener";
	private JLabel fileDelim;
	private ChoseDelimitersPanel delim;
	private JCheckBox isOnlyVisibleData;
	private JCheckBox onlyAllowedColumns;
	private JPanel optionsPanel;
	
	private JPanel extensiblePanel;
	private JToggleButton optionsButton;
	private JLabel extensibleLabel;
	
	
	
	public CsvFileChooser(){
		super();
		initGui();
		super.addActionListener(this);
		optionsButton.addActionListener(this);
		setPreferredSize(new Dimension(550,400));
		
		
	}
	
	private void initGui(){
		optionsPanel = new JPanel();
        
        GridBagLayout searchPanelLayout = new GridBagLayout();
		searchPanelLayout.rowWeights = new double[] {0,0};
		searchPanelLayout.rowHeights = new int[] {7,7};
		searchPanelLayout.columnWeights = new double[] {0,1};
		searchPanelLayout.columnWidths = new int[] {7, 7};
		optionsPanel.setLayout(searchPanelLayout);
		fileDelim = new JLabel("File Delimiter:");
		delim = new ChoseDelimitersPanel();
		delim.setDefaulValue(2);
		
		isOnlyVisibleData = new JCheckBox("Only data in the table");
		onlyAllowedColumns = new JCheckBox("Only allowed columns");
		
		optionsPanel.add(fileDelim,new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 12, 0, 5), 0, 0));
		optionsPanel.add(delim,new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 12), 0, 0));
		optionsPanel.add(isOnlyVisibleData,new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(10, 0, 0, 12), 0, 0));
		optionsPanel.add(onlyAllowedColumns,new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(10, 0, 0, 12), 0, 0));
		optionsPanel.setBorder(BorderFactory.createTitledBorder(null, "Additional Options", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
		
		extensiblePanel = new JPanel();
		GridBagLayout exLayout = new GridBagLayout();
		exLayout.rowWeights = new double[] {0,0};
		exLayout.rowHeights = new int[] {0,1};
		exLayout.columnWeights = new double[] {1,0};
		exLayout.columnWidths = new int[] {0, 0};
		extensiblePanel.setLayout(exLayout);
		
		extensibleLabel = new JLabel("Additional Options");
		ImageIcon cup = new ImageIcon(getClass().getClassLoader().getResource("icons/down.png"));
		optionsButton = new JToggleButton(cup);
		optionsButton.setActionCommand(EXTRA_OPTIONS);
		
		extensiblePanel.add(extensibleLabel,new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(0, 0, 0, 5), 0, 0));
		extensiblePanel.add(optionsButton,new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 12), 0, 0));
		
	}
	
	protected void searchOptionButtonPressed() {
		boolean isSearchOptionsButtonTopggle = optionsButton.isSelected();

		if (isSearchOptionsButtonTopggle) {
			extensiblePanel.add(optionsPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			extensibleLabel.setText("");
		} else {
			extensibleLabel.setText("Additional Options");
			extensiblePanel.remove(optionsPanel);
			
		}
		extensiblePanel.updateUI();
		updateUI();
	}
	
		   
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        dialog.add(extensiblePanel, java.awt.BorderLayout.NORTH);

        dialog.pack();
        return dialog;
    }
   

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String action = arg0.getActionCommand();
		
		if(action.equals(EXTRA_OPTIONS)){
			searchOptionButtonPressed();
		}
		
	}
	
	public Delimiter getDelimiter(){
		return delim.getDelimiter();
	}
	
	public Boolean isOnlyWriteVisibleData(){
		return isOnlyVisibleData.isSelected();
	}

	public Boolean isOnlyAllowedColumns() {
		return onlyAllowedColumns.isSelected();
	}

	public void setWriteVisibleDataButton(boolean b){
		isOnlyVisibleData.setVisible(b);
	}
	    
}
