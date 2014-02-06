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

import java.awt.Component;

public class WizardPanelDescriptor {
	protected Wizard<?> wizard;
    protected Component targetPanel;
    protected String panelIdentifier;
    
    public WizardPanelDescriptor(String id){
    	this.panelIdentifier = id;
    }
    
    public WizardPanelDescriptor(String panelIdentifier,Component panel) {
        this.panelIdentifier = panelIdentifier;
        targetPanel = panel;
        targetPanel.setSize(300,180);
    }
    
    public final Component getPanelComponent() {
        return targetPanel;
    }
    
    public final void setPanelComponent(Component panel) {
        targetPanel = panel;
    }
    
    public String getPanelDescriptorIdentifier() {
        return panelIdentifier;
    }
    
    public final void setPanelDescriptorIdentifier(String id) {
        panelIdentifier = id;
    }
    
    public void setWizard(Wizard<?> w) {
        wizard = w;
    }
    
    public final Wizard<?> getWizard() {
        return wizard;
    }   
    
    public String getNextPanelDescriptor() {
        return null;
    }
    
    public String getBackPanelDescriptor() {
        return null;
    }
    
    public void actionBeforeDisplayPanel() {

    }
 
    public void actionDuringDisplayingPanel() {

    }
 
    public void actionAfterDisplayPanel() {
    	
    }
    
    public boolean validConditions(){
    	return true;
    }
    
    public void onNext(){
    	
    }
	
}
