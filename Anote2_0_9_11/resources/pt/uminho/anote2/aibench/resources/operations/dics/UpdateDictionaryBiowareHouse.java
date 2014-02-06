package pt.uminho.anote2.aibench.resources.operations.dics;

import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.aibench.resources.datastructures.biowarehouse.BiowarehouseExtension;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.gui.report.UpdateReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class UpdateDictionaryBiowareHouse {
	
	private DictionaryAibench dic;
	private IDatabase biowarehousedb;
	private Set<String> classLoader;
	
	private TimeLeftProgress progress = new TimeLeftProgress("Update From Bioware House");
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel
	public void cancel(){
		//cancelMethod();
		dic.notifyViewObservers();
		Workbench.getInstance().warn("Dictionary Update Cancel!");	
	}
	


	@Port(name="biowaredatabase",direction=Direction.INPUT,order=1)
	public void setBiowarehouse(IDatabase biowarehousedb)
	{
		this.biowarehousedb=biowarehousedb;
	}
	
	@Port(name="dictionary",direction=Direction.INPUT,order=2)
	public void setSource(DictionaryAibench dictionaryAibench)
	{
		this.dic=dictionaryAibench;
	}
	
	@Port(name="classLoader",direction=Direction.INPUT,order=3)
	public void setEntity(Set<String> classLoader)
	{
		this.classLoader=classLoader;
	}
	
	@Port(name="organism",direction=Direction.INPUT,order=4)
	public void setOrganism(Boolean syn)
	{
		System.gc();
		BiowarehouseExtension bio = new BiowarehouseExtension(dic,biowarehousedb,false,progress);
		try {
			bio.loadTermsFromBiowareHouse(classLoader, syn);
			dic.notifyViewObservers();
			IResourceUpdateReport report = bio.getReport();
			new UpdateReportGUI(report);
			new ShowMessagePopup("Update Dictionary Complete .");
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Update Dictionary Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("Update Dictionary Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	

}
