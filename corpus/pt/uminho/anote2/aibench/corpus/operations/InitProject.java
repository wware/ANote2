package pt.uminho.anote2.aibench.corpus.operations;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class InitProject {
	
	@Port(name="project",direction=Direction.OUTPUT,order=1)
	public Corpora getProject()
	{
		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);

		if(lisRm.size()!=0)
		{
			return null;
		}
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("DB-Host");
		elements.add("DB-Port");
		elements.add("DB-Schema");
		elements.add("DB-User");
		elements.add("DB-Pwd");
		ArrayList<String> data = Configuration.getElementByXMLFile("conf/settings.conf",elements);
		MySQLDatabase mysql = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
		Corpora project = new Corpora(mysql);
		new ShowMessagePopup("Anote Ready");
		return project;	
	}

}
