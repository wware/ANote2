package pt.uminho.anote2.aibench.curator.operation.help;

import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

public class CallLookupTablesOp {

	public static void updateLookupTables() {

		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.refreshlookuptables")){			
				Workbench.getInstance().executeOperation(def);
				return;
			}
		}
	}
	
	
	public static void updateLookupTableElements(IResource<IResourceElement> look) {
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("refresh", Integer.class,look.getID(), null)
		};

		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.refreshlookuptableselements")){			
				Workbench.getInstance().executeOperation(def, paramsSpec);
				return;
			}
		}
	}
	

}
