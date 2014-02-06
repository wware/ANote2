package pt.uminho.anote2.aibench.publicationmanager.gui;

import javax.swing.JDialog;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * AibenchGUI for UpdateQueryOperation
 */
public class UpdateGUI extends JDialog implements InputGUI{

	
	private static final long serialVersionUID = 4034324428651515372L;
	
	protected ParamsReceiver paramsRec = null;

	public UpdateGUI()
	{
		super(Workbench.getInstance().getMainFrame());
	}


	public void finish() {
		this.setVisible(false);
		this.dispose();	
		
	}

	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		this.paramsRec = arg0;
		Object obj = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Query Itens on clipboard");
			dispose();
		}
		else if(!((QueryInformationRetrievalExtension) obj).isFromPubmedSearch())
		{
			Workbench.getInstance().error("just Queries created by Pubmed Search can be updated.");
			dispose();
		}
		else
		{
			QueryInformationRetrievalExtension query = (QueryInformationRetrievalExtension) obj;
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
					new ParamSpec("query",QueryInformationRetrievalExtension.class,query,null),
			});
		}
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}

}
