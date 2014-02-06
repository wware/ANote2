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
package pt.uminho.generic.genericpanel.result;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class GenericResultGUI extends JPanel implements ActionListener{
	
    
	private static final long serialVersionUID = 1L;
	protected JPanel mainPanel;
    protected List<ButtonPanelData> buttonPanelList;
    protected ButtonPanelData currentButtonPanel;
    protected JSplitPane splitPane;

    public GenericResultGUI(JPanel mainPanel,List<ButtonPanelData> buttonPanelList){
        this.mainPanel = mainPanel;
        this.buttonPanelList = buttonPanelList;
        currentButtonPanel = null;
        initGUI();
    }

    protected void initGUI() {
        setLayout(new BorderLayout());
        Border mainPanelBorder = BorderFactory.createLoweredBevelBorder();
        Border toolbarBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        addToolBarButtons(toolbar);
        mainPanel.setBorder(mainPanelBorder);
        add(mainPanel,BorderLayout.CENTER);
        toolbar.setBorder(toolbarBorder);
        add(toolbar,BorderLayout.SOUTH);
    }

    protected void addToolBarButtons(JToolBar toolbar){
        for(ButtonPanelData buttonPanelData:buttonPanelList){
            JButton button  = buttonPanelData.getButton();
            toolbar.add(button);
            button.addActionListener(this);
        }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        ButtonPanelData buttonPanelData = getButtonPanelData(actionCommand);

        if(currentButtonPanel != null){
            JButton currentButton = currentButtonPanel.getButton();
            String currentButtonActionCommand = currentButton.getActionCommand();
            if(actionCommand.equals(currentButtonActionCommand))
                currentButtonPanel = null;
        }else
             currentButtonPanel = buttonPanelData;

        buildMainPanel();
        updateUI();
    }

    private void buildMainPanel() {
        remove(mainPanel);

        if(splitPane != null)
                remove(splitPane);

        if(currentButtonPanel != null){
            JPanel buttonPanel = currentButtonPanel.getPanel();
            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,mainPanel,buttonPanel);
            splitPane.setDividerLocation(750);
            splitPane.setOneTouchExpandable(true);
            add(splitPane,BorderLayout.CENTER);
        }else
            add(mainPanel,BorderLayout.CENTER);
    }

    private ButtonPanelData getButtonPanelData(String actionCommand) {
        for(ButtonPanelData buttonPanelData:buttonPanelList){
            JButton button = buttonPanelData.getButton();
            String buttonActionCommand = button.getActionCommand();
            if(buttonActionCommand.equals(actionCommand))
                return buttonPanelData;
                
        }

        return null;
    }


}
