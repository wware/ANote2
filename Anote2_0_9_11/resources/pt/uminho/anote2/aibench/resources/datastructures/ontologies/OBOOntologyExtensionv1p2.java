package pt.uminho.anote2.aibench.resources.datastructures.ontologies;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.resources.ontology.loaders.LoadOBOOntologies;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.ontologies.IOntology;

public class OBOOntologyExtensionv1p2 extends LoadOBOOntologies{

	private TimeLeftProgress progress;
	private boolean cancel = false;
	private long start;
	public static String version = "1.2";
	
	public OBOOntologyExtensionv1p2(IOntology ontology,String font,TimeLeftProgress progress) {
		super(ontology, font, version);
		this.progress=progress;
	}
	
	public IResourceUpdateReport processOntologyFile(File file) throws IOException, DatabaseLoadDriverException, SQLException
	{
		progress.setTimeString("Calculating...loading Ontology File...");
		progress.setProgress(0);
		start = GregorianCalendar.getInstance().getTimeInMillis();
		return super.processOntologyFile(file);
	}
	
	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format(((double) step / (double) total) * 100) + " %..."));
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
		long end;
		long differtim;
		end = GregorianCalendar.getInstance().getTimeInMillis();
		differtim = end-start;
		progress.setTime(differtim, step, total);
		progress.setProgress((float) step / (float) total);
	}

	public void cancel()
	{
		this.cancel = true;
	}
	
	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		OBOOntologyExtensionv1p2.version = version;
	}


}
