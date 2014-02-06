package pt.uminho.anote2.aibench.resources.gui;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class MergeLexicalWordsGUI extends MergeResources{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MergeLexicalWordsGUI() {
		super(LexicalWordsAibench.class);
		this.setModal(true);
	}

	public String getResourceType() {
		return "Lexical Words";
	}

	protected void okButtonAction() {
	   boolean createNewResource = getJRadioResourcesYes().isSelected();
	   LexicalWordsAibench lexicaWordsSources = (LexicalWordsAibench) getComboBoxSource().getSelectedItem();
	   LexicalWordsAibench lexicaWordsDestiny = (LexicalWordsAibench) getComboBoxDestiny().getSelectedItem();
	   String name = new String();
	   String notes = new String();
	   if(createNewResource)
	   {
		   name = getNewResourceName();
		   notes = getNewResourceNotes();
		   if(name.length()>0)
		   {
			   testSAmeResourcesAndOperation(createNewResource, lexicaWordsSources,
						lexicaWordsDestiny, name, notes);
		   }
		   else
		   {
			   new ShowMessagePopup("The new Lexical Words name cannot be empty");	
			   Workbench.getInstance().warn("The new Lexical Words name cannot be empty");
		   }
	   }
	   else
	   {
		   testSAmeResourcesAndOperation(createNewResource, lexicaWordsSources,
				lexicaWordsDestiny, name, notes);
	   }

	}

	private void testSAmeResourcesAndOperation(boolean createNewResource,
			LexicalWordsAibench lexicaWordsSources,
			LexicalWordsAibench lexicaWordsDestiny, String name, String notes) {
		if(lexicaWordsSources.compareTo(lexicaWordsDestiny)==0)
		   {
			   new ShowMessagePopup("Source and Target Lexical Words are the same");	
			   Workbench.getInstance().warn("Source and Target Lexical Words are the same");
		   }
		   else
		   {
			   callOperation(createNewResource, lexicaWordsSources,lexicaWordsDestiny, name, notes);	
		   }
	}

	private void callOperation(boolean createNewResource,
			LexicalWordsAibench lexicaWordsSources,
			LexicalWordsAibench lexicaWordsDestiny, String name, String notes) {
		paramsRec.paramsIntroduced( new ParamSpec[]{ 
				   new ParamSpec("new resource option",Boolean.class,createNewResource,null),
				   new ParamSpec("name",String.class,name,null),
				   new ParamSpec("notes",String.class,notes,null),
				   new ParamSpec("lexical words dest",LexicalWordsAibench.class,lexicaWordsDestiny,null),
				   new ParamSpec("lexical words source",LexicalWordsAibench.class,lexicaWordsSources,null)
		   });
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LexicalWords_Merge#Configuration";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(LexicalWordsAibench.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Lexical Words selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.generalWidth,GlobalOptions.generalHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);	
		}
	}

}
