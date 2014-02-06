package pt.uminho.anote2.relation.datastructures;

import gate.util.GateException;

import java.net.MalformedURLException;

import pt.uminho.anote2.process.IE.re.IDirectionality;
import pt.uminho.anote2.process.IE.re.IPolarity;
import pt.uminho.anote2.relation.gate.Gate7PosTagger;
import pt.uminho.anote2.relation.gate.LingPipePosTagger;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.gate.process.IGatePosTagger;

public enum PosTaggerEnem {
	Gate_POS
	{
		public IGatePosTagger getPostagger(IDirectionality dir,IPolarity pol, ILexicalWords verbFilter,ILexicalWords verbAddition){
			try {
				return new Gate7PosTagger(new Directionality(),new Polarity(),verbFilter,verbAddition);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public String toString(){
			return "Gate POS Tagging";
		}
		
		public String getDescrition()
		{
			return "ANNIE POS Tagger: based in Mark Hepple's Brill-style POS tagger. More information available in http://gate.ac.uk/sale/tao/splitch6.html#sec:annie:tagger";
		}
		
		public String getImagePath()
		{
			return "icons/gate.png";
		}
	},
	LingPipe_POS
	{
		public IGatePosTagger getPostagger(IDirectionality dir,IPolarity pol, ILexicalWords verbFilter,ILexicalWords verbAddition){
			try {
				return new LingPipePosTagger(new Directionality(),new Polarity(),verbFilter,verbAddition);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public String toString(){
			return "Ling Pipe POS-Tagger";
		}
		
		public String getDescrition()
		{
			return "LingPipe is a suite of Java libraries for the linguistic analysis of human language3. We have provided a plugin called �LingPipe� with wrappers for some of the resources available in the LingPipe library. In order to use these resources, please load the �LingPipe� plugin. More information in http://gate.ac.uk/sale/tao/splitch21.html#sec:misc-creole:lingpipe:postagger	";
		}
		
		public String getImagePath()
		{
			return "icons/gate.png";
		}
	};
	
	public IGatePosTagger getPostagger(IDirectionality dir,IPolarity pol, ILexicalWords verbFilter,ILexicalWords verbAddition) {
		return this.getPostagger(dir,pol,verbFilter,verbAddition);
	}
	
	public String getDescrition()
	{
		return this.getDescrition();
	}
	
	public String getImagePath()
	{
		return this.getImagePath();
	}
	
	public static PosTaggerEnem convertStringInPosTaggerEnem(String str)
	{
		if(str.equals("Gate POS Tagging"))
		{
			return PosTaggerEnem.Gate_POS;
		}
		else if(str.equals("Ling Pipe POS-Tagger"))
		{
			return PosTaggerEnem.LingPipe_POS;
		}
		return null;
	}
	
}
