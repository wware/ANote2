package pt.uminho.anote2.aibench.resources.operations.ontologies;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datastructures.ontologies.OBOOntologyExtensionv1p2;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.resources.gui.report.UpdateReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
public class LoadOBOOntologyv1p2 {
	
	private OntologyAibench ontology;	
	private TimeLeftProgress progress = new TimeLeftProgress("Update Ontology: OBO File");
	private OBOOntologyExtensionv1p2 onto;


	@Port(name="Ontology",direction=Direction.INPUT,order=1)
	public void setOntology(OntologyAibench ontology)
	{
		try {
			if(ontology.isOntologyFill())
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.ontologyLoaderProblem);
				return;
			}
		} catch (SQLException e) {
			new ShowMessagePopup("Ontology Updated Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Ontology Updated Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		this.ontology=ontology;
	}


	@Port(name="File",direction=Direction.INPUT,order=2)
	public void setFile(File file)
	{
		System.gc();
		IResourceUpdateReport report;
		onto = new OBOOntologyExtensionv1p2(ontology, ontology.getName(), progress);
		try {

			if(!onto.validateFile(file))
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.fileNotcompatible);
				return;
			}
			report = onto.processOntologyFile(file);
			ontology.notifyViewObservers();
		} catch (IOException e) {
			new ShowMessagePopup("Ontology Updated Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Ontology Updated Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (SQLException e) {
			new ShowMessagePopup("Ontology Updated Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		new UpdateReportGUI(report);
		new ShowMessagePopup(GlobalTextInfoSmall.ontologyUpdate);
	}

	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel(cancelInBackground=true)
	public void cancel(){
		Workbench.getInstance().warn(GlobalTextInfoSmall.ontologyUpdateCancel);
		onto.cancel();
		return;
	}
}
