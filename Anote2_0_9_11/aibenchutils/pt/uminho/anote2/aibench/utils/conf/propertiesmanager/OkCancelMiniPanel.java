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
package pt.uminho.anote2.aibench.utils.conf.propertiesmanager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;


public class OkCancelMiniPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String OK_BUTTON_ACTION_COMMAND = "okButtonActionCommand";
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "cancelButtonActionCommand";
	protected JButton okButton;
	protected JButton cancelButton;
	protected JButton help;

	public OkCancelMiniPanel(){
		initGUI();
	}


	protected void initGUI(){
		GridBagLayout thisLayout = new GridBagLayout();
		this.setLayout(thisLayout);
		thisLayout.rowWeights = new double[] {0.0};
		thisLayout.rowHeights = new int[] {7};
		thisLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7};
		okButton = new JButton("Ok");
		okButton.setActionCommand(OK_BUTTON_ACTION_COMMAND);
		okButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ok22.png")));	
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
		URL str = getClass().getClassLoader().getResource("icons/cancel22.png");
		cancelButton.setIcon(new ImageIcon(str));
		help = new JButton();
		help.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					openWiki();
				} catch (IOException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
			}
		});
		this.add(cancelButton, new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(okButton, new GridBagConstraints(2, 0, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(help, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
	}
	
	private void openWiki() throws IOException {
		Help.internetAccess(GlobalOptions.wikiGeneralLink+"Preferences");
	}
	
	public void addButtonsActionListener(ActionListener actionListener){
		okButton.addActionListener(actionListener);
		cancelButton.addActionListener(actionListener);
	}
	
	public void addOkButtonActionListener(ActionListener actionListener){
		okButton.addActionListener(actionListener);
	}
	
	public void addCancelButtonActionListener(ActionListener actionListener){
		cancelButton.addActionListener(actionListener);
	}
	
	public void setEnabledOkButton(Boolean b){
		okButton.setEnabled(b);
	}


	public JButton getOkButton() {
		return okButton;
	}


	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}


	public JButton getCancelButton() {
		return cancelButton;
	}


	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}
	
	

}
