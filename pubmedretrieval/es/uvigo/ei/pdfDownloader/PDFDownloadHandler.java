package es.uvigo.ei.pdfDownloader;

import java.io.File;

public interface PDFDownloadHandler {
	public void info(String info);
	public void error(Exception exception);
	public void error(String message);
	public void error(String message, Exception e);
	public void finished(File file);
}
