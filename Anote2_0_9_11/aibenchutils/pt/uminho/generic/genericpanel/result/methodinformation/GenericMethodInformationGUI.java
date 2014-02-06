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
package pt.uminho.generic.genericpanel.result.methodinformation;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GenericMethodInformationGUI extends JPanel{
    
	private static final long serialVersionUID = 1L;
	protected String methodDescription;


    public GenericMethodInformationGUI(String methodDescription){
        this.methodDescription = methodDescription;
        initGUI();
    }

    protected void initGUI() {
        setLayout(new FlowLayout());
        JLabel methodLabel = new JLabel("Method Description: ");
        Font methodLabelFont = methodLabel.getFont();
        methodLabel.setFont(methodLabelFont.deriveFont(methodLabelFont.getStyle() ^ Font.BOLD));
        add(methodLabel);

        JLabel methodDescriptionLabel = new JLabel(methodDescription);
        add(methodDescriptionLabel);
    }
    
}
