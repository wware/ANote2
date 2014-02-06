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

import java.awt.event.ActionListener;

public class WizardController implements ActionListener {
	protected Wizard<?> wizard;

	public WizardController(Wizard<?> w) {
		wizard = w;
	}

	public void actionPerformed(java.awt.event.ActionEvent evt) {

		if (evt.getActionCommand().equals(
				DefinitionsWizard.CANCEL_BUTTON_ACTION_COMMAND))
			cancelButtonPressed();
		else if (evt.getActionCommand().equals(
				DefinitionsWizard.BACK_BUTTON_ACTION_COMMAND))
			try {
				backButtonPressed();
			} catch (NonExistantWizardPanelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if (evt.getActionCommand().equals(
				DefinitionsWizard.NEXT_BUTTON_ACTION_COMMAND))
			try {
				nextButtonPressed();
			} catch (NonExistantWizardPanelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private void cancelButtonPressed() {
		wizard.dispose();
	}

	private void nextButtonPressed() throws NonExistantWizardPanelException {

		WizardPanelDescriptor currentPanel = wizard.getCurrentPanelDescriptor();
		if (currentPanel != null && currentPanel.validConditions()) {//TODO - ver este IF / se manda excepção
			currentPanel.onNext(); //NOTE: verify this!
			String nextPanelDescriptorId = currentPanel.getNextPanelDescriptor();
			String lastPanelDescriptorId = currentPanel.getBackPanelDescriptor();
			
			if(!currentPanel.validConditions()){
				wizard.resetPanel(currentPanel);
				wizard.setCurrentPanel(lastPanelDescriptorId);
			}
			else if (nextPanelDescriptorId != null){				
				wizard.setCurrentPanel(nextPanelDescriptorId);
			}			
			else{
				currentPanel.actionAfterDisplayPanel();
				wizard.close();
			}
		}
	}

	private void backButtonPressed() throws NonExistantWizardPanelException {
		WizardPanelDescriptor currentPanel = wizard.getCurrentPanelDescriptor();
		if (currentPanel != null) {
			String BackPanelDescriptorId = currentPanel.getBackPanelDescriptor();

			if (BackPanelDescriptorId != null)
				wizard.setCurrentPanel(BackPanelDescriptorId);
		}
	}



}
