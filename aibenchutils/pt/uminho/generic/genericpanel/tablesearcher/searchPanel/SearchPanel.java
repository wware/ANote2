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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class SearchPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected JTextField searchTextField;
	private SearchBasicOptionsPanel searchBasicOptionsPanel;

	public SearchPanel(){
		initGUI();
	}
	
	protected void initGUI() {
		{
			GridBagLayout searchPanelLayout = new GridBagLayout();
			searchPanelLayout.rowWeights = new double[] {0.1};
			searchPanelLayout.rowHeights = new int[] {7};
			searchPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.1};
			searchPanelLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 27, 7};
			this.setLayout(searchPanelLayout);
			{
				JLabel searchLabel = new JLabel();
				add(searchLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				searchLabel.setText("search :");
				searchLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
				searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
				searchLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Search.png")));
			}
			{
				searchTextField = new JTextField();
				this.add(searchTextField, new GridBagConstraints(1, 0, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				searchBasicOptionsPanel = new SearchBasicOptionsPanel();
				this.add(searchBasicOptionsPanel, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		
	}
	
	public void addActionListener(ActionListener actionListener){
		searchBasicOptionsPanel.addAction(actionListener);
	}
	
	public void addSearchTextFieldKeyListener(KeyListener keyListener){
		searchTextField.addKeyListener(keyListener);
	}
	
	public String getSearchText(){
		return searchTextField.getText();
	}
	
	public boolean isSearchCaseSensitive(){
		return searchBasicOptionsPanel.isCaseSensitiveSelected();
	}
	
	public boolean isSearchWholeWord(){
		return searchBasicOptionsPanel.isMatchWholeWordSelected();
	}
}
