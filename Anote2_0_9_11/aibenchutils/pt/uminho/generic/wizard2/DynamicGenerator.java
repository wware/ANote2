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

import java.util.List;

import javax.swing.JPanel;

public class DynamicGenerator extends WizardPanelDescriptor{
	
	List<WizardPanelDescriptor> descriptors;

	public DynamicGenerator(String id,JPanel baseMainPanel, Object[] objects) {
		super(id);
	}
	
	
	private void generateDescriptors(DynamicPanel base, Object[] objects) throws InstantiationException, IllegalAccessException{
		
		final int numSubSteps = objects.length;
		
		for(int i = 0; i< numSubSteps;i++){
			Class<?> klass = base.getClass();
			DynamicPanel subPanel = (DynamicPanel) klass.newInstance();
			subPanel.setBaseObject(objects[i]);
			subPanel.populatePanel();
			WizardPanelDescriptor desc = new WizardPanelDescriptor("subPanel"+i,subPanel){
				
//				 public String getNextPanelDescriptor() {
//					 	if(i+1<numSubSteps)
//					 		return "subPanel"+i+1;
//					 	return this.getNextPanelDescriptor();
//				    }
//				    
//				    public String getBackPanelDescriptor() {
//				    	
//				        return null;
//				    }
//				    
//				    
//				    public void onNext(){
//				    	
//				    }
			};
			
			descriptors.add(desc);
		}		
	}
	
		
}
