package pt.uminho.anote2.process.ir.pubmed;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.List;

import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.datastructures.process.IRProcess;
import pt.uminho.anote2.datastructures.report.processes.IRCrawlingReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IR.IIRCrawl;
import es.uvigo.ei.pdfDownloader.PDFDownloadHandler;

public class PubMedCrawl extends IRProcess implements IIRCrawl{
	
	private boolean cancel ;
	private boolean onlyFreeFullText;

	public PubMedCrawl(boolean onlyFreeFullText)
	{
		super();
		this.onlyFreeFullText = onlyFreeFullText;
		cancel = false;
	}
	
	
	@Override
	public IIRCrawlingProcessReport getFullText(List<IPublication> publications) {
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		int step =0;
		int total = publications.size();
		IIRCrawlingProcessReport report = new IRCrawlingReport();
		for(IPublication pub:publications)
		{
			if(cancel)
			{
				report.setcancel();
				break;
			}
			if(pub.isPDFAvailable())
			{
				report.addFileAlreadyDownloaded(pub);
			}
			else if(onlyFreeFullText && !pub.getAvailableFreeFullTExt())
			{
				report.addFileRestrictedDownloaded(pub);
			}
			else
			{
				if(getFullTextPMID(pub) != null)
				{
					report.addFileDownloaded(pub);
				}
				else
				{
					report.addFileNotDownloaded(pub);
				}
			}
			memoryAndProgress(start,step,total);
			step++;
		}
		if(cancel)
			report.setcancel();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-start);
		return report;
	}
	
	
	protected void memoryAndProgress(long start, int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}


	private File getFullTextPMID(IPublication pub){

	
		PubMexAnote ex = new PubMexAnote();
		
		PDFDownloadHandler handler = new PDFDownloadHandler() {
			public void error(Exception exception) {
			}
			
			public void error(String message) {
			}

			public void error(String message, Exception e) {
			}

			public void finished(File file) {

			}

			public void info(String info) {
			}
		};
		
		File file = ex.downloadPDF(pub.getOtherID(),GlobalOptions.saveDocDirectoty, handler);

		return file;
	}

	public IDocumentSet getDocuments() { return null;}

	public void setDocuments(IDocumentSet arg0) {}

	public String getType() {
		return "IRCrawl";
	}

	public int getID() {
		return 0;
	}


	@Override
	public void stop() {
		cancel = true;		
	}


}
