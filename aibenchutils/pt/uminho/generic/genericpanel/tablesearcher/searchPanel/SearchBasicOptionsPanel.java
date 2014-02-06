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

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class SearchBasicOptionsPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final String Case_Sensitive_Pressed = "Case_Sensitive_Pressed";
	public static final String Match_Whole_Word_Pressed = "Match_Whole_Word_Pressed";
	protected JCheckBox caseSensitiveCheckBox;
	protected JCheckBox matchWholeWordCheckBox;
	
	public SearchBasicOptionsPanel() {
		initGUI();
	}
	
	protected void initGUI() {
		caseSensitiveCheckBox = new JCheckBox("Case sensitive");
		matchWholeWordCheckBox = new JCheckBox("Whole word");
		caseSensitiveCheckBox.setActionCommand(Case_Sensitive_Pressed);
		matchWholeWordCheckBox.setActionCommand(Match_Whole_Word_Pressed);
		try {
			GridLayout thisLayout = new GridLayout(0, 1);
			thisLayout.setColumns(1);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			this.setLayout(thisLayout);
			add(caseSensitiveCheckBox);
			add(matchWholeWordCheckBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isCaseSensitiveSelected(){
		return caseSensitiveCheckBox.isSelected();
	}
	
	public boolean isMatchWholeWordSelected(){
		return matchWholeWordCheckBox.isSelected();
	}
	
	public void addAction(ActionListener j){
		caseSensitiveCheckBox.addActionListener(j);
		matchWholeWordCheckBox.addActionListener(j);
	}

}
