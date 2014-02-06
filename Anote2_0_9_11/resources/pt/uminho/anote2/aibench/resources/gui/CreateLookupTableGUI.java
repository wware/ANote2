package pt.uminho.anote2.aibench.resources.gui;

import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTables;
import pt.uminho.anote2.aibench.resources.gui.help.CreateResourceGUI;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateLookupTableGUI extends CreateResourceGUI{
	
	private static final long serialVersionUID = 1L;

	public CreateLookupTableGUI() {
		super();
		this.setModal(true);	

	}

	protected void okButtonAction() {

		if(!getjTextFieldName().getText().equals(""))
		{
			List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(LookupTables.class);
			LookupTables look = (LookupTables) items.get(0).getUserData();
			paramsRec.paramsIntroduced( new ParamSpec[]{ 
					new ParamSpec("lookuptables",LookupTables.class,look,null),
					new ParamSpec("name",String.class,this.getjTextFieldName().getText(),null),
					new ParamSpec("info",String.class,this.getjTextFieldNote().getText(),null)
			});		
		}
		else
			Workbench.getInstance().warn("Please insert a Lookup Table name");
	}

	public String getResourceType() {
		return "Lookup Table";
	}

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LookupTable_Create";
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.paramsRec = arg0;
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);		
	}

}
