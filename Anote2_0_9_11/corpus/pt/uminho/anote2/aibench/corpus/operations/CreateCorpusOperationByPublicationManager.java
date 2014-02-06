package pt.uminho.anote2.aibench.corpus.operations;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.gui.report.CorpusCreateReportGUI;
import pt.uminho.anote2.aibench.corpus.settings.corpora.CorporaDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.report.corpora.ICorpusCreateReport;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.report.corpora.CorpusCreateReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=true)
public class CreateCorpusOperationByPublicationManager {
	
	private Corpora project;
	private String description;
	private Properties prop;
	private TimeLeftProgress progress = new TimeLeftProgress("Create Corpus");
	
	
	@Progress
	public TimeLeftProgress getProgress()
	{
		return progress;
	}
	
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
	public void setDocs(Set<Integer> ids)
	{
		System.gc();
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		Corpus corpus;
		try {
			corpus = project.createCorpus(description,prop);	
			CorpusTextType corpusTextType = CorpusTextType.Abstract;
			int step = 0;
			int size = ids.size();
			int maxCorpusValue = Integer.valueOf(PropertiesManager.getPManager().getProperty(CorporaDefaultSettings.CORPUS_SIZE_LIMIT).toString());
			if( maxCorpusValue != -1)
			{
				if(size > maxCorpusValue)
				{
					if(!stopQuestion(size,maxCorpusValue))
					{
						size = maxCorpusValue;
					}
				}
			}
			for(Integer id:ids)
			{
				if(step >= size)
				{
					break;
				}
				IAnnotatedDocument annotDocument = new AnnotatedDocument(id);
				corpus.addDocument(annotDocument);
				progress.setProgress((float) step / (float) size );
				long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
				progress.setTime(nowTime-startTime, step, size);
				step++;
			}
			if(prop.containsKey(GlobalNames.textType))
			{
				String property = prop.getProperty(GlobalNames.textType);
				if(property.equals(GlobalNames.fullText))
					corpusTextType = CorpusTextType.FullText;
				else if(property.equals(GlobalNames.abstractOrFullText))
					corpusTextType= CorpusTextType.Hybrid;
				else
					corpusTextType= CorpusTextType.Abstract;
			}
			ICorpusCreateReport report = new CorpusCreateReport(corpusTextType,description);
			report.setCorpus(corpus);
			long end = GregorianCalendar.getInstance().getTimeInMillis();
			report.setTime(end-startTime);
			project.addCorpus(new Corpus(corpus.getID(), corpus.getDescription(), project, prop));
			project.notifyViewObservers();
			new CorpusCreateReportGUI(report);
			new ShowMessagePopup("Corpus Created");
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Corpus Created Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("Corpus Created Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}	
	}
	
	
	private boolean stopQuestion(int publication,int max){
		Object[] options = new String[]{"All ["+publication+"]", "Restricted ["+max+"]"};
		int opt = showOptionPane("Corpus Restrition", "Duo Corpus Restrition you select "+publication+" and limit is "+max+". ", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);
		//option_pane.setIcon(new ImageIcon(getClass().getClassLoader().getResource("messagebox_question.png")));
		option_pane.setOptions(options);
		
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);
		
		Object choice = option_pane.getValue();
					
		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;
		
		return -1;		
	}
}
