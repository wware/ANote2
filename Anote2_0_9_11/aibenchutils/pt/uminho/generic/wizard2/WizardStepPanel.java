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
package pt.uminho.generic.wizard2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import pt.uminho.generic.components.gradientpanel.topgradientpanel.TextInformationPanel;

public class WizardStepPanel extends javax.swing.JPanel {
	
	private static final long serialVersionUID = 1L;
	protected TextInformationPanel headerPanel;
	protected JLabel descriptionLabel;
	protected JPanel mainPanel;
	protected JPanel stepPanel;
	
	public WizardStepPanel(){
		initGUI();
	}
	
	private void initGUI() {
		try {
				setPreferredSize(new java.awt.Dimension(600,300));
				setLayout(new BorderLayout());
				createStepPanel();
				headerPanel = new TextInformationPanel();
				add(headerPanel,BorderLayout.PAGE_START);
				descriptionLabel = new JLabel();
				add(descriptionLabel,BorderLayout.NORTH);//TODO Juntar os dois num painel novo
				
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void createStepPanel(){
		stepPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		layout.rowWeights = new double[] { 0.0005, 0.1,0.0005};
		layout.rowHeights = new int[] {7,20,7};
		layout.columnWeights = new double[] {0.0005,0.1,0.0005};
		layout.columnWidths = new int[] {7,20,7};
		stepPanel.setLayout(layout);
	}

	public void addMainPanel(JPanel panel){
		mainPanel = panel;
		mainPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		stepPanel.add(mainPanel, new GridBagConstraints(1,1,1,1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0, 0, 0),0, 0));
		add(stepPanel,BorderLayout.CENTER);
	}
	
	public void addMainPanel(JPanel panel,Border border){
		mainPanel = panel;
		mainPanel.setBorder(border);
		add(mainPanel,BorderLayout.CENTER);
	}
	
	public void setMainPanelBorder(Border border){
		mainPanel.setBorder(border);
	}
	
	public void setHeaderPanelTitleTextFont(Font font){
		headerPanel.setTitleFont(font);
	}
	
	public void setHeaderPanelTitleText(String message){
		headerPanel.setTitle(message);
	}
	
	public void setDescriptionLabelText(String message){
		descriptionLabel.setText(message);
	}
	
	public void setDescriptionLabelFont(Font font){
		descriptionLabel.setFont(font);
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
}


