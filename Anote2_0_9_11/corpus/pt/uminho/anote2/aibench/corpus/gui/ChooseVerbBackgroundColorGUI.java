package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.Color;

import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;


public class ChooseVerbBackgroundColorGUI extends ChooseColorGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChooseVerbBackgroundColorGUI(String color) {
		super("Channge Verb (Clue) Background Color", color,0);
	}

	void okButtonPerformed(int classID,String newColor) {
		OtherConfigurations.setVerbColorBackGround(newColor);
		CorporaProperties.updateVerbColors();
		finish();
	}
	
	protected void colorRestrition(Color c) {
		{
			okButtonPerformed(0,getNewColor());
		}
	}
	
	

}
