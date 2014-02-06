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
import java.awt.CardLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

//TODO por icons como variaveis do Wizard.
public class Wizard<T> extends WindowAdapter{
	
	private JPanel cardPanel;
    private CardLayout cardLayout;            
    private JButton backButton;
    private JButton nextButton;
    private JButton cancelButton;
    private T dataContainerObject;
    private WizardController wizardController;
    private JDialog wizardDialog;
    private WizardPanelDescriptor currentPanel;
    private Map<String,WizardPanelDescriptor> wizardPanelDescriptorMap;
    private IWizardInputGUI wizardParentGUI;
    private int width;
    private int height;
  
    public Wizard(T dataContainer,IWizardInputGUI inputGUI){
    	width = 900;
    	height = 550;
    	wizardPanelDescriptorMap = new HashMap<String,WizardPanelDescriptor>();
    	wizardController = new WizardController(this);
    	wizardDialog = new JDialog(Workbench.getInstance().getMainFrame());
    	wizardParentGUI = inputGUI;
    	currentPanel = null;
    	this.dataContainerObject = dataContainer;
        initComponents();
		nextButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/next.png")));
		backButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/back.png")));
		cancelButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));		
    }
    
    public Wizard(int width,int height,T dataContainer,IWizardInputGUI inputGUI){
    	this.width = width;
    	this.height = height;
    	wizardPanelDescriptorMap = new HashMap<String,WizardPanelDescriptor>();
    	wizardController = new WizardController(this);
    	wizardDialog = new JDialog(Workbench.getInstance().getMainFrame());
    	wizardParentGUI = inputGUI;
    	currentPanel = null;
    	this.dataContainerObject = dataContainer;
        initComponents();
        nextButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/next.png")));
		backButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/back.png")));
		cancelButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));	
    }
    
    public T getDataContainerObject(){
    	return dataContainerObject;
    }
    
    public void setDataContainerObject(T dataContainerObject){
    	this.dataContainerObject = dataContainerObject;
    }
    
    public void showDialog() {
        wizardDialog.setModal(true);
        wizardDialog.pack();
        wizardDialog.setSize(width,height);
        Utilities.centerOnOwner(wizardDialog);
        wizardDialog.setVisible(true);
        
    }
    
    public void registerPanel(WizardPanelDescriptor panel){
    	String id = panel.getPanelDescriptorIdentifier();
    	cardPanel.add(panel.getPanelComponent(),id);
    	panel.setWizard(this);
    	wizardPanelDescriptorMap.put(id,panel);
    }
    
    public void unregisterPanel(WizardPanelDescriptor panel){
    	String id = panel.getPanelDescriptorIdentifier();
    	cardPanel.remove(panel.getPanelComponent());
    	wizardPanelDescriptorMap.remove(id);
    	panel = null;
    }
    
    public void resetPanel(WizardPanelDescriptor panel){
    	Class<?> klass = panel.getClass();
    	WizardPanelDescriptor newPanel = null;
    	try {
			newPanel = (WizardPanelDescriptor) klass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	unregisterPanel(panel);
    	registerPanel(newPanel);
    }
    
    public void setCurrentPanel(String id) throws NonExistantWizardPanelException{
    	if(currentPanel != null)
    		currentPanel.actionAfterDisplayPanel();
    	WizardPanelDescriptor panel = wizardPanelDescriptorMap.get(id);
    	currentPanel = panel;
    	
    	if(panel != null){
    		String backPanelId = panel.getBackPanelDescriptor();
    		String nextPanelId = panel.getNextPanelDescriptor();
    		if(backPanelId == null)
    			setEnableBackButton(false);
    		else
    			setEnableBackButton(true);
    		if(nextPanelId == null){
    			nextButton.setText(DefinitionsWizard.FINISH_BUTTON_TEXT);
    			//TODO por o icon de finish
    		}else{
    			setEnableNextButton(true);
    			nextButton.setText(DefinitionsWizard.NEXT_BUTTON_TEXT);
    		}
    		panel.actionBeforeDisplayPanel();
    		cardLayout.show(cardPanel,id);
    		panel.actionDuringDisplayingPanel();
    	}else throw new NonExistantWizardPanelException();
    }
    
    public void setTitle(String title) {
       wizardDialog.setTitle(title);
    }
    
    public String getTitle() {
        return wizardDialog.getTitle();
    }
    
    public void setEnableNextButton(boolean isNextButtonEnabled){
    	nextButton.setEnabled(isNextButtonEnabled);
    }
    
    public void setEnableBackButton(boolean isBackButtonEnabled){
    	backButton.setEnabled(isBackButtonEnabled);
    }
    
    public void setNextButtonIcon(ImageIcon icon){
    	nextButton.setIcon(icon);
    }
    
    public void setBackButtonIcon(ImageIcon icon){
    	backButton.setIcon(icon);
    }
    
    public void setCancelButtonIcon(ImageIcon icon){
    	cancelButton.setIcon(icon);
    }
     
    private void initComponents() {

       wizardDialog.getContentPane().setLayout(new BorderLayout());
       //addWindowListener(this);
                
        
        JPanel buttonPanel = new JPanel();
        JSeparator separator = new JSeparator();
        Box buttonBox = new Box(BoxLayout.X_AXIS);

        cardPanel = new JPanel();       

        cardLayout = new CardLayout(); 
        cardPanel.setLayout(cardLayout);
        
        backButton = new JButton();
        backButton.setText(DefinitionsWizard.BACK_BUTTON_TEXT);
        nextButton = new JButton();
        nextButton.setText(DefinitionsWizard.NEXT_BUTTON_TEXT);
        cancelButton = new JButton();
        cancelButton.setText(DefinitionsWizard.CANCEL_BUTTON_TEXT);
        
        backButton.setActionCommand(DefinitionsWizard.BACK_BUTTON_ACTION_COMMAND);
        nextButton.setActionCommand(DefinitionsWizard.NEXT_BUTTON_ACTION_COMMAND);
        cancelButton.setActionCommand(DefinitionsWizard.CANCEL_BUTTON_ACTION_COMMAND);

        backButton.addActionListener(wizardController);
        nextButton.addActionListener(wizardController);
        cancelButton.addActionListener(wizardController);
        
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(separator, BorderLayout.NORTH);

        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));       
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
        
        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
        
       wizardDialog.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
       wizardDialog.getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);

    }
    
    public void close(){
    	wizardParentGUI.termination();
    	wizardDialog.dispose();
    }
    
    public void dispose(){
    	wizardDialog.dispose();
    }

	public WizardPanelDescriptor getCurrentPanelDescriptor() {
		return currentPanel;
	}
    
	public void setResizable(boolean isResizable){
		wizardDialog.setResizable(isResizable);
	}
}
