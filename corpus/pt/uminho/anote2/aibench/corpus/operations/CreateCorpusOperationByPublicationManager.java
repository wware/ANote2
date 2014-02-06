package pt.uminho.anote2.aibench.corpus.operations;

import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class CreateCorpusOperationByPublicationManager {
	
	private Corpora project;
	private String description;
	private Properties prop;
	
	@Port(name="project",direction=Direction.INPUT,order=1)
	public void setProject(Corpora project)
	{
		this.project=project;
	}
	
	@Port(name="setdescription",direction=Direction.INPUT,order=2)
	public void setDescription(String description)
	{
		this.description=description;
	}
	
	@Port(name="setdescription",direction=Direction.INPUT,order=3)
	public void setProperties(Properties prop)
	{
		this.prop=prop;
	}

	@Port(name="setdocsid",direction=Direction.INPUT,order=4)
	public void setRule(Set<Integer> ids)
	{
		Corpus corpus = project.createCorpus(description,prop);
		for(Integer id:ids)
		{
			IAnnotatedDocument annotDocument = new AnnotatedDocument(id);
			corpus.addDocument(annotDocument);
		}
		project.notifyViewObservers();
		new ShowMessagePopup("Corpus Created");
		
	}
	
}
