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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class TestPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new TestPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public TestPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			JLabel label1 = new JLabel();
			JLabel label2 = new JLabel();
			add(label1);
			label1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/wizardSelectOptimization.png")));
			add(label2);
			label2.setText("PT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
