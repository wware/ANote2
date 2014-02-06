package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.Color;

import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;


public class ChooseVerbColorGUI extends ChooseColorGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChooseVerbColorGUI(String color) {
		super("Channge Verb (Clue) Color", color,0);
	}

	void okButtonPerformed(int classID,String newColor) {
		OtherConfigurations.setVerbColor(newColor);
		CorporaProperties.updateVerbColors();
		finish();
	}
	
	protected void colorRestrition(Color c) {
		{
			okButtonPerformed(0,getNewColor());
		}
	}
	
	

}
